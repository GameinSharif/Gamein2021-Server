package ir.sharif.gamein2021.ClientHandler.controller;


import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.*;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.domain.dto.*;
import ir.sharif.gamein2021.core.mainThread.GameCalendar;
import ir.sharif.gamein2021.core.manager.PushMessageManagerInterface;
import ir.sharif.gamein2021.core.manager.ReadJsonFilesManager;
import ir.sharif.gamein2021.core.manager.TeamManager;
import ir.sharif.gamein2021.core.manager.TransportManager;
import ir.sharif.gamein2021.core.service.*;
import ir.sharif.gamein2021.core.util.Enums;
import ir.sharif.gamein2021.core.util.GameConstants;
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
public class NegotiationController {
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final PushMessageManagerInterface pushMessageManager;
    private final TransportManager transportManager;
    private final UserService userService;
    private final TeamService teamService;
    private final NegotiationService negotiationService;
    private final ProviderService providerService;
    private final ProductionLineService productionLineService;
    private final StorageService storageService;
    private final GameCalendar gameCalendar;
    private final TeamManager teamManager;
    private final Gson gson = new Gson();

    public void getNegotiations(ProcessedRequest request, GetNegotiationsRequest getNegotiationsRequest) {
        GetNegotiationsResponse getNegotiationsResponse;
        try {
            int playerId = request.playerId;
            UserDto user = userService.loadById(playerId);
            Team userTeam = teamService.findTeamById(user.getTeamId());

            ArrayList<NegotiationDto> negotiations = negotiationService.findByTeam(userTeam);
            getNegotiationsResponse = new GetNegotiationsResponse(ResponseTypeConstant.GET_NEGOTIATIONS, negotiations);
        } catch (Exception e) {
            getNegotiationsResponse = new GetNegotiationsResponse(ResponseTypeConstant.GET_NEGOTIATIONS, null);
        }
        pushMessageManager.sendMessageByUserId(request.playerId.toString(), gson.toJson(getNegotiationsResponse));
    }

