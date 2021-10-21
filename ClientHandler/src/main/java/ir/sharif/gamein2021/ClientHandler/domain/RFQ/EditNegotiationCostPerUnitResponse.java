package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;
import ir.sharif.gamein2021.core.domain.dto.NegotiationDto;

import java.io.Serializable;


public class EditNegotiationCostPerUnitResponse extends ResponseObject implements Serializable
{
    private NegotiationDto negotiationDto;
    private String result;

    public EditNegotiationCostPerUnitResponse(ResponseTypeConstant responseTypeConstant, NegotiationDto negotiationDto, String result)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.negotiationDto = negotiationDto;
        this.result = result;
    }
}
