package ir.sharif.gamein2021.core.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeeklyReportDto extends BaseWeeklyReport
{
    private int ranking;
    private Float brand;
    private Float rawMaterialPercentage;
    private Float intermediateMaterialPercentage;
    private Float finalProductPercentage;
    private Float totalCapital;
    private Float inFlow;
    private Float outFlow;
    private Float transportationCosts;
    private Float productionCosts;
}
