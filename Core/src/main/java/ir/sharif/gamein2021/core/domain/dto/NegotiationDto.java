package ir.sharif.gamein2021.core.domain.dto;


import ir.sharif.gamein2021.core.domain.entity.Team;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NegotiationDto implements BaseDto<Integer> {
    public enum State
    {
        closed, deal, in_progress
    }

    private Integer id;
    private Team demander;
    private Team supplier;
    private String type;
    private Integer volume;
    private Integer costPerUnit;
    private LocalDateTime earliestExpectedArrival;
    private LocalDateTime latestExpectedArrival;
    private State state;

}
