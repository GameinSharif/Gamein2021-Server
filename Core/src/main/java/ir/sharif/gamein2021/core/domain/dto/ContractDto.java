package ir.sharif.gamein2021.core.domain.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractDto implements BaseDto<Integer>
{
    private Integer id;
    private Integer teamId;
    private Integer storageId;
    private Integer gameinCustomerId;
    private Integer productId;
    private LocalDate contractDate;
    private Integer supplyAmount;
    private Float pricePerUnit;
    private Integer boughtAmount;
    private Float currentBrand;

    private Float valueShare;
    private Float demandShare;
    private Float minPrice;
    private Float maxPrice;

    private Integer lostSalePenalty;
    private Integer terminatePenalty;
    private Boolean isTerminated;
}
