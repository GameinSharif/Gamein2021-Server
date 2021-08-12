package ir.sharif.gamein2021.ClientHandler.view;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.TeamController;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.ClientHandler.view.requests.CreatePlayerRequest;
import ir.sharif.gamein2021.core.util.RequestConstants;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import static ir.sharif.gamein2021.core.util.RequestConstants.CREATE_PLAYER_REQUEST;
import static ir.sharif.gamein2021.core.util.RequestConstants.GET_PLAYER_DETAILS_REQUEST;

@Component
public class View {
    private final Gson gson = new Gson();
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());
    private TeamController teamController;

    public View(TeamController teamController) {
        this.teamController = teamController;
    }

    public String processCommands(String data) {
        try {
            JSONObject obj = new JSONObject(data);
            int type = obj.getInt("type");
            int playerId = obj.getInt("playerId");
            JSONObject decDataJsonObject;
            String decData = "";
            if (obj.has("decData")) {
                decDataJsonObject = obj.getJSONObject("decData");
                decData = decDataJsonObject.toString();
            }
            ResponseObject<Object> response;
            switch (type) {
                case CREATE_PLAYER_REQUEST:
                    CreatePlayerRequest createPlayerRequest = gson.fromJson(decData, CreatePlayerRequest.class);
                    response = teamController.createPlayer(createPlayerRequest, playerId);
                    break;
                case GET_PLAYER_DETAILS_REQUEST:
                    response = teamController.getPlayerDetails(playerId);
                    break;
                default:
                    response = new ResponseObject<>(RequestConstants.UNKNOWN_TYPE_RESPONSE);
            }
            return gson.toJson(response);
        } catch (Exception e) {
            logger.error("view error", e);
            e.printStackTrace();
            ResponseObject<Object> response = new ResponseObject<>(RequestConstants.UNKNOWN_TYPE_RESPONSE);
            return gson.toJson(response);
        }
    }
}
