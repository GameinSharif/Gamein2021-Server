package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Leaderboard.GetLeaderboardResponse;
import ir.sharif.gamein2021.ClientHandler.domain.UpdateGameStatusResponse;
import ir.sharif.gamein2021.core.domain.dto.*;
import ir.sharif.gamein2021.core.mainThread.GameCalendar;
import ir.sharif.gamein2021.core.response.GetCurrentWeekSuppliesResponse;
import ir.sharif.gamein2021.ClientHandler.domain.ServerTimeResponse;
import ir.sharif.gamein2021.core.response.GetAllAuctionsResponse;
import ir.sharif.gamein2021.core.response.GetCurrentWeekDemandsResponse;
import ir.sharif.gamein2021.ClientHandler.domain.GetGameDataResponse;
import ir.sharif.gamein2021.ClientHandler.manager.LocalPushMessageManager;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.service.*;
import ir.sharif.gamein2021.core.util.GameConstants;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.response.GetAllActiveDcResponse;
import ir.sharif.gamein2021.core.util.models.Ranking;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final NewsService newsService;
    private final GameCalendar gameCalendar;
    private final CoronaService coronaService;
    private final DynamicConfigService dynamicConfigService;
    private final Gson gson = new Gson();

    private static GetLeaderboardResponse cachedLeaderboardResponse;

    public void getGameData(ProcessedRequest request) {
        List<TeamDto> teams = teamService.findAllTeams();
        List<GameinCustomerDto> gameinCustomers = gameinCustomerService.list();
        List<NewsDto> newsDtos = newsService.findAllLessThanEqualCurrentWeek(gameCalendar.getCurrentWeek());
        List<CoronaInfoDto> coronaInfo = coronaService.getCoronasInfoIfCoronaIsStarted();

        GetGameDataResponse getGameDataResponse = new GetGameDataResponse(
                ResponseTypeConstant.GET_GAME_DATA,
                teams, gameinCustomers, newsDtos, coronaInfo);

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

    public void getLeaderboard(ProcessedRequest request) {
        try {
            if (dynamicConfigService.isLeaderBoardFreeze() && cachedLeaderboardResponse != null) {
                pushMessageManager.sendMessageBySession(request.session, gson.toJson(cachedLeaderboardResponse));
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<TeamDto> teamsOrderedByWealthDesc = teamService.getTeamsOrderByWealthDesc();
        List<Ranking> ranks = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            TeamDto teamDto = teamsOrderedByWealthDesc.get(i);
            ranks.add(new Ranking(teamDto.getId(), teamDto.getWealth()));
        }

        int rank = 0;
        TeamDto myTeam = null;
        for (TeamDto teamDto : teamsOrderedByWealthDesc) {
            if (teamDto.getId().equals(request.teamId)) {
                rank = teamsOrderedByWealthDesc.indexOf(teamDto) + 1;
                myTeam = teamDto;
            }
        }

        GetLeaderboardResponse getLeaderboardResponse = new GetLeaderboardResponse(
                ResponseTypeConstant.GET_LEADERBOARD,
                ranks,
                rank,
                myTeam.getWealth()
        );

        cachedLeaderboardResponse = getLeaderboardResponse;

        pushMessageManager.sendMessageBySession(request.session, gson.toJson(getLeaderboardResponse));
    }
}
