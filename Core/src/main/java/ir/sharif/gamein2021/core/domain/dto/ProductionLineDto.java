package ir.sharif.gamein2021.core.domain.dto;

import ir.sharif.gamein2021.core.domain.entity.ProductionLineProduct;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.util.Enums;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductionLineDto {
    private Integer id;
    private Integer productionLineTemplateId;
    private List<ProductionLineProduct> products;
    private Team team;
    private Enums.QualityLevel qualityLevel;
    private Enums.EfficiencyLevel efficiencyLevel;
    private Enums.ProductionLineStatus status;
}
