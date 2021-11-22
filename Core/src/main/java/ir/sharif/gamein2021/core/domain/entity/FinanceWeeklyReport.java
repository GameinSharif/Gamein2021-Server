package ir.sharif.gamein2021.core.domain.entity;

public interface FinanceWeeklyReport extends BaseWeeklyReportInterface {
    long getTotalCapital();

    long getInFlow();

    long getOutFlow();
}
