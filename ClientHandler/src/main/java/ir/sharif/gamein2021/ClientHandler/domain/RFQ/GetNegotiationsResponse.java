package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.ClientHandler.util.ResponseTypeConstant;
import ir.sharif.gamein2021.ClientHandler.view.ResponseObject;

import java.io.Serializable;
import java.util.ArrayList;

public class GetNegotiationsResponse extends ResponseObject implements Serializable {
    ArrayList<GetNegotiationsTransitModel> negotiations;

    public GetNegotiationsResponse(ResponseTypeConstant responseTypeConstant, ArrayList<GetNegotiationsTransitModel> negotiations){
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.negotiations = negotiations;
    }
}
