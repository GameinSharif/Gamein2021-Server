package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.GetTeamTransportsRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.GetTeamTransportsResponse;
import ir.sharif.gamein2021.ClientHandler.manager.LocalPushMessageManager;
import ir.sharif.gamein2021.core.dao.TransportRepository;
import ir.sharif.gamein2021.core.domain.dto.TransportDto;
import ir.sharif.gamein2021.core.domain.dto.UserDto;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.service.TransportService;
import ir.sharif.gamein2021.core.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


@AllArgsConstructor
@Component
public class TransportController {

    private final LocalPushMessageManager pushMessageManager;
    private final TransportService transportService;
    private final UserService userService;
    private final Gson gson = new Gson();


    public void getTeamTransports(ProcessedRequest processedRequest, GetTeamTransportsRequest getTeamTransportsRequest) {
        int playerId = getTeamTransportsRequest.playerId;
        UserDto user = userService.loadById(playerId);
        Team userTeam = user.getTeam();
        ArrayList<TransportDto> transportDtos = transportService.getTransportsByTeam(userTeam.getId());
        GetTeamTransportsResponse response = new GetTeamTransportsResponse(transportDtos);
        pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(response));
    }
}
