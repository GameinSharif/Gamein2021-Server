package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.UpdateGameStatusResponse;
import ir.sharif.gamein2021.core.mainThread.GameCalendar;
import ir.sharif.gamein2021.core.response.GetCurrentWeekSuppliesResponse;
import ir.sharif.gamein2021.ClientHandler.domain.ServerTimeResponse;
import ir.sharif.gamein2021.core.domain.dto.WeekSupplyDto;
import ir.sharif.gamein2021.core.domain.dto.TeamDto;
import ir.sharif.gamein2021.core.response.GetAllAuctionsResponse;
import ir.sharif.gamein2021.core.response.GetCurrentWeekDemandsResponse;
import ir.sharif.gamein2021.ClientHandler.domain.GetGameDataResponse;
import ir.sharif.gamein2021.ClientHandler.manager.LocalPushMessageManager;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.service.WeekSupplyService;
import ir.sharif.gamein2021.core.service.TeamService;
import ir.sharif.gamein2021.core.util.GameConstants;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.domain.dto.AuctionDto;
import ir.sharif.gamein2021.core.domain.dto.DcDto;
import ir.sharif.gamein2021.core.domain.dto.GameinCustomerDto;
import ir.sharif.gamein2021.core.domain.dto.WeekDemandDto;
import ir.sharif.gamein2021.core.response.GetAllActiveDcResponse;
import ir.sharif.gamein2021.core.service.AuctionService;
import ir.sharif.gamein2021.core.service.DcService;
import ir.sharif.gamein2021.core.service.GameinCustomerService;
import ir.sharif.gamein2021.core.service.WeekDemandService;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Component
public class GameDataController {
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final LocalPushMessageManager pushMessageManager;
    private final TeamService teamService;
    private final GameinCustomerService gameinCustomerService;
    private final WeekDemandService weekDemandService;
    private final WeekSupplyService weekSupplyService;
    private final AuctionService auctionService;
    private final DcService dcService;
    private final GameCalendar gameCalendar;
    private final Gson gson = new Gson();

    public void getGameData(ProcessedRequest request) {
        List<TeamDto> teams = teamService.list();
        List<GameinCustomerDto> gameinCustomers = gameinCustomerService.list();

        GetGameDataResponse getGameDataResponse = new GetGameDataResponse(
                ResponseTypeConstant.GET_GAME_DATA,
                teams, gameinCustomers);

        pushMessageManager.sendMessageBySession(request.session, gson.toJson(getGameDataResponse));
    }

    public void getCurrentWeekDemands(ProcessedRequest request) {
        int week = gameCalendar.getCurrentWeek();
        List<WeekDemandDto> currentWeekDemands = weekDemandService.findByWeek(week);

        GetCurrentWeekDemandsResponse getCurrentWeekDemandsResponse = new GetCurrentWeekDemandsResponse(
                ResponseTypeConstant.GET_CURRENT_WEEK_DEMANDS,
                currentWeekDemands
        );

        pushMessageManager.sendMessageBySession(request.session, gson.toJson(getCurrentWeekDemandsResponse));
    }

    public void getCurrentWeekSupplies(ProcessedRequest request) {
        int week = gameCalendar.getCurrentWeek();
        List<WeekSupplyDto> currentWeekSupplies = weekSupplyService.findByWeek(week);

        GetCurrentWeekSuppliesResponse getCurrentWeekSuppliesResponse = new GetCurrentWeekSuppliesResponse(
                ResponseTypeConstant.GET_CURRENT_WEEK_SUPPLIES,
                currentWeekSupplies
        );

        pushMessageManager.sendMessageBySession(request.session, gson.toJson(getCurrentWeekSuppliesResponse));
    }

    public void getAllAuctions(ProcessedRequest request) {
        List<AuctionDto> auctions = auctionService.readAllAuctionsWithStatus();

        GetAllAuctionsResponse getAllAuctionsResponse = new GetAllAuctionsResponse(
                ResponseTypeConstant.GET_ALL_AUCTIONS,
                auctions
        );

        pushMessageManager.sendMessageBySession(request.session, gson.toJson(getAllAuctionsResponse));
    }

    public void getAllActiveDc(ProcessedRequest request) {
        List<DcDto> dcs = dcService.getAllActiveDc();

        GetAllActiveDcResponse getAllActiveDcResponse = new GetAllActiveDcResponse(
                ResponseTypeConstant.GET_ALL_ACTIVE_DC, dcs);

        pushMessageManager.sendMessageBySession(request.session, gson.toJson(getAllActiveDcResponse));

    }

    public void getServerTime(ProcessedRequest request) {
        ServerTimeResponse serverTimeResponse = new ServerTimeResponse(
                ResponseTypeConstant.SERVER_TIME,
                LocalDateTime.now()
        );

        pushMessageManager.sendMessageBySession(request.session, gson.toJson(serverTimeResponse));
    }

    public void getGameStatus(ProcessedRequest processedRequest) {
        UpdateGameStatusResponse response = new UpdateGameStatusResponse(GameConstants.gameStatus);
        pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(response));
    }
}
