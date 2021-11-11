package ir.sharif.gamein2021.core.domain.entity;

import ir.sharif.gamein2021.core.util.Enums.ContractType;
import lombok.*;

import javax.persistence.*;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_type", nullable = false)
    private ContractType contractType;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "contract_id")
    private List<ContractDetail> contractDetails;

    @Column(name = "terminate_penalty", nullable = false)
    private Integer terminatePenalty;

    @Column(name = "is_terminated", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isTerminated;
}
