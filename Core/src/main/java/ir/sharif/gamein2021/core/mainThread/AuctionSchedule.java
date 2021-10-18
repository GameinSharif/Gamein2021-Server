package ir.sharif.gamein2021.core.mainThread;

import com.google.gson.Gson;
import ir.sharif.gamein2021.core.domain.dto.AuctionDto;
import ir.sharif.gamein2021.core.manager.PushMessageManagerInterface;
import ir.sharif.gamein2021.core.response.GetAllAuctionsResponse;
import ir.sharif.gamein2021.core.service.AuctionService;
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
    private final Gson gson;

    @Scheduled(fixedRateString = "${auctionRoundLengthMilliSecond}")
    public void endAuctionCurrentRound()
    {
        System.out.println("Complete auction this round.");

        auctionService.completeActiveAuctions();
        sendAllAuctionsDataToAllClients();
    }

    //Second, Minute, Hour, DayOfMonth, Month, WeekDays
    @Scheduled(cron = "0 39 18 18 10 ?")
    public void endAuctionPhase()
    {
        System.out.println("Auction is Over!");

        auctionService.assignRemainedFactoriesRandomly();
        //TODO send all teams to all client
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
}
