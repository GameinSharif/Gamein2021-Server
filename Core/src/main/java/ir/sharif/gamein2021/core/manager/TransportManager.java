package ir.sharif.gamein2021.core.manager;

import com.google.gson.Gson;
import ir.sharif.gamein2021.core.domain.dto.DcDto;
import ir.sharif.gamein2021.core.domain.dto.GameinCustomerDto;
import ir.sharif.gamein2021.core.domain.dto.TransportDto;
import ir.sharif.gamein2021.core.response.TransportStateChangedResponse;
import ir.sharif.gamein2021.core.service.DcService;
import ir.sharif.gamein2021.core.service.GameinCustomerService;
import ir.sharif.gamein2021.core.service.StorageService;
import ir.sharif.gamein2021.core.service.TeamService;
import ir.sharif.gamein2021.core.service.TransportService;
import ir.sharif.gamein2021.core.util.Enums;
import ir.sharif.gamein2021.core.util.GameConstants;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.util.models.Product;
import ir.sharif.gamein2021.core.util.models.Vehicle;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@AllArgsConstructor
@Component
public class TransportManager
{

    private final TransportService transportService;
    private final GameinCustomerService gameinCustomerService;
    private final DcService dcService;
    private final TeamService teamService;
    private final StorageService storageService;
    private final PushMessageManagerInterface pushMessageManager;
    private final GameCalendar gameCalendar;
    private final Gson gson = new Gson();

    public void updateTransports()
    {
        handleTransportCrush();
        // TODO : handle transport crashing when day starts or ends?
        LocalDate today = gameCalendar.getCurrentDate();
        startTransports(today);
        endTransports(today);
    }

    private void handleTransportCrush()
    {
        ArrayList<TransportDto> inWayTransports = transportService.getTransportsByState(Enums.TransportState.IN_WAY);
        // TODO : needs seed?
        Random random = new Random();
        ArrayList<TransportDto> crushingTransports = new ArrayList<>();
        for (TransportDto inWayTransport : inWayTransports)
        {
            if (!inWayTransport.getHasInsurance() && random.nextFloat() < GameConstants.CrushProbability)
            {
                crushingTransports.add(inWayTransport);
            }
        }
        changeTransportsStateAndSendToClients(crushingTransports, Enums.TransportState.CRUSHED);
    }

    private void startTransports(LocalDate today)
    {
        ArrayList<TransportDto> startingTransports = transportService.getStartingTransports(today);

        for (TransportDto transport : startingTransports)
        {
            if(!transport.getTransportState().equals(Enums.TransportState.TERMINATED))
            {
                removeProductWhenTransportStart(transport);
                changeTransportsStateAndSendToClients(startingTransports, Enums.TransportState.IN_WAY);
            }
        }
    }

    private void endTransports(LocalDate today)
    {
        ArrayList<TransportDto> arrivedTransports = transportService.getEndingTransports(today);
        for (TransportDto transport : arrivedTransports)
        {
            addProductWhenTransportEnd(transport);
        }
        changeTransportsStateAndSendToClients(arrivedTransports, Enums.TransportState.SUCCESSFUL);
    }

    private void changeTransportsStateAndSendToClients(ArrayList<TransportDto> transportDtos, Enums.TransportState newState)
    {
        for (TransportDto transportDto : transportDtos)
        {
            TransportDto savedTransportDto = transportService.changeTransportState(transportDto, newState);
            sendResponseToTransportOwners(savedTransportDto);
        }
    }

    private void sendResponseToTransportOwners(TransportDto transportDto)
    {
        Integer sourceTeamId = getTransportSourceOwnerId(transportDto);
        Integer destinationId = getTransportDestinationOwnerId(transportDto);
        TransportStateChangedResponse response = new TransportStateChangedResponse(ResponseTypeConstant.TRANSPORT_STATE_CHANGED, transportDto);
        if (sourceTeamId != null)
        {
            pushMessageManager.sendMessageByTeamId(sourceTeamId.toString(), gson.toJson(response));
        }
        if (destinationId != null)
        {
            pushMessageManager.sendMessageByTeamId(destinationId.toString(), gson.toJson(response));
        }
    }

    private Integer getTransportDestinationOwnerId(TransportDto transportDto)
    {
        Assert.notNull(transportDto, "transport should have destination type");
        switch (transportDto.getDestinationType())
        {
            case DC:
                return dcService.loadById(transportDto.getSourceId()).getOwnerId();
            case FACTORY:
                return teamService.findTeamIdByFactoryId(transportDto.getDestinationId());
            default:
                return null;
        }
    }

    private Integer getTransportSourceOwnerId(TransportDto transportDto)
    {
        Assert.notNull(transportDto, "transport should have source type");
        switch (transportDto.getSourceType())
        {
            case DC:
                return dcService.loadById(transportDto.getSourceId()).getOwnerId();
            case FACTORY:
                return teamService.findTeamIdByFactoryId(transportDto.getSourceId());
            default:
                return null;
        }
    }

