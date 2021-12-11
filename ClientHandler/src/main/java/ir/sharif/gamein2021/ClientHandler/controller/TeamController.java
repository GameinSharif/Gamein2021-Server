package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.domain.dto.TeamDto;
import ir.sharif.gamein2021.core.mainThread.GameCalendar;
import ir.sharif.gamein2021.core.manager.PushMessageManagerInterface;
import ir.sharif.gamein2021.core.response.BanResponse;
import ir.sharif.gamein2021.core.service.TeamService;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@AllArgsConstructor

@Component
public class TeamController {
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final PushMessageManagerInterface pushMessageManager;
    private final TeamService teamService;
    private final GameCalendar gameCalendar;
    private final Gson gson;

    public boolean validateTeamAccess(ProcessedRequest request){
        TeamDto team = teamService.loadById(request.teamId);
        if(team.getBanEnd() != null && team.getBanEnd().isAfter(gameCalendar.getCurrentDate())){
            BanResponse banResponse = new BanResponse(ResponseTypeConstant.BAN, team.getBanEnd());
            pushMessageManager.sendMessageByTeamId(request.teamId.toString(), gson.toJson(banResponse));
            return false;
        } else return true;
    }
}
