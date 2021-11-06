package ir.sharif.gamein2021.core.domain.dto;

import ir.sharif.gamein2021.core.domain.entity.ProductionLineProduct;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.util.Enums;
import lombok.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductionLineDto {
    private Integer id;
    private Integer productionLineTemplateId;
    private Integer teamId;
    private List<ProductionLineProduct> products;
    private Integer qualityLevel;
    private Integer efficiencyLevel;
    private Enums.ProductionLineStatus status;
}
