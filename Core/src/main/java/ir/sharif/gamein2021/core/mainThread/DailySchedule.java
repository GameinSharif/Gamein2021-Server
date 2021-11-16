package ir.sharif.gamein2021.core.mainThread;

import ir.sharif.gamein2021.core.manager.ContractManager;
import ir.sharif.gamein2021.core.manager.GameCalendar;
import ir.sharif.gamein2021.core.manager.TransportManager;
import ir.sharif.gamein2021.core.manager.WeekSupplyManager;
import ir.sharif.gamein2021.core.domain.dto.WeekDemandDto;
import ir.sharif.gamein2021.core.manager.*;
import ir.sharif.gamein2021.core.manager.clientHandlerConnection.ClientHandlerRequestSenderInterface;
import ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests.ChangeGameStatusRequest;
import ir.sharif.gamein2021.core.response.GetCurrentWeekDemandsResponse;
import ir.sharif.gamein2021.core.service.ProductionLineProductService;
import ir.sharif.gamein2021.core.service.ProductionLineService;
import ir.sharif.gamein2021.core.util.GameConstants;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.util.models.GameStatus;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

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

    private final ClientHandlerRequestSenderInterface clientRequestSender;

    //Second, Minute, Hour, DayOfMonth, Month, WeekDays
    @Scheduled(cron = "0 49 22 13 11 ?")
    public void startGame() {
        GameConstants.gameStatus = GameStatus.RUNNING;
        ChangeGameStatusRequest request = new ChangeGameStatusRequest("Done", GameConstants.gameStatus);
        clientRequestSender.send(request);
    }

    @Scheduled(fixedRateString = "${dayLengthMilliSecond}")
    public void scheduledTask() {
        if (!GameConstants.IsGameStarted) {
            return;
        }

        System.out.println(gameCalendar.getCurrentDate());
        gameCalendar.increaseOneDay();

        doDailyTasks();

        switch (gameCalendar.getCurrentDate().getDayOfWeek()) {
            case MONDAY:
                break;
            case TUESDAY:
                break;
            case WEDNESDAY:
                break;
            case THURSDAY:
                break;
            case FRIDAY:
                break;
            case SATURDAY:
                doWeeklyTasks();
                break;
            case SUNDAY:
                break;
        }
    }

    private void doDailyTasks() {
        gameDateManager.SendGameDateToAllUsers();
        productionLineService.enableProductionLines();
        productService.finishProductCreation(gameCalendar.getCurrentDate());
        transportManager.updateTransports();
        contractManager.updateContracts();
    }

    private void doWeeklyTasks() {
        contractManager.updateGameinCustomerContracts();
        demandAndSupplyManager.SendCurrentWeekSupplyAndDemandsToAllUsers();
        productionLineService.decreaseWeeklyMaintenanceCost();
        weekSupplyManager.updateWeekSupplyPrices(gameCalendar.getWeek());
    }
}
