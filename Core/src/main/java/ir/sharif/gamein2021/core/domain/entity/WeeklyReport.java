package ir.sharif.gamein2021.core.domain.entity;

import lombok.*;

import javax.persistence.*;

@Table(indexes = @Index(columnList = "week_number, team_id"))
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeeklyReport implements BaseEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(name = "week_number")
    private int weekNumber;

    @Column(name = "team_id")
    private int teamId;

    @Column(name = "ranking")
    private int ranking;

    @Column(name = "brand")
    private Float brand;

    @Column(name = "transportation_costs")
    private Float transportationCosts;

    @Column(name = "production_costs")
    private Float productionCosts;

    @Column(name = "total_capital")
    private Float totalCapital;

    @Column(name = "in_flow")
    private Float inFlow;

    @Column(name = "out_flow")
    private Float outFlow;

    @Column(name = "raw_material_percentage")
    private Float rawMaterialPercentage;

    @Column(name = "intermediate_material_percentage")
    private Float intermediateMaterialPercentage;

    @Column(name = "final_product_percentage")
    private Float finalProductPercentage;

    public WeeklyReport(int weekNumber, int teamId) {
        this.weekNumber = weekNumber;
        this.teamId = teamId;
    }
}
