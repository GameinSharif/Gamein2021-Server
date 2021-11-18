package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Auction.BidForAuctionRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Contract.GetContractsRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Contract.NewContractRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Contract.TerminateLongtermContractRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Dc.BuyingDcRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Dc.SellingDcRequest;
import ir.sharif.gamein2021.ClientHandler.domain.GetContractsSupplierRequest;
import ir.sharif.gamein2021.ClientHandler.domain.GetGameDataRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Login.LoginRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Messenger.GetAllChatsRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Messenger.NewMessageRequest;
import ir.sharif.gamein2021.ClientHandler.domain.NewContractSupplierRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Product.AddProductRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Product.GetStorageProductsRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Product.RemoveProductRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.*;
import ir.sharif.gamein2021.ClientHandler.domain.TerminateLongtermContractSupplierRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Transport.GetTeamTransportsRequest;
import ir.sharif.gamein2021.ClientHandler.domain.productionLine.*;
import ir.sharif.gamein2021.core.util.RequestTypeConstant;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class MainController {
    private final UserController userController;
    private final OfferController offerController;
    private final GameDataController gameDataController;
    private final ContractController contractController;
    private final NegotiationController negotiationController;
    private final ProviderController providerController;
    private final AuctionController auctionController;
    private final ProductionLineController productionLineController;
    private final MessageController messageController;
    private final ContractSupplierController contractSupplierController;
    private final TransportController transportController;
    private final DcController dcController;
    private final ProductController productController;
    private final GameStatusController gameStatusController;
    private final AccessManagementController accessManagementController;
    private final Gson gson;

    public void HandleMessage(ProcessedRequest processedRequest) {
        String requestData = processedRequest.requestData;
        JSONObject obj = new JSONObject(requestData);
        RequestTypeConstant requestType = RequestTypeConstant.values()[obj.getInt("requestTypeConstant")];

        if (!gameStatusController.validateGameStatus(processedRequest, requestType)) return;
        if (!accessManagementController.validateAccess(processedRequest, requestType)) return;

        switch (requestType) {
            case LOGIN:
                LoginRequest loginRequest = gson.fromJson(requestData, LoginRequest.class);
                userController.authenticate(processedRequest, loginRequest);
                break;
            case NEW_OFFER:
                NewOfferRequest newOfferRequest = gson.fromJson(requestData, NewOfferRequest.class);
                offerController.createNewOffer(processedRequest, newOfferRequest);
                break;
            case GET_OFFERS:
                GetOffersRequest getOffersRequest = gson.fromJson(requestData, GetOffersRequest.class);
                offerController.handleGetOffers(processedRequest, getOffersRequest);
                break;
            case GET_GAME_DATA:
                GetGameDataRequest getGameDataRequest = gson.fromJson(requestData, GetGameDataRequest.class);
                gameDataController.getGameData(processedRequest);
                gameDataController.getCurrentWeekDemands(processedRequest);
                gameDataController.getCurrentWeekSupplies(processedRequest);
                gameDataController.getAllAuctions(processedRequest);
                gameDataController.getAllActiveDc(processedRequest);
                gameDataController.getServerTime(processedRequest);
                break;
            case GET_CONTRACTS:
                GetContractsRequest getContractsRequest = gson.fromJson(requestData, GetContractsRequest.class);
                contractController.getContracts(processedRequest, getContractsRequest);
                break;
            case GET_NEGOTIATIONS:
                GetNegotiationsRequest getNegotiationsRequest = gson.fromJson(requestData, GetNegotiationsRequest.class);
                negotiationController.getNegotiations(processedRequest, getNegotiationsRequest);
                break;
            case EDIT_NEGOTIATION_COST_PER_UNIT:
                EditNegotiationCostPerUnitRequest editRequest = gson.fromJson(requestData, EditNegotiationCostPerUnitRequest.class);
                negotiationController.editNegotiationCostPerUnit(processedRequest, editRequest);
                break;
            case NEW_PROVIDER:
                NewProviderRequest newProviderRequest = gson.fromJson(requestData, NewProviderRequest.class);
                providerController.newProvider(processedRequest, newProviderRequest);
                break;
            case GET_PROVIDERS:
                GetProvidersRequest getProvidersRequest = gson.fromJson(requestData, GetProvidersRequest.class);
                providerController.getProviders(processedRequest, getProvidersRequest);
                break;
            case REMOVE_PROVIDER:
                RemoveProviderRequest removeProviderRequest = gson.fromJson(requestData, RemoveProviderRequest.class);
                providerController.removeProvider(processedRequest, removeProviderRequest);
                break;
            case NEW_PROVIDER_NEGOTIATION:
                NewProviderNegotiationRequest newProviderNegotiationRequest = gson.fromJson(requestData, NewProviderNegotiationRequest.class);
                negotiationController.newProviderNegotiation(processedRequest, newProviderNegotiationRequest);
                break;
            case BID_FOR_AUCTION:
                BidForAuctionRequest bidForAuctionRequest = gson.fromJson(requestData, BidForAuctionRequest.class);
                auctionController.addBidForAuction(processedRequest, bidForAuctionRequest);
                break;
            case TERMINATE_OFFER:
                TerminateOfferRequest terminateOfferRequest = gson.fromJson(requestData, TerminateOfferRequest.class);
                offerController.terminateOffer(processedRequest, terminateOfferRequest);
                break;
            case NEW_MESSAGE:
                NewMessageRequest newMessageRequest = gson.fromJson(requestData, NewMessageRequest.class);
                messageController.addNewChatMessage(processedRequest, newMessageRequest);
                break;
            case GET_ALL_CHATS:
                GetAllChatsRequest getAllChatsRequest = gson.fromJson(requestData, GetAllChatsRequest.class);
                messageController.getAllChats(processedRequest, getAllChatsRequest);
                break;
            case GET_PRODUCTION_LINES:
                GetProductionLinesRequest getProductionLinesRequest = gson.fromJson(requestData, GetProductionLinesRequest.class);
                productionLineController.GetProductionLines(processedRequest, getProductionLinesRequest);
                break;
            case CONSTRUCT_PRODUCTION_LINE:
                ConstructProductionLineRequest constructProductionLineRequest = gson.fromJson(requestData, ConstructProductionLineRequest.class);
                productionLineController.constructProductionLine(processedRequest, constructProductionLineRequest);
                break;
            case SCRAP_PRODUCTION_LINE:
                ScrapProductionLineRequest scrapProductionLineRequest = gson.fromJson(requestData, ScrapProductionLineRequest.class);
                productionLineController.scrapProductionLine(processedRequest, scrapProductionLineRequest);
                break;
            case START_PRODUCTION:
                StartProductionRequest startProductionRequest = gson.fromJson(requestData, StartProductionRequest.class);
                productionLineController.StartProduction(processedRequest, startProductionRequest);
                break;
            case UPGRADE_PRODUCTION_LINE_QUALITY:
                UpgradeProductionLineQualityRequest upgradeQualityRequest = gson.fromJson(requestData, UpgradeProductionLineQualityRequest.class);
                productionLineController.UpgradeProductionLineQuality(processedRequest, upgradeQualityRequest);
                break;
            case UPGRADE_PRODUCTION_LINE_EFFICIENCY:
                UpgradeProductionLineEfficiencyRequest upgradeEfficiencyRequest = gson.fromJson(requestData, UpgradeProductionLineEfficiencyRequest.class);
                productionLineController.UpgradeProductionLineEfficiency(processedRequest, upgradeEfficiencyRequest);
                break;
            case NEW_CONTRACT_WITH_SUPPLIER:
                NewContractSupplierRequest newContractSupplierRequest = gson.fromJson(requestData, NewContractSupplierRequest.class);
                contractSupplierController.newContractSupplier(processedRequest, newContractSupplierRequest);
                break;
            case TERMINATE_LONGTERM_CONTRACT_WITH_SUPPLIER:
                TerminateLongtermContractSupplierRequest terminateLongtermContractSupplierRequest = gson.fromJson(requestData, TerminateLongtermContractSupplierRequest.class);
                contractSupplierController.terminateLongtermContractSupplier(processedRequest, terminateLongtermContractSupplierRequest);
                break;
            case GET_TEAM_TRANSPORTS:
                GetTeamTransportsRequest getTeamTransportsRequest = gson.fromJson(requestData, GetTeamTransportsRequest.class);
                transportController.getTeamTransports(processedRequest, getTeamTransportsRequest);
                break;
            case GET_CONTRACTS_WITH_SUPPLIER:
                GetContractsSupplierRequest getContractsSupplierRequest = gson.fromJson(requestData, GetContractsSupplierRequest.class);
                contractSupplierController.getContractsSupplier(processedRequest, getContractsSupplierRequest);
            case BUY_DC:
                try {
                    BuyingDcRequest buyingDcRequest = gson.fromJson(requestData, BuyingDcRequest.class);
                    dcController.buyDc(processedRequest, buyingDcRequest);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            case SELL_DC:
                SellingDcRequest sellingDcRequest = gson.fromJson(requestData, SellingDcRequest.class);
                dcController.sellDc(processedRequest, sellingDcRequest);
                break;
            case ADD_PRODUCT:
                AddProductRequest addProductRequest = gson.fromJson(requestData, AddProductRequest.class);
                try {
                    productController.addProduct(processedRequest, addProductRequest);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            case REMOVE_PRODUCT:
                RemoveProductRequest removeProductRequest = gson.fromJson(requestData, RemoveProductRequest.class);
                try {
                    productController.removeProduct(processedRequest, removeProductRequest);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            case GET_STORAGES:
                GetStorageProductsRequest getStorageProductsRequest = gson.fromJson(requestData, GetStorageProductsRequest.class);
                try {
                    productController.getStorages(processedRequest, getStorageProductsRequest);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            case ACCEPT_OFFER:
                AcceptOfferRequest acceptOfferRequest = gson.fromJson(requestData, AcceptOfferRequest.class);
                offerController.acceptOffer(processedRequest, acceptOfferRequest);
                break;
            case NEW_CONTRACT:
                NewContractRequest newContractRequest = gson.fromJson(requestData, NewContractRequest.class);
                contractController.newContract(processedRequest, newContractRequest);
                break;
            case TERMINATE_CONTRACT:
                TerminateLongtermContractRequest terminateLongtermContractRequest = gson.fromJson(requestData, TerminateLongtermContractRequest.class);
                contractController.terminateLongtermContract(processedRequest, terminateLongtermContractRequest);
                break;
            case GET_GAME_STATUS:
                gameDataController.getGameStatus(processedRequest);
                break;
            default:
                System.out.println("Request type is invalid.");
        }
    }
}
