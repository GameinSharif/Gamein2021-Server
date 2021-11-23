package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.domain.dto.*;
import ir.sharif.gamein2021.core.domain.entity.WeeklyReport;
import ir.sharif.gamein2021.core.manager.GameCalendar;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BusinessIntelligenceService {

    private final WeeklyReportService weeklyReportService;
    private final TeamService teamService;
    private final GameCalendar gameCalendar;
    private final Map<Integer, TeamDto> teamsByTeamId;

    public BusinessIntelligenceService(WeeklyReportService weeklyReportService, TeamService teamService, GameCalendar gameCalendar) {
        this.weeklyReportService = weeklyReportService;
        this.teamService = teamService;
        this.gameCalendar = gameCalendar;

        List<TeamDto> teams = teamService.getAllTeams();
        teamsByTeamId = teams.stream().collect(Collectors.toMap(TeamDto::getId, x->x));
    }

    public void prepareWeeklyReport() {
        int weakNumber=  gameCalendar.getCurrentWeek();
        Map<Integer, WeeklyReport> weeklyReportByTeamId = teamsByTeamId.values().stream()
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
        for (TeamDto team : teamsByTeamId.values()) {
            WeeklyReport weeklyReport = weeklyReportByTeamId.get(team.getId());
            weeklyReport.setBrand(team.getBrand());
        }
    }
}
