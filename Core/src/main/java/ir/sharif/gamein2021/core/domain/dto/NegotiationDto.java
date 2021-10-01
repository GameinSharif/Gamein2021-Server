package ir.sharif.gamein2021.core.domain.dto;

import ir.sharif.gamein2021.core.util.Enums.NegotiationState;
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

    private Integer id;
    private Team demander;
    private Team supplier;
    private String type;
    private Integer volume;
    private Integer costPerUnitDemander;
    private Integer costPerUnitSupplier;
    private LocalDateTime earliestExpectedArrival;
    private LocalDateTime latestExpectedArrival;
    private NegotiationState state;

}
