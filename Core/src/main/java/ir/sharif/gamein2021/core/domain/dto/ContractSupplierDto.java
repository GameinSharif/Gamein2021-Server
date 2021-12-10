package ir.sharif.gamein2021.core.domain.dto;

import ir.sharif.gamein2021.core.util.Enums;
import lombok.*;

import java.time.LocalDate;

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
    private Boolean hasInsurance;
    private Float transportationCost;
    private Float terminatePenalty;
    private Boolean isTerminated;
    private Float noMoneyPenalty;
}
