package ir.sharif.gamein2021.core.domain.entity;

import ir.sharif.gamein2021.core.util.models.Supplier;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeekSupply implements BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "week", nullable = false)
    private Integer week;

    @Column(name = "supplier_id", nullable = false)
    private Integer supplierId;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "price", nullable = false)
    private Float price;

    @Column(name = "sales", nullable = false)
    private Integer sales;


    @Override
    public Integer getId()
    {
        return id;
    }
}
