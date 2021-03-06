package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.core.domain.dto.OfferDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class NewOfferResponse extends ResponseObject implements Serializable
{
    private OfferDto offer;
    private String message;

    public NewOfferResponse(ResponseTypeConstant responseTypeConstant, OfferDto offer, String message)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.message = message;
        this.offer = offer;
    }
}
