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

    @ManyToOne
    private Integer supplierId;

    @ManyToOne
    private Integer teamId;

    @Column(name = "material_id", nullable = false)
    private Integer materialId;

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_type", nullable = false)
    private Enums.ContractType contractType;

    @OneToMany
    @JoinColumn(name = "contract_detail")
    private List<ContractSupplierDetail> contractSupplierDetails;

    @Column(name = "terminate_penalty", nullable = false)
    private Integer terminatePenalty;

    @Override
    public Integer getId() {
        return id;
    }
}