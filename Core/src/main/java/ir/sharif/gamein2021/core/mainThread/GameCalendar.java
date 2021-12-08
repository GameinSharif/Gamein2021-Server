package ir.sharif.gamein2021.core.mainThread;

import ir.sharif.gamein2021.core.manager.clientHandlerConnection.ClientHandlerRequestSenderInterface;
import ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests.UpdateCalendarRequest;
import ir.sharif.gamein2021.core.service.DynamicConfigService;
import ir.sharif.gamein2021.core.util.GameConstants;
import ir.sharif.gamein2021.core.util.models.GameStatus;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Component
@Scope("singleton")
public class GameCalendar
{
    private Integer currentWeek;
    private LocalDate currentDate;
    private final ClientHandlerRequestSenderInterface requestSender;
    private final DynamicConfigService dynamicConfigService;
    private final GameStatusSchedule gameStatusSchedule;

    public GameCalendar(ClientHandlerRequestSenderInterface requestSender, DynamicConfigService dynamicConfigService, GameStatusSchedule gameStatusSchedule)
    {
        this.requestSender = requestSender;
        this.dynamicConfigService = dynamicConfigService;
        this.gameStatusSchedule = gameStatusSchedule;

        currentDate = dynamicConfigService.getCurrentDate();
        if (currentDate == null)
        {
            currentDate = GameConstants.startDate;
        }

        currentWeek = dynamicConfigService.getCurrentWeek();
        if (currentWeek == null)
        {
            currentWeek = 1;
        }

        requestSender.send(new UpdateCalendarRequest("nothing!", currentDate, currentWeek));
    }

    public LocalDate getCurrentDate()
    {
        return currentDate;
    }

    public int getCurrentWeek()
    {
        return currentWeek;
    }

    void increaseOneDay()
    {
        currentDate = currentDate.plusDays(1);
        dynamicConfigService.setCurrentDate(currentDate);

        if (currentDate.getDayOfWeek() == DayOfWeek.SATURDAY)
        {
            currentWeek++;
            dynamicConfigService.setCurrentWeek(currentWeek);

            switch (currentWeek)
            {
                case 26:
                case 51:
                case 76:
                    gameStatusSchedule.setGameStatus(GameStatus.PAUSED);
                    break;
                case 100:
                    gameStatusSchedule.setGameStatus(GameStatus.FINISHED);
            }
        }

        requestSender.send(new UpdateCalendarRequest("nothing!", currentDate, currentWeek));
    }

    public void updateCalendarLocally(LocalDate newCurrentDate, Integer newCurrentWeek)
    {
        currentDate = newCurrentDate;
        currentWeek = newCurrentWeek;
    }

    void setCurrentDate(LocalDate newCurrentDate)
    {
        currentDate = newCurrentDate;
        requestSender.send(new UpdateCalendarRequest("nothing!", currentDate, currentWeek));
    }

    void setCurrentWeek(Integer newCurrentWeek)
    {
        currentWeek = newCurrentWeek;
        requestSender.send(new UpdateCalendarRequest("nothing!", currentDate, currentWeek));
    }
}
