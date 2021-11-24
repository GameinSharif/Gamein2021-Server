package ir.sharif.gamein2021.ClientHandler.domain.weeklyReport;

import ir.sharif.gamein2021.core.domain.dto.WeeklyReportDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.io.Serializable;

public class UpdateWeeklyReportResponse extends ResponseObject implements Serializable {

    private final WeeklyReportDto weeklyReport;

    public UpdateWeeklyReportResponse(WeeklyReportDto weeklyReport) {
        this.responseTypeConstant = ResponseTypeConstant.UPDATE_WEEKLY_REPORT.ordinal();
        this.weeklyReport = weeklyReport;
    }

    public WeeklyReportDto getWeeklyReport() {
        return weeklyReport;
    }
}
