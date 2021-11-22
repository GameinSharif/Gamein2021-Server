package ir.sharif.gamein2021.core.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseWeeklyReport {
    protected int weekNumber;
    protected int teamId;
}
