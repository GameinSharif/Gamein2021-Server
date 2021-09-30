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

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "price_per_unit", nullable = false)
    private Integer pricePerUnit;


    @Override
    public Integer getId() {
        return id;
    }
}
