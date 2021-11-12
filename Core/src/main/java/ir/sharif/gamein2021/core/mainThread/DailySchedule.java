package ir.sharif.gamein2021.core.mainThread;

import ir.sharif.gamein2021.core.manager.ContractManager;
import ir.sharif.gamein2021.core.manager.GameCalendar;
import ir.sharif.gamein2021.core.manager.TransportManager;
import ir.sharif.gamein2021.core.service.ProductionLineProductService;
import ir.sharif.gamein2021.core.service.ProductionLineService;
import ir.sharif.gamein2021.core.util.GameConstants;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Profile(value = {"scheduled"})
public class DailySchedule {
    private final GameCalendar gameCalendar;
    private final ProductionLineProductService productService;
    private final TransportManager transportManager;
    private final ContractManager contractManager;
    private final ProductionLineService productionLineService;

    public DailySchedule(GameCalendar gameCalendar,
                         ProductionLineProductService productService,
                         TransportManager transportManager,
                         ContractManager contractManager,
                         ProductionLineService productionLineService) {
        this.gameCalendar = gameCalendar;
        this.productService = productService;
        this.transportManager = transportManager;
        this.contractManager = contractManager;
        this.productionLineService = productionLineService;
    }

    @Scheduled(cron = "0 58 19 12 11 ?")
    public void startGame(){
        GameConstants.IsGameStarted = true;
    }

    @Scheduled(fixedRateString = "${dayLengthMilliSecond}")
    public void scheduledTask() {
        if (!GameConstants.IsGameStarted) {
            return;
        }

        try {
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
                    break;
                case SUNDAY:
                    doWeeklyTasks();
                    break;
            }
        } finally {
            System.out.println(gameCalendar.getCurrentDate());
            gameCalendar.increaseOneDay();
        }
    }

    private void doDailyTasks() {
        productionLineService.enableProductionLines();
        productService.finishProductCreation(gameCalendar.getCurrentDate());
        transportManager.updateTransports();
        contractManager.updateContracts();

    }

    private void doWeeklyTasks() {
        GameConstants.addWeakNumber();
        productionLineService.decreaseWeeklyMaintenanceCost();
    }
}
