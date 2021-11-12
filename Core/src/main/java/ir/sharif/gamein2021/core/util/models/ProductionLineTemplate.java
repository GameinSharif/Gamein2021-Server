package ir.sharif.gamein2021.core.util.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductionLineTemplate {
    private int id;
    private String name;
    private int constructionCost;
    private int constructRequiredDays;
    private int scrapPrice;
    private int batchSize;
    private int dailyProductionRate;
    private List<EfficiencyLevel> efficiencyLevels;
    private int weeklyMaintenanceCost;
    private int setupCost;
    private int productionCostPerOneProduct;
    private List<QualityLevel> qualityLevels;
}
