package ir.sharif.gamein2021.core.domain.dto;

import ir.sharif.gamein2021.core.util.Enums.NegotiationState;
import ir.sharif.gamein2021.core.domain.entity.Team;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NegotiationDto implements BaseDto<Integer> {

    private Integer id;
    private Integer demanderId;
    private Integer supplierId;
    private Integer productId;
    private Integer amount;
    private Float costPerUnitDemander;
    private Float costPerUnitSupplier;
    private NegotiationState state;
    private Integer sourceStorageId;

}
