package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.*;
import ir.sharif.gamein2021.ClientHandler.manager.LocalPushMessageManager;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.domain.dto.*;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.manager.GameCalendar;
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

    private final LocalPushMessageManager pushMessageManager;
    private final ContractSupplierService contractSupplierService;
    private final UserService userService;
    private final WeekSupplyService weekSupplyService;
    private final TeamService teamService;
    private final GameCalendar gameCalendar;
    private final Gson gson = new Gson();

    public void newContractSupplier(ProcessedRequest request, NewContractSupplierRequest newContractSupplierRequest)
    {
        Integer supplierId = newContractSupplierRequest.getSupplierId();
        Integer materialId = newContractSupplierRequest.getMaterialId();
        Enums.VehicleType vehicleType = ReadJsonFilesManager.findVehicleById(newContractSupplierRequest.getVehicleId()).getVehicleType();
        System.out.println(vehicleType);
        Boolean hasInsurance = newContractSupplierRequest.getHasInsurance();
        Integer weeks = newContractSupplierRequest.getWeeks();
        Integer currentWeek = gameCalendar.getWeek();
        Integer amount = newContractSupplierRequest.getAmount();
        Supplier supplier = contractSupplierService.SupplierIdValidation(newContractSupplierRequest.getSupplierId());
        NewContractSupplierResponse newContractSupplierResponse;
        if (supplier == null)
        {
            newContractSupplierResponse = new NewContractSupplierResponse(ResponseTypeConstant.NEW_CONTRACT_WITH_SUPPLIER, null, "supplier_not_found");
        }
        else
        {
            if (supplier.getMaterials().contains(newContractSupplierRequest.getMaterialId()))
            {

                List<ContractSupplierDto> contractSupplierDtos = new ArrayList<>();
                Float totalMaterialPrice = (float)0;
                float teamCredit = teamService.findTeamById(userService.loadById(request.playerId).getTeamId()).getCredit();
                System.out.println("######TEAM CREDIT" + ((Float)(teamCredit)));
                boolean success = false;
                for (int i = 0; i < weeks; i++)
                {
                    Float materialPrice = weekSupplyService.findSpecificWeekSupply(supplierId, materialId, currentWeek+i).getPrice();
                    totalMaterialPrice += (float) materialPrice * amount;
                    if(materialPrice > teamCredit && i == 0) {
                        break;
                    }else {
                        // Got enough money for at least this week's purchase
                        System.out.println("I GOT MONEY BABY");
                        success = true;
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
                }


                if(success){
                    newContractSupplierResponse = new NewContractSupplierResponse(ResponseTypeConstant.NEW_CONTRACT_WITH_SUPPLIER,
                            contractSupplierDtos, "success");
                }else{
                    newContractSupplierResponse = new NewContractSupplierResponse(ResponseTypeConstant.NEW_CONTRACT_WITH_SUPPLIER,
                            null, "not_enough_money");
                }

                System.out.println("####NOW "+teamService.findTeamById(request.playerId).getCredit());


            }
            else
            {
                newContractSupplierResponse = new NewContractSupplierResponse(ResponseTypeConstant.NEW_CONTRACT_WITH_SUPPLIER, null, "supplier_material_not_matching");
            }
        }
        pushMessageManager.sendMessageBySession(request.session, gson.toJson(newContractSupplierResponse));
    }

    public void terminateLongtermContractSupplier(ProcessedRequest request, TerminateLongtermContractSupplierRequest terminateLongtermContractSupplierRequest)
    {
        ContractSupplierDto contractSupplierDto = contractSupplierService.findById(terminateLongtermContractSupplierRequest.getContractId());
        TerminateLongtermContractSupplierResponse terminateLongtermContractSupplierResponse;
        if (contractSupplierDto.getTeamId().equals(userService.loadById(request.playerId).getTeamId()) &&
                contractSupplierDto.getContractDate().isBefore(gameCalendar.getCurrentDate()))
        {

            Integer penalty = contractSupplierDto.getTerminatePenalty();
            float teamCredit = teamService.findTeamById(userService.loadById(request.playerId).getTeamId()).getCredit();
            System.out.println("######TEAM CREDIT" + ((Float)(teamCredit)));
            if(teamCredit < penalty)
                terminateLongtermContractSupplierResponse = new TerminateLongtermContractSupplierResponse(ResponseTypeConstant.TERMINATE_LONGTERM_CONTRACT_WITH_SUPPLIER, "not_enough_money", contractSupplierDto);
            else{
                // Terminating contract's transports
                contractSupplierDto.setTerminated(true);
                TeamDto team = teamService.loadById(request.playerId);
                team.setCredit(teamCredit - penalty);
                teamService.saveOrUpdate(team);
                terminateLongtermContractSupplierResponse = new TerminateLongtermContractSupplierResponse(ResponseTypeConstant.TERMINATE_LONGTERM_CONTRACT_WITH_SUPPLIER, "terminated", contractSupplierDto);

            }

            contractSupplierService.saveOrUpdate(contractSupplierDto);
        }
        else
        {
            terminateLongtermContractSupplierResponse = new TerminateLongtermContractSupplierResponse(ResponseTypeConstant.TERMINATE_LONGTERM_CONTRACT_WITH_SUPPLIER, "not_terminated", contractSupplierDto);
        }
        pushMessageManager.sendMessageBySession(request.session, gson.toJson(terminateLongtermContractSupplierResponse));
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

        pushMessageManager.sendMessageBySession(request.session, gson.toJson(getContractsSupplierResponse));
    }
}
