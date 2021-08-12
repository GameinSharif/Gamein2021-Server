package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.view.ResponseObject;
import ir.sharif.gamein2021.ClientHandler.view.requests.CreatePlayerRequest;
import ir.sharif.gamein2021.core.model.TeamModel;
import ir.sharif.gamein2021.core.service.TeamService;
import ir.sharif.gamein2021.core.util.RequestConstants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TeamController {


    private TeamService teamService;

    static Logger logger = Logger.getLogger(GameController.class.getName());
    private final Gson gson = new Gson();

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }


//    public void addPlayer(String name, int playerId) {
//        String databaseResponse = playerCommands.get(String.valueOf(playerId));
//        if (databaseResponse == null) {
//            String data = "{\"name\":\"" + name + "\",\"id\":" + playerId + ",\"cash\":5000000}";
//            Player player = gson.fromJson(data, Player.class);
//            playerCommands.set(String.valueOf(playerId), data);
//            String lastRankingString = mainThreadCommands.get("lastRanking");
//            int i;
//            if (lastRankingString == null) {
//                i = 0;
//            } else {
//                i = Integer.parseInt(lastRankingString);
//            }
//            rankingsCommands.set(String.valueOf(i), String.valueOf(player.getId()));
//            mainThreadCommands.set("lastRanking", String.valueOf(i + 1));
//        }
//    }

    public ResponseObject<Object> createPlayer(CreatePlayerRequest request, int playerId) {
        synchronized (this) {
            TeamModel teamModel = teamService.getTeamById((long)playerId);
            if (teamModel != null) {
                return new ResponseObject<>(RequestConstants.CREATE_PLAYER_RESPONSE, teamModel);
            }
            TeamModel team = new TeamModel();
            team.setId((long) playerId);
            //TODO: player
            teamService.saveTeam(team);
            return new ResponseObject<>(RequestConstants.CREATE_PLAYER_RESPONSE, team);
        }
    }

    public ResponseObject<Object> getPlayerDetails(int playerId) {
        TeamModel teamModel = teamService.getTeamById((long) playerId);
        ResponseObject<Object> responseObject;

        if (teamModel != null) {
            responseObject = new ResponseObject<>(RequestConstants.GET_PLAYER_DETAILS_RESPONSE, teamModel);
        } else {
            responseObject = new ResponseObject<>(RequestConstants.INVALID_PLAYER_ID);
        }
        return responseObject;
    }

    public void addPlayer(String teamName, int playerId) {

    }
}
