package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.*;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.domain.dto.*;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.manager.ContractManager;
import ir.sharif.gamein2021.core.mainThread.GameCalendar;
import ir.sharif.gamein2021.core.manager.PushMessageManagerInterface;
import ir.sharif.gamein2021.core.manager.ReadJsonFilesManager;
import ir.sharif.gamein2021.core.manager.TransportManager;
import ir.sharif.gamein2021.core.service.*;
import ir.sharif.gamein2021.core.util.Enums;
import ir.sharif.gamein2021.core.util.GameConstants;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.util.models.Supplier;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class ContractSupplierController
{
    static Logger logger = Logger.getLogger(ExecutorThread.class.getName());

    private final PushMessageManagerInterface pushMessageManager;
    private final ContractSupplierService contractSupplierService;
    private final UserService userService;
    private final WeekSupplyService weekSupplyService;
    private final TeamService teamService;
    private final GameCalendar gameCalendar;
    private final ContractManager contractManager;
    private final TransportManager transportManager;
    private final Gson gson = new Gson();

    public void newContractSupplier(ProcessedRequest request, NewContractSupplierRequest newContractSupplierRequest)
    {
        NewContractSupplierResponse newContractSupplierResponse;
        try
        {
            TeamDto teamDto = teamService.loadById(request.teamId);

            Integer supplierId = newContractSupplierRequest.getSupplierId();
            Integer materialId = newContractSupplierRequest.getMaterialId();
            Enums.VehicleType vehicleType = ReadJsonFilesManager.findVehicleById(newContractSupplierRequest.getVehicleId()).getVehicleType();
            Boolean hasInsurance = newContractSupplierRequest.getHasInsurance();
            Integer weeks = newContractSupplierRequest.getWeeks();
            Integer amount = newContractSupplierRequest.getAmount();

            int currentWeek = gameCalendar.getCurrentWeek();
            Supplier supplier = contractSupplierService.SupplierIdValidation(newContractSupplierRequest.getSupplierId());
            if (!supplier.getMaterials().contains(newContractSupplierRequest.getMaterialId()))
            {
                System.out.println("supplier does not have what you want");
                throw new Exception();
            }

            WeekSupplyDto weekSupplyDto = weekSupplyService.findSpecificWeekSupply(supplierId, materialId, currentWeek);
            ContractSupplierDto contractSupplierDto = createNewContract(teamDto, supplierId, materialId, vehicleType, hasInsurance, amount, 0, weekSupplyDto.getPrice());

            boolean result = contractManager.finalizeTheContractWithSupplier(contractSupplierDto, teamDto, weekSupplyDto, true);
            if (!result)
            {
                System.out.println("get some money");
                throw new Exception();
            }

            List<ContractSupplierDto> contractSupplierDtos = new ArrayList<>();
            contractSupplierDtos.add(contractSupplierDto);
            for (int i = 1; i < weeks; i++)
            {
                ContractSupplierDto newContractSupplierDto = createNewContract(teamDto, supplierId, materialId, vehicleType, hasInsurance, amount, i * 7, weekSupplyDto.getPrice());

                ContractSupplierDto savedContractSupplierDto = contractSupplierService.saveOrUpdate(newContractSupplierDto);
                contractSupplierDtos.add(savedContractSupplierDto);
            }

            newContractSupplierResponse = new NewContractSupplierResponse(ResponseTypeConstant.NEW_CONTRACT_WITH_SUPPLIER, contractSupplierDtos, "Successful");
            pushMessageManager.sendMessageByTeamId(request.teamId.toString(), gson.toJson(newContractSupplierResponse));

        }
        catch (Exception e)
        {
            newContractSupplierResponse = new NewContractSupplierResponse(ResponseTypeConstant.NEW_CONTRACT_WITH_SUPPLIER, null, "Failed");
            pushMessageManager.sendMessageByUserId(request.playerId.toString(), gson.toJson(newContractSupplierResponse));
        }
    }

    private ContractSupplierDto createNewContract(TeamDto teamDto, Integer supplierId, Integer materialId, Enums.VehicleType vehicleType, Boolean hasInsurance, Integer amount, Integer daysToAdd, Float price)
    {
        ContractSupplierDto newContractSupplierDto = new ContractSupplierDto();
        newContractSupplierDto.setContractDate(gameCalendar.getCurrentDate().plusDays(daysToAdd));
        newContractSupplierDto.setBoughtAmount(amount);
        newContractSupplierDto.setSupplierId(supplierId);
        newContractSupplierDto.setMaterialId(materialId);
        newContractSupplierDto.setTeamId(teamDto.getId());
        newContractSupplierDto.setIsTerminated(false);
        newContractSupplierDto.setHasInsurance(hasInsurance);
        newContractSupplierDto.setTransportType(vehicleType);
        newContractSupplierDto.setTerminatePenalty(GameConstants.terminatePenalty * amount * price);
        newContractSupplierDto.setNoMoneyPenalty(0f);

        return newContractSupplierDto;
    }

    public void terminateLongtermContractSupplier(ProcessedRequest request, TerminateLongtermContractSupplierRequest terminateLongtermContractSupplierRequest)
    {
        ContractSupplierDto contractSupplierDto = contractSupplierService.findById(terminateLongtermContractSupplierRequest.getContractId());
        TerminateLongtermContractSupplierResponse terminateLongtermContractSupplierResponse;
        if (contractSupplierDto.getTeamId().equals(request.teamId) && contractSupplierDto.getContractDate().isAfter(gameCalendar.getCurrentDate()) && !contractSupplierDto.getIsTerminated())
        {
            Float penalty = contractSupplierDto.getTerminatePenalty();
            float teamCredit = teamService.findTeamById(request.teamId).getCredit();
            if (teamCredit < penalty)
            {
                terminateLongtermContractSupplierResponse = new TerminateLongtermContractSupplierResponse(ResponseTypeConstant.TERMINATE_LONGTERM_CONTRACT_WITH_SUPPLIER, "not_enough_money", contractSupplierDto);
            }
            else
            {
                contractSupplierDto.setIsTerminated(true);
                TeamDto team = teamService.loadById(request.teamId);
                team.setCredit(teamCredit - penalty);
                team.setWealth(team.getWealth() - penalty);
                teamService.saveOrUpdate(team);
                contractSupplierService.saveOrUpdate(contractSupplierDto);

                terminateLongtermContractSupplierResponse = new TerminateLongtermContractSupplierResponse(ResponseTypeConstant.TERMINATE_LONGTERM_CONTRACT_WITH_SUPPLIER, "Successful", contractSupplierDto);
            }
        }
        else
        {
            terminateLongtermContractSupplierResponse = new TerminateLongtermContractSupplierResponse(ResponseTypeConstant.TERMINATE_LONGTERM_CONTRACT_WITH_SUPPLIER, "not_terminated", contractSupplierDto);
        }
        pushMessageManager.sendMessageByTeamId(teamService.loadById(request.teamId).getId().toString(), gson.toJson(terminateLongtermContractSupplierResponse));
    }

    public void getContractsSupplier(ProcessedRequest request, GetContractsSupplierRequest getContractsSupplierRequest)
    {
        int playerId = request.playerId;
        UserDto user = userService.loadById(playerId);
        Team userTeam = teamService.findTeamById(user.getTeamId());

        GetContractsSupplierResponse getContractsSupplierResponse;
        if (userTeam == null)
        {
            getContractsSupplierResponse = new GetContractsSupplierResponse(ResponseTypeConstant.GET_CONTRACTS_WITH_SUPPLIER, null);
        }
        else
        {
            List<ContractSupplierDto> contractSupplierDtos = contractSupplierService.findByTeamAndNotTerminated(userTeam);
            getContractsSupplierResponse = new GetContractsSupplierResponse(ResponseTypeConstant.GET_CONTRACTS_WITH_SUPPLIER, contractSupplierDtos);
        }

        pushMessageManager.sendMessageByUserId(user.getId().toString(), gson.toJson(getContractsSupplierResponse));
    }
}
