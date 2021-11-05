package ir.sharif.gamein2021.core.response;

import ir.sharif.gamein2021.core.domain.dto.AuctionDto;
import ir.sharif.gamein2021.core.domain.dto.TeamDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.io.Serializable;
import java.util.List;

public class GetAllTeamsResponse extends ResponseObject implements Serializable
{
    private List<TeamDto> teams;

    public GetAllTeamsResponse(ResponseTypeConstant responseTypeConstant, List<TeamDto> teams)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.teams = teams;
    }
}
