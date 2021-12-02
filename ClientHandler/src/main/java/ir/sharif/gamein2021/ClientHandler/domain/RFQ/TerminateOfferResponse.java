package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class TerminateOfferResponse extends ResponseObject implements Serializable
{
    private Integer terminatedOfferId;

    public TerminateOfferResponse(ResponseTypeConstant responseTypeConstant, Integer terminatedOfferId)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.terminatedOfferId = terminatedOfferId;
    }
}