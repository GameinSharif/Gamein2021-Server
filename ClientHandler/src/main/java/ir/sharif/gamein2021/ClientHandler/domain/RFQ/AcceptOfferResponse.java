package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.core.domain.dto.OfferDto;
import ir.sharif.gamein2021.core.domain.dto.TransportDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class AcceptOfferResponse  extends ResponseObject implements Serializable {
    private OfferDto acceptedOffer;
    private String message;

    public AcceptOfferResponse(ResponseTypeConstant responseTypeConstant, OfferDto acceptedOffer, String message)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.acceptedOffer = acceptedOffer;
        this.message = message;
    }
}
