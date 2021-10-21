package ir.sharif.gamein2021.core.domain.entity;

import ir.sharif.gamein2021.core.util.Enums.OfferStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@Table(name = "Offer")
public class Offer implements BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Team team;

    @Enumerated(value = EnumType.STRING)
    private OfferStatus offerStatus;

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



    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "id=" + id +
                ", team=" + team +
                ", type='" + type + '\'' +
                ", volume=" + volume +
                ", costPerUnit=" + costPerUnit +
                ", earliestExpectedArrival=" + earliestExpectedArrival +
                ", latestExpectedArrival=" + latestExpectedArrival +
                ", offerDeadline=" + offerDeadline +
                '}';
    }
}
