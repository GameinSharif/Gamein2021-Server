package ir.sharif.gamein2021.core.domain.entity;


public interface CostsWeeklyReport extends BaseWeeklyReportInterface {
    long getTransportationCosts();

    long getProductionCosts();

    long getMarketingCosts();
}
