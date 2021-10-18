package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;
import ir.sharif.gamein2021.core.domain.dto.ProviderDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;

@AllArgsConstructor
@Getter
public class GetProvidersResponse extends ResponseObject implements Serializable
{
    private ArrayList<ProviderDto> myTeamProviders;
    private ArrayList<ProviderDto> otherTeamsProviders;

    public GetProvidersResponse(ResponseTypeConstant responseTypeConstant, ArrayList<ProviderDto> myTeamProviders, ArrayList<ProviderDto> otherTeamsProviders)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.myTeamProviders = myTeamProviders;
        this.otherTeamsProviders = otherTeamsProviders;
    }

}
