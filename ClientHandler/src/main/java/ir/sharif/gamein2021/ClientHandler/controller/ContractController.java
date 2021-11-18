package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.Contract.*;
import ir.sharif.gamein2021.ClientHandler.domain.NewContractSupplierResponse;
import ir.sharif.gamein2021.ClientHandler.manager.LocalPushMessageManager;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.domain.dto.*;
import ir.sharif.gamein2021.core.manager.GameCalendar;
import ir.sharif.gamein2021.core.manager.PushMessageManagerInterface;
import ir.sharif.gamein2021.core.manager.ReadJsonFilesManager;
import ir.sharif.gamein2021.core.manager.TransportManager;
import ir.sharif.gamein2021.core.service.GameinCustomerService;
import ir.sharif.gamein2021.core.service.TeamService;
import ir.sharif.gamein2021.core.util.Enums;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.service.ContractService;
import ir.sharif.gamein2021.core.service.UserService;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.util.models.Product;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
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
    private final GameinCustomerService gameinCustomerService;
    private final GameCalendar gameCalendar;
    private final Gson gson = new Gson();

    public void getContracts(ProcessedRequest request, GetContractsRequest getContractsRequest)
    {
        int playerId = request.playerId;
        UserDto user = userService.loadById(playerId);
        Team userTeam = teamService.findTeamById(user.getTeamId());
        //TODO check user and his team is not null
        List<ContractDto> contracts = contractService.findByTeam(userTeam);
        GetContractsResponse getContractsResponse = new GetContractsResponse(
                ResponseTypeConstant.GET_CONTRACTS,
                contracts
        );

        pushMessageManager.sendMessageByTeamId(userTeam.getId().toString(), gson.toJson(getContractsResponse));
    }

    public void newContract(ProcessedRequest request, NewContractRequest newContractRequest)
    {
        int playerId = request.playerId;
        UserDto user = userService.loadById(playerId);
        Team userTeam = teamService.findTeamById(user.getTeamId());

        NewContractResponse newContractResponse;
        try
        {
            for (int i = 0; i < newContractRequest.getWeeks(); i++)
            {
                GameinCustomerDto gameinCustomerDto = gameinCustomerService.loadById(newContractRequest.getGameinCustomerId());
                LocalDate startDate = gameCalendar.getCurrentDate().with(TemporalAdjusters.next(DayOfWeek.FRIDAY)).plusDays(i * 7L);
                Product product = ReadJsonFilesManager.findProductById(newContractRequest.getProductId());

                ContractDto existedContract = contractService.findByTeamAndDateAndGameinCustomerAndProduct(userTeam, startDate, gameinCustomerDto, product);
                if (existedContract != null)
                {
                    throw new Exception();
                }

                //TODO check pricePerUnit to be in range

                ContractDto contractDto = new ContractDto();
                contractDto.setTeamId(userTeam.getId());
                contractDto.setGameinCustomerId(gameinCustomerDto.getId());
                contractDto.setProductId(product.getId());
                contractDto.setTerminatePenalty(1000); //TODO set this penalty
                contractDto.setLostSalePenalty(1500); //TODO set this penalty
                contractDto.setTerminated(false);

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
            System.out.println("Repetitive Contract");
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
            if (contractDto.isTerminated() || !contractDto.getTeamId().equals(userTeam.getId()))
            {
                throw new Exception();
            }

            contractDto.setTerminated(true);
            ContractDto savedContractDto = contractService.saveOrUpdate(contractDto);

            userTeam.setCredit(userTeam.getCredit() - contractDto.getTerminatePenalty());
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
