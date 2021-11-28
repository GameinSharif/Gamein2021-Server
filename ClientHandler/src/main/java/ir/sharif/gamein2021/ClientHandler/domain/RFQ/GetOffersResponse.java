package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.core.domain.dto.OfferDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Getter
public class GetOffersResponse extends ResponseObject implements Serializable {

    private List<OfferDto> myTeamOffers;
    private List<OfferDto> otherTeamsOffers;
    private List<OfferDto> acceptedOffersByMyTeam;

    public GetOffersResponse(ResponseTypeConstant responseTypeConstant, List<OfferDto> myTeamOffers, List<OfferDto> otherTeamsOffers, List<OfferDto> acceptedOffersByMyTeam)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.myTeamOffers = myTeamOffers;
        this.otherTeamsOffers = otherTeamsOffers;
        this.acceptedOffersByMyTeam = acceptedOffersByMyTeam;
    }
}
