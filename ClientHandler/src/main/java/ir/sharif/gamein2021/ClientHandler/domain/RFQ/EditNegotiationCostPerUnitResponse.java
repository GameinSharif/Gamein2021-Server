package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.ClientHandler.util.ResponseTypeConstant;
import ir.sharif.gamein2021.ClientHandler.view.RequestObject;
import ir.sharif.gamein2021.ClientHandler.view.ResponseObject;
import ir.sharif.gamein2021.core.domain.dto.NegotiationDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;


@Getter
@AllArgsConstructor
public class EditNegotiationCostPerUnitResponse extends ResponseObject implements Serializable {
    NegotiationDto negotiationDto;
    String result;

    public EditNegotiationCostPerUnitResponse(ResponseTypeConstant responseTypeConstant, NegotiationDto negotiationDto, String result){
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.negotiationDto = negotiationDto;
        this.result =result;
    }
}
