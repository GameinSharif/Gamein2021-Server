package ir.sharif.gamein2021.core.mainThread;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@Profile(value = {"scheduled"})
public class WeeklySchedule
{
    @Scheduled(fixedDelayString = "${weekLengthMilliSecond}")
    private void scheduledTask()
    {
        System.out.println("weekly schedule");
    }
}
