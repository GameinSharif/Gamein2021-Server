package ir.sharif.gamein2021.core.domain.entity;

import ir.sharif.gamein2021.core.util.Enums.Country;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;

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

    @Column(name = "factory_id", unique = true)
    private Integer factoryId;

    @Column(updatable = false)
    @Enumerated(value = EnumType.STRING)
    private Country country;

    @Column
    private Float credit;

    @Column(name = "brand", nullable = false)
    private Float brand;

    @Column
    private Float wealth;

    @Column(name = "in_flow")
    private Float inFlow;

    @Column(name = "out_flow")
    private Float outFlow;

    @Column(name = "transportation_cost")
    private Float transportationCost;

    @Column(name = "production_cost")
    private Float productionCost;

    @Column(name = "donated_money")
    private Float donatedAmount;

    @Column(name = "used_water")
    private Long usedWater;

    @Column(name = "ban_end")
    private LocalDate banEnd;



    @Override
    public Integer getId() {
        return id;
    }
}
