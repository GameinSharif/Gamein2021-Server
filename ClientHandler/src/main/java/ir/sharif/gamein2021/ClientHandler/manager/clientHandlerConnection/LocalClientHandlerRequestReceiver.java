package ir.sharif.gamein2021.ClientHandler.manager.clientHandlerConnection;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.domain.ChangeGameStatusResponse;
import ir.sharif.gamein2021.ClientHandler.domain.productionLine.ProductCreationCompletedResponse;
import ir.sharif.gamein2021.ClientHandler.domain.productionLine.ProductionLineConstructionCompletedResponse;
import ir.sharif.gamein2021.ClientHandler.manager.LocalPushMessageManager;
import ir.sharif.gamein2021.core.domain.entity.ProductionLine;
import ir.sharif.gamein2021.core.manager.clientHandlerConnection.ClientHandlerRequestReceiverInterface;
import ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests.*;
import ir.sharif.gamein2021.core.util.GameConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Handles ClientHandlerRequests.
 */
@Component
public class LocalClientHandlerRequestReceiver implements ClientHandlerRequestReceiverInterface {
    @Autowired
    private LocalPushMessageManager pushMessageManager;
    private Gson gson = new Gson();

    @Override
    public void receive(BaseClientHandlerRequest request) throws InterruptedException {
//        System.out.println("message received in client : " + request.getMessage());

        if (request instanceof ClientsSendMessageToAllRequest) {
            pushMessageManager.sendMessageToAll(request.getMessage());
        } else if (request instanceof ClientsSendMessageByTeamIdRequest) {
            String teamId = ((ClientsSendMessageByTeamIdRequest) request).getTeamId();
            pushMessageManager.sendMessageByTeamId(teamId, request.getMessage());
        } else if (request instanceof ClientsSendMessageByUserIdRequest) {
            String userId = ((ClientsSendMessageByUserIdRequest) request).getUserId();
            pushMessageManager.sendMessageByUserId(userId, request.getMessage());
        } else if (request instanceof ProductionLinesConstructionCompletedRequest) {
            ProductionLinesConstructionCompletedRequest constructionCompletedRequest = (ProductionLinesConstructionCompletedRequest) request;
            for (ProductionLine productionLine : constructionCompletedRequest.getSavedProductionLines()) {
                ProductionLineConstructionCompletedResponse response = new ProductionLineConstructionCompletedResponse(productionLine);
                pushMessageManager.sendMessageByTeamId(productionLine.getTeam().getId().toString(), gson.toJson(response));
            }
        } else if (request instanceof ProductCreationCompletedRequest) {
            ProductCreationCompletedRequest productCreationCompletedRequest = (ProductCreationCompletedRequest) request;
            ProductCreationCompletedResponse response = new ProductCreationCompletedResponse(productCreationCompletedRequest);
            pushMessageManager.sendMessageByTeamId(productCreationCompletedRequest.getTeamId().toString(), gson.toJson(response));
        } else if (request instanceof ChangeGameStatusRequest) {
            ChangeGameStatusRequest changeGameStatusRequest = (ChangeGameStatusRequest) request;
            GameConstants.gameStatus = changeGameStatusRequest.getGameStatus();
            ChangeGameStatusResponse response = new ChangeGameStatusResponse(changeGameStatusRequest.getGameStatus());
            pushMessageManager.sendMessageToAll(gson.toJson(response));
        }

    }
}
