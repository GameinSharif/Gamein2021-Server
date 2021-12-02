package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.core.domain.dto.ProviderDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class EditProviderResponse extends ResponseObject implements Serializable {

    private final String result;
    private final ProviderDto editedProvider;

    public EditProviderResponse(ResponseTypeConstant responseTypeConstant, String result, ProviderDto editedProvider) {
        this.result = result;
        this.editedProvider = editedProvider;
        this.responseTypeConstant = responseTypeConstant.ordinal();
    }
}
