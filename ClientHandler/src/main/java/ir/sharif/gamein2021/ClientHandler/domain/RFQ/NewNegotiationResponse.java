package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.ClientHandler.util.ResponseTypeConstant;
import ir.sharif.gamein2021.ClientHandler.view.ResponseObject;
import ir.sharif.gamein2021.core.domain.dto.NegotiationDto;
import ir.sharif.gamein2021.core.util.Enums.NegotiationState;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NewNegotiationResponse extends ResponseObject implements Serializable
{
    private NegotiationDto negotiation;

    public NewNegotiationResponse(ResponseTypeConstant responseTypeConstant, NegotiationDto negotiation)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.negotiation = negotiation;
    }
}
