package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.core.domain.dto.TransportDto;
import ir.sharif.gamein2021.core.view.ResponseObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

@AllArgsConstructor
@Getter
public class GetTeamTransportsResponse extends ResponseObject {

    // TODO : separate incoming and outgoing transports?
    private ArrayList<TransportDto> myTeamTransports;

}
