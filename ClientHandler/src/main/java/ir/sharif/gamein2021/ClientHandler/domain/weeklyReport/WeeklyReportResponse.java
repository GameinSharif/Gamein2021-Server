package ir.sharif.gamein2021.ClientHandler.domain.weeklyReport;

import ir.sharif.gamein2021.core.domain.dto.WeeklyReportDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.io.Serializable;
import java.util.List;

public class WeeklyReportResponse extends ResponseObject implements Serializable {
    private List<WeeklyReportDto> weeklyReports;

    public WeeklyReportResponse(List<WeeklyReportDto> weeklyReports) {
        this.responseTypeConstant = ResponseTypeConstant.WEEKLY_REPORT.ordinal();
        this.weeklyReports = weeklyReports;
    }
}
