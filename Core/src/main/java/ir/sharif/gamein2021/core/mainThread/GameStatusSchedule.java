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

@AllArgsConstructor
@Component
@Profile(value = {"scheduled"})
public class GameStatusSchedule
{
    private final GameCalendar gameCalendar;
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

    private final ClientHandlerRequestSenderInterface clientRequestSender;

    //Second, Minute, Hour, DayOfMonth, Month, WeekDays
    @Scheduled(cron = "0 24 19 27 11 ?")
    public void startFirstPart()
    {
        setGameStatus(GameStatus.RUNNING);
    }

    @Scheduled(cron = "0 24 19 27 11 ?")
    public void startSecondPart()
    {
        setGameStatus(GameStatus.RUNNING);
    }

    @Scheduled(cron = "0 24 19 27 11 ?")
    public void startThirdPart()
    {
        setGameStatus(GameStatus.RUNNING);
    }

    @Scheduled(cron = "0 24 19 27 11 ?")
    public void startFourthPart()
    {
        setGameStatus(GameStatus.RUNNING);
    }

    public void setGameStatus(GameStatus gameStatus)
    {
        GameConstants.gameStatus = gameStatus;
        dynamicConfigService.setGameStatus(GameConstants.gameStatus);

        UpdateGameStatusRequest request = new UpdateGameStatusRequest("Done", GameConstants.gameStatus);
        clientRequestSender.send(request);

        System.out.println("GameStatus: " + GameConstants.gameStatus);
    }
}
