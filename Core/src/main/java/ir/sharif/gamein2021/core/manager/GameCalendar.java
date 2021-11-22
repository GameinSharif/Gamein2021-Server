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
    private int week = 1;
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
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public int getWeek() {
        return week;
    }

    public void setCurrentDate(LocalDate newCurrentData) {
        currentDate = newCurrentData;
        if (currentDate.getDayOfWeek() == DayOfWeek.SATURDAY) {
            week++;
        }
    }

    public void increaseOneDay() {
        currentDate = currentDate.plusDays(1);
        dynamicConfigService.setCurrentDate(currentDate);
        requestSender.send(new UpdateCalendarRequest("nothing!", currentDate));
    }
}
