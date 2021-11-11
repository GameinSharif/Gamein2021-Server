package ir.sharif.gamein2021.core.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

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
    private LocalDate contractDate;

    @Column(name = "max_amount", nullable = false)
    private Integer maxAmount;

    @Column(name = "bought_amount")
    private Integer boughtAmount;

    @Column(name = "price_per_unit", nullable = false)
    private Float pricePerUnit;

    @Column(name = "lost_sale_penalty")
    private Integer lostSalePenalty;

    @ManyToOne
    @JoinColumn(name = "contract_id")
    private Contract contract;


    @Override
    public Integer getId() {
        return id;
    }
}
