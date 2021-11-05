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
public class ContractSupplierDetail implements BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "contract_date", nullable = false)
    private LocalDate contractDate;

    @Column(name = "bought_amount", nullable = false)
    private Integer boughtAmount;

    @Column(name = "price_per_unit", nullable = false)
    private Integer pricePerUnit;

    @ManyToOne
    @JoinColumn(name = "contract_supplier_id", nullable = false)
    private ContractSupplier contractSupplier;

    @Override
    public Integer getId() {
        return id;
    }
}
