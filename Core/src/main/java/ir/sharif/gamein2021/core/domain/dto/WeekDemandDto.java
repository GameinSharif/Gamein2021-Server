package ir.sharif.gamein2021.core.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeekDemandDto implements BaseDto<Integer>
{
    private Integer id;
    private Integer week;
    private Integer gameinCustomerId;
    private Integer productId;
    private Integer amount;
    private Integer remainedAmount;
}