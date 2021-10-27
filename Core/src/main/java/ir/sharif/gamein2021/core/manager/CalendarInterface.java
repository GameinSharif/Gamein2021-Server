package ir.sharif.gamein2021.core.manager;

import java.time.LocalDate;

public interface CalendarInterface {
    LocalDate getCurrentDate();
    LocalDate setCurrentDate();
    void increaseOneDay();
}
