package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Corona.DonateRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Corona.DonateResponse;
import ir.sharif.gamein2021.ClientHandler.domain.Dc.BuyingDcResponse;
import ir.sharif.gamein2021.ClientHandler.manager.LocalPushMessageManager;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.domain.dto.CoronaInfoDto;
import ir.sharif.gamein2021.core.domain.dto.TeamDto;
import ir.sharif.gamein2021.core.domain.dto.UserDto;
import ir.sharif.gamein2021.core.service.CoronaService;
import ir.sharif.gamein2021.core.service.TeamService;
import ir.sharif.gamein2021.core.service.UserService;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class CoronaController {
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final Gson gson;
    private final LocalPushMessageManager pushMessageManager;
    private final TeamService teamService;
    private final CoronaService coronaService;
    private final UserService userService;

    public void donate(ProcessedRequest request, DonateRequest donateRequest) {
        Integer id = request.playerId;
        DonateResponse response;
        try {
            UserDto userDto = userService.loadById(id);
            Integer teamId = userDto.getTeamId();
            TeamDto teamDto = teamService.loadById(teamId);
            List<CoronaInfoDto> infos = teamService.donate(teamDto, donateRequest.getDonatedAmount());
            response = new DonateResponse(ResponseTypeConstant.DONATE , infos , "success");
            pushMessageManager.sendMessageToAll(gson.toJson(response));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.debug(e.getMessage());
            response = new DonateResponse(ResponseTypeConstant.DONATE, null, e.getMessage());
            pushMessageManager.sendMessageBySession(request.session, gson.toJson(response));
        }
    }
}
