package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.GetContractsRequest;
import ir.sharif.gamein2021.ClientHandler.domain.GetContractsResponse;
import ir.sharif.gamein2021.ClientHandler.manager.PushMessageManager;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.ClientHandler.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.Service.ContractService;
import ir.sharif.gamein2021.core.Service.UserService;
import ir.sharif.gamein2021.core.domain.dto.ContractDto;
import ir.sharif.gamein2021.core.domain.dto.UserDto;
import ir.sharif.gamein2021.core.domain.entity.Team;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContractController
{
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final PushMessageManager pushMessageManager;
    private final ContractService contractService;
    private final UserService userService;
    private final Gson gson = new Gson();

    public ContractController(PushMessageManager pushMessageManager, ContractService contractService, UserService userService)
    {
        this.pushMessageManager = pushMessageManager;
        this.contractService = contractService;
        this.userService = userService;
    }

    public void getContracts(ProcessedRequest request, GetContractsRequest getContractsRequest)
    {
        int playerId = getContractsRequest.playerId;
        UserDto user = userService.loadById(playerId);
        Team userTeam = user.getTeam();
        //TODO check user and his team is not null
        List<ContractDto> contracts = contractService.findByTeam(userTeam);
        GetContractsResponse getContractsResponse = new GetContractsResponse(
                ResponseTypeConstant.GET_CONTRACTS,
                contracts
        );

        pushMessageManager.sendMessageBySession(request.session, gson.toJson(getContractsResponse));
    }
}
