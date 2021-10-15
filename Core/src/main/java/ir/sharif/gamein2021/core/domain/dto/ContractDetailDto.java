package ir.sharif.gamein2021.core.domain.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractDetailDto implements BaseDto<Integer>
{
    private Integer id;
    private LocalDate contractDate;
    private Integer maxAmount;
    private Integer boughtAmount;
    private Integer pricePerUnit;
    private Integer lostSalePenalty;
}
