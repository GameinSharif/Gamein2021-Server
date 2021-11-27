package ir.sharif.gamein2021.core.domain.entity;

import ir.sharif.gamein2021.core.util.Enums.NegotiationState;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDate;

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

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "cost_per_unit_demander", nullable = false)
    private Float costPerUnitDemander;

    @Column(name = "cost_per_unit_supplier", nullable = false)
    private Float costPerUnitSupplier;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private NegotiationState state;

    @ManyToOne
    private Storage sourceStorage;

    @Override
    public Integer getId() {
        return id;
    }
}
