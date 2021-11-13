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

    @Column(name = "price_per_unit")
    private Float pricePerUnit;

    @ManyToOne
    @JoinColumn(name = "contract_supplier_id")
    private ContractSupplier contractSupplier;

    @ManyToOne
    @JoinColumn(name = "transport_id")
    private Transport transport;

    @Override
    public Integer getId() {
        return id;
    }
}