    public void newProviderNegotiation(ProcessedRequest request, NewProviderNegotiationRequest newProviderNegotiationRequest) {
        Integer demanderId = 0;
        Integer supplierId = 0;
        NewProviderNegotiationResponse newProviderNegotiationResponse;
        try {
            ProviderDto provider = providerService.findProviderById(newProviderNegotiationRequest.getProviderId());
            Team team = teamService.findTeamById(request.teamId);
            UserDto user = userService.loadById(request.playerId);

            if (newProviderNegotiationRequest.getAmount() <= 0) {
                newProviderNegotiationResponse = new NewProviderNegotiationResponse(ResponseTypeConstant.NEW_PROVIDER_NEGOTIATION, null);
            } else if (!isCostInRange(newProviderNegotiationRequest.getProviderId(), newProviderNegotiationRequest.getCostPerUnitDemander())) {
                newProviderNegotiationResponse = new NewProviderNegotiationResponse(ResponseTypeConstant.NEW_PROVIDER_NEGOTIATION, null);
            } else if (!isProductionLineValid(team, provider.getProductId())) {
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

                CheckNegotiationDeal(newNegotiation);

                NegotiationDto savedNegotiation = negotiationService.saveOrUpdate(newNegotiation);
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
            if (editRequest.getNewCostPerUnit() < 0) {
                editResponse = new EditNegotiationCostPerUnitResponse(ResponseTypeConstant.EDIT_NEGOTIATION_COST_PER_UNIT, null);
            } else {
                if(!negotiationDto.getState().equals(NegotiationState.REJECTED)){
                    if (userTeam.getId().equals(negotiationDto.getDemanderId())) {
                        negotiationDto.setCostPerUnitDemander(editRequest.getNewCostPerUnit());
                        CheckNegotiationDeal(negotiationDto);

                        negotiationService.saveOrUpdate(negotiationDto);
                        editResponse = new EditNegotiationCostPerUnitResponse(ResponseTypeConstant.EDIT_NEGOTIATION_COST_PER_UNIT, negotiationDto);
                    } else if (userTeam.getId().equals(negotiationDto.getSupplierId())) {
                        negotiationDto.setCostPerUnitSupplier(editRequest.getNewCostPerUnit());
                        CheckNegotiationDeal(negotiationDto);

                        negotiationService.saveOrUpdate(negotiationDto);
                        editResponse = new EditNegotiationCostPerUnitResponse(ResponseTypeConstant.EDIT_NEGOTIATION_COST_PER_UNIT, negotiationDto);
                    } else {
                        editResponse = new EditNegotiationCostPerUnitResponse(ResponseTypeConstant.EDIT_NEGOTIATION_COST_PER_UNIT, null);
                    }
                }else{
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

    public void rejectNegotiation(ProcessedRequest request, RejectNegotiationRequest rejectNegotiationRequest){
        UserDto user = userService.loadById(request.playerId);
        RejectNegotiationResponse response;
        try {
            NegotiationDto negotiationDto = negotiationService.findById(rejectNegotiationRequest.getNegotiationId());
            Team userTeam = teamService.findTeamById(user.getTeamId());
            if (userTeam.getId().equals(negotiationDto.getDemanderId()) ||
                userTeam.getId().equals(negotiationDto.getSupplierId())) {
                negotiationDto.setState(NegotiationState.REJECTED);
                negotiationService.saveOrUpdate(negotiationDto);
                response = new RejectNegotiationResponse(ResponseTypeConstant.REJECT_NEGOTIATION, negotiationDto);
            } else {
                response = new RejectNegotiationResponse(ResponseTypeConstant.REJECT_NEGOTIATION, null);
            }

        } catch (Exception e) {
            response = new RejectNegotiationResponse(ResponseTypeConstant.REJECT_NEGOTIATION, null);
        }

        if (response.getNegotiation() == null) {
            pushMessageManager.sendMessageByUserId(request.playerId.toString(), gson.toJson(response));
        } else {
            pushMessageManager.sendMessageByTeamId(negotiationService.findById(rejectNegotiationRequest.getNegotiationId()).getDemanderId().toString(), gson.toJson(response));
            pushMessageManager.sendMessageByTeamId(negotiationService.findById(rejectNegotiationRequest.getNegotiationId()).getSupplierId().toString(), gson.toJson(response));
        }
    }

    private void CheckNegotiationDeal(NegotiationDto negotiationDto) throws Exception {
        if (negotiationDto.getCostPerUnitDemander().equals(negotiationDto.getCostPerUnitSupplier()) &&
            !negotiationDto.getState().equals(NegotiationState.REJECTED)) {
            float totalPayment = calculateTotalPayment(
                    teamService.findTeamById(negotiationDto.getSupplierId()).getFactoryId(),
                    teamService.findTeamById(negotiationDto.getDemanderId()).getFactoryId(),
                    negotiationDto.getAmount(),
                    negotiationDto.getCostPerUnitDemander(),
                    negotiationDto.getProductId());

            TeamDto demanderDto = teamService.loadById(negotiationDto.getDemanderId());
            TeamDto supplierDto = teamService.loadById(negotiationDto.getSupplierId());

            if (totalPayment > demanderDto.getCredit()) {
                throw new Exception();
            }

            if (!hasRequiredAmount(supplierDto, negotiationDto.getProductId(), negotiationDto.getAmount())) {
                throw new Exception();
            }

            float demanderNewCredit = demanderDto.getCredit() - totalPayment;
            float demanderNewWealth = demanderDto.getWealth() - totalPayment;
            float negotiationPrice = negotiationDto.getAmount() * negotiationDto.getCostPerUnitSupplier();

            demanderDto.setCredit(demanderNewCredit);
            demanderDto.setWealth(demanderNewWealth);
            demanderDto.setOutFlow(demanderDto.getOutFlow() + negotiationPrice);
            demanderDto.setTransportationCost(demanderDto.getTransportationCost() + totalPayment - negotiationPrice);

            supplierDto.setCredit(supplierDto.getCredit() + negotiationPrice);
            supplierDto.setWealth(supplierDto.getWealth() + negotiationPrice);
            supplierDto.setInFlow(supplierDto.getInFlow() + negotiationPrice);

            storageService.deleteProducts(supplierDto.getFactoryId(), false, negotiationDto.getProductId(), negotiationDto.getAmount());

            teamService.saveOrUpdate(demanderDto);
            teamService.saveOrUpdate(supplierDto);

            teamManager.updateTeamBrand(demanderDto, GameConstants.brandIncreaseAfterDeal);
            teamManager.updateTeamBrand(supplierDto, GameConstants.brandIncreaseAfterDeal);

            negotiationDto.setState(NegotiationState.DEAL);
            startTransport(negotiationDto);
        }
    }

    private float calculateTotalPayment(int sourceId, int destinationId, int amount, float costPerUnit, int productId) {
        return amount * costPerUnit + transportManager.calculateTransportCost(
                Enums.VehicleType.TRUCK,
                transportManager.getTransportDistance(
                        Enums.TransportNodeType.FACTORY, //TODO this can be DC too
                        teamService.findTeamById(sourceId).getFactoryId(),
                        Enums.TransportNodeType.FACTORY,
                        teamService.findTeamById(destinationId).getFactoryId(),
                        Enums.VehicleType.TRUCK),
                productId,
                amount
        );
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

    private boolean isProductionLineValid(Team team, int productId) {
        List<Integer> categories = Arrays.stream(ReadJsonFilesManager.findProductById(productId).getCategoryIds().split(",")).map(string -> Integer.parseInt(string)).collect(Collectors.toList());
        List<ProductionLineDto> productionLines = productionLineService.findProductionLinesByTeam(team);
        for (ProductionLineDto productionLineDto : productionLines) {
            for (Integer category : categories) {
                if (productionLineDto.getProductionLineTemplateId().equals(category) && productionLineDto.getStatus() != Enums.ProductionLineStatus.SCRAPPED) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasRequiredAmount(TeamDto team, int productId, int amount) {
        StorageDto storageDto = storageService.findStorageWithBuildingIdAndDc(teamService.loadById(team.getId()).getFactoryId(), false);
        for (StorageProductDto product : storageDto.getProducts()) {
            if (product.getId().equals(productId)) {
                if (product.getAmount() >= amount) {
                    return true;
                }
            }
        }
        return false;
    }
}
