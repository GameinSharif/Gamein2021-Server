package ir.sharif.gamein2021.core.manager;

import ir.sharif.gamein2021.core.manager.clientHandlerConnection.ClientHandlerRequestSenderInterface;
import ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests.UpdateCalendarRequest;
import ir.sharif.gamein2021.core.util.GameConstants;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Scope("singleton")
public class GameCalendar {
    private LocalDate currentDate = GameConstants.startDate;
    private final ClientHandlerRequestSenderInterface requestSender;

    public GameCalendar(ClientHandlerRequestSenderInterface requestSender) {
        this.requestSender = requestSender;
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(LocalDate newCurrentData) {
        currentDate = newCurrentData;
    }

    public void increaseOneDay() {
        currentDate = currentDate.plusDays(1);
        requestSender.send(new UpdateCalendarRequest("nothing!", currentDate));
    }
}
