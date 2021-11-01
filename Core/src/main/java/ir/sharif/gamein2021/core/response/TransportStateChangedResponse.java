package ir.sharif.gamein2021.core.response;

import ir.sharif.gamein2021.core.domain.dto.TransportDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.io.Serializable;

public class TransportStateChangedResponse extends ResponseObject implements Serializable {
    private TransportDto transport;

    public TransportStateChangedResponse(ResponseTypeConstant responseTypeConstant, TransportDto transport)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.transport = transport;
    }
}
