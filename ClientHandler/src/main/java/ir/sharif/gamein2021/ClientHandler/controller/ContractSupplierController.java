package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.*;
import ir.sharif.gamein2021.ClientHandler.manager.LocalPushMessageManager;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.domain.dto.*;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.manager.GameCalendar;
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
    private final Gson gson = new Gson();

    public void newContractSupplier(ProcessedRequest request, NewContractSupplierRequest newContractSupplierRequest)
    {
        NewContractSupplierResponse newContractSupplierResponse;
        try
        {
            Integer supplierId = newContractSupplierRequest.getSupplierId();
            Integer materialId = newContractSupplierRequest.getMaterialId();
            Enums.VehicleType vehicleType = ReadJsonFilesManager.findVehicleById(newContractSupplierRequest.getVehicleId()).getVehicleType();
            Boolean hasInsurance = newContractSupplierRequest.getHasInsurance();
            Integer weeks = newContractSupplierRequest.getWeeks();
            Integer amount = newContractSupplierRequest.getAmount();

            int currentWeek = gameCalendar.getWeek();
            Supplier supplier = contractSupplierService.SupplierIdValidation(newContractSupplierRequest.getSupplierId());
            if (supplier == null)
            {
                System.out.println("supp null");
                throw new Exception();
            }
            else
            {
                if (!supplier.getMaterials().contains(newContractSupplierRequest.getMaterialId()))
                {
                    System.out.println("supp not got what you want");
                    throw new Exception();
                }

                List<ContractSupplierDto> contractSupplierDtos = new ArrayList<>();
                float teamCredit = teamService.findTeamById(userService.loadById(request.playerId).getTeamId()).getCredit();
                System.out.println(teamCredit);
                Float materialPrice = weekSupplyService.findSpecificWeekSupply(supplierId, materialId, currentWeek).getPrice();

                if(materialPrice < teamCredit) {
                    for (int i = 0; i < weeks; i++)
                    {
                            // Got enough money for at least this week's purchase
                            System.out.println("I GOT MONEY BABY");
                            ContractSupplierDto contractSupplierDto = new ContractSupplierDto();
                            contractSupplierDto.setContractDate(gameCalendar.getCurrentDate().plusDays(i * 7L));
                            contractSupplierDto.setBoughtAmount(newContractSupplierRequest.getAmount());
                            contractSupplierDto.setSupplierId(supplierId);
                            contractSupplierDto.setMaterialId(materialId);
                            contractSupplierDto.setTeamId(userService.loadById(request.playerId).getTeamId());
                            contractSupplierDto.setTerminated(false);
                            contractSupplierDto.setTerminatePenalty(100); //TODO
                            contractSupplierDto.setHasInsurance(hasInsurance);
                            contractSupplierDto.setTransportType(vehicleType);
                            contractSupplierDto.setNoMoneyPenalty(100); //TODO
                            System.out.println(contractSupplierDto);
                            // TODO compute cost for transportation?
                            ContractSupplierDto savedContractSupplierDto = contractSupplierService.saveOrUpdate(contractSupplierDto);
                            contractSupplierDtos.add(savedContractSupplierDto);

                    }
                    newContractSupplierResponse = new NewContractSupplierResponse(ResponseTypeConstant.NEW_CONTRACT_WITH_SUPPLIER, contractSupplierDtos, "Successful");
                    pushMessageManager.sendMessageByTeamId(request.teamId.toString(), gson.toJson(newContractSupplierResponse));

                }else{
                    System.out.println("get some money");
                    throw new Exception();
                }
            }
        }
        catch (Exception e)
        {
            newContractSupplierResponse = new NewContractSupplierResponse(ResponseTypeConstant.NEW_CONTRACT_WITH_SUPPLIER, null, "Failed");
            pushMessageManager.sendMessageByUserId(request.playerId.toString(), gson.toJson(newContractSupplierResponse));
        }
    }

    public void terminateLongtermContractSupplier(ProcessedRequest request, TerminateLongtermContractSupplierRequest terminateLongtermContractSupplierRequest)
    {
        ContractSupplierDto contractSupplierDto = contractSupplierService.findById(terminateLongtermContractSupplierRequest.getContractId());
        TerminateLongtermContractSupplierResponse terminateLongtermContractSupplierResponse;
        if (contractSupplierDto.getTeamId().equals(request.teamId) && contractSupplierDto.getContractDate().isAfter(gameCalendar.getCurrentDate()))
        {
            Integer penalty = contractSupplierDto.getTerminatePenalty();
            float teamCredit = teamService.findTeamById(request.teamId).getCredit();
            if (teamCredit < penalty)
            {
                terminateLongtermContractSupplierResponse = new TerminateLongtermContractSupplierResponse(ResponseTypeConstant.TERMINATE_LONGTERM_CONTRACT_WITH_SUPPLIER, "not_enough_money", contractSupplierDto);
            }
            else
            {
                contractSupplierDto.setTerminated(true);
                TeamDto team = teamService.loadById(request.teamId);
                team.setCredit(teamCredit - penalty);
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
            List<ContractSupplierDto> contractSupplierDtos = contractSupplierService.findByTeam(userTeam);
            getContractsSupplierResponse = new GetContractsSupplierResponse(ResponseTypeConstant.GET_CONTRACTS_WITH_SUPPLIER, contractSupplierDtos);
        }

        pushMessageManager.sendMessageByUserId(user.getId().toString(), gson.toJson(getContractsSupplierResponse));
    }
}
