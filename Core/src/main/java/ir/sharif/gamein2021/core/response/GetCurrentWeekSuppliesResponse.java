package ir.sharif.gamein2021.core.response;

import ir.sharif.gamein2021.core.domain.dto.WeekSupplyDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;
import ir.sharif.gamein2021.core.domain.dto.WeekDemandDto;

import java.io.Serializable;
import java.util.List;

public class GetCurrentWeekSuppliesResponse extends ResponseObject implements Serializable
{
    private List<WeekSupplyDto> currentWeekSupplies;

    public GetCurrentWeekSuppliesResponse(ResponseTypeConstant responseTypeConstant, List<WeekSupplyDto> currentWeekSupplies)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.currentWeekSupplies = currentWeekSupplies;
    }
}
