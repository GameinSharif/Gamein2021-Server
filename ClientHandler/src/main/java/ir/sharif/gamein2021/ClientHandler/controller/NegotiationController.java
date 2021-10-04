package ir.sharif.gamein2021.ClientHandler.controller;


import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.*;
import ir.sharif.gamein2021.ClientHandler.manager.PushMessageManager;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.domain.dto.TeamDto;
import ir.sharif.gamein2021.core.service.NegotiationService;
import ir.sharif.gamein2021.core.service.TeamService;
import ir.sharif.gamein2021.core.service.UserService;
import ir.sharif.gamein2021.core.domain.dto.UserDto;
import ir.sharif.gamein2021.core.domain.dto.NegotiationDto;
import ir.sharif.gamein2021.ClientHandler.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.domain.entity.Team;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Null;
import java.util.ArrayList;

@Component
public class NegotiationController {
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final PushMessageManager pushMessageManager;
    private final UserService userService;
    private final TeamService teamService;
    private final NegotiationService negotiationService;
    private final Gson gson = new Gson();

    public NegotiationController(PushMessageManager pushMessageManager, UserService userService,
                                 TeamService teamService,
                                 NegotiationService negotiationService){
        this.pushMessageManager = pushMessageManager;
        this.userService = userService;
        this.negotiationService = negotiationService;
        this.teamService = teamService;
    }

    public void getNegotiations(ProcessedRequest processedRequest, GetNegotiationsRequest getNegotiationsRequest){
        int playerid = getNegotiationsRequest.playerId;
        UserDto user = userService.findById(playerid);
        Team userTeam = user.getTeam();
        if(userTeam == null){
            GetNegotiationsResponse getNegotiationsResponse = new GetNegotiationsResponse(ResponseTypeConstant.GET_NEGOTIATIONS, null);
            pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(getNegotiationsResponse));
            return;
        }
        ArrayList<NegotiationDto> negotiations = negotiationService.findByTeam(userTeam);

        GetNegotiationsResponse getNegotiationsResponse = new GetNegotiationsResponse(ResponseTypeConstant.GET_NEGOTIATIONS, negotiations);
        pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(getNegotiationsResponse));

    }

    public void newNegotiation(ProcessedRequest processedRequest, NewNegotiationRequest newNegotiationRequest){
        // this negotiation doesn't need to be with a provider

        NegotiationDto newNegotiation = newNegotiationRequest.getNegotiation();
        UserDto user = userService.findById(newNegotiationRequest.playerId);
        if (user.getTeam().getId() == newNegotiation.getDemander().getId()){
            Team supplier = teamService.findTeamById(newNegotiation.getSupplier().getId());
            // TODO does request send a negotiationDto though?

            NegotiationDto savedNegotiation = negotiationService.save(newNegotiation);
            NewNegotiationResponse newNegotiationResponse;
            if(savedNegotiation != null){
                newNegotiationResponse = new NewNegotiationResponse(ResponseTypeConstant.NEW_NEGOTIATION, savedNegotiation, "success");
            }else{
                newNegotiationResponse = new NewNegotiationResponse(ResponseTypeConstant.NEW_NEGOTIATION, null, "fail");
            }
            pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(newNegotiationResponse));
        }else{
            NewNegotiationResponse newNegotiationResponse = new NewNegotiationResponse(ResponseTypeConstant.NEW_NEGOTIATION, null,"fail");
            pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(newNegotiationResponse));
        }
    }


}
