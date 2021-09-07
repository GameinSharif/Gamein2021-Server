package ir.sharif.gamein2021.ClientHandler.model.RFQ;

import ir.sharif.gamein2021.ClientHandler.transport.model.RequestObject;
import ir.sharif.gamein2021.core.entity.Offer;

import java.io.Serializable;

public class NewOfferRequest extends RequestObject implements Serializable {

    NewOfferTransitModel offer;

    public NewOfferTransitModel getOffer() {
        return offer;
    }

}
