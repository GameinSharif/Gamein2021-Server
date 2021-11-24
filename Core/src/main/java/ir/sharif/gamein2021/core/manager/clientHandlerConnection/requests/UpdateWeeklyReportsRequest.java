package ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests;

import ir.sharif.gamein2021.core.domain.dto.WeeklyReportDto;

import java.util.List;

public class UpdateWeeklyReportsRequest extends BaseClientHandlerRequest {
    private List<WeeklyReportDto> weeklyReports;

    public UpdateWeeklyReportsRequest(String message, List<WeeklyReportDto> weeklyReports) {
        super(message);
        this.weeklyReports = weeklyReports;
    }

    public List<WeeklyReportDto> getWeeklyReports() {
        return weeklyReports;
    }
}
