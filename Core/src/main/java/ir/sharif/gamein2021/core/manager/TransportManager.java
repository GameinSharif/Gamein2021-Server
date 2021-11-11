package ir.sharif.gamein2021.core.manager;

import com.google.gson.Gson;
import ir.sharif.gamein2021.core.domain.dto.TransportDto;
import ir.sharif.gamein2021.core.response.TransportStateChangedResponse;
import ir.sharif.gamein2021.core.service.DcService;
import ir.sharif.gamein2021.core.service.TeamService;
import ir.sharif.gamein2021.core.service.TransportService;
import ir.sharif.gamein2021.core.util.Enums;
import ir.sharif.gamein2021.core.util.GameConstants;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

@AllArgsConstructor
@Component
public class TransportManager {

    private final TransportService transportService;
    private final DcService dcService;
    private final TeamService teamService;
    private final PushMessageManagerInterface pushMessageManager;
    private final GameCalendar gameCalendar;
    private final Gson gson = new Gson();

    public void updateTransports() {
        handleTransportCrush();
        // TODO : handle transport crashing when day starts or ends?
        LocalDate today = gameCalendar.getCurrentDate();
        startTransports(today);
        endTransports(today);
    }

    private void handleTransportCrush() {
        ArrayList<TransportDto> inWayTransports = transportService.getTransportsByState(Enums.TransportState.IN_WAY);
        // TODO : needs seed?
        Random random = new Random();
        ArrayList<TransportDto> crushingTransports = new ArrayList<>();
        for (TransportDto inWayTransport : inWayTransports) {
            if (!inWayTransport.getHasInsurance() && random.nextFloat() < GameConstants.CrushProbability) {
                crushingTransports.add(inWayTransport);
            }
        }
        changeTransportsStateAndSendToClients(crushingTransports, Enums.TransportState.CRUSHED);
    }

    private void startTransports(LocalDate today) {
        ArrayList<TransportDto> startingTransports = transportService.getStartingTransports(today);
        changeTransportsStateAndSendToClients(startingTransports, Enums.TransportState.IN_WAY);
    }

    private void endTransports(LocalDate today) {
        ArrayList<TransportDto> arrivedTransports = transportService.getEndingTransports(today);
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

    private void sendResponseToTransportOwners(TransportDto transportDto) {
        Integer sourceTeamId = getTransportSourceOwnerId(transportDto);
        Integer destinationId = getTransportDestinationOwnerId(transportDto);
        TransportStateChangedResponse response = new TransportStateChangedResponse(ResponseTypeConstant.TRANSPORT_STATE_CHANGED, transportDto);
        if (sourceTeamId != null) {
            pushMessageManager.sendMessageByTeamId(sourceTeamId.toString(), gson.toJson(response));
        }
        if (destinationId != null) {
            pushMessageManager.sendMessageByTeamId(destinationId.toString(), gson.toJson(response));
        }
    }

    private Integer getTransportDestinationOwnerId(TransportDto transportDto) {
        Assert.notNull(transportDto, "transport should have destination type");
        switch (transportDto.getDestinationType()) {
            case DC:
                return dcService.loadById(transportDto.getSourceId()).getOwnerId();
            case FACTORY:
                return teamService.findTeamIdByFactoryId(transportDto.getDestinationId());
            default:
                return null;
        }
    }

    private Integer getTransportSourceOwnerId(TransportDto transportDto) {
        Assert.notNull(transportDto, "transport should have source type");
        switch (transportDto.getSourceType()) {
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
            , Boolean hasInsurance, Integer contentProductId, Integer contentProductAmount) {
        // TODO : check inputs. validate source and dest? check start date has'nt passed

        int transportDuration = calculateTransportDuration(vehicleType, sourceId, sourceType, destinationId, destinationType);
        Enums.TransportState transportState = Enums.TransportState.IN_WAY;
        if (gameCalendar.getCurrentDate().isBefore(startDate)) {
            transportState = Enums.TransportState.PENDING;
        }
        TransportDto transport = TransportDto.builder()
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
                .endDate(startDate.plusDays(transportDuration))
                .build();

        TransportDto savedTransport = transportService.saveOrUpdate(transport);

        sendResponseToTransportOwners(savedTransport);
        return savedTransport;
    }

    private int calculateTransportDuration(Enums.VehicleType vehicleType, Integer sourceId, Enums.TransportNodeType sourceType, Integer destinationId, Enums.TransportNodeType destinationType) {
        // TODO
        // TODO : change inputs : source position? transport?
        return 7;
    }

    private double[] getLocation(Enums.TransportNodeType type, Integer id)
    {
        switch (type)
        {
            case FACTORY:
                return new double[] {ReadJsonFilesManager.Factories[id].getLatitude(), ReadJsonFilesManager.Factories[id].getLatitude()};
                //TODO
        }

        return null;
    }

    public float calculateTransportCost(Enums.VehicleType vehicleType, Integer sourceId, Enums.TransportNodeType sourceType, Integer destinationId, Enums.TransportNodeType destinationType)
    {
        // TODO
        return 0;
    }

}
