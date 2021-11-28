package ir.sharif.gamein2021.core.domain.dto;

import ir.sharif.gamein2021.core.util.Enums;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransportDto implements BaseDto<Integer> {

    private Integer id;
    private Enums.VehicleType vehicleType;
    private Enums.TransportNodeType sourceType;
    private Integer sourceId;
    private Enums.TransportNodeType destinationType;
    private Integer destinationId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean hasInsurance;
    private Enums.TransportState transportState;
    private Integer contentProductId;
    private Integer contentProductAmount;

    @Override
    public Integer getId() {
        return id;
    }
}
