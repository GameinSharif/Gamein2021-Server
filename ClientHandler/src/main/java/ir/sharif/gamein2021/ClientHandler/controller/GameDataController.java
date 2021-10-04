package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.GetGameDataRequest;
import ir.sharif.gamein2021.ClientHandler.domain.GetGameDataResponse;
import ir.sharif.gamein2021.ClientHandler.manager.PushMessageManager;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.ClientHandler.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.service.GameinCustomerService;
import ir.sharif.gamein2021.core.service.WeekDemandService;
import ir.sharif.gamein2021.core.domain.dto.GameinCustomerDto;
import ir.sharif.gamein2021.core.domain.dto.WeekDemandDto;
import ir.sharif.gamein2021.core.manager.ReadJsonFilesManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GameDataController
{
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final PushMessageManager pushMessageManager;
    private final GameinCustomerService gameinCustomerService;
    private final WeekDemandService weekDemandService;
    private final Gson gson = new Gson();

    public GameDataController(PushMessageManager pushMessageManager, GameinCustomerService gameinCustomerService, WeekDemandService weekDemandService)
    {
        this.pushMessageManager = pushMessageManager;
        this.gameinCustomerService = gameinCustomerService;
        this.weekDemandService = weekDemandService;
    }

    public void getGameData(ProcessedRequest request, GetGameDataRequest getGameDataRequest)
    {
        List<GameinCustomerDto> gameinCustomers = gameinCustomerService.list();

        int week = 1; //TODO read week from another place and update it after every week
        List<WeekDemandDto> thisWeekDemands = weekDemandService.findByWeek(week);

        GetGameDataResponse getGameDataResponse = new GetGameDataResponse(ResponseTypeConstant.GET_GAME_DATA,
                gameinCustomers,
                thisWeekDemands,
                ReadJsonFilesManager.Products);

        pushMessageManager.sendMessageBySession(request.session, gson.toJson(getGameDataResponse));
    }
}