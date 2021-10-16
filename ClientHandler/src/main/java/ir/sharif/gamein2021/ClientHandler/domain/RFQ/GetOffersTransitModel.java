package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import java.time.LocalDateTime;

public class GetOffersTransitModel {

    private String teamName;
    private String type;
    private Integer volume;
    private Integer costPerUnit;
    private LocalDateTime earliestExpectedArrival;
    private LocalDateTime latestExpectedArrival;
    private LocalDateTime offerDeadline;

    public GetOffersTransitModel() {}

    public GetOffersTransitModel(String teamName, String type, Integer volume, Integer costPerUnit,
                                LocalDateTime earliestExpectedArrival, LocalDateTime latestExpectedArrival, LocalDateTime offerDeadline) {
        this.teamName = teamName;
        this.type = type;
        this.volume = volume;
        this.costPerUnit = costPerUnit;
        this.earliestExpectedArrival = earliestExpectedArrival;
        this.latestExpectedArrival = latestExpectedArrival;
        this.offerDeadline = offerDeadline;
    }

    public String getTeamName() {
        return teamName;
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
