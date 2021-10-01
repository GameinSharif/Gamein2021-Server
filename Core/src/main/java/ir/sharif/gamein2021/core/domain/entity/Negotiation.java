package ir.sharif.gamein2021.core.domain.entity;

import ir.sharif.gamein2021.core.util.Enums.NegotiationState;
import ir.sharif.gamein2021.core.domain.dto.NegotiationDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Negotiation implements BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Team demander;

    @ManyToOne
    private Team supplier;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Integer volume;

    @Column(name = "cost_per_unit_demander", nullable = false)
    private Integer costPerUnitDemander;

    @Column(name = "cost_per_unit_supplier", nullable = false)
    private Integer costPerUnitSupplier;

    @Column(name = "earliest_expected_arrival", nullable = false)
    private LocalDateTime earliestExpectedArrival;

    @Column(name = "latest_expected_arrival", nullable = false)
    private LocalDateTime latestExpectedArrival;

    //TODO state should be string
    @Column(name = "state", nullable = false)
    private NegotiationState state;


    @Override
    public Integer getId() {
        return id;
    }
}
