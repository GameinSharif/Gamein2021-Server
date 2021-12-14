package ir.sharif.gamein2021.core.mainThread;

import ir.sharif.gamein2021.core.importer.DatabaseSeeder;
import ir.sharif.gamein2021.core.manager.*;
import ir.sharif.gamein2021.core.manager.clientHandlerConnection.ClientHandlerRequestSenderInterface;
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
public class DailySchedule
{
    private final GameCalendar gameCalendar;
    private final GameStatusSchedule gameStatusSchedule;
    private final ProductionLineProductService productService;
    private final TransportManager transportManager;
    private final ContractManager contractManager;
    private final ProductionLineService productionLineService;
    private final DemandAndSupplyManager demandAndSupplyManager;
    private final GameDateManager gameDateManager;
    private final WeekSupplyManager weekSupplyManager;
    private final TeamManager teamManager;
    private final NewsManager newsManager;
    private final StorageManager storageManager;
    private final DynamicConfigService dynamicConfigService;
    private final BusinessIntelligenceService businessIntelligenceService;
    private final CoronaManager coronaManager;
    private final DatabaseSeeder databaseSeeder;
    private final ClientHandlerRequestSenderInterface clientRequestSender;
    private static boolean isSeeded = false;

    @Scheduled(fixedRateString = "${dayLengthMilliSecond}")
    public void scheduledTask()
    {
        if (!isSeeded)
        {
            isSeeded = true;
//            databaseSeeder.seed();
        }

        if (GameConstants.gameStatus == GameStatus.RUNNING)
        {
            try
            {
                DoRunningStateTasks();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        updateConfigs();
    }

    private void DoRunningStateTasks()
    {
        gameCalendar.increaseOneDay();
        System.out.println(gameCalendar.getCurrentDate());

        if (gameCalendar.getCurrentDate().getDayOfWeek() == DayOfWeek.SATURDAY)
        {
            doWeeklyTasks();
        }
        doDailyTasks();
    }

    private void doDailyTasks()
    {
        gameDateManager.SendGameDateToAllUsers();
        productionLineService.enableProductionLines();
        productService.finishProductCreation(gameCalendar.getCurrentDate());
        transportManager.updateTransports();
        contractManager.updateContracts();
        teamManager.updateAllTeamsMoneyOnClient();
        try
        {
            storageManager.MaintenanceCost();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void doWeeklyTasks()
    {
        weekSupplyManager.updateWeekSupplyPrices(gameCalendar.getCurrentWeek());
        contractManager.updateGameinCustomerContracts();
        productionLineService.decreaseWeeklyMaintenanceCost();
        teamManager.updateTeamsBrands(GameConstants.brandWeeklyDecrease);
        demandAndSupplyManager.SendCurrentWeekSupplyAndDemandsToAllUsers();
        newsManager.SendNews();
        businessIntelligenceService.prepareWeeklyReport();
        coronaManager.SendCoronaInfoToAllUsers();
    }

    private void updateConfigs()
    {
        if (GameConstants.gameStatus != GameStatus.RUNNING)
        {
            LocalDate newCurrentDate = dynamicConfigService.getCurrentDate();
            if (newCurrentDate != null)
            {
                gameCalendar.setCurrentDate(newCurrentDate);
            }

            Integer newCurrentWeek = dynamicConfigService.getCurrentWeek();
            if (newCurrentWeek != null)
            {
                gameCalendar.setCurrentWeek(newCurrentWeek);
            }
        }

        GameStatus newGameStatus = dynamicConfigService.getGameStatus();
        if (newGameStatus != null)
        {
            gameStatusSchedule.setGameStatus(newGameStatus);
        }
    }
}
