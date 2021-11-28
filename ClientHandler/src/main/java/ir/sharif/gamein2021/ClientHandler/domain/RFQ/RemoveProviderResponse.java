package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RemoveProviderResponse extends ResponseObject
{
    private Integer removedProviderId;
    private String result;

    public RemoveProviderResponse(ResponseTypeConstant responseTypeConstant, Integer removedProviderId, String result)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.removedProviderId = removedProviderId;
        this.result = result;
    }
}
