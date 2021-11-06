package ir.sharif.gamein2021.core.mainThread;

import com.google.gson.Gson;
import ir.sharif.gamein2021.core.domain.dto.AuctionDto;
import ir.sharif.gamein2021.core.domain.dto.TeamDto;
import ir.sharif.gamein2021.core.manager.PushMessageManagerInterface;
import ir.sharif.gamein2021.core.response.GetAllAuctionsResponse;
import ir.sharif.gamein2021.core.response.AuctionFinishedResponse;
import ir.sharif.gamein2021.core.service.AuctionService;
import ir.sharif.gamein2021.core.service.TeamService;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
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

    //Second, Minute, Hour, DayOfMonth, Month, WeekDays
    @Scheduled(cron = "0 20,23,26 0 6 11 ?")
    public void endAuctionCurrentRound()
    {
        System.out.println("Complete auction this round.");

        auctionService.completeActiveAuctions();
        sendAllAuctionsDataToAllClients();
    }

    @Scheduled(cron = "2 26 0 6 11 ?")
    public void endAuctionPhase()
    {
        System.out.println("Auction is Over!");

        auctionService.assignRemainedFactoriesRandomly();
        sendAllTeamsDataToAllClients();
        //TODO not allow auction requests anymore
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
