package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Corona.DonateRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Corona.DonateResponse;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.domain.dto.CoronaInfoDto;
import ir.sharif.gamein2021.core.domain.dto.TeamDto;
import ir.sharif.gamein2021.core.manager.PushMessageManagerInterface;
import ir.sharif.gamein2021.core.service.TeamService;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class CoronaController
{
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final Gson gson;
    private final PushMessageManagerInterface pushMessageManager;
    private final TeamService teamService;

    public void donate(ProcessedRequest request, DonateRequest donateRequest)
    {
        Integer userId = request.playerId;
        DonateResponse response;
        try
        {
            TeamDto teamDto = teamService.loadById(request.teamId);
            List<CoronaInfoDto> infos = teamService.donate(teamDto, donateRequest.getDonatedAmount());
            response = new DonateResponse(ResponseTypeConstant.DONATE, infos, "success");
            pushMessageManager.sendMessageToAll(gson.toJson(response));
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            logger.debug(e.getMessage());
            response = new DonateResponse(ResponseTypeConstant.DONATE, null, e.getMessage());
            pushMessageManager.sendMessageByUserId(userId.toString(), gson.toJson(response));
        }
    }
}
