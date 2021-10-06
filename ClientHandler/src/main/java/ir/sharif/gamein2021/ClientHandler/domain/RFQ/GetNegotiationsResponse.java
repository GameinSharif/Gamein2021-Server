package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.ClientHandler.util.ResponseTypeConstant;
import ir.sharif.gamein2021.ClientHandler.view.ResponseObject;
import ir.sharif.gamein2021.core.domain.dto.NegotiationDto;

import java.io.Serializable;
import java.util.ArrayList;

public class GetNegotiationsResponse extends ResponseObject implements Serializable {
    ArrayList<NegotiationDto> negotiations;

    public GetNegotiationsResponse(ResponseTypeConstant responseTypeConstant, ArrayList<NegotiationDto> negotiations){
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.negotiations = negotiations;
    }
}
