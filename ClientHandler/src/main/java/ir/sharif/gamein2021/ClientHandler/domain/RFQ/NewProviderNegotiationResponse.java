package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.ClientHandler.util.ResponseTypeConstant;
import ir.sharif.gamein2021.ClientHandler.view.ResponseObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;


@Getter
@AllArgsConstructor
public class NewProviderNegotiationResponse extends ResponseObject implements Serializable {
    Integer providerId;

    public NewProviderNegotiationResponse(ResponseTypeConstant responseTypeConstant, Integer providerId)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.providerId = providerId;
    }
}
