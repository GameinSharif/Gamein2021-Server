package ir.sharif.gamein2021.core.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

public class Offer extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "team_id", nullable = false)
    @JoinColumn(name = "id")
    private Team team;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Integer volume;

    @Column(name = "cost_per_unit", nullable = false)
    private Integer costPerUnit;

    @Column(name = "earliest_expected_arrival", nullable = false)
    private LocalDateTime earliestExpectedArrival;

    @Column(name = "latest_expected_arrival", nullable = false)
    private LocalDateTime latestExpectedArrival;

    @Column(name = "offer_deadline", nullable = false)
    private LocalDateTime offerDeadline;

    public Offer() {}

    public Offer(Team team, String type, Integer volume, Integer costPerUnit,
                 LocalDateTime earliestExpectedArrival, LocalDateTime latestExpectedArrival, LocalDateTime offerDeadline) {
        this.team = team;
        this.type = type;
        this.volume = volume;
        this.costPerUnit = costPerUnit;
        this.earliestExpectedArrival = earliestExpectedArrival;
        this.latestExpectedArrival = latestExpectedArrival;
        this.offerDeadline = offerDeadline;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Integer getCostPerUnit() {
        return costPerUnit;
    }

    public void setCostPerUnit(Integer costPerUnit) {
        this.costPerUnit = costPerUnit;
    }

    public LocalDateTime getEarliestExpectedArrival() {
        return earliestExpectedArrival;
    }

    public void setEarliestExpectedArrival(LocalDateTime earliestExpectedArrival) {
        this.earliestExpectedArrival = earliestExpectedArrival;
    }

    public LocalDateTime getLatestExpectedArrival() {
        return latestExpectedArrival;
    }

    public void setLatestExpectedArrival(LocalDateTime latestExpectedArrival) {
        this.latestExpectedArrival = latestExpectedArrival;
    }

    public LocalDateTime getOfferDeadline() {
        return offerDeadline;
    }

    public void setOfferDeadline(LocalDateTime offerDeadline) {
        this.offerDeadline = offerDeadline;
    }

    @Override
    public Integer getId() {
        return id;
    }

}
