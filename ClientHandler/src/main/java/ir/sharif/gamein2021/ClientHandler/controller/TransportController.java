package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Transport.GetTeamTransportsRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Transport.GetTeamTransportsResponse;
import ir.sharif.gamein2021.ClientHandler.manager.LocalPushMessageManager;
import ir.sharif.gamein2021.core.domain.dto.TransportDto;
import ir.sharif.gamein2021.core.domain.dto.UserDto;
import ir.sharif.gamein2021.core.service.TransportService;
import ir.sharif.gamein2021.core.service.UserService;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


@AllArgsConstructor
@Component
public class TransportController
{
    private final LocalPushMessageManager pushMessageManager;
    private final TransportService transportService;
    private final UserService userService;
    private final Gson gson = new Gson();

    public void getTeamTransports(ProcessedRequest processedRequest, GetTeamTransportsRequest getTeamTransportsRequest)
    {
        int playerId = processedRequest.playerId;
        UserDto user = userService.loadById(playerId);
        Integer teamId = user.getTeamId();
        ArrayList<TransportDto> transportDtos = transportService.getTransportsByTeam(teamId);
        GetTeamTransportsResponse response = new GetTeamTransportsResponse(ResponseTypeConstant.GET_TEAM_TRANSPORTS, transportDtos);
        pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(response));
    }
    
}
