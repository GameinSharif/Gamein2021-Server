package ir.sharif.gamein2021.core.manager;

import ir.sharif.gamein2021.core.domain.dto.TransportDto;
import ir.sharif.gamein2021.core.service.TransportService;
import ir.sharif.gamein2021.core.util.Enums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;

@Component
public class TransportManager {

    private final TransportService transportService;

    @Autowired
    public TransportManager(TransportService transportService) {
        this.transportService = transportService;
    }

    public void updateTransports() {
        ArrayList<TransportDto> pending_transports = transportService.getPendingTransports();
        // TODO : handle transport crashing
        LocalDate today = LocalDate.now();
        ArrayList<TransportDto> startingTransports = transportService.getStartingTransports(today);
        // TODO : update state to in_way, send response
        // TODO : could transport crash on first day?
        ArrayList<TransportDto> arrivedTransports = transportService.getEndingTransports(today);
        // TODO : update state to finished, send response
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
