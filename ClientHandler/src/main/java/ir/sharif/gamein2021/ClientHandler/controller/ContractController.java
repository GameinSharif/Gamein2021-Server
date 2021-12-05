package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Contract.*;
import ir.sharif.gamein2021.ClientHandler.domain.RFQ.NewProviderResponse;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.domain.dto.*;
import ir.sharif.gamein2021.core.mainThread.GameCalendar;
import ir.sharif.gamein2021.core.manager.PushMessageManagerInterface;
import ir.sharif.gamein2021.core.manager.ReadJsonFilesManager;
import ir.sharif.gamein2021.core.service.*;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.util.models.Product;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@AllArgsConstructor
@Component
public class ContractController
{
    private static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final PushMessageManagerInterface pushMessageManager;
    private final ContractService contractService;
    private final UserService userService;
    private final TeamService teamService;
    private final StorageService storageService;
    private final GameinCustomerService gameinCustomerService;
    private final GameCalendar gameCalendar;
    private final Gson gson = new Gson();

    public void getContracts(ProcessedRequest request, GetContractsRequest getContractsRequest)
    {
        int playerId = request.playerId;
        UserDto user = userService.loadById(playerId);
        Team userTeam = teamService.findTeamById(user.getTeamId());

        List<ContractDto> contracts = contractService.findByTeamAndTerminatedIsFalse(userTeam);
        GetContractsResponse getContractsResponse = new GetContractsResponse(
                ResponseTypeConstant.GET_CONTRACTS,
                contracts
        );

        pushMessageManager.sendMessageByUserId(userTeam.getId().toString(), gson.toJson(getContractsResponse));
    }

    public void newContract(ProcessedRequest request, NewContractRequest newContractRequest)
    {
        int playerId = request.playerId;
        UserDto user = userService.loadById(playerId);
        TeamDto userTeam = teamService.loadById(user.getTeamId());

        NewContractResponse newContractResponse;
        try
        {
            if (!storageService.storageBelongsToTeam(newContractRequest.getStorageId(), userTeam))
            {
                throw new Exception();
            }

            for (int i = 0; i < newContractRequest.getWeeks(); i++)
            {
                GameinCustomerDto gameinCustomerDto = gameinCustomerService.loadById(newContractRequest.getGameinCustomerId());
                LocalDate startDate = gameCalendar.getCurrentDate().with(TemporalAdjusters.next(DayOfWeek.SATURDAY)).plusDays(i * 7L);
                Product product = ReadJsonFilesManager.findProductById(newContractRequest.getProductId());

                ContractDto existedContract = contractService.findByTeamAndDateAndGameinCustomerAndProduct(userTeam, startDate, gameinCustomerDto, product);
                if (existedContract != null)
                {
                    throw new Exception();
                }

                if (newContractRequest.getPricePerUnit() < product.getMinPrice() || newContractRequest.getPricePerUnit() > product.getMaxPrice())
                {
                    throw new Exception();
                }

                ContractDto contractDto = new ContractDto();
                contractDto.setTeamId(userTeam.getId());
                contractDto.setStorageId(newContractRequest.getStorageId());
                contractDto.setGameinCustomerId(gameinCustomerDto.getId());
                contractDto.setProductId(product.getId());
                contractDto.setTerminatePenalty(1000); //TODO set this penalty
                contractDto.setLostSalePenalty(1500); //TODO set this penalty
                contractDto.setIsTerminated(false);

                TeamDto teamDto = teamService.loadById(request.teamId);
                contractDto.setCurrentBrand(teamDto.getBrand());

                contractDto.setContractDate(startDate);
                contractDto.setSupplyAmount(newContractRequest.getAmount());
                contractDto.setPricePerUnit(newContractRequest.getPricePerUnit());

                ContractDto savedContractDto = contractService.saveOrUpdate(contractDto);
                newContractResponse = new NewContractResponse(ResponseTypeConstant.NEW_CONTRACT, savedContractDto);
                pushMessageManager.sendMessageByTeamId(userTeam.getId().toString(), gson.toJson(newContractResponse));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            newContractResponse = new NewContractResponse(ResponseTypeConstant.NEW_CONTRACT, null);
            pushMessageManager.sendMessageByUserId(user.getId().toString(), gson.toJson(newContractResponse));
        }
    }

    public void terminateLongtermContract(ProcessedRequest request, TerminateLongtermContractRequest terminateLongtermContractRequest)
    {
        int playerId = request.playerId;
        UserDto user = userService.loadById(playerId);
        TeamDto userTeam = teamService.loadById(user.getTeamId());

        TerminateLongtermContractResponse terminateLongtermContractResponse;
        try
        {
            ContractDto contractDto = contractService.loadById(terminateLongtermContractRequest.getContractId());
            if (contractDto.getIsTerminated() || !contractDto.getTeamId().equals(userTeam.getId()))
            {
                throw new Exception();
            }

            contractDto.setIsTerminated(true);
            ContractDto savedContractDto = contractService.saveOrUpdate(contractDto);

            userTeam.setCredit(userTeam.getCredit() - contractDto.getTerminatePenalty());
            userTeam.setWealth(userTeam.getWealth() - contractDto.getTerminatePenalty());
            teamService.saveOrUpdate(userTeam);

            terminateLongtermContractResponse = new TerminateLongtermContractResponse(ResponseTypeConstant.TERMINATE_CONTRACT, savedContractDto);
            pushMessageManager.sendMessageByTeamId(userTeam.getId().toString(), gson.toJson(terminateLongtermContractResponse));
        }
        catch (Exception e)
        {
            System.out.println("Not valid request");
            terminateLongtermContractResponse = new TerminateLongtermContractResponse(ResponseTypeConstant.TERMINATE_CONTRACT, null);
            pushMessageManager.sendMessageByUserId(user.getId().toString(), gson.toJson(terminateLongtermContractResponse));
        }
    }
}
