package ir.sharif.gamein2021.ClientHandler.controller;

import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.*;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.NewProviderResponse;
import ir.sharif.gamein2021.ClientHandler.manager.LocalPushMessageManager;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.service.TeamService;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.service.ProviderService;
import ir.sharif.gamein2021.core.service.UserService;
import ir.sharif.gamein2021.core.domain.dto.ProviderDto;
import ir.sharif.gamein2021.core.domain.dto.UserDto;
import ir.sharif.gamein2021.core.domain.entity.Team;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ProviderController
{
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final LocalPushMessageManager pushMessageManager;
    private final UserService userService;
    private final TeamService teamService;
    private final ProviderService providerService;
    private final Gson gson = new Gson();

    public ProviderController(LocalPushMessageManager pushMessageManager, UserService userService, TeamService teamService, ProviderService providerService)
    {
        this.pushMessageManager = pushMessageManager;
        this.userService = userService;
        this.teamService = teamService;
        this.providerService = providerService;
    }

    public void newProvider(ProcessedRequest processedRequest, NewProviderRequest newProviderRequest)
    {
        int playerId = newProviderRequest.playerId;
        UserDto user = userService.loadById(playerId);
        Team userTeam = teamService.findTeamById(user.getTeamId());

        //TODO check team is not null, product id is valid, ...

        //TODO this product is semi-Finished
        //TODO have productionLineTemplate of this product
        //TODO every provider should have one provider for every product
        ProviderDto providerDto = new ProviderDto();
        providerDto.setTeamId(userTeam.getId());
        providerDto.setProductId(newProviderRequest.getProductId());
        providerDto.setCapacity(newProviderRequest.getCapacity());
        providerDto.setPrice(newProviderRequest.getPrice());
        providerService.save(providerDto);
        // TODO : what if couldn't save

        NewProviderResponse newProviderResponse = new NewProviderResponse(ResponseTypeConstant.NEW_PROVIDER, providerDto, "Success");
        pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(newProviderResponse));
    }

    public void getProviders(ProcessedRequest processedRequest, GetProvidersRequest getProvidersRequest)
    {
        int playerId = getProvidersRequest.playerId;
        UserDto user = userService.loadById(playerId);
        Team userTeam = teamService.findTeamById(user.getTeamId());

        ArrayList<ProviderDto> teamProviders = providerService.findProvidersByTeam(userTeam);
        ArrayList<ProviderDto> otherProviders = providerService.findProvidersExceptTeam(userTeam);
        GetProvidersResponse getProvidersResponse = new GetProvidersResponse(ResponseTypeConstant.GET_PROVIDERS, teamProviders, otherProviders);
        pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(getProvidersResponse));
    }

    public void removeProvider(ProcessedRequest processedRequest, RemoveProviderRequest removeProviderRequest)
    {
        int playerId = removeProviderRequest.playerId;
        UserDto user = userService.loadById(playerId);
        Team userTeam = teamService.findTeamById(user.getTeamId());

        Integer providerId = removeProviderRequest.getProviderId();
        // TODO : Exception -> if provider does not exist
        ProviderDto requestedProvider = providerService.loadById(providerId);
        Team requestedProviderTeam = teamService.findTeamById(requestedProvider.getTeamId());
        if (userTeam.getId().equals(requestedProviderTeam.getId()))
        {
            providerService.deleteProvider(providerId);
            RemoveProviderResponse removeProviderResponse = new RemoveProviderResponse(ResponseTypeConstant.REMOVE_PROVIDER, providerId, "Success");
            pushMessageManager.sendMessageBySession(processedRequest.session, gson.toJson(removeProviderResponse));
        }
        else
        {
            // TODO : Exception -> provider team does not match
        }
    }
}
