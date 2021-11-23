package ir.sharif.gamein2021.core.domain.entity;

import ir.sharif.gamein2021.core.util.Enums;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductionLine {
    @Id
    @Column(name = "production_line_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "production_line_template_id", nullable = false, updatable = false)
    private Integer productionLineTemplateId;

    @JoinColumn(name = "production_line_id")
    @OneToMany
    private List<ProductionLineProduct> products;

    @ManyToOne
    private Team team;

    @Column(name = "quality_level", nullable = false)
    private Integer qualityLevel;

    @Column(name = "efficiency_level", nullable = false)
    private Integer efficiencyLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Enums.ProductionLineStatus status;

    @Column(name = "activation_date", nullable = false, updatable = false)
    private LocalDate activationDate;
}
