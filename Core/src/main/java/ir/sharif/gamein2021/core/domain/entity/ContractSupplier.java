package ir.sharif.gamein2021.core.domain.entity;

import ir.sharif.gamein2021.core.util.Enums;
import lombok.*;

import javax.persistence.*;
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

    @Column(name = "supplier_id", nullable = false)
    private Integer supplierId;

    @ManyToOne
    private Team team;

    @Column(name = "material_id", nullable = false)
    private Integer materialId;

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_type", nullable = false)
    private Enums.ContractType contractType;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "contract_supplier_id")
    private List<ContractSupplierDetail> contractSupplierDetails;

    @Column(name = "terminate_penalty", nullable = false)
    private Integer terminatePenalty;

    @Column(name = "is_terminated", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isTerminated;

    @Override
    public Integer getId() {
        return id;
    }
}
