package ir.sharif.gamein2021.core.mainThread;

import ir.sharif.gamein2021.core.service.AuctionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;

@AllArgsConstructor
@Configuration
@Profile(value = {"scheduled"})
public class AuctionSchedule
{
    private AuctionService auctionService;

    @Scheduled(fixedRateString = "${auctionRoundLengthMilliSecond}")
    public void endAuctionCurrentRound()
    {
        System.out.println("Complete auction this round.");
        auctionService.completeActiveAuctions();
    }

    //Second, Minute, Hour, DayOfMonth, Month, WeekDays
    @Scheduled(cron = "0 52 0 18 10 ?")
    public void endAuctionPhase()
    {
        System.out.println("Auction is Over!");
        auctionService.assignRemainedFactoriesRandomly();
    }
}
