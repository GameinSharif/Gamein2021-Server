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
            if (random.nextFloat() < GameConstants.CrushProbability) {
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
                return teamService.findTeamIdByFactoryId(transportDto.getSourceId());
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
            , Enums.TransportNodeType destinationType, Integer destinationId, LocalDate start_date
            , Boolean hasInsurance, Integer contentProductId, Integer contentProductAmount) {
        // TODO : check inputs. validate source and dest? check start date has'nt passed

        int transportDuration = calculateTransportDuration(vehicleType, sourceId, sourceType, destinationId, destinationType);
        Enums.TransportState transportState = Enums.TransportState.IN_WAY;
        if (LocalDate.now().isBefore(start_date)) {
            transportState = Enums.TransportState.PENDING;
        }
        TransportDto transport = TransportDto.builder()
                .vehicleType(vehicleType)
                .sourceType(sourceType)
                .sourceId(sourceId)
                .destinationType(destinationType)
                .destinationId(destinationId)
                .start_date(start_date)
                .hasInsurance(hasInsurance)
                .contentProductId(contentProductId)
                .contentProductAmount(contentProductAmount)
                .transportState(transportState)
                .end_date(start_date.plusDays(transportDuration))
                .build();

        transportService.saveOrUpdate(transport);
        return null;
    }

    private int calculateTransportDuration(Enums.VehicleType vehicleType, Integer sourceId, Enums.TransportNodeType sourceType, Integer destinationId, Enums.TransportNodeType destinationType) {
        // TODO
        // TODO : change inputs : source position? transport?
        return 0;
    }

}
