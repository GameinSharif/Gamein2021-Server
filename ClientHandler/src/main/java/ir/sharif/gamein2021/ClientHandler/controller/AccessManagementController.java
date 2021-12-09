package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.AccessDeniedResponse;
import ir.sharif.gamein2021.ClientHandler.manager.LocalPushMessageManager;
import ir.sharif.gamein2021.core.util.RequestTypeConstant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AccessManagementController {
    private final LocalPushMessageManager pushMessageManager;
    private final Gson gson = new Gson();

    public boolean validateAccess(ProcessedRequest request) {
        switch (request.requestType) {
            case LOGIN:
            case GET_GAME_DATA:
            case GET_GAME_STATUS:
                return true;
            case NEW_OFFER:
            case GET_OFFERS:
            case GET_CONTRACTS:
            case ACCEPT_OFFER:
            case GET_NEGOTIATIONS:
            case EDIT_NEGOTIATION_COST_PER_UNIT:
            case NEW_PROVIDER:
            case GET_PROVIDERS:
            case REMOVE_PROVIDER:
            case NEW_PROVIDER_NEGOTIATION:
            case BID_FOR_AUCTION:
            case TERMINATE_OFFER:
            case NEW_MESSAGE:
            case GET_ALL_CHATS:
            case GET_PRODUCTION_LINES:
            case CONSTRUCT_PRODUCTION_LINE:
            case SCRAP_PRODUCTION_LINE:
            case START_PRODUCTION:
            case UPGRADE_PRODUCTION_LINE_QUALITY:
            case UPGRADE_PRODUCTION_LINE_EFFICIENCY:
            case NEW_CONTRACT_WITH_SUPPLIER:
            case TERMINATE_LONGTERM_CONTRACT_WITH_SUPPLIER:
            case BUY_DC:
            case SELL_DC:
            case GET_TEAM_TRANSPORTS:
            case GET_CONTRACTS_WITH_SUPPLIER:
            case REMOVE_PRODUCT:
            case GET_STORAGES:
            case NEW_CONTRACT:
            case TERMINATE_CONTRACT:
            case GET_LEADERBOARD:
            case TRANSPORT_TO_STORAGE:
            case GET_ALL_WEEKLY_REPORTS:
            case REJECT_NEGOTIATION:
            case EDIT_PROVIDER:
            case DONATE:
            case REPORT_MESSAGE:
            default:
                if (request.playerId != null) {
                    return true;
                }

                AccessDeniedResponse response = new AccessDeniedResponse();
                pushMessageManager.sendMessageBySession(request.session, gson.toJson(response));
                return false;
        }
    }
}
