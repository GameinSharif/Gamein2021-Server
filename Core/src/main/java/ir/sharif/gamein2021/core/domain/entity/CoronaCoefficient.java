package ir.sharif.gamein2021.core.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoronaCoefficient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer week;

    @Column(nullable = false)
    private Float coefficient;
}
