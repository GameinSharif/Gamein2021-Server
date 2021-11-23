package ir.sharif.gamein2021.core.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeeklyReportDto extends BaseWeeklyReport {
    private int rawMaterialPercentage;
    private int intermediateMaterialPercentage;
    private int finalProductPercentage;
    private long totalCapital;
    private long inFlow;
    private long outFlow;
    private long transportationCosts;
    private long productionCosts;
    private int ranking;
    private Float brand;
}
