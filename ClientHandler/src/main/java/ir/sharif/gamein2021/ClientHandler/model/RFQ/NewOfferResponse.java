package ir.sharif.gamein2021.ClientHandler.model.RFQ;

import ir.sharif.gamein2021.ClientHandler.transport.model.RequestObject;
import ir.sharif.gamein2021.ClientHandler.view.ResponseObject;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;

import java.io.Serializable;

public class NewOfferResponse extends ResponseObject implements Serializable {

    public String result;

    public NewOfferResponse(ResponseTypeConstant responseTypeConstant, String result)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.result = result;
    }

}
