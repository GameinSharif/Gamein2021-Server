package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.weeklyReport.GetAllWeeklyReportsResponse;
import ir.sharif.gamein2021.ClientHandler.manager.LocalPushMessageManager;
import ir.sharif.gamein2021.core.domain.dto.WeeklyReportDto;
import ir.sharif.gamein2021.core.service.WeeklyReportService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class WeeklyReportController {
    private final WeeklyReportService weeklyReportService;
    private final LocalPushMessageManager pushMessageManager;
    private final Gson gson = new Gson();

    public void getWeeklyReport(ProcessedRequest processedRequest) {
        List<WeeklyReportDto> weeklyReports = weeklyReportService.findAllByTeamId(processedRequest.teamId);
        GetAllWeeklyReportsResponse response = new GetAllWeeklyReportsResponse(weeklyReports);
        pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(response));
    }
}
