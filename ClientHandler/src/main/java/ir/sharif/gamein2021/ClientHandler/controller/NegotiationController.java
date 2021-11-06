package ir.sharif.gamein2021.ClientHandler.controller;


import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.*;
import ir.sharif.gamein2021.ClientHandler.manager.LocalPushMessageManager;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.domain.dto.ProviderDto;
import ir.sharif.gamein2021.core.manager.GameCalendar;
import ir.sharif.gamein2021.core.manager.PushMessageManagerInterface;
import ir.sharif.gamein2021.core.manager.TransportManager;
import ir.sharif.gamein2021.core.service.NegotiationService;
import ir.sharif.gamein2021.core.service.ProviderService;
import ir.sharif.gamein2021.core.service.TeamService;
import ir.sharif.gamein2021.core.service.UserService;
import ir.sharif.gamein2021.core.domain.dto.UserDto;
import ir.sharif.gamein2021.core.domain.dto.NegotiationDto;
import ir.sharif.gamein2021.core.util.Enums;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.util.Enums.NegotiationState;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@AllArgsConstructor
@Component
public class NegotiationController
{
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final LocalPushMessageManager localPushMessageManager;
    private final PushMessageManagerInterface pushMessageManager;
    private final TransportManager transportManager;
    private final UserService userService;
    private final TeamService teamService;
    private final NegotiationService negotiationService;
    private final ProviderService providerService;
    private final GameCalendar gameCalendar;
    private final Gson gson = new Gson();

    public void getNegotiations(ProcessedRequest processedRequest, GetNegotiationsRequest getNegotiationsRequest)
    {
        int playerId = getNegotiationsRequest.playerId;
        UserDto user = userService.loadById(playerId);
        Team userTeam = teamService.findTeamById(user.getTeamId());
        if (userTeam == null)
        {
            GetNegotiationsResponse getNegotiationsResponse = new GetNegotiationsResponse(ResponseTypeConstant.GET_NEGOTIATIONS, new ArrayList<>());
            localPushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(getNegotiationsResponse));
            return;
        }

        ArrayList<NegotiationDto> negotiations = negotiationService.findByTeam(userTeam);
        GetNegotiationsResponse getNegotiationsResponse = new GetNegotiationsResponse(ResponseTypeConstant.GET_NEGOTIATIONS, negotiations);
        localPushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(getNegotiationsResponse));
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
                if (newNegotiation.getCostPerUnitDemander().equals(newNegotiation.getCostPerUnitSupplier()))
                {
                    //TODO check if supplier has product and demander has money
                    //TODO check if demander has transport money
                    newNegotiation.setState(NegotiationState.DEAL);
                    startTransport(newNegotiation);
                }
                NegotiationDto savedNegotiation = negotiationService.save(newNegotiation);

                if (savedNegotiation != null)
                {
                    newProviderNegotiationResponse = new NewProviderNegotiationResponse(ResponseTypeConstant.NEW_PROVIDER_NEGOTIATION, savedNegotiation);
                    pushMessageManager.sendMessageByTeamId(savedNegotiation.getDemanderId().toString(), gson.toJson(newProviderNegotiationResponse));
                    pushMessageManager.sendMessageByTeamId(savedNegotiation.getSupplierId().toString(), gson.toJson(newProviderNegotiationResponse));
                    return;
                }
            }
        }
        newProviderNegotiationResponse = new NewProviderNegotiationResponse(ResponseTypeConstant.NEW_PROVIDER_NEGOTIATION, null);
        localPushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(newProviderNegotiationResponse));
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
                if (negotiationDto.getCostPerUnitDemander().equals(negotiationDto.getCostPerUnitSupplier()))
                {
                    //TODO check if supplier has product and demander has money
                    //TODO check if demander has transport money
                    negotiationDto.setState(NegotiationState.DEAL);
                    startTransport(negotiationDto);
                }
                negotiationService.saveOrUpdate(negotiationDto);
                editResponse = new EditNegotiationCostPerUnitResponse(ResponseTypeConstant.EDIT_NEGOTIATION_COST_PER_UNIT, negotiationDto);
                pushMessageManager.sendMessageByTeamId(negotiationDto.getDemanderId().toString(), gson.toJson(editResponse));
                pushMessageManager.sendMessageByTeamId(negotiationDto.getSupplierId().toString(), gson.toJson(editResponse));
                return;
            }
            else if (userTeam.getId().equals(negotiationDto.getSupplierId()))
            {
                negotiationDto.setCostPerUnitSupplier(editRequest.getNewCostPerUnit());
                if (negotiationDto.getCostPerUnitDemander().equals(negotiationDto.getCostPerUnitSupplier()))
                {
                    //TODO check if supplier has product and demander has money
                    //TODO check if demander has transport money
                    negotiationDto.setState(NegotiationState.DEAL);
                    startTransport(negotiationDto);
                }
                negotiationService.saveOrUpdate(negotiationDto);
                editResponse = new EditNegotiationCostPerUnitResponse(ResponseTypeConstant.EDIT_NEGOTIATION_COST_PER_UNIT, negotiationDto);
                pushMessageManager.sendMessageByTeamId(negotiationDto.getDemanderId().toString(), gson.toJson(editResponse));
                pushMessageManager.sendMessageByTeamId(negotiationDto.getSupplierId().toString(), gson.toJson(editResponse));
                return;
            }
        }
        editResponse = new EditNegotiationCostPerUnitResponse(ResponseTypeConstant.EDIT_NEGOTIATION_COST_PER_UNIT, null);
        localPushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(editResponse));
    }

    private void startTransport(NegotiationDto negotiationDto)
    {
        transportManager.createTransport(
                Enums.VehicleType.TRUCK,
                Enums.TransportNodeType.FACTORY,
                teamService.findTeamById(negotiationDto.getSupplierId()).getFactoryId(),
                Enums.TransportNodeType.FACTORY,
                teamService.findTeamById(negotiationDto.getDemanderId()).getFactoryId(),
                gameCalendar.getCurrentDate(),
                true,
                negotiationDto.getProductId(),
                negotiationDto.getAmount()
        );
    }
}