package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.transport.SocketHandler;
import ir.sharif.gamein2021.ClientHandler.view.ResponseObject;
import ir.sharif.gamein2021.ClientHandler.view.requests.CreatePlayerRequest;
import ir.sharif.gamein2021.core.model.Player;
import ir.sharif.gamein2021.core.util.RequestConstants;
import org.apache.log4j.Logger;

public class GameController {
    static Logger logger = Logger.getLogger(GameController.class.getName());
    private static GameController _instance = null;
    private final Gson gson;
//    SocketHandler handler = SocketHandler.getInstance();

    private GameController() {
        gson = new Gson();
    }


    public static GameController getInstance() {
        if (_instance == null)
            _instance = new GameController();
        return _instance;
    }
//
////    public void addPlayer(String name, int playerId) {
////        String databaseResponse = playerCommands.get(String.valueOf(playerId));
////        if (databaseResponse == null) {
////            String data = "{\"name\":\"" + name + "\",\"id\":" + playerId + ",\"cash\":5000000}";
////            Player player = gson.fromJson(data, Player.class);
////            playerCommands.set(String.valueOf(playerId), data);
////            String lastRankingString = mainThreadCommands.get("lastRanking");
////            int i;
////            if (lastRankingString == null) {
////                i = 0;
////            } else {
////                i = Integer.parseInt(lastRankingString);
////            }
////            rankingsCommands.set(String.valueOf(i), String.valueOf(player.getId()));
////            mainThreadCommands.set("lastRanking", String.valueOf(i + 1));
////        }
////    }
//
//    public ResponseObject<Object> createPlayer(CreatePlayerRequest request, int playerId) {
//        synchronized (this) {
//            String databaseResponse = playerCommands.get(String.valueOf(playerId));
//            if (databaseResponse != null) {
//                return new ResponseObject<>(RequestConstants.CREATE_PLAYER_RESPONSE, databaseResponse);
//            }
//            Player player = new Player();
//            player.setId(playerId);
//            //TODO: player
//            playerCommands.set(String.valueOf(playerId), gson.toJson(player));
//            return new ResponseObject<>(RequestConstants.CREATE_PLAYER_RESPONSE, player);
//        }
//    }
//
//    public ResponseObject<Object> getPlayerDetails(int playerId) {
//        Player player;
//        String databaseResponse;
//        ResponseObject<Object> responseObject;
//        databaseResponse = playerCommands.get(String.valueOf(playerId));
//        if (databaseResponse != null) {
//            player = gson.fromJson(databaseResponse, Player.class);
//            responseObject = new ResponseObject<>(RequestConstants.GET_PLAYER_DETAILS_RESPONSE, player);
//        } else {
//            responseObject = new ResponseObject<>(RequestConstants.INVALID_PLAYER_ID);
//        }
//        return responseObject;
//    }
}
