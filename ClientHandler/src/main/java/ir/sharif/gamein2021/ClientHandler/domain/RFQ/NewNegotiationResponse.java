package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.ClientHandler.util.ResponseTypeConstant;
import ir.sharif.gamein2021.ClientHandler.view.ResponseObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class NewNegotiationResponse extends ResponseObject implements Serializable {

    public String result;

    public NewNegotiationResponse(ResponseTypeConstant responseTypeConstant, String result)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.result = result;
    }
}
