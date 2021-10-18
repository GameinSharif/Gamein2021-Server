package ir.sharif.gamein2021.core.mainThread;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@Profile(value = {"scheduled"})
public class DailySchedule
{
    @Scheduled(fixedRateString = "${dayLengthMilliSecond}")
    public void scheduledTask()
    {
        //System.out.println("daily schedule");
    }
}
