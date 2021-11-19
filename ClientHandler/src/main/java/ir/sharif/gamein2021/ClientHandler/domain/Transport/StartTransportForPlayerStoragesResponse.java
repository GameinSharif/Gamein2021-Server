package ir.sharif.gamein2021.ClientHandler.domain.Transport;

import ir.sharif.gamein2021.core.domain.dto.TransportDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;

public class StartTransportForPlayerStoragesResponse extends ResponseObject {
    private final TransportDto transportDto;
    private final String response;

    public StartTransportForPlayerStoragesResponse(ResponseTypeConstant responseTypeConstant, TransportDto transportDto , String response) {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.response = response;
        this.transportDto = transportDto;
    }
}
