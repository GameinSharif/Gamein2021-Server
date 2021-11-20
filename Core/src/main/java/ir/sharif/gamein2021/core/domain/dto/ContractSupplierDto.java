package ir.sharif.gamein2021.core.domain.dto;

import ir.sharif.gamein2021.core.util.Enums;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractSupplierDto
{

    private Integer id;
    private LocalDate contractDate;
    private Integer supplierId;
    private Integer teamId;
    private Integer materialId;
    private Float pricePerUnit;
    private Integer boughtAmount;
    private Enums.VehicleType transportType;
    private boolean hasInsurance;
    private Float transportationCost;
    private Integer terminatePenalty;
    private boolean isTerminated;
    private Integer noMoneyPenalty;

}
