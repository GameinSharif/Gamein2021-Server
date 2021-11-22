package ir.sharif.gamein2021.core.manager;

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
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public int getCurrentWeek() {
        return currentWeek;
    }

    public void setCurrentDate(LocalDate newCurrentData) {
        currentDate = newCurrentData;
        if (currentDate.getDayOfWeek() == DayOfWeek.SATURDAY) {
            currentWeek++;
            dynamicConfigService.setCurrentWeek(currentWeek);
        }
    }

    public void increaseOneDay() {
        setCurrentDate(currentDate.plusDays(1));
        dynamicConfigService.setCurrentDate(currentDate);
        requestSender.send(new UpdateCalendarRequest("nothing!", currentDate));
    }

    public void setCurrentWeek(Integer newCurrentWeek) {
        currentWeek = newCurrentWeek;
    }
}
