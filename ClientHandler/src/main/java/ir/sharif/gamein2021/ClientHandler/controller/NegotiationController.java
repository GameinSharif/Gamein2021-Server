package ir.sharif.gamein2021.ClientHandler.controller;


import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.*;
import ir.sharif.gamein2021.ClientHandler.manager.LocalPushMessageManager;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.domain.dto.*;
import ir.sharif.gamein2021.core.manager.GameCalendar;
import ir.sharif.gamein2021.core.manager.PushMessageManagerInterface;
import ir.sharif.gamein2021.core.manager.ReadJsonFilesManager;
import ir.sharif.gamein2021.core.manager.TransportManager;
import ir.sharif.gamein2021.core.service.*;
import ir.sharif.gamein2021.core.util.Enums;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.util.Enums.NegotiationState;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class NegotiationController
{
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final PushMessageManagerInterface pushMessageManager;
    private final TransportManager transportManager;
    private final UserService userService;
    private final TeamService teamService;
    private final NegotiationService negotiationService;
    private final ProviderService providerService;
    private final ProductionLineService productionLineService;
    private final GameCalendar gameCalendar;
    private final Gson gson = new Gson();

    public void getNegotiations(ProcessedRequest request, GetNegotiationsRequest getNegotiationsRequest) {
        int playerId = request.playerId;
        UserDto user = userService.loadById(playerId);
        Team userTeam = teamService.findTeamById(user.getTeamId());
        if (userTeam == null) {
            GetNegotiationsResponse getNegotiationsResponse = new GetNegotiationsResponse(ResponseTypeConstant.GET_NEGOTIATIONS, new ArrayList<>());
            pushMessageManager.sendMessageByUserId(request.playerId.toString(), gson.toJson(getNegotiationsResponse));
            return;
        }
        ArrayList<NegotiationDto> negotiations = negotiationService.findByTeam(userTeam);
        GetNegotiationsResponse getNegotiationsResponse = new GetNegotiationsResponse(ResponseTypeConstant.GET_NEGOTIATIONS, negotiations);
        pushMessageManager.sendMessageByUserId(request.playerId.toString(), gson.toJson(getNegotiationsResponse));
    }

    public void newProviderNegotiation(ProcessedRequest request, NewProviderNegotiationRequest newProviderNegotiationRequest) {
        Integer demanderId = 0;
        Integer supplierId = 0;
        NewProviderNegotiationResponse newProviderNegotiationResponse;
        try {
            float totalPayment =
                    newProviderNegotiationRequest.getAmount() * newProviderNegotiationRequest.getCostPerUnitDemander() +
                    transportManager.calculateTransportCost(
                            Enums.VehicleType.TRUCK,
                            transportManager.calculateTransportDistance(
                            Enums.TransportNodeType.FACTORY,
                            teamService.findTeamById(providerService.findProviderById(newProviderNegotiationRequest.getProviderId()).getTeamId()).getFactoryId(),
                            Enums.TransportNodeType.FACTORY,
                            teamService.findTeamById(request.teamId).getFactoryId(),
                            Enums.VehicleType.TRUCK),
                    providerService.findProviderById(newProviderNegotiationRequest.getProviderId()).getProductId(),
                    newProviderNegotiationRequest.getAmount()
            );
            UserDto user = userService.loadById(request.playerId);
            ProviderDto provider = providerService.findProviderById(newProviderNegotiationRequest.getProviderId());
            if (newProviderNegotiationRequest.getAmount() <= 0) {
                newProviderNegotiationResponse = new NewProviderNegotiationResponse(ResponseTypeConstant.NEW_PROVIDER_NEGOTIATION, null);
            } else if (!isCostInRange(newProviderNegotiationRequest.getProviderId(), newProviderNegotiationRequest.getCostPerUnitDemander())) {
                newProviderNegotiationResponse = new NewProviderNegotiationResponse(ResponseTypeConstant.NEW_PROVIDER_NEGOTIATION, null);
            } else if (!isProductionLineValid(user.getTeamId(), teamService.findTeamById(request.teamId))) {
                newProviderNegotiationResponse = new NewProviderNegotiationResponse(ResponseTypeConstant.NEW_PROVIDER_NEGOTIATION, null);
            } else if (totalPayment > teamService.findTeamById(request.teamId).getCredit()) {
                newProviderNegotiationResponse = new NewProviderNegotiationResponse(ResponseTypeConstant.NEW_PROVIDER_NEGOTIATION, null);
            } else {
                NegotiationDto newNegotiation = new NegotiationDto();
                newNegotiation.setDemanderId(user.getTeamId());
                newNegotiation.setSupplierId(provider.getTeamId());
                newNegotiation.setCostPerUnitDemander(newProviderNegotiationRequest.getCostPerUnitDemander());
                newNegotiation.setCostPerUnitSupplier(provider.getPrice());
                newNegotiation.setProductId(provider.getProductId());
                newNegotiation.setAmount(newProviderNegotiationRequest.getAmount());
                newNegotiation.setState(NegotiationState.IN_PROGRESS);
                if (newNegotiation.getCostPerUnitDemander().equals(newNegotiation.getCostPerUnitSupplier())) {
                    //TODO check if supplier has product and demander has money
                    TeamDto demanderDto = teamService.loadById(newNegotiation.getDemanderId());
                    demanderDto.setCredit(demanderDto.getCredit() - newNegotiation.getCostPerUnitDemander() * newNegotiation.getAmount());
                    TeamDto supplierDto = teamService.loadById(newNegotiation.getSupplierId());
                    supplierDto.setCredit(supplierDto.getCredit() - newNegotiation.getAmount() * newNegotiation.getCostPerUnitSupplier());
                    teamService.saveOrUpdate(demanderDto);
                    teamService.saveOrUpdate(supplierDto);

                    newNegotiation.setState(NegotiationState.DEAL);
                    startTransport(newNegotiation);
                }
                NegotiationDto savedNegotiation = negotiationService.save(newNegotiation);
                demanderId = savedNegotiation.getDemanderId();
                supplierId = savedNegotiation.getSupplierId();

                newProviderNegotiationResponse = new NewProviderNegotiationResponse(ResponseTypeConstant.NEW_PROVIDER_NEGOTIATION, savedNegotiation);
            }

        } catch (Exception e) {
            newProviderNegotiationResponse = new NewProviderNegotiationResponse(ResponseTypeConstant.NEW_PROVIDER_NEGOTIATION, null);
        }

        if (newProviderNegotiationResponse.getNegotiation() == null) {
            pushMessageManager.sendMessageByUserId(request.playerId.toString(), gson.toJson(newProviderNegotiationResponse));
        } else {
            pushMessageManager.sendMessageByTeamId(demanderId.toString(), gson.toJson(newProviderNegotiationResponse));
            pushMessageManager.sendMessageByTeamId(supplierId.toString(), gson.toJson(newProviderNegotiationResponse));
        }
    }

    public void editNegotiationCostPerUnit(ProcessedRequest request, EditNegotiationCostPerUnitRequest editRequest) {
        UserDto user = userService.loadById(request.playerId);
        EditNegotiationCostPerUnitResponse editResponse;
        try {
            NegotiationDto negotiationDto = negotiationService.findById(editRequest.getNegotiationId());
            Team userTeam = teamService.findTeamById(user.getTeamId());
            if (negotiationDto.getCostPerUnitDemander() < 0 || negotiationDto.getCostPerUnitSupplier() < 0) {
                editResponse = new EditNegotiationCostPerUnitResponse(ResponseTypeConstant.EDIT_NEGOTIATION_COST_PER_UNIT, null);
            } else if (
                    !isCostInRange(
                            providerService.findProviderById(negotiationDto.getSupplierId()).getId(), negotiationDto.getCostPerUnitDemander()
                    ) ||
                    !isCostInRange(
                            providerService.findProviderById(negotiationDto.getSupplierId()).getId(), negotiationDto.getCostPerUnitDemander())
            ) {
                editResponse = new EditNegotiationCostPerUnitResponse(ResponseTypeConstant.EDIT_NEGOTIATION_COST_PER_UNIT, null);
            } else {
                if (userTeam.getId().equals(negotiationDto.getDemanderId())) {
                    negotiationDto.setCostPerUnitDemander(editRequest.getNewCostPerUnit());
                    if (negotiationDto.getCostPerUnitDemander().equals(negotiationDto.getCostPerUnitSupplier())) {
                        //TODO check if supplier has product and demander has money
                        //TODO check if demander has transport money
                        TeamDto demanderDto = teamService.loadById(negotiationDto.getDemanderId());
                        demanderDto.setCredit(demanderDto.getCredit() - negotiationDto.getCostPerUnitDemander() * negotiationDto.getAmount());
                        TeamDto supplierDto = teamService.loadById(negotiationDto.getSupplierId());
                        supplierDto.setCredit(supplierDto.getCredit() - negotiationDto.getAmount() * negotiationDto.getCostPerUnitSupplier());
                        teamService.saveOrUpdate(demanderDto);
                        teamService.saveOrUpdate(supplierDto);
                        negotiationDto.setState(NegotiationState.DEAL);
                        startTransport(negotiationDto);
                    }
                    negotiationService.saveOrUpdate(negotiationDto);
                    editResponse = new EditNegotiationCostPerUnitResponse(ResponseTypeConstant.EDIT_NEGOTIATION_COST_PER_UNIT, negotiationDto);
                } else if (userTeam.getId().equals(negotiationDto.getSupplierId())) {
                    negotiationDto.setCostPerUnitSupplier(editRequest.getNewCostPerUnit());
                    if (negotiationDto.getCostPerUnitDemander().equals(negotiationDto.getCostPerUnitSupplier())) {
                        //TODO check if supplier has product and demander has money
                        //TODO check if demander has transport money
                        TeamDto demanderDto = teamService.loadById(negotiationDto.getDemanderId());
                        demanderDto.setCredit(demanderDto.getCredit() - negotiationDto.getCostPerUnitDemander() * negotiationDto.getAmount());
                        TeamDto supplierDto = teamService.loadById(negotiationDto.getSupplierId());
                        supplierDto.setCredit(supplierDto.getCredit() - negotiationDto.getAmount() * negotiationDto.getCostPerUnitSupplier());
                        teamService.saveOrUpdate(demanderDto);
                        teamService.saveOrUpdate(supplierDto);
                        negotiationDto.setState(NegotiationState.DEAL);
                        startTransport(negotiationDto);
                    }
                    negotiationService.saveOrUpdate(negotiationDto);
                    editResponse = new EditNegotiationCostPerUnitResponse(ResponseTypeConstant.EDIT_NEGOTIATION_COST_PER_UNIT, negotiationDto);
                } else {
                    editResponse = new EditNegotiationCostPerUnitResponse(ResponseTypeConstant.EDIT_NEGOTIATION_COST_PER_UNIT, null);
                }
            }

        } catch (Exception e) {
            editResponse = new EditNegotiationCostPerUnitResponse(ResponseTypeConstant.EDIT_NEGOTIATION_COST_PER_UNIT, null);
        }

        if (editResponse.getNegotiation() == null) {
            pushMessageManager.sendMessageByUserId(request.playerId.toString(), gson.toJson(editResponse));
        } else {
            pushMessageManager.sendMessageByTeamId(negotiationService.findById(editRequest.getNegotiationId()).getDemanderId().toString(), gson.toJson(editResponse));
            pushMessageManager.sendMessageByTeamId(negotiationService.findById(editRequest.getNegotiationId()).getSupplierId().toString(), gson.toJson(editResponse));
        }

    }

    private void startTransport(NegotiationDto negotiationDto) {
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

    private boolean isCostInRange(Integer providerId, Float cost) {
        ProviderDto providerDto = providerService.findProviderById(providerId);
        int minPrice = ReadJsonFilesManager.findProductById(providerDto.getProductId()).getMinPrice();
        int maxPrice = ReadJsonFilesManager.findProductById(providerDto.getProductId()).getMaxPrice();
        return cost >= minPrice && cost <= maxPrice;
    }

    private boolean isProductionLineValid(Integer demanderId, Team team) {
        ProviderDto providerDto = providerService.findProviderById(demanderId);
        List<Integer> categories = Arrays.stream(ReadJsonFilesManager.findProductById(providerDto.getProductId()).getCategoryIds().split(",")).map(string -> Integer.parseInt(string)).collect(Collectors.toList());
        List<ProductionLineDto> productionLines = productionLineService.findProductionLinesByTeam(team);
        outer: for (Integer category : categories) {
            for (ProductionLineDto productionLineDto : productionLines) {
                if (
                        productionLineDto.getProductionLineTemplateId().equals(category) &&
                                productionLineDto.getStatus() != Enums.ProductionLineStatus.SCRAPPED
                ) continue outer;
            }
            return false;
        }
        return true;
    }

}