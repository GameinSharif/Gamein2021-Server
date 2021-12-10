package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;
import ir.sharif.gamein2021.core.domain.dto.NegotiationDto;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class EditNegotiationCostPerUnitResponse extends ResponseObject implements Serializable
{
    private NegotiationDto negotiation;
    private String message;

    public EditNegotiationCostPerUnitResponse(ResponseTypeConstant responseTypeConstant, NegotiationDto negotiation, String message)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.negotiation = negotiation;
        this.message = message;
    }
}
