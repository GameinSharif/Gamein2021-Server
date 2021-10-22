package ir.sharif.gamein2021.core.manager;

import ir.sharif.gamein2021.core.domain.dto.TransportDto;
import ir.sharif.gamein2021.core.service.TransportService;
import ir.sharif.gamein2021.core.util.Enums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

@Component
public class TransportManager {

    private final TransportService transportService;
    private final static float CrushProbability = 0.1f;

    @Autowired
    public TransportManager(TransportService transportService) {
        this.transportService = transportService;
    }

    public void updateTransports() {
        handleTransportCrush();
        // TODO : handle transport crashing when day starts or ends?
        // TODO : how to get game's today?
        LocalDate today = LocalDate.now();
        startTransports(today);
        endTransports(today);
    }

    private void handleTransportCrush() {
        ArrayList<TransportDto> inWayTransports = transportService.getTransportsByState(Enums.TransportState.IN_WAY);
        // TODO : needs seed?
        Random random = new Random();
        ArrayList<TransportDto> crushingTransports = new ArrayList<>();
        for (TransportDto inWayTransport : inWayTransports) {
            if (random.nextFloat() < CrushProbability) {
                crushingTransports.add(inWayTransport);
            }
        }
        transportService.changeTransportsStates(crushingTransports, Enums.TransportState.CRUSHED);
        // TODO : send response
    }

    private void startTransports(LocalDate today) {
        ArrayList<TransportDto> startingTransports = transportService.getStartingTransports(today);
        transportService.changeTransportsStates(startingTransports, Enums.TransportState.IN_WAY);
        // TODO : send response
    }

    private void endTransports(LocalDate today) {
        ArrayList<TransportDto> arrivedTransports = transportService.getEndingTransports(today);
        transportService.changeTransportsStates(arrivedTransports, Enums.TransportState.SUCCESSFUL);
        // TODO : send response
        // TODO : do transport actions
    }

    public TransportDto createTransport(Enums.VehicleType vehicleType, Enums.TransportNodeType sourceType, Integer sourceId
            , Enums.TransportNodeType destinationType, Integer destinationId, LocalDate start_date
            , Boolean hasInsurance, Integer contentProductId, Integer contentProductAmount) {
        // TODO : check inputs. validate source and dest? check start date has'nt passed

        int transportDuration = calculateTransportDuration(vehicleType, sourceId, sourceType, destinationId, destinationType);
        Enums.TransportState transportState = Enums.TransportState.IN_WAY;
        if(LocalDate.now().isBefore(start_date)){
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

        transportService.save(transport);
        return null;
    }

    private int calculateTransportDuration(Enums.VehicleType vehicleType, Integer sourceId, Enums.TransportNodeType sourceType, Integer destinationId, Enums.TransportNodeType destinationType) {
        // TODO
        // TODO : change inputs : source position? transport?
        return 0;
    }

}
