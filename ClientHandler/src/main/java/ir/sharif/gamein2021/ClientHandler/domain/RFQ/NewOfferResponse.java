package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.ClientHandler.util.ResponseTypeConstant;
import ir.sharif.gamein2021.ClientHandler.view.ResponseObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class NewOfferResponse extends ResponseObject implements Serializable {
    private final String message;

    public NewOfferResponse(ResponseTypeConstant responseTypeConstant, String message) {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.message = message;
    }
}
