package ir.sharif.gamein2021.core.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompetitionWeeklyReportDto extends BaseWeeklyReport {
    private int ranking;
    private Float brand;
}