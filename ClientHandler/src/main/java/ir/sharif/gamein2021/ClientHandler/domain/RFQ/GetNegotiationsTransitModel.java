package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.core.domain.entity.Team;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GetNegotiationsTransitModel {
    public enum State
    {
        closed, deal, in_progress
    }

    private Team demander;
    private Team supplier;
    private String type;
    private Integer volume;
    private Integer costPerUnit;
    private LocalDateTime earliestExpectedArrival;
    private LocalDateTime latestExpectedArrival;
    private State state;

    //public GetNegotiationsTransitModel() {}

    /*public GetNegotiationsTransitModel(Integer demander_team_id, Integer supplier_team_id, String type,
                                       Integer volume, Integer cost_per_unit, LocalDateTime earliest_expected_arrival,
                                       LocalDateTime latest_expected_arrival, State state)
    {
        this.demander_team_id = demander_team_id;
        this.supplier_team_id = supplier_team_id;
        this.type = type;
        this.volume = volume;
        this.cost_per_unit = cost_per_unit;
        this.earliest_expected_arrival = earliest_expected_arrival;
        this.latest_expected_arrival = latest_expected_arrival;
        this.state = state;
    }*/

}
