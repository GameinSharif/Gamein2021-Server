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
import ir.sharif.gamein2021.core.util.Enums.NegotiationState;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        for(NegotiationDto element: negotiations){
            System.out.println(element.getId());
        }
        System.out.println("in getNego");
        GetNegotiationsResponse getNegotiationsResponse = new GetNegotiationsResponse(ResponseTypeConstant.GET_NEGOTIATIONS, negotiations);
        System.out.println("resp ready");
        pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(getNegotiationsResponse));
        System.out.println("sent");

    }

    public void newNegotiation(ProcessedRequest processedRequest, NewNegotiationRequest newNegotiationRequest){
        // this negotiation doesn't need to be with a provider
        System.out.println("beginingggg");
        //NegotiationDto requestedNegotiation = newNegotiationRequest.getNegotiationDto();
        UserDto user = userService.findById(newNegotiationRequest.playerId);
        NewNegotiationResponse newNegotiationResponse;
        System.out.println("hereeee");
        if (user != null){
            Team supplier = teamService.findTeamById(newNegotiationRequest.getSupplierId());
            if(supplier != null) {
                NegotiationDto newNegotiation = new NegotiationDto();
                newNegotiation.setDemander(user.getTeam());
                newNegotiation.setSupplier(supplier);
                newNegotiation.setCostPerUnitSupplier(newNegotiationRequest.getCostPerUnitSupplier());
                newNegotiation.setCostPerUnitDemander(newNegotiationRequest.getCostPerUnitDemander());
                newNegotiation.setType(newNegotiationRequest.getType());
                newNegotiation.setVolume(newNegotiationRequest.getVolume());
                newNegotiation.setEarliestExpectedArrival(LocalDateTime.of(2021,10, 6, 20,20,20));
                newNegotiation.setLatestExpectedArrival(LocalDateTime.of(2021,10, 10, 20,20,20));
                newNegotiation.setState(NegotiationState.in_progress);

                NegotiationDto savedNegotiation = negotiationService.save(newNegotiation);
                if (savedNegotiation != null) {
                    System.out.println("hooray");
                    newNegotiationResponse = new NewNegotiationResponse(ResponseTypeConstant.NEW_NEGOTIATION, newNegotiationRequest.getSupplierId());
                    System.out.println("before push");
                    pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(newNegotiationResponse));
                    System.out.println("after push");
                    return;
                }
            }
        }
        System.out.println("nooo");
        newNegotiationResponse = new NewNegotiationResponse(ResponseTypeConstant.NEW_NEGOTIATION, -1);
        pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(newNegotiationResponse));

    }

    public void editNegotiationCostPerUnit(ProcessedRequest processedRequest, EditNegotiationCostPerUnitRequest editRequest){
        UserDto user = userService.findById(editRequest.playerId);
        EditNegotiationCostPerUnitResponse editResponse;
        if(user != null){
            NegotiationDto negotiationDto = negotiationService.findById(editRequest.getNegotiationId());
            Team userTeam = user.getTeam();
            if(userTeam.getId() == negotiationDto.getDemander().getId()){
                negotiationDto.setCostPerUnitDemander(editRequest.getNewCostPerUnit());
                negotiationService.saveOrUpdate(negotiationDto);
                editResponse = new EditNegotiationCostPerUnitResponse(negotiationDto, "success");
                pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(editResponse));
                return;
            }else if(userTeam.getId() == negotiationDto.getSupplier().getId()){
                negotiationDto.setCostPerUnitSupplier(editRequest.getNewCostPerUnit());
                negotiationService.saveOrUpdate(negotiationDto);
                editResponse = new EditNegotiationCostPerUnitResponse(negotiationDto, "success");
                pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(editResponse));
                return;
            }
        }
        editResponse = new EditNegotiationCostPerUnitResponse(null, "fail");
        pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(editResponse));
    }




}
