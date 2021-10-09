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
public class NewNegotiationResponse extends ResponseObject implements Serializable {

    Integer supplierId;

    public NewNegotiationResponse(ResponseTypeConstant responseTypeConstant, Integer supplierId)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.supplierId = supplierId;
    }
}
