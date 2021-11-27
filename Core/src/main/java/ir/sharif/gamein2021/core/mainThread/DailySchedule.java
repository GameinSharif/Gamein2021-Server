package ir.sharif.gamein2021.core.mainThread;

import ir.sharif.gamein2021.core.manager.*;
import ir.sharif.gamein2021.core.manager.clientHandlerConnection.ClientHandlerRequestSenderInterface;
import ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests.UpdateGameStatusRequest;
import ir.sharif.gamein2021.core.service.BusinessIntelligenceService;
import ir.sharif.gamein2021.core.service.DynamicConfigService;
import ir.sharif.gamein2021.core.service.ProductionLineProductService;
import ir.sharif.gamein2021.core.service.ProductionLineService;
import ir.sharif.gamein2021.core.util.GameConstants;
import ir.sharif.gamein2021.core.util.models.GameStatus;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;

@AllArgsConstructor
@Component
@Profile(value = {"scheduled"})
public class DailySchedule {
    private final GameCalendar gameCalendar;
    private final ProductionLineProductService productService;
    private final TransportManager transportManager;
    private final ContractManager contractManager;
    private final ProductionLineService productionLineService;
    private final DemandAndSupplyManager demandAndSupplyManager;
    private final GameDateManager gameDateManager;
    private final WeekSupplyManager weekSupplyManager;
    private final TeamManager teamManager;
    private final DynamicConfigService dynamicConfigService;
    private final BusinessIntelligenceService businessIntelligenceService;

    private final ClientHandlerRequestSenderInterface clientRequestSender;

    //Second, Minute, Hour, DayOfMonth, Month, WeekDays
    @Scheduled(cron = "0 44 18 27 11 ?")
    public void startGame()
    {
        GameConstants.gameStatus = GameStatus.RUNNING;
        dynamicConfigService.setGameStatus(GameConstants.gameStatus);
        UpdateGameStatusRequest request = new UpdateGameStatusRequest("Done", GameConstants.gameStatus);
        clientRequestSender.send(request);
    }

    @Scheduled(fixedRateString = "${dayLengthMilliSecond}")
    public void scheduledTask() {
        if (GameConstants.gameStatus == GameStatus.RUNNING) {
            try {
                DoRunningStateTasks();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        updateConfigs();
    }

    private void DoRunningStateTasks()
    {
        gameCalendar.increaseOneDay();
        System.out.println(gameCalendar.getCurrentDate());

        doDailyTasks();
        if (gameCalendar.getCurrentDate().getDayOfWeek() == DayOfWeek.SATURDAY)
        {
            doWeeklyTasks();
        }
    }

    private void doDailyTasks()
    {
        gameDateManager.SendGameDateToAllUsers();
        productionLineService.enableProductionLines();
        productService.finishProductCreation(gameCalendar.getCurrentDate());
        transportManager.updateTransports();
        contractManager.updateContracts();
        teamManager.updateAllTeamsMoneyOnClient();
    }

    private void doWeeklyTasks()
    {
        contractManager.updateGameinCustomerContracts();
        productionLineService.decreaseWeeklyMaintenanceCost();
        teamManager.updateTeamsBrands(GameConstants.brandDailyDecrease);
        weekSupplyManager.updateWeekSupplyPrices(gameCalendar.getCurrentWeek());
        demandAndSupplyManager.SendCurrentWeekSupplyAndDemandsToAllUsers();
        businessIntelligenceService.prepareWeeklyReport();
    }

    private void updateConfigs() {
        if (GameConstants.gameStatus != GameStatus.RUNNING) {
            LocalDate newCurrentDate = dynamicConfigService.getCurrentDate();
            if (newCurrentDate != null) {
                gameCalendar.setCurrentDate(newCurrentDate);
            }

            Integer newCurrentWeek = dynamicConfigService.getCurrentWeek();
            if (newCurrentWeek != null) {
                gameCalendar.setCurrentWeek(newCurrentWeek);
            }
        }

        GameStatus newGameStatus = dynamicConfigService.getGameStatus();
        if (newGameStatus != null) {
            GameConstants.gameStatus = newGameStatus;
        }
    }
}
