package ir.sharif.gamein2021.core.mainThread;

import ir.sharif.gamein2021.core.manager.clientHandlerConnection.ClientHandlerRequestSenderInterface;
import ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests.UpdateCalendarRequest;
import ir.sharif.gamein2021.core.service.DynamicConfigService;
import ir.sharif.gamein2021.core.util.GameConstants;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Component
@Scope("singleton")
public class GameCalendar {
    private Integer currentWeek;
    private LocalDate currentDate;
    private final ClientHandlerRequestSenderInterface requestSender;
    private final DynamicConfigService dynamicConfigService;

    public GameCalendar(ClientHandlerRequestSenderInterface requestSender,
                        DynamicConfigService dynamicConfigService) {
        this.requestSender = requestSender;
        this.dynamicConfigService = dynamicConfigService;

        currentDate = dynamicConfigService.getCurrentDate();
        if (currentDate == null) {
            currentDate = GameConstants.startDate;
        }

        currentWeek = dynamicConfigService.getCurrentWeek();
        if (currentWeek == null) {
            currentWeek = 1;
        }

        requestSender.send(new UpdateCalendarRequest("nothing!", currentDate, currentWeek));
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public int getCurrentWeek() {
        return currentWeek;
    }

    void increaseOneDay() {
        currentDate = currentDate.plusDays(1);
        dynamicConfigService.setCurrentDate(currentDate);

        if (currentDate.getDayOfWeek() == DayOfWeek.SATURDAY) {
            currentWeek++;
            dynamicConfigService.setCurrentWeek(currentWeek);
        }

        requestSender.send(new UpdateCalendarRequest("nothing!", currentDate, currentWeek));
    }

    public void updateCalendarLocally(LocalDate newCurrentDate, Integer newCurrentWeek) {
        currentDate = newCurrentDate;
        currentWeek = newCurrentWeek;
    }

    void setCurrentDate(LocalDate newCurrentDate) {
        currentDate = newCurrentDate;
        requestSender.send(new UpdateCalendarRequest("nothing!", currentDate, currentWeek));
    }

    void setCurrentWeek(Integer newCurrentWeek) {
        currentWeek = newCurrentWeek;
        requestSender.send(new UpdateCalendarRequest("nothing!", currentDate, currentWeek));
    }
}
