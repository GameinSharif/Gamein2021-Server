package ir.sharif.gamein2021.core.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
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
    private long transportationCosts;

    @Column(name = "production_costs")
    private long productionCosts;

    @Column(name = "total_capital")
    private long totalCapital;

    @Column(name = "in_flow")
    private long inFlow;

    @Column(name = "out_flow")
    private long outFlow;

    @Column(name = "raw_material_percentage")
    private int rawMaterialPercentage;

    @Column(name = "intermediate_material_percentage")
    private int intermediateMaterialPercentage;

    @Column(name = "final_product_percentage")
    private int finalProductPercentage;

    public WeeklyReport(int weekNumber, int teamId) {
        this.weekNumber = weekNumber;
        this.teamId = teamId;
    }
}
