package ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests;

import java.time.LocalDate;

public class UpdateCalendarRequest extends BaseClientHandlerRequest {
    private LocalDate newDate;
    public UpdateCalendarRequest(String message, LocalDate newDate) {
        super(message);
        this.newDate = newDate;
    }

    public LocalDate getNewDate() {
        return newDate;
    }
}
