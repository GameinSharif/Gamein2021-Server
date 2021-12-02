package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.UpdateGameStatusResponse;
import ir.sharif.gamein2021.ClientHandler.manager.LocalPushMessageManager;
import ir.sharif.gamein2021.core.util.GameConstants;
import ir.sharif.gamein2021.core.util.RequestTypeConstant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class GameStatusController {
    private final LocalPushMessageManager pushMessageManager;
    private final Gson gson = new Gson();

    public boolean validateGameStatus(ProcessedRequest processedRequest) {
        switch (GameConstants.gameStatus) {
            case RUNNING:
                return true;
            case PAUSED:
                return validatePausedGame(processedRequest, processedRequest.requestType);
            case STOPPED:
                UpdateGameStatusResponse response = new UpdateGameStatusResponse(GameConstants.gameStatus);
                pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(response));
                return false;
        }

        return false;
    }

    private boolean validatePausedGame(ProcessedRequest request, RequestTypeConstant requestType) {
        switch (requestType) {
            case LOGIN:
            case GET_OFFERS:
            case GET_GAME_DATA:
            case GET_CONTRACTS:
            case GET_NEGOTIATIONS:
            case GET_PROVIDERS:
            case GET_ALL_CHATS:
            case GET_PRODUCTION_LINES:
            case GET_TEAM_TRANSPORTS:
            case GET_CONTRACTS_WITH_SUPPLIER:
            case GET_STORAGES:
            case GET_GAME_STATUS:
            case BID_FOR_AUCTION:
            case GET_ALL_WEEKLY_REPORTS:
                return true;

            case NEW_OFFER:
            case ACCEPT_OFFER:
            case EDIT_NEGOTIATION_COST_PER_UNIT:
            case NEW_PROVIDER:
            case REMOVE_PROVIDER:
            case NEW_PROVIDER_NEGOTIATION:
            case TERMINATE_OFFER:
            case NEW_MESSAGE:
            case CONSTRUCT_PRODUCTION_LINE:
            case SCRAP_PRODUCTION_LINE:
            case START_PRODUCTION:
            case UPGRADE_PRODUCTION_LINE_QUALITY:
            case UPGRADE_PRODUCTION_LINE_EFFICIENCY:
            case NEW_CONTRACT_WITH_SUPPLIER:
            case TERMINATE_LONGTERM_CONTRACT_WITH_SUPPLIER:
            case BUY_DC:
            case SELL_DC:
            case ADD_PRODUCT:
            case REMOVE_PRODUCT:
            case NEW_CONTRACT:
            case TERMINATE_CONTRACT:
            case TRANSPORT_TO_STORAGE:
            case REJECT_NEGOTIATION:
            case EDIT_PROVIDER:
            default:
                UpdateGameStatusResponse response = new UpdateGameStatusResponse(GameConstants.gameStatus);
                pushMessageManager.sendMessageBySession(request.session, gson.toJson(response));
                return false;
        }
    }
}
