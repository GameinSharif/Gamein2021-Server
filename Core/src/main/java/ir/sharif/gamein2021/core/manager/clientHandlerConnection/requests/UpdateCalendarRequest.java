package ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests;

import java.time.LocalDate;

public class UpdateCalendarRequest extends BaseClientHandlerRequest {
    private LocalDate newDate;
    private Integer currentWeek;

    public UpdateCalendarRequest(String message, LocalDate newDate, Integer currentWeek) {
        super(message);
        this.newDate = newDate;
        this.currentWeek = currentWeek;
    }

    public LocalDate getNewDate() {
        return newDate;
    }

    public Integer getCurrentWeek() {
        return currentWeek;
    }
}
