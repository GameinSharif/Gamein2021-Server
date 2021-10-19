package ir.sharif.gamein2021.core.manager;

import ir.sharif.gamein2021.core.domain.dto.TransportDto;
import ir.sharif.gamein2021.core.domain.entity.Transport;
import ir.sharif.gamein2021.core.util.Enums;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TransportManager {
    public TransportDto createTransport(Enums.VehicleType vehicleType, Enums.TransportNodeType sourceType, Integer sourceId
            , Enums.TransportNodeType destinationType, Integer destinationId, LocalDate start_date
            , Boolean hasInsurance, Integer contentProductId, Integer contentProductAmount) {
//        TransportDto transport = Transport.builder()
        return null;
    }
}
