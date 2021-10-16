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
}
