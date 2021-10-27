package ir.sharif.gamein2021.core.mainThread;

import ir.sharif.gamein2021.core.manager.GameCalendar;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Profile(value = {"scheduled"})
public class DailySchedule {
    private final GameCalendar gameCalendar;

    public DailySchedule(GameCalendar gameCalendar) {
        this.gameCalendar = gameCalendar;
    }

    @Scheduled(fixedRateString = "${dayLengthMilliSecond}")
    public void scheduledTask() {
        doDailyTasks();

        switch (gameCalendar.getCurrentDate().getDayOfWeek()) {
            case MONDAY:
                break;
            case TUESDAY:
                break;
            case WEDNESDAY:
                break;
            case THURSDAY:
                break;
            case FRIDAY:
                break;
            case SATURDAY:
                break;
            case SUNDAY:
                doWeeklyTasks();
                break;
        }

//        System.out.println("daily schedule");
        System.out.println(gameCalendar.getCurrentDate());
        gameCalendar.increaseOneDay();
    }

    private void doDailyTasks() {
    }

    private void doWeeklyTasks() {

    }
}
