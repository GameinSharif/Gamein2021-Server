package ir.sharif.gamein2021.ClientHandler.controller;


import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Login.LoginRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Login.LoginResponse;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.GetNegotiationsRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.NewNegotiationRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.NewNegotiationTransitModel;
import ir.sharif.gamein2021.ClientHandler.manager.EncryptDecryptManager;
import ir.sharif.gamein2021.ClientHandler.manager.PushMessageManager;
import ir.sharif.gamein2021.ClientHandler.manager.SocketSessionManager;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.Service.NegotiationService;
import ir.sharif.gamein2021.core.Service.UserService;
import ir.sharif.gamein2021.core.domain.dto.UserDto;
import ir.sharif.gamein2021.core.domain.dto.NegotiationDto;
import ir.sharif.gamein2021.ClientHandler.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.domain.entity.Team;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
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

    public ArrayList<NegotiationDto> getNegotiations(GetNegotiationsRequest getNegotiationsRequest){
        int playerid = getNegotiationsRequest.playerId;
        UserDto user = userService.findById(playerid);
        Team userTeam = user.getTeam();
        return negotiationService.findByTeam(userTeam);
    }

    public void newNegotiation(NewNegotiationRequest newNegotiationRequest){
        NewNegotiationTransitModel newNegotiationTransitModel = newNegotiationRequest.getNegotiation();
        NegotiationDto negotiationDto = new NegotiationDto(newNegotiationTransitModel.getSupplier(),); // TODO how do we initialize id attribute of negotiationDto?
        negotiationService.save(negotiationDto);
        // Todo what if couldn't save new negotiation for whatever reason? save doesn't have orElseThrow ...
        return;
    }


}
