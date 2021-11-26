package ir.sharif.gamein2021.core.domain.entity;

import ir.sharif.gamein2021.core.util.Enums;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoronaInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true, updatable = false)
    @Enumerated(EnumType.STRING)
    private Enums.Country country;
    @Column(nullable = false, updatable = false)
    private Float amountToBeCollect = 10000F;
    @Column(nullable = false)
    private Float currentCollectedAmount = 0F;
    @Column(nullable = false)
    boolean isCoronaOver = true;

}
