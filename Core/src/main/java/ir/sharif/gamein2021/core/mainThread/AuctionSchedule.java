package ir.sharif.gamein2021.core.mainThread;

import com.google.gson.Gson;
import ir.sharif.gamein2021.core.domain.dto.AuctionDto;
import ir.sharif.gamein2021.core.domain.dto.TeamDto;
import ir.sharif.gamein2021.core.manager.PushMessageManagerInterface;
import ir.sharif.gamein2021.core.manager.clientHandlerConnection.ClientHandlerRequestSenderInterface;
import ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests.UpdateGameStatusRequest;
import ir.sharif.gamein2021.core.response.GetAllAuctionsResponse;
import ir.sharif.gamein2021.core.response.AuctionFinishedResponse;
import ir.sharif.gamein2021.core.service.AuctionService;
import ir.sharif.gamein2021.core.service.DynamicConfigService;
import ir.sharif.gamein2021.core.service.TeamService;
import ir.sharif.gamein2021.core.util.GameConstants;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.util.models.GameStatus;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@AllArgsConstructor
@Configuration
@Profile(value = {"scheduled"})
public class AuctionSchedule
{
    private final PushMessageManagerInterface pushMessageManager;
    private final AuctionService auctionService;
    private final TeamService teamService;
    private final Gson gson;
    private final GameStatusSchedule gameStatusSchedule;

    @Scheduled(cron = "0 30 17 15 12 ?")
    public void startAuction()
    {
        gameStatusSchedule.setGameStatus(GameStatus.AUCTION);
    }

    //Second, Minute, Hour, DayOfMonth, Month, WeekDays
    @Scheduled(cron = "0 35,40,45 17 15 12 ?")
    public void endAuctionCurrentRound()
    {
        System.out.println("Complete auction this round.");

        auctionService.completeActiveAuctions();
        sendAllAuctionsDataToAllClients();
    }

    @Scheduled(cron = "2 45 17 15 12 ?")
    public void endAuctionPhase()
    {
        System.out.println("Auction is Over!");

        auctionService.assignRemainedFactoriesRandomly();
        sendAllTeamsDataToAllClients();
    }

    private void sendAllAuctionsDataToAllClients()
    {
        List<AuctionDto> auctions = auctionService.readAllAuctionsWithStatus();

        GetAllAuctionsResponse getAllAuctionsResponse = new GetAllAuctionsResponse(
                ResponseTypeConstant.GET_ALL_AUCTIONS,
                auctions
        );

        pushMessageManager.sendMessageToAll(gson.toJson(getAllAuctionsResponse));
    }

    public void sendAllTeamsDataToAllClients()
    {
        List<TeamDto> teams = teamService.list();

        AuctionFinishedResponse auctionFinishedResponse = new AuctionFinishedResponse(
                ResponseTypeConstant.AUCTION_FINISHED,
                teams
        );

        pushMessageManager.sendMessageToAll(gson.toJson(auctionFinishedResponse));
    }
}
