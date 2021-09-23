package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.ClientHandler.util.ResponseTypeConstant;
import ir.sharif.gamein2021.ClientHandler.view.ResponseObject;
import ir.sharif.gamein2021.core.domain.dto.OfferDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Getter
public class GetTeamOffersResponse extends ResponseObject implements Serializable {
    List<OfferDto> offers;
    public GetTeamOffersResponse(ResponseTypeConstant responseTypeConstant, List<OfferDto> offers) {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.offers = offers;
    }
}
