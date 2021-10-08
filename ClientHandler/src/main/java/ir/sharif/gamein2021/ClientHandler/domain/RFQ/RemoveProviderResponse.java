package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.ClientHandler.view.ResponseObject;
import ir.sharif.gamein2021.core.domain.dto.ProviderDto;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RemoveProviderResponse extends ResponseObject {
    ProviderDto removedProvider;
}
