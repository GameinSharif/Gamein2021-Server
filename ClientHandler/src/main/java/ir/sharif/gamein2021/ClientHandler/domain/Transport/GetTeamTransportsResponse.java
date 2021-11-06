package ir.sharif.gamein2021.ClientHandler.domain.Transport;

import ir.sharif.gamein2021.core.domain.dto.TransportDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.util.List;

public class GetTeamTransportsResponse extends ResponseObject {

    // TODO : separate incoming and outgoing transports?
    private List<TransportDto> myTeamTransports;

    public GetTeamTransportsResponse(ResponseTypeConstant responseTypeConstant, List<TransportDto> myTeamTransports)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.myTeamTransports = myTeamTransports;
    }
}
