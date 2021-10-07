package ir.sharif.gamein2021.core.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Provider implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Team team;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Column(name = "average_price", nullable = false)
    private Integer averagePrice;

    @Column(name = "min_price_on_record", nullable = false)
    private Integer minPriceOnRecord;

    @Column(name = "max_price_on_record", nullable = false)
    private Integer maxPriceOnRecord;

    @Override
    public Integer getId() {
        return id;
    }
}
