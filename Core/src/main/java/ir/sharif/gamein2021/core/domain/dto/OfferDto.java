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
public class OfferDto implements BaseDto<Integer>{

    private Integer id;
    private Team team;
    private String type;
    private Integer volume;
    private Integer costPerUnit;
    private LocalDateTime earliestExpectedArrival;
    private LocalDateTime latestExpectedArrival;
    private LocalDateTime offerDeadline;

}
