package ir.sharif.gamein2021.core.mainThread;

import ir.sharif.gamein2021.core.domain.dto.WeekDemandDto;
import ir.sharif.gamein2021.core.manager.ContractManager;
import ir.sharif.gamein2021.core.manager.DemandAndSupplyManager;
import ir.sharif.gamein2021.core.manager.GameCalendar;
import ir.sharif.gamein2021.core.manager.TransportManager;
import ir.sharif.gamein2021.core.response.GetCurrentWeekDemandsResponse;
import ir.sharif.gamein2021.core.service.ProductionLineProductService;
import ir.sharif.gamein2021.core.util.GameConstants;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
@Profile(value = {"scheduled"})
public class DailySchedule
{
    private final GameCalendar gameCalendar;
    private ProductionLineProductService productService;
    private final TransportManager transportManager;
    private final ContractManager contractManager;
    private final DemandAndSupplyManager demandAndSupplyManager;

    @Scheduled(fixedRateString = "${dayLengthMilliSecond}")
    public void scheduledTask()
    {
        doDailyTasks();

        switch (gameCalendar.getCurrentDate().getDayOfWeek())
        {
            case MONDAY:
                break;
            case TUESDAY:
                break;
            case WEDNESDAY:
                break;
            case THURSDAY:
                break;
            case FRIDAY:
                doWeeklyTasks();
                break;
            case SATURDAY:
                break;
            case SUNDAY:
                break;
        }

//        System.out.println("daily schedule");
        System.out.println(gameCalendar.getCurrentDate());
        gameCalendar.increaseOneDay();
    }

    private void doDailyTasks()
    {
        productService.finishProductCreation();
        transportManager.updateTransports();
        contractManager.updateContracts();

    }

    private void doWeeklyTasks()
    {
        contractManager.updateGameinCustomerContracts();
        GameConstants.addWeakNumber();
        demandAndSupplyManager.SendCurrentWeekSupplyAndDemandsToAllUsers();
    }
}
