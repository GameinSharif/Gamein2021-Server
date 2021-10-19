package ir.sharif.gamein2021.core.mainThread;

import ir.sharif.gamein2021.core.manager.TransportManager;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@Profile(value = {"scheduled"})
@AllArgsConstructor
public class DailySchedule
{
    private final TransportManager transportManager;

    @Scheduled(fixedRateString = "${dayLengthMilliSecond}")
    public void scheduledTask()
    {
        transportManager.updateTransports();
        //System.out.println("daily schedule");
    }
}
