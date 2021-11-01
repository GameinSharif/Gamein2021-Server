package ir.sharif.gamein2021.core.domain.entity;

import ir.sharif.gamein2021.core.util.Enums.OfferStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

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

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(nullable = false)
    private Integer volume;

    @Column(name = "cost_per_unit", nullable = false)
    private Integer costPerUnit;

    @Column(name = "offer_deadline", nullable = false)
    private LocalDate offerDeadline;


    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "id=" + id +
                ", team=" + team +
                ", productId='" + productId + '\'' +
                ", volume=" + volume +
                ", costPerUnit=" + costPerUnit +
                ", offerDeadline=" + offerDeadline +
                '}';
    }
}
