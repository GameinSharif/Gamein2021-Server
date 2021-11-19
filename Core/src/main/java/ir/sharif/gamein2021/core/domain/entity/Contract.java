package ir.sharif.gamein2021.core.domain.entity;

import ir.sharif.gamein2021.core.util.Enums.ContractType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contract implements BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_id")
    private Integer id;

    @ManyToOne
    private Team team;

    @ManyToOne
    private GameinCustomer gameinCustomer;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "contract_date", nullable = false)
    private LocalDate contractDate;

    @Column(name = "supply_amount", nullable = false)
    private Integer supplyAmount;

    @Column(name = "price_per_unit", nullable = false)
    private Float pricePerUnit;

    @Column(name = "bought_amount")
    private Integer boughtAmount;

    //TODO add parameters needed after calculating shares

    @Column(name = "lost_sale_penalty")
    private Integer lostSalePenalty;

    @Column(name = "terminate_penalty", nullable = false)
    private Integer terminatePenalty;

    @Column(name = "is_terminated", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isTerminated;
}
