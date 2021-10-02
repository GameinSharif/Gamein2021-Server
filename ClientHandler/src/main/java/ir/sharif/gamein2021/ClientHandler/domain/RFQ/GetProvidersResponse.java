package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.ClientHandler.view.ResponseObject;
import ir.sharif.gamein2021.core.domain.dto.ProviderDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;

@AllArgsConstructor
@Getter
public class GetProvidersResponse extends ResponseObject implements Serializable {

    ArrayList<ProviderDto> teamProviders;
    ArrayList<ProviderDto> otherProviders;

}
