package ir.sharif.gamein2021.core.response;

import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.io.Serializable;
import java.time.LocalDate;

public class BanResponse extends ResponseObject implements Serializable
{
    LocalDate date;

    public BanResponse(ResponseTypeConstant responseTypeConstant, LocalDate date)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.date = date;
    }
}
