package ir.sharif.gamein2021.core.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinanceWeeklyReportDto extends BaseWeeklyReport {
    private long totalCapital;
    private long inFlow;
    private long outFlow;
}
