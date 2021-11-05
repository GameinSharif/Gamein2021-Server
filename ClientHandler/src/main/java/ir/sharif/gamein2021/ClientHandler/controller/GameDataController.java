package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.GetCurrentWeekSuppliesResponse;
import ir.sharif.gamein2021.core.domain.dto.WeekSupplyDto;
import ir.sharif.gamein2021.core.domain.dto.TeamDto;
import ir.sharif.gamein2021.core.response.GetAllAuctionsResponse;
import ir.sharif.gamein2021.ClientHandler.domain.GetCurrentWeekDemandsResponse;
import ir.sharif.gamein2021.ClientHandler.domain.GetGameDataResponse;
import ir.sharif.gamein2021.ClientHandler.manager.LocalPushMessageManager;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.response.GetAllTeamsResponse;
import ir.sharif.gamein2021.core.service.WeekSupplyService;
import ir.sharif.gamein2021.core.service.TeamService;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.domain.dto.AuctionDto;
import ir.sharif.gamein2021.core.service.AuctionService;
import ir.sharif.gamein2021.core.service.GameinCustomerService;
import ir.sharif.gamein2021.core.service.WeekDemandService;
import ir.sharif.gamein2021.core.domain.dto.GameinCustomerDto;
import ir.sharif.gamein2021.core.domain.dto.WeekDemandDto;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GameDataController
{
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final LocalPushMessageManager pushMessageManager;
    private final TeamService teamService;
    private final GameinCustomerService gameinCustomerService;
    private final WeekDemandService weekDemandService;
    private final WeekSupplyService weekSupplyService;
    private final AuctionService auctionService;
    private final Gson gson = new Gson();

    public GameDataController(LocalPushMessageManager pushMessageManager, TeamService teamService, GameinCustomerService gameinCustomerService, WeekDemandService weekDemandService,WeekSupplyService weekSupplyService, AuctionService auctionService)
    {
        this.pushMessageManager = pushMessageManager;
        this.teamService = teamService;
        this.gameinCustomerService = gameinCustomerService;
        this.weekDemandService = weekDemandService;
        this.weekSupplyService = weekSupplyService;
        this.auctionService = auctionService;
    }

    public void getGameData(ProcessedRequest request)
    {
        List<GameinCustomerDto> gameinCustomers = gameinCustomerService.list();

        GetGameDataResponse getGameDataResponse = new GetGameDataResponse(
                ResponseTypeConstant.GET_GAME_DATA,
                gameinCustomers);

        pushMessageManager.sendMessageBySession(request.session, gson.toJson(getGameDataResponse));
    }

    public void getCurrentWeekDemands(ProcessedRequest request)
    {
        int week = 1; //TODO read week from another place and update it after every week
        List<WeekDemandDto> currentWeekDemands = weekDemandService.findByWeek(week);

        GetCurrentWeekDemandsResponse getCurrentWeekDemandsResponse = new GetCurrentWeekDemandsResponse(
                ResponseTypeConstant.GET_CURRENT_WEEK_DEMANDS,
                currentWeekDemands
        );

        pushMessageManager.sendMessageBySession(request.session, gson.toJson(getCurrentWeekDemandsResponse));
    }

    public void getCurrentWeekSupplies(ProcessedRequest request)
    {
        int week = 1; //TODO read week from another place and update it after every week
        List<WeekSupplyDto> currentWeekSupplies = weekSupplyService.findByWeek(week);

        GetCurrentWeekSuppliesResponse getCurrentWeekSuppliesResponse = new GetCurrentWeekSuppliesResponse(
                ResponseTypeConstant.GET_CURRENT_WEEK_SUPPLIES,
                currentWeekSupplies
        );

        pushMessageManager.sendMessageBySession(request.session, gson.toJson(getCurrentWeekSuppliesResponse));
    }

    public void getAllAuctions(ProcessedRequest request)
    {
        List<AuctionDto> auctions = auctionService.readAllAuctionsWithStatus();

        GetAllAuctionsResponse getAllAuctionsResponse = new GetAllAuctionsResponse(
                ResponseTypeConstant.GET_ALL_AUCTIONS,
                auctions
        );

        pushMessageManager.sendMessageBySession(request.session, gson.toJson(getAllAuctionsResponse));
    }

    public void getAllTeams(ProcessedRequest request)
    {
        List<TeamDto> teams = teamService.list();

        GetAllTeamsResponse getAllTeamsResponse = new GetAllTeamsResponse(
                ResponseTypeConstant.GET_ALL_TEAMS,
                teams
        );

        pushMessageManager.sendMessageBySession(request.session, gson.toJson(getAllTeamsResponse));
    }
}
