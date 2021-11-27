package ir.sharif.gamein2021.ClientHandler.controller;

import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.*;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.NewProviderResponse;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.domain.dto.*;
import ir.sharif.gamein2021.core.manager.PushMessageManagerInterface;
import ir.sharif.gamein2021.core.manager.ReadJsonFilesManager;
import ir.sharif.gamein2021.core.service.*;
import ir.sharif.gamein2021.core.util.Enums;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.util.models.Product;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class ProviderController
{
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final PushMessageManagerInterface pushMessageManager;
    private final UserService userService;
    private final TeamService teamService;
    private final ProductionLineService productionLineService;
    private final ProviderService providerService;
    private final StorageService storageService;
    private final ModelMapper modelMapper;
    private final Gson gson = new Gson();

    public void newProvider(ProcessedRequest processedRequest, NewProviderRequest newProviderRequest)
    {
        NewProviderResponse newProviderResponse;
        try
        {
            Team userTeam = teamService.findTeamById(processedRequest.teamId);
            int minPrice = ReadJsonFilesManager.findProductById(newProviderRequest.getProductId()).getMinPrice();
            int maxPrice = ReadJsonFilesManager.findProductById(newProviderRequest.getProductId()).getMaxPrice();

            if (userTeam == null)
            {
                newProviderResponse = new NewProviderResponse(ResponseTypeConstant.NEW_PROVIDER, null, "Come On!");
            }
            else if (ReadJsonFilesManager.findProductById(newProviderRequest.getProductId()).getProductType() != Enums.ProductType.SemiFinished)
            {
                newProviderResponse = new NewProviderResponse(ResponseTypeConstant.NEW_PROVIDER, null, "Product is not SemiFinished");
            }
            else if (newProviderRequest.getCapacity() <= 0)
            {
                newProviderResponse = new NewProviderResponse(ResponseTypeConstant.NEW_PROVIDER, null, "Product Amount is not valid!");
            }
            else if (!isInProductionLinesProducts(userTeam, newProviderRequest.getProductId()))
            {
                newProviderResponse = new NewProviderResponse(ResponseTypeConstant.NEW_PROVIDER, null, "Product cannot be provided by you!");
            }
            else if (newProviderRequest.getPrice() > maxPrice || newProviderRequest.getPrice() < minPrice)
            {
                newProviderResponse = new NewProviderResponse(ResponseTypeConstant.NEW_PROVIDER, null, "The price is not in its range!");
            }
            else if (isAlreadyProviderOfThisProduct(userTeam, newProviderRequest.getProductId(), newProviderRequest.getStorageId()))
            {
                newProviderResponse = new NewProviderResponse(ResponseTypeConstant.NEW_PROVIDER, null, "You already are a provider of this product.");
            }
            else if (!storageBelongsToTeam(userTeam, newProviderRequest.getStorageId()))
            {
                newProviderResponse = new NewProviderResponse(ResponseTypeConstant.NEW_PROVIDER, null, "Provided storage does not belong to you!");
            }
            else
            {
                ProviderDto providerDto = new ProviderDto();
                providerDto.setTeamId(userTeam.getId());
                providerDto.setProductId(newProviderRequest.getProductId());
                providerDto.setCapacity(newProviderRequest.getCapacity());
                providerDto.setPrice(newProviderRequest.getPrice());
                providerDto.setState(Enums.ProviderState.ACTIVE);
                providerDto.setStorageId(newProviderRequest.getStorageId());
                ProviderDto savedProviderDto = providerService.save(providerDto);
                newProviderResponse = new NewProviderResponse(ResponseTypeConstant.NEW_PROVIDER, savedProviderDto, "Provider Created!");
            }
        } catch (Exception e)
        {
            newProviderResponse = new NewProviderResponse(ResponseTypeConstant.NEW_PROVIDER, null, "An Internal Server Error Occurred!");
        }

        if (newProviderResponse.getNewProvider() == null)
        {
            pushMessageManager.sendMessageByUserId(processedRequest.playerId.toString(), gson.toJson(newProviderResponse));
        }
        else
        {
            pushMessageManager.sendMessageByTeamId(processedRequest.teamId.toString(), gson.toJson(newProviderResponse));
        }
    }

    private boolean storageBelongsToTeam(Team userTeam, Integer storageId)
    {
        TeamDto userTeamDto = modelMapper.map(userTeam, TeamDto.class);
        return storageService.storageBelongsToTeam(storageId, userTeamDto);
    }

    public void getProviders(ProcessedRequest processedRequest, GetProvidersRequest getProvidersRequest)
    {
        GetProvidersResponse getProvidersResponse;
        try
        {
            int playerId = processedRequest.playerId;
            UserDto user = userService.loadById(playerId);
            Team userTeam = teamService.findTeamById(user.getTeamId());

            ArrayList<ProviderDto> teamProviders = providerService.findProvidersByTeam(userTeam);
            ArrayList<ProviderDto> otherProviders = providerService.findProvidersExceptTeam(userTeam);
            getProvidersResponse = new GetProvidersResponse(ResponseTypeConstant.GET_PROVIDERS, teamProviders, otherProviders);
        } catch (Exception e)
        {
            getProvidersResponse = new GetProvidersResponse(ResponseTypeConstant.GET_PROVIDERS, null, null);
        }
        pushMessageManager.sendMessageByUserId(processedRequest.playerId.toString(), gson.toJson(getProvidersResponse));
    }

    public void removeProvider(ProcessedRequest processedRequest, RemoveProviderRequest removeProviderRequest)
    {
        RemoveProviderResponse removeProviderResponse;
        try
        {
            int playerId = processedRequest.playerId;
            UserDto user = userService.loadById(playerId);
            Team userTeam = teamService.findTeamById(user.getTeamId());

            Integer providerId = removeProviderRequest.getProviderId();
            ProviderDto requestedProvider = providerService.loadById(providerId);
            Team requestedProviderTeam = teamService.findTeamById(requestedProvider.getTeamId());
            if (userTeam.getId().equals(requestedProviderTeam.getId()) && requestedProvider.getState() == Enums.ProviderState.ACTIVE)
            {
                providerService.terminateProvider(providerId);
                removeProviderResponse = new RemoveProviderResponse(ResponseTypeConstant.REMOVE_PROVIDER, providerId, "Provider Removed!");
            }
            else
            {
                removeProviderResponse = new RemoveProviderResponse(ResponseTypeConstant.REMOVE_PROVIDER, null, "Provider couldn't be removed!");
            }
        } catch (Exception e)
        {
            removeProviderResponse = new RemoveProviderResponse(ResponseTypeConstant.REMOVE_PROVIDER, null, "An internal error occurred!");
        }

        if (removeProviderResponse.getRemovedProviderId() == null)
        {
            pushMessageManager.sendMessageByUserId(processedRequest.playerId.toString(), gson.toJson(removeProviderResponse));
        }
        else
        {
            pushMessageManager.sendMessageByTeamId(processedRequest.teamId.toString(), gson.toJson(removeProviderResponse));
        }
    }

    private boolean isInProductionLinesProducts(Team team, Integer productId)
    {
        Product product = ReadJsonFilesManager.findProductById(productId);
        List<ProductionLineDto> productionLines = productionLineService.findProductionLinesByTeam(team);
        for (ProductionLineDto productionLineDto : productionLines)
        {
            if (productionLineDto.getProductionLineTemplateId().equals(product.getProductionLineTemplateId()) && productionLineDto.getStatus() != Enums.ProductionLineStatus.SCRAPPED)
            {
                return true;
            }
        }
        return false;
    }

    private boolean isAlreadyProviderOfThisProduct(Team team, Integer productId, Integer storageId)
    {
        List<ProviderDto> providerDtos = providerService.findProvidersByTeam(team);
        for (ProviderDto providerDto : providerDtos)
        {
            if (providerDto.getProductId().equals(productId) && providerDto.getStorageId().equals(storageId) && providerDto.getState() == Enums.ProviderState.ACTIVE)
            {
                return true;
            }
        }
        return false;
    }

    public void editProvider(ProcessedRequest processedRequest, EditProviderRequest editRequest)
    {
        UserDto user = userService.loadById(processedRequest.playerId);
        EditProviderResponse editResponse;
        try
        {
            ProviderDto providerDto = providerService.findProviderById(editRequest.getProviderId());
            Team userTeam = teamService.findTeamById(user.getTeamId());

            int minPrice = ReadJsonFilesManager.findProductById(providerDto.getProductId()).getMinPrice();
            int maxPrice = ReadJsonFilesManager.findProductById(providerDto.getProductId()).getMaxPrice();

            if (editRequest.getNewPrice() > maxPrice || editRequest.getNewPrice() < minPrice)
            {
                editResponse = new EditProviderResponse(ResponseTypeConstant.EDIT_PROVIDER_RESPONSE, "The price is not in its range!", null);
            }
            else if (editRequest.getNewCapacity() <= 0)
            {
                editResponse = new EditProviderResponse(ResponseTypeConstant.EDIT_PROVIDER_RESPONSE, "Product Amount is not valid!", null);
            }
            else
            {
                if (providerDto.getState().equals(Enums.ProviderState.TERMINATED))
                {
                    throw new Exception();
                }

                if (userTeam.getId().equals(providerDto.getTeamId()))
                {
                    providerDto.setPrice(editRequest.getNewPrice());
                    providerDto.setCapacity(editRequest.getNewCapacity());

                    ProviderDto savedProvider = providerService.saveOrUpdate(providerDto);
                    editResponse = new EditProviderResponse(ResponseTypeConstant.EDIT_PROVIDER_RESPONSE, "successful!", savedProvider);
                }
                else
                {
                    editResponse = new EditProviderResponse(ResponseTypeConstant.EDIT_PROVIDER_RESPONSE, "this provider does not belong to this team!", null);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            editResponse = new EditProviderResponse(ResponseTypeConstant.EDIT_PROVIDER_RESPONSE, "some error has happened!", null);
        }

        if (editResponse.getEditedProvider() == null)
        {
            pushMessageManager.sendMessageByUserId(processedRequest.playerId.toString(), gson.toJson(editResponse));
        }
        else
        {
            pushMessageManager.sendMessageByTeamId(processedRequest.teamId.toString(), gson.toJson(editResponse));
        }
    }
}
