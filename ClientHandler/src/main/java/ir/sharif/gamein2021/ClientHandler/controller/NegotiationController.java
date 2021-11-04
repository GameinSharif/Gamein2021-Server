package ir.sharif.gamein2021.ClientHandler.controller;


import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.*;
import ir.sharif.gamein2021.ClientHandler.manager.LocalPushMessageManager;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.domain.dto.ProviderDto;
import ir.sharif.gamein2021.core.service.NegotiationService;
import ir.sharif.gamein2021.core.service.ProviderService;
import ir.sharif.gamein2021.core.service.TeamService;
import ir.sharif.gamein2021.core.service.UserService;
import ir.sharif.gamein2021.core.domain.dto.UserDto;
import ir.sharif.gamein2021.core.domain.dto.NegotiationDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.util.Enums.NegotiationState;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class NegotiationController
{
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final LocalPushMessageManager pushMessageManager;
    private final UserService userService;
    private final TeamService teamService;
    private final NegotiationService negotiationService;
    private final ProviderService providerService;
    private final Gson gson = new Gson();

    public NegotiationController(LocalPushMessageManager pushMessageManager, UserService userService, TeamService teamService, NegotiationService negotiationService, ProviderService providerService)
    {
        this.pushMessageManager = pushMessageManager;
        this.userService = userService;
        this.negotiationService = negotiationService;
        this.teamService = teamService;
        this.providerService = providerService;
    }

    public void getNegotiations(ProcessedRequest processedRequest, GetNegotiationsRequest getNegotiationsRequest)
    {
        int playerId = getNegotiationsRequest.playerId;
        UserDto user = userService.loadById(playerId);
        Team userTeam = teamService.findTeamById(user.getTeamId());
        if (userTeam == null)
        {
            GetNegotiationsResponse getNegotiationsResponse = new GetNegotiationsResponse(ResponseTypeConstant.GET_NEGOTIATIONS, new ArrayList<>());
            pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(getNegotiationsResponse));
            return;
        }

        ArrayList<NegotiationDto> negotiations = negotiationService.findByTeam(userTeam);
        GetNegotiationsResponse getNegotiationsResponse = new GetNegotiationsResponse(ResponseTypeConstant.GET_NEGOTIATIONS, negotiations);
        pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(getNegotiationsResponse));
    }

    public void newProviderNegotiation(ProcessedRequest processedRequest, NewProviderNegotiationRequest newProviderNegotiationRequest)
    {
        UserDto user = userService.loadById(newProviderNegotiationRequest.playerId);
        NewProviderNegotiationResponse newProviderNegotiationResponse;
        if (user != null)
        {
            ProviderDto provider = providerService.findProviderById(newProviderNegotiationRequest.getProviderId());
            if (provider != null)
            {
                NegotiationDto newNegotiation = new NegotiationDto();
                newNegotiation.setDemanderId(user.getTeamId());
                newNegotiation.setSupplierId(provider.getTeamId());
                newNegotiation.setCostPerUnitDemander(newProviderNegotiationRequest.getCostPerUnitDemander());
                newNegotiation.setCostPerUnitSupplier(provider.getPrice());
                newNegotiation.setProductId(provider.getProductId());
                newNegotiation.setAmount(newProviderNegotiationRequest.getAmount());
                newNegotiation.setState(NegotiationState.IN_PROGRESS);
                NegotiationDto savedNegotiation = negotiationService.save(newNegotiation);
                //TODO check if both prices are equal

                if (savedNegotiation != null)
                {
                    newProviderNegotiationResponse = new NewProviderNegotiationResponse(ResponseTypeConstant.NEW_PROVIDER_NEGOTIATION, savedNegotiation);
                    pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(newProviderNegotiationResponse));
                    return;
                }
            }
        }
        newProviderNegotiationResponse = new NewProviderNegotiationResponse(ResponseTypeConstant.NEW_PROVIDER_NEGOTIATION, null);
        pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(newProviderNegotiationResponse));

    }

    public void editNegotiationCostPerUnit(ProcessedRequest processedRequest, EditNegotiationCostPerUnitRequest editRequest)
    {
        UserDto user = userService.loadById(editRequest.playerId);
        EditNegotiationCostPerUnitResponse editResponse;
        if (user != null)
        {
            NegotiationDto negotiationDto = negotiationService.findById(editRequest.getNegotiationId());
            Team userTeam = teamService.findTeamById(user.getTeamId());
            if (userTeam.getId().equals(negotiationDto.getDemanderId()))
            {
                negotiationDto.setCostPerUnitDemander(editRequest.getNewCostPerUnit());
                negotiationService.saveOrUpdate(negotiationDto);
                editResponse = new EditNegotiationCostPerUnitResponse(ResponseTypeConstant.EDIT_NEGOTIATION_COST_PER_UNIT, negotiationDto, "success");
                pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(editResponse));
                return;
            }
            else if (userTeam.getId().equals(negotiationDto.getSupplierId()))
            {
                negotiationDto.setCostPerUnitSupplier(editRequest.getNewCostPerUnit());
                negotiationService.saveOrUpdate(negotiationDto);
                editResponse = new EditNegotiationCostPerUnitResponse(ResponseTypeConstant.EDIT_NEGOTIATION_COST_PER_UNIT, negotiationDto, "success");
                pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(editResponse));
                return;
            }
        }
        editResponse = new EditNegotiationCostPerUnitResponse(ResponseTypeConstant.EDIT_NEGOTIATION_COST_PER_UNIT, null, "fail");
        pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(editResponse));
    }
}