package ir.sharif.gamein2021.core.domain.entity;

public interface InventoryWeeklyReport extends BaseWeeklyReportInterface {
    int getRawMaterialPercentage();

    int getIntermediateMaterialPercentage();

    int getFinalProductPercentage();
}
