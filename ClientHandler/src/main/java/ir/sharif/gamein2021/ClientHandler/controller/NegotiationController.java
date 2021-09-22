package ir.sharif.gamein2021.ClientHandler.controller;


import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.*;
import ir.sharif.gamein2021.ClientHandler.manager.PushMessageManager;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.service.NegotiationService;
import ir.sharif.gamein2021.core.service.UserService;
import ir.sharif.gamein2021.core.domain.dto.UserDto;
import ir.sharif.gamein2021.core.domain.dto.NegotiationDto;
import ir.sharif.gamein2021.ClientHandler.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.domain.entity.Team;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class NegotiationController {
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final PushMessageManager pushMessageManager;
    private final UserService userService;
    private final NegotiationService negotiationService;
    private final Gson gson = new Gson();

    public NegotiationController(PushMessageManager pushMessageManager, UserService userService,
                                 NegotiationService negotiationService){
        this.pushMessageManager = pushMessageManager;
        this.userService = userService;
        this.negotiationService = negotiationService;
    }

    public void getNegotiations(ProcessedRequest processedRequest, GetNegotiationsRequest getNegotiationsRequest){
        int playerid = getNegotiationsRequest.playerId;
        UserDto user = userService.findById(playerid);
        Team userTeam = user.getTeam();
        ArrayList<NegotiationDto> negotiations = negotiationService.findByTeam(userTeam);
        ArrayList<GetNegotiationsTransitModel> getNegotiationsTransitModels = new ArrayList<>();
        for(NegotiationDto element : negotiations){
            getNegotiationsTransitModels.add(new GetNegotiationsTransitModel(element.getId(), element.getDemander().getTeamName(),
                    element.getSupplier().getTeamName(), element.getType(), element.getVolume(), element.getCostPerUnit(),
                    element.getEarliestExpectedArrival(), element.getLatestExpectedArrival(), element.getState()));
        }
        GetNegotiationsResponse getNegotiationsResponse = new GetNegotiationsResponse(ResponseTypeConstant.GET_NEGOTIATIONS, getNegotiationsTransitModels);
        pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(getNegotiationsResponse));

    }

    public void newNegotiation(NewNegotiationRequest newNegotiationRequest){
        NewNegotiationTransitModel newNegotiationTransitModel = newNegotiationRequest.getNegotiation();
        // Todo use provider service to check if provider id is valid, or if it provides this specific type
        NegotiationDto negotiationDto = new NegotiationDto(); // TODO how do we initialize id attribute of negotiationDto?
        negotiationService.save(negotiationDto);
        // Todo what if couldn't save new negotiation for whatever reason? save doesn't have orElseThrow ...
        return;
    }


}
