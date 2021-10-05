package ir.sharif.gamein2021.core.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractDetail implements BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "contract_date", nullable = false)
    private LocalDateTime contractDate;

    @Column(name = "max_amount", nullable = false)
    private Integer maxAmount;

    @Column(name = "bought_amount", nullable = false)
    private Integer boughtAmount;

    @Column(name = "price_per_unit", nullable = false)
    private Integer pricePerUnit;

    @Column(name = "lost_sale_penalty", nullable = false)
    private Integer lostSalePenalty;


    @Override
    public Integer getId() {
        return id;
    }
}
