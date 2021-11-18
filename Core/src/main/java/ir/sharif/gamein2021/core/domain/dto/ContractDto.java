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
    private Integer gameinCustomerId;
    private Integer productId;
    private LocalDate contractDate;
    private Integer supplyAmount;
    private Float pricePerUnit;
    private Integer boughtAmount;
    //TODO add parameters needed after calculating shares
    private Integer lostSalePenalty;
    private Integer terminatePenalty;
    private boolean isTerminated;
}
