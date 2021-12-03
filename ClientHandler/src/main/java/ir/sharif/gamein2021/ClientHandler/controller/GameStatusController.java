package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.UpdateGameStatusResponse;
import ir.sharif.gamein2021.ClientHandler.manager.LocalPushMessageManager;
import ir.sharif.gamein2021.core.util.GameConstants;
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
                return validateRunningStatus(processedRequest);
            case PAUSED:
                return validatePausedStatus(processedRequest);
            case AUCTION:
                return validateAuctionStatus(processedRequest);
            case STOPPED:
                UpdateGameStatusResponse response = new UpdateGameStatusResponse(GameConstants.gameStatus);
                pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(response));
                return false;
        }

        return false;
    }

    private boolean validateAuctionStatus(ProcessedRequest request) {
        switch (request.requestType) {
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
            default:
                UpdateGameStatusResponse response = new UpdateGameStatusResponse(GameConstants.gameStatus);
                pushMessageManager.sendMessageBySession(request.session, gson.toJson(response));
                return false;
        }
    }

        private boolean validateRunningStatus (ProcessedRequest request){
            switch (request.requestType) {
                case BID_FOR_AUCTION:
                    UpdateGameStatusResponse response = new UpdateGameStatusResponse(GameConstants.gameStatus);
                    pushMessageManager.sendMessageBySession(request.session, gson.toJson(response));
                    return false;
                default:
                    return true;
            }
        }

        private boolean validatePausedStatus (ProcessedRequest request){
            switch (request.requestType) {
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
                case GET_ALL_WEEKLY_REPORTS:
                    return true;

                default:
                    UpdateGameStatusResponse response = new UpdateGameStatusResponse(GameConstants.gameStatus);
                    pushMessageManager.sendMessageBySession(request.session, gson.toJson(response));
                    return false;
            }
        }
    }
