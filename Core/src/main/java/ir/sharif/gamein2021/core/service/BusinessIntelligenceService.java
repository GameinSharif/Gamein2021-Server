package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.domain.dto.*;
import ir.sharif.gamein2021.core.domain.entity.WeeklyReport;
import ir.sharif.gamein2021.core.manager.GameCalendar;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BusinessIntelligenceService {

    private final WeeklyReportService weeklyReportService;
    private final TeamService teamService;
    private final GameCalendar gameCalendar;

    public BusinessIntelligenceService(WeeklyReportService weeklyReportService, TeamService teamService, GameCalendar gameCalendar) {
        this.weeklyReportService = weeklyReportService;
        this.teamService = teamService;
        this.gameCalendar = gameCalendar;
    }

    public void prepareWeeklyReport() {
        List<TeamDto> teams = teamService.getAllTeams();
        int weakNumber=  gameCalendar.getWeek();
        Map<Integer, WeeklyReport> weeklyReportByTeamId = teams.stream()
                .collect(Collectors.toMap(TeamDto::getId, x -> new WeeklyReport(weakNumber, x.getId())));
        
        setCompetitionReportData(weeklyReportByTeamId);
        setFinanceReportData(weeklyReportByTeamId);
        setCostsReportData(weeklyReportByTeamId);
        setInventoryReportData(weeklyReportByTeamId);

        weeklyReportService.saveAll(weeklyReportByTeamId.values());
    }

    private void setInventoryReportData(Map<Integer, WeeklyReport> weeklyReportByTeamId) {
    }

    private void setCostsReportData(Map<Integer, WeeklyReport> weeklyReportByTeamId) {
    }

    private void setFinanceReportData(Map<Integer, WeeklyReport> weeklyReportByTeamId) {
    }

    private void setCompetitionReportData(Map<Integer, WeeklyReport> weeklyReportByTeamId) {
        List<TeamDto> teams = teamService.getAllTeams();
    }
}
