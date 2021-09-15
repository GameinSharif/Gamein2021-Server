package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.ClientHandler.transport.model.RequestObject;
import ir.sharif.gamein2021.core.domain.dto.OfferDto;

import java.io.Serializable;

public class NewOfferRequest extends RequestObject implements Serializable {

    OfferDto offer;

    public OfferDto getOffer() {
        return offer;
    }

}
