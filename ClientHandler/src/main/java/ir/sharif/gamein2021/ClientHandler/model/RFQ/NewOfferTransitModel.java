package ir.sharif.gamein2021.ClientHandler.model.RFQ;

import ir.sharif.gamein2021.core.entity.Team;

import javax.persistence.Column;
import java.time.LocalDateTime;

public class NewOfferTransitModel {

    private String type;
    private Integer volume;
    private Integer costPerUnit;
    private LocalDateTime earliestExpectedArrival;
    private LocalDateTime latestExpectedArrival;
    private LocalDateTime offerDeadline;

    public NewOfferTransitModel() {}

    public NewOfferTransitModel(Team team, String type, Integer volume, Integer costPerUnit,
                                LocalDateTime earliestExpectedArrival, LocalDateTime latestExpectedArrival, LocalDateTime offerDeadline) {
        this.type = type;
        this.volume = volume;
        this.costPerUnit = costPerUnit;
        this.earliestExpectedArrival = earliestExpectedArrival;
        this.latestExpectedArrival = latestExpectedArrival;
        this.offerDeadline = offerDeadline;
    }

    public String getType() {
        return type;
    }

    public Integer getVolume() {
        return volume;
    }

    public Integer getCostPerUnit() {
        return costPerUnit;
    }

    public LocalDateTime getEarliestExpectedArrival() {
        return earliestExpectedArrival;
    }

    public LocalDateTime getLatestExpectedArrival() {
        return latestExpectedArrival;
    }

    public LocalDateTime getOfferDeadline() {
        return offerDeadline;
    }

}
