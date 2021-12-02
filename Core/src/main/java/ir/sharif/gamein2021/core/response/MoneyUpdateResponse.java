package ir.sharif.gamein2021.core.response;

import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.io.Serializable;

public class MoneyUpdateResponse extends ResponseObject implements Serializable
{
    private Float money;
    private Float value;
    private Float brand;

    public MoneyUpdateResponse(ResponseTypeConstant responseTypeConstant, Float money, Float value, Float brand)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.money = money;
        this.value = value;
        this.brand = brand;
    }

}
