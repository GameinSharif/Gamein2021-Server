package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.service.TeamService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class TeamController {
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final TeamService teamService;
    private final Gson gson;

    public TeamController(TeamService teamService, Gson gson) {
        this.teamService = teamService;
        this.gson = gson;
    }
}
