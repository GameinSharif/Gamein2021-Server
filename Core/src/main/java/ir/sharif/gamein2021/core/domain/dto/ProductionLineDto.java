package ir.sharif.gamein2021.core.domain.dto;

import ir.sharif.gamein2021.core.util.Enums;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductionLineDto implements Serializable {
    private Integer id;
    private Integer productionLineTemplateId;
    private Integer teamId;
    private List<ProductionLineProductDto> products;
    private Integer qualityLevel;
    private Integer efficiencyLevel;
    private Enums.ProductionLineStatus status;
    private LocalDate activationDate;
}
