package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;
import ir.sharif.gamein2021.core.domain.dto.ProviderDto;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RemoveProviderResponse extends ResponseObject
{
    private ProviderDto removedProvider;
    private String result;

    public RemoveProviderResponse(ResponseTypeConstant responseTypeConstant, ProviderDto removedProvider, String result)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.removedProvider = removedProvider;
        this.result = result;
    }
}
