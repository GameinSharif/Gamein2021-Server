package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.ClientHandler.view.ResponseObject;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;

import java.io.Serializable;
import java.util.ArrayList;

public class GetOffersResponse extends ResponseObject implements Serializable {

    ArrayList<GetOffersTransitModel> offers;

    public GetOffersResponse(ResponseTypeConstant responseTypeConstant, ArrayList<GetOffersTransitModel> offers) {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.offers = offers;
    }
}
