package ir.sharif.gamein2021.core.mainThread;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@Profile(value = {"scheduled"})
public class AuctionSchedule
{
    @Scheduled(fixedDelayString = "${auctionRoundLengthMilliSecond}")
    public void scheduledTask()
    {
        //TODO read active auctions from auction table and assign the factory to its winner
    }

    //use this website https://www.freeformatter.com/cron-expression-generator-quartz.html
    @Scheduled(cron = "0 0 9 25 NOV ? 2021")
    public void startAuctionPhase()
    {
        //TODO to start auction phase
    }
}
