package ir.sharif.gamein2021.core.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostsWeeklyReportDto extends BaseWeeklyReport {
    private long transportationCosts;
    private long productionCosts;
    private long marketingCosts;
}
