package ir.sharif.gamein2021.ClientHandler.view;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.TeamController;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class View {
    private final Gson gson = new Gson();
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());
    private TeamController teamController;

    public View(TeamController teamController) {
        this.teamController = teamController;
    }

//    public String processCommands(String data) {
//        try {
//            JSONObject obj = new JSONObject(data);
//            int type = obj.getInt("type");
//            int playerId = obj.getInt("playerId");
//            JSONObject decDataJsonObject;
//            String decData = "";
//            if (obj.has("decData")) {
//                decDataJsonObject = obj.getJSONObject("decData");
//                decData = decDataJsonObject.toString();
//            }
//            ResponseObject<Object> response;
//            switch (type) {
//                case CREATE_PLAYER_REQUEST:
//                    CreatePlayerRequest createPlayerRequest = gson.fromJson(decData, CreatePlayerRequest.class);
//                    response = teamController.createPlayer(createPlayerRequest, playerId);
//                    break;
//                case GET_PLAYER_DETAILS_REQUEST:
//                    response = teamController.getPlayerDetails(playerId);
//                    break;
//                default:
//                    response = new ResponseObject<>(RequestTypeConstant.UNKNOWN_TYPE_RESPONSE);
//            }
//            return gson.toJson(response);
//        } catch (Exception e) {
//            logger.error("view error", e);
//            e.printStackTrace();
//            ResponseObject<Object> response = new ResponseObject<>(RequestTypeConstant.UNKNOWN_TYPE_RESPONSE);
//            return gson.toJson(response);
//        return "0";
//        }
}


