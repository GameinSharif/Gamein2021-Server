package ir.sharif.gamein2021.core.domain.entity;

import ir.sharif.gamein2021.core.util.Enums.Country;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Table(name = "Team")
@DynamicUpdate
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Team implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "team_name", nullable = false, unique = true, updatable = false)
    private String teamName;

    @Column(name = "factory_id")
    private Integer factoryId;

    @Column(updatable = false)
    @Enumerated(value = EnumType.STRING)
    private Country country;
    //TODO this field should be initialize
    private float credit;

    @Column(name = "brand", nullable = false)
    private Float brand;

    @Column
    private Long wealth;

    @Column(name = "in_flow")
    private Long inFlow;

    @Column(name = "out_flow")
    private Long outFlow;

    @Column(name = "transportation_cost")
    private Long transportationCost;

    @Column(name = "production_cost")
    private Long productionCost;

    @Override
    public Integer getId() {
        return id;
    }
}
