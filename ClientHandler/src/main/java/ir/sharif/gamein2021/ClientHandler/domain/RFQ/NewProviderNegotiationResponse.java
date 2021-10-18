package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;
import ir.sharif.gamein2021.core.domain.dto.NegotiationDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;


@Getter
@AllArgsConstructor
public class NewProviderNegotiationResponse extends ResponseObject implements Serializable
{
    private NegotiationDto negotiation;

    public NewProviderNegotiationResponse(ResponseTypeConstant responseTypeConstant, NegotiationDto negotiation)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.negotiation = negotiation;
    }
}