    public TransportDto createTransport(Enums.VehicleType vehicleType, Enums.TransportNodeType sourceType, Integer sourceId
            , Enums.TransportNodeType destinationType, Integer destinationId, LocalDate startDate
            , Boolean hasInsurance, Integer contentProductId, Integer contentProductAmount)
    {
        // TODO : check inputs. validate source and dest? check start date has'nt passed

        Enums.TransportState transportState = Enums.TransportState.IN_WAY;
        if (gameCalendar.getCurrentDate().isBefore(startDate))
        {
            transportState = Enums.TransportState.PENDING;
        }
        TransportDto transportDto = TransportDto.builder()
                .vehicleType(vehicleType)
                .sourceType(sourceType)
                .sourceId(sourceId)
                .destinationType(destinationType)
                .destinationId(destinationId)
                .startDate(startDate)
                .hasInsurance(hasInsurance)
                .contentProductId(contentProductId)
                .contentProductAmount(contentProductAmount)
                .transportState(transportState)
                .build();

        int transportDuration = calculateTransportDuration(transportDto);
        transportDto.setEndDate(startDate.plusDays(transportDuration));

        TransportDto savedTransport = transportService.saveOrUpdate(transportDto);

        sendResponseToTransportOwners(savedTransport);
        return savedTransport;
    }

    private int calculateTransportDuration(TransportDto transportDto) {
        int transportDistance = getTransportDistance(transportDto);
        return (int) Math.ceil((float) transportDistance / ReadJsonFilesManager.findVehicleByType(transportDto.getVehicleType()).getSpeed()) ;
    }

    //TODO need testing
    private void removeProductWhenTransportStart(TransportDto transportDto)
    {
        if (transportDto.getSourceType().equals(Enums.TransportNodeType.DC))
        {
            storageService.deleteProducts(transportDto.getSourceId(), true, transportDto.getContentProductId(), transportDto.getContentProductAmount());
        }
        else if (transportDto.getSourceType().equals(Enums.TransportNodeType.FACTORY))
        {
            storageService.deleteProducts(transportDto.getSourceId(), false, transportDto.getContentProductId(), transportDto.getContentProductAmount());
        }
    }

    //TODO testing
    private void addProductWhenTransportEnd(TransportDto transportDto)
    {
        if (transportDto.getDestinationType().equals(Enums.TransportNodeType.DC))
        {
            storageService.addProduct(transportDto.getDestinationId(), true, transportDto.getContentProductId(), transportDto.getContentProductAmount());
        }
        else if (transportDto.getDestinationType().equals(Enums.TransportNodeType.FACTORY))
        {
            storageService.addProduct(transportDto.getDestinationId(), false, transportDto.getContentProductId(), transportDto.getContentProductAmount());
        }
    }

    private double[] getLocation(Enums.TransportNodeType type, Integer id)
    {
        switch (type)
        {
            case FACTORY:
                return new double[] {ReadJsonFilesManager.Factories[id].getLatitude(), ReadJsonFilesManager.Factories[id].getLongitude()};
            case DC:
                DcDto dcDto = dcService.loadById(id);
                return new double[] {dcDto.getLatitude(), dcDto.getLongitude()};
            case SUPPLIER:
                return new double[] {ReadJsonFilesManager.Suppliers[id].getLatitude(), ReadJsonFilesManager.Suppliers[id].getLongitude()};
            case GAMEIN_CUSTOMER:
                GameinCustomerDto customerDto = gameinCustomerService.loadById(id);
                return new double[] {customerDto.getLatitude(), customerDto.getLongitude()};
            default:
                return null;
                // TODO : Exception?
        }
    }

    private int getTransportDistance(TransportDto transportDto) {
        double[] sourceLocation = getLocation(transportDto.getSourceType(), transportDto.getSourceId());
        double[] destinationLocation = getLocation(transportDto.getDestinationType(), transportDto.getDestinationId());
        double distance = (sourceLocation[0] - destinationLocation[0]) * (sourceLocation[0] - destinationLocation[0]);
        distance += (sourceLocation[1] - destinationLocation[1]) * (sourceLocation[1] - destinationLocation[1]);
        distance = Math.sqrt(distance);
        return (int) Math.ceil(distance * GameConstants.Instance.distanceConstant * ReadJsonFilesManager.findVehicleByType(transportDto.getVehicleType()).getCoefficient());
    }

    public float calculateTransportCost(Enums.VehicleType vehicleType, int distance, int productId, int productAmount, boolean hasInsurance)
    {
        float insuranceFactor = (hasInsurance ? (1 + GameConstants.Instance.insuranceCostFactor) : 1);
        Vehicle transportVehicle = ReadJsonFilesManager.findVehicleByType(vehicleType);
        float vehicleCost = transportVehicle.getCostPerKilometer() * distance * insuranceFactor;
        int productVolume = ReadJsonFilesManager.findProductById(productId).getVolumetricUnit() * productAmount;
        int vehicleCount = (int) Math.ceil((float) productVolume / transportVehicle.getCapacity());
        return vehicleCost * vehicleCount;
    }

    public float calculateTransportCost(Enums.VehicleType vehicleType, int distance, int productId, int productAmount)
    {
        Vehicle transportVehicle = ReadJsonFilesManager.findVehicleByType(vehicleType);
        float vehicleCost = transportVehicle.getCostPerKilometer() * distance ;
        int productVolume = ReadJsonFilesManager.findProductById(productId).getVolumetricUnit() * productAmount;
        int vehicleCount = (int) Math.ceil((float) productVolume / transportVehicle.getCapacity());
        return vehicleCost * vehicleCount;
    }

}
