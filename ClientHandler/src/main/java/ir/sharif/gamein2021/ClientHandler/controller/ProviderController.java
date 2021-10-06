package ir.sharif.gamein2021.ClientHandler.controller;

import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.GetProvidersRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.GetProvidersResponse;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.NewProviderRequest;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.manager.PushMessageManager;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.Service.ProviderService;
import ir.sharif.gamein2021.core.Service.UserService;
import ir.sharif.gamein2021.core.domain.dto.ProviderDto;
import ir.sharif.gamein2021.core.domain.dto.UserDto;
import ir.sharif.gamein2021.core.domain.entity.Team;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ProviderController {

    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());
    private final PushMessageManager pushMessageManager;
    private final UserService userService;
    private final ProviderService providerService;
    private final Gson gson = new Gson();

    public ProviderController(PushMessageManager pushMessageManager, UserService userService,
                              ProviderService providerService) {
        this.pushMessageManager = pushMessageManager;
        this.userService = userService;
        this.providerService = providerService;
    }

    public void newProvider(NewProviderRequest newProviderRequest) {
        ProviderDto providerDto = newProviderRequest.getNewProviderDto();
        // TODO : validate newProviderDto
        providerDto.setupToZero();
        providerService.save(providerDto);
        // TODO : what if couldn't save
    }

    public void getProviders(ProcessedRequest processedRequest, GetProvidersRequest getProvidersRequest) {
        int playerId = getProvidersRequest.playerId;
        UserDto user = userService.findById(playerId);
        Team userTeam = user.getTeam();
        ArrayList<ProviderDto> teamProviders = providerService.findProvidersByTeam(userTeam);
        ArrayList<ProviderDto> otherProviders = providerService.findProvidersExceptTeam(userTeam);
        GetProvidersResponse getProvidersResponse = new GetProvidersResponse(teamProviders, otherProviders);
        pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(getProvidersResponse));
    }
}
