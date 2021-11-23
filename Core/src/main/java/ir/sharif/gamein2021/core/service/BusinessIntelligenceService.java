package ir.sharif.gamein2021.core.service;

import ir.sharif.gamein2021.core.domain.dto.*;
import ir.sharif.gamein2021.core.domain.entity.WeeklyReport;
import ir.sharif.gamein2021.core.manager.GameCalendar;
import ir.sharif.gamein2021.core.manager.ReadJsonFilesManager;
import ir.sharif.gamein2021.core.util.GameConstants;
import ir.sharif.gamein2021.core.util.models.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BusinessIntelligenceService {

    private final WeeklyReportService weeklyReportService;
    private final TeamService teamService;
    private final GameCalendar gameCalendar;
    private final StorageService storageService;
    private final List<TeamDto> teamsOrderedByWealthDesc;
    private final Map<Integer, TeamDto> teamsByTeamId;
    private final Map<Integer, TeamDto> teamsByFactoryId;

    public BusinessIntelligenceService(WeeklyReportService weeklyReportService,
                                       TeamService teamService,
                                       GameCalendar gameCalendar,
                                       StorageService storageService) {
        this.weeklyReportService = weeklyReportService;
        this.teamService = teamService;
        this.gameCalendar = gameCalendar;
        this.storageService = storageService;

        teamsOrderedByWealthDesc = teamService.getTeamsOrderByWealthDesc();
        teamsByTeamId = teamsOrderedByWealthDesc.stream().collect(Collectors.toMap(TeamDto::getId, x -> x));
        teamsByFactoryId = teamsOrderedByWealthDesc.stream().collect(Collectors.toMap(TeamDto::getFactoryId, x -> x));
    }

    public void prepareWeeklyReport() {
        int weakNumber = gameCalendar.getCurrentWeek();
        Map<Integer, WeeklyReport> weeklyReportByTeamId = teamsByTeamId.values().stream()
                .collect(Collectors.toMap(TeamDto::getId, x -> new WeeklyReport(weakNumber, x.getId())));

        setTeamRelatedReportData(weeklyReportByTeamId);
        setInventoryReportData(weeklyReportByTeamId);

        weeklyReportService.saveAll(weeklyReportByTeamId.values());
    }

    private void setInventoryReportData(Map<Integer, WeeklyReport> weeklyReportByTeamId) {
        for (StorageDto storageDto : storageService.list()) {
            if (storageDto.getDc()) {
                continue;
            }

            int rawMaterialUsedVolume = 0;
            int semiFinishedUsedVolume = 0;
            int finishedUsedVolume = 0;

            for (StorageProductDto product : storageDto.getProducts()) {
                Product productTemplate = ReadJsonFilesManager.ProductHashMap.get(product.getProductId());
                int volume = productTemplate.getVolumetricUnit() * product.getAmount();
                switch (productTemplate.getProductType()) {
                    case RawMaterial:
                        rawMaterialUsedVolume += volume;
                        break;
                    case SemiFinished:
                        semiFinishedUsedVolume += volume;
                        break;
                    case Finished:
                        finishedUsedVolume += volume;
                        break;
                }
            }

            TeamDto teamDto = teamsByFactoryId.get(storageDto.getBuildingId());
            WeeklyReport weeklyReport = weeklyReportByTeamId.get(teamDto.getId());

            weeklyReport.setRawMaterialPercentage(1f * rawMaterialUsedVolume / GameConstants.Instance.rawMaterialCapacity);
            weeklyReport.setIntermediateMaterialPercentage(1f * semiFinishedUsedVolume / GameConstants.Instance.semiFinishedProductCapacity);
            weeklyReport.setFinalProductPercentage(1f * finishedUsedVolume / GameConstants.Instance.finishedProductCapacity);
        }
    }

    private void setTeamRelatedReportData(Map<Integer, WeeklyReport> weeklyReportByTeamId) {
        int ranking = 1;

        for (TeamDto team : teamsOrderedByWealthDesc) {
            WeeklyReport weeklyReport = weeklyReportByTeamId.get(team.getId());
            weeklyReport.setRanking(ranking);
            weeklyReport.setBrand(team.getBrand() != null ? team.getBrand() : 0);
            weeklyReport.setInFlow(team.getInFlow() != null ? team.getInFlow() : 0);
            weeklyReport.setOutFlow(team.getOutFlow() != null ? team.getOutFlow() : 0);
            weeklyReport.setTotalCapital(team.getCredit());
            weeklyReport.setProductionCosts(team.getProductionCost() != null ? team.getOutFlow() : 0);
            weeklyReport.setTransportationCosts(team.getTransportationCost()!= null ? team.getOutFlow() : 0);

            ranking++;
        }
    }
}
