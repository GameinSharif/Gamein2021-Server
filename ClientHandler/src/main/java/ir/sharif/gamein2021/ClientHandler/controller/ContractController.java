package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Contract.*;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.domain.dto.*;
import ir.sharif.gamein2021.core.mainThread.GameCalendar;
import ir.sharif.gamein2021.core.manager.PushMessageManagerInterface;
import ir.sharif.gamein2021.core.manager.ReadJsonFilesManager;
import ir.sharif.gamein2021.core.manager.TeamManager;
import ir.sharif.gamein2021.core.service.*;
import ir.sharif.gamein2021.core.util.GameConstants;
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
    private final TeamManager teamManager;
    private final ContractService contractService;
    private final TeamService teamService;
    private final StorageService storageService;
    private final GameinCustomerService gameinCustomerService;
    private final GameCalendar gameCalendar;
    private final Gson gson = new Gson();

    public void getContracts(ProcessedRequest request, GetContractsRequest getContractsRequest)
    {
        Team userTeam = teamService.findTeamById(request.teamId);

        List<ContractDto> contracts = contractService.findByTeamAndTerminatedIsFalse(userTeam);
        GetContractsResponse getContractsResponse = new GetContractsResponse(
                ResponseTypeConstant.GET_CONTRACTS,
                contracts
        );

        pushMessageManager.sendMessageByUserId(request.playerId.toString(), gson.toJson(getContractsResponse));
    }

    public void newContract(ProcessedRequest request, NewContractRequest newContractRequest)
    {
        TeamDto userTeam = teamService.loadById(request.teamId);

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

                if (newContractRequest.getWeeks() < 1 || newContractRequest.getWeeks() > 10)
                {
                    throw new Exception();
                }

                ContractDto contractDto = new ContractDto();
                contractDto.setTeamId(userTeam.getId());
                contractDto.setStorageId(newContractRequest.getStorageId());
                contractDto.setGameinCustomerId(gameinCustomerDto.getId());
                contractDto.setProductId(product.getId());
                contractDto.setIsTerminated(false);

                TeamDto teamDto = teamService.loadById(request.teamId);
                contractDto.setCurrentBrand(teamDto.getBrand());

                contractDto.setContractDate(startDate);
                contractDto.setSupplyAmount(newContractRequest.getAmount());
                contractDto.setPricePerUnit(newContractRequest.getPricePerUnit());
                contractDto.setTerminatePenalty(GameConstants.terminatePenalty * contractDto.getSupplyAmount() * contractDto.getPricePerUnit());
                contractDto.setLostSalePenalty(0f);

                ContractDto savedContractDto = contractService.saveOrUpdate(contractDto);
                newContractResponse = new NewContractResponse(ResponseTypeConstant.NEW_CONTRACT, savedContractDto);
                pushMessageManager.sendMessageByTeamId(userTeam.getId().toString(), gson.toJson(newContractResponse));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            newContractResponse = new NewContractResponse(ResponseTypeConstant.NEW_CONTRACT, null);
            pushMessageManager.sendMessageByUserId(request.playerId.toString(), gson.toJson(newContractResponse));
        }
    }

    public void terminateLongtermContract(ProcessedRequest request, TerminateLongtermContractRequest terminateLongtermContractRequest)
    {
        TeamDto userTeam = teamService.loadById(request.teamId);

        TerminateLongtermContractResponse terminateLongtermContractResponse;
        try
        {
            ContractDto contractDto = contractService.loadById(terminateLongtermContractRequest.getContractId());
            if (contractDto.getIsTerminated() || !contractDto.getTeamId().equals(userTeam.getId()))
            {
                throw new Exception();
            }

            Float terminatePenalty = contractDto.getTerminatePenalty();
            if (terminatePenalty > userTeam.getCredit())
            {
                throw new Exception();
            }

            contractDto.setIsTerminated(true);
            ContractDto savedContractDto = contractService.saveOrUpdate(contractDto);

            userTeam.setCredit(userTeam.getCredit() - terminatePenalty);
            userTeam.setWealth(userTeam.getWealth() - terminatePenalty);
            teamService.saveOrUpdate(userTeam);

            teamManager.updateTeamBrand(userTeam, GameConstants.brandTerminateContractPenaltyDecrease);

            terminateLongtermContractResponse = new TerminateLongtermContractResponse(ResponseTypeConstant.TERMINATE_CONTRACT, savedContractDto);
            pushMessageManager.sendMessageByTeamId(userTeam.getId().toString(), gson.toJson(terminateLongtermContractResponse));
        }
        catch (Exception e)
        {
            System.out.println("Not valid request");
            terminateLongtermContractResponse = new TerminateLongtermContractResponse(ResponseTypeConstant.TERMINATE_CONTRACT, null);
            pushMessageManager.sendMessageByUserId(request.playerId.toString(), gson.toJson(terminateLongtermContractResponse));
        }
    }
}
