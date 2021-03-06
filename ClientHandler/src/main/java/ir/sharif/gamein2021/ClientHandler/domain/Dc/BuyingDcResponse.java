package ir.sharif.gamein2021.ClientHandler.domain.Dc;

import ir.sharif.gamein2021.core.domain.dto.DcDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class BuyingDcResponse extends ResponseObject implements Serializable {
    private DcDto dc;
    private String result;  //TODO change to enum

    public BuyingDcResponse(ResponseTypeConstant responseTypeConstant, DcDto dc, String result) {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.dc = dc;
        this.result = result;
    }
}
