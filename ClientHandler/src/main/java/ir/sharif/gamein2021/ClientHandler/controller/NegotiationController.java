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
        Team userTeam = user.getTeam();
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

    public void newNegotiation(ProcessedRequest processedRequest, NewNegotiationRequest newNegotiationRequest)
    {
        UserDto user = userService.loadById(newNegotiationRequest.playerId);
        NewNegotiationResponse newNegotiationResponse;
        if (user != null)
        {
            Team supplier = teamService.findTeamById(newNegotiationRequest.getSupplierId());
            if (supplier != null)
            {
                NegotiationDto newNegotiation = new NegotiationDto();
                newNegotiation.setDemander(user.getTeam());
                newNegotiation.setSupplier(supplier);
                newNegotiation.setProductId(newNegotiationRequest.getProductId());
                newNegotiation.setCostPerUnitDemander(newNegotiationRequest.getCostPerUnitDemander());
                newNegotiation.setCostPerUnitSupplier(newNegotiationRequest.getCostPerUnitDemander());
                newNegotiation.setAmount(newNegotiationRequest.getAmount());
                newNegotiation.setState(NegotiationState.PENDING);

                NegotiationDto savedNegotiation = negotiationService.save(newNegotiation);
                if (savedNegotiation != null)
                {
                    newNegotiationResponse = new NewNegotiationResponse(ResponseTypeConstant.NEW_NEGOTIATION, savedNegotiation);
                    pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(newNegotiationResponse));
                    return;
                }
            }
        }
        newNegotiationResponse = new NewNegotiationResponse(ResponseTypeConstant.NEW_NEGOTIATION, null);
        pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(newNegotiationResponse));
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
                newNegotiation.setDemander(user.getTeam());
                newNegotiation.setSupplier(provider.getTeam());
                newNegotiation.setCostPerUnitDemander(newProviderNegotiationRequest.getCostPerUnitDemander());
                newNegotiation.setCostPerUnitSupplier(newProviderNegotiationRequest.getCostPerUnitDemander());
                newNegotiation.setProductId(provider.getProductId());
                newNegotiation.setAmount(newProviderNegotiationRequest.getAmount());
                newNegotiation.setState(NegotiationState.PENDING);
                NegotiationDto savedNegotiation = negotiationService.save(newNegotiation);
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
            Team userTeam = user.getTeam();
            if (userTeam.getId().equals(negotiationDto.getDemander().getId()))
            {
                negotiationDto.setCostPerUnitDemander(editRequest.getNewCostPerUnit());
                negotiationService.saveOrUpdate(negotiationDto);
                editResponse = new EditNegotiationCostPerUnitResponse(ResponseTypeConstant.EDIT_NEGOTIATION_COST_PER_UNIT, negotiationDto, "success");
                pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(editResponse));
                return;
            }
            else if (userTeam.getId().equals(negotiationDto.getSupplier().getId()))
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