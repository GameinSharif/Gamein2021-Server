package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.ClientHandler.util.ResponseTypeConstant;
import ir.sharif.gamein2021.ClientHandler.view.ResponseObject;
import ir.sharif.gamein2021.core.domain.dto.ProviderDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class NewProviderResponse extends ResponseObject implements Serializable
{
    private ProviderDto newProvider;
    private String result;

    public NewProviderResponse(ResponseTypeConstant responseTypeConstant, ProviderDto newProvider, String result)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.newProvider = newProvider;
        this.result = result;
    }
}
