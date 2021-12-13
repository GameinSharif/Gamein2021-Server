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
    private Float amountToBeCollect;

    @Column(nullable = false)
    private Float currentCollectedAmount;

    @Column(nullable = false)
    private Boolean isCoronaOver;

}
