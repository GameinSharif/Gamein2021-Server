package ir.sharif.gamein2021.core.response;

import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.io.Serializable;
import java.time.LocalDate;

public class GameTimeResponse extends ResponseObject implements Serializable
{
    private LocalDate gameDate;
    private int week;

    public GameTimeResponse(ResponseTypeConstant responseTypeConstant, LocalDate gameDate, int week)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.gameDate = gameDate;
        this.week = week;
    }

}
