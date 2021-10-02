package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.manager.PushMessageManager;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.Service.TeamService;
import ir.sharif.gamein2021.core.domain.dto.TeamDto;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class TeamController {
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final TeamService teamService;
    private final Gson gson;
    private final PushMessageManager pushMessageManager;

    public TeamController(TeamService teamService, Gson gson, PushMessageManager pushMessageManager) {
        this.teamService = teamService;
        this.gson = gson;
        this.pushMessageManager = pushMessageManager;
    }

    public void setRandomCountryForTeam(ProcessedRequest request) {
        TeamDto teamDto = gson.fromJson(request.requestData, TeamDto.class);
        Integer id = teamDto.getId();
        try {
            teamDto = teamService.choiceRandomCountry(id);
        }catch (Exception e){
            logger.debug(e);
        }
        //TODO Validation for teamDto
        //TODO exceptions and validations
        pushMessageManager.sendMessageBySession(request.session, gson.toJson(teamDto));
    }


}
