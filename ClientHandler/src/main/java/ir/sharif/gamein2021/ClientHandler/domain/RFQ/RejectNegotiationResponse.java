package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.core.domain.dto.NegotiationDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class RejectNegotiationResponse extends ResponseObject implements Serializable {
    private NegotiationDto negotiation;

    public RejectNegotiationResponse(ResponseTypeConstant responseTypeConstant, NegotiationDto negotiation)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.negotiation = negotiation;
    }
}
