package ir.sharif.gamein2021.core.response;

import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;
import ir.sharif.gamein2021.core.domain.dto.WeekDemandDto;

import java.io.Serializable;
import java.util.List;

public class GetCurrentWeekDemandsResponse extends ResponseObject implements Serializable
{
    private List<WeekDemandDto> currentWeekDemands;

    public GetCurrentWeekDemandsResponse(ResponseTypeConstant responseTypeConstant, List<WeekDemandDto> currentWeekDemands)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.currentWeekDemands = currentWeekDemands;
    }
}
