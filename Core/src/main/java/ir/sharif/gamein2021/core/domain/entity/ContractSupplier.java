package ir.sharif.gamein2021.core.domain.entity;

import ir.sharif.gamein2021.core.util.Enums;
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
public class ContractSupplier implements BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_supplier_id")
    private Integer id;

    @Column(name = "contract_date", nullable = false)
    private LocalDate contractDate;

    @Column(name = "supplier_id", nullable = false)
    private Integer supplierId;

    @ManyToOne
    private Team team;

    @Column(name = "material_id", nullable = false)
    private Integer materialId;

    @Column(name = "price_per_unit")
    private Float pricePerUnit;

    @Column(name = "bought_amount", nullable = false)
    private Integer boughtAmount;

    @Column(name = "transport_type", nullable = false)
    private Enums.VehicleType transportType;

    @Column(name = "has_insurance", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean hasInsurance;

    @Column(name = "transportation_cost", nullable = false)
    private Integer transportationCost;

    @Column(name = "terminate_penalty", nullable = false)
    private Integer terminatePenalty;

    @Column(name = "is_terminated", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isTerminated;

    @Column(name = "no_money_penalty", nullable = false)
    private Integer noMoneyPenalty;

    @Override
    public Integer getId() {
        return id;
    }
}
