package ir.sharif.gamein2021.core.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryWeeklyReportDto extends BaseWeeklyReport {
    private int rawMaterialPercentage;
    private int intermediateMaterialPercentage;
    private int finalProductPercentage;
}
