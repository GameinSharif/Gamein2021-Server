package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.*;
import ir.sharif.gamein2021.ClientHandler.manager.LocalPushMessageManager;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.domain.dto.*;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.manager.GameCalendar;
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
    private final TransportService transportService;
    private final TransportManager transportManager;
    private final GameCalendar gameCalendar;
    private final Gson gson = new Gson();

    public void newContractSupplier(ProcessedRequest request, NewContractSupplierRequest newContractSupplierRequest)
    {
        ContractSupplierDto contractSupplierDto = new ContractSupplierDto();
        Integer supplierId = newContractSupplierRequest.getSupplierId();
        Integer materialId = newContractSupplierRequest.getMaterialId();
        Enums.VehicleType vehicleType = newContractSupplierRequest.getVehicleType();
        Boolean hasInsurance = newContractSupplierRequest.getHasInsurance();
        Integer weeks = newContractSupplierRequest.getWeeks();
        Integer currentWeek = GameConstants.getWeakNumber();
        Integer amount = newContractSupplierRequest.getAmount();
        Supplier supplier = contractSupplierService.SupplierIdValidation(newContractSupplierRequest.getSupplierId());
        NewContractSupplierResponse newContractSupplierResponse;
        if (supplier == null)
        {
            newContractSupplierResponse = new NewContractSupplierResponse(ResponseTypeConstant.NEW_CONTRACT_WITH_SUPPLIER, null, (float)0, "supplier_not_found");
        }
        else
        {
            if (supplier.getMaterials().contains(newContractSupplierRequest.getMaterialId()))
            {
                Float materialPrice = weekSupplyService.findSpecificWeekSupply(supplierId, materialId, currentWeek).getPrice();
                List<ContractSupplierDetailDto> contractSupplierDetailDtos = new ArrayList<>();
                Float totalMaterialPrice = (float)weeks * materialPrice * amount;
                float teamCredit = teamService.findTeamById(userService.loadById(newContractSupplierRequest.playerId).getTeamId()).getCredit();
                System.out.println("######TEAM CREDIT" + ((Float)(teamCredit)));
                if(totalMaterialPrice <= teamCredit) {
                    // TODO should we tell in advance about transport cost?
                    for (int i = 0; i < weeks + 1; i++)
                    {
                        ContractSupplierDetailDto contractSupplierDetailDto = new ContractSupplierDetailDto();
                        contractSupplierDetailDto.setContractDate(gameCalendar.getCurrentDate().plusDays(i * 7L));
                        contractSupplierDetailDto.setBoughtAmount(newContractSupplierRequest.getAmount());
                        contractSupplierDetailDto.setPricePerUnit(materialPrice);



                        // TODO compute cost for transportation
                        System.out.println("##############"+ teamService.findTeamById(userService.loadById(newContractSupplierRequest.playerId).getTeamId()).toString());
                        TransportDto transportDto = transportManager.createTransport(
                                vehicleType,
                                Enums.TransportNodeType.SUPPLIER,
                                supplierId,
                                Enums.TransportNodeType.FACTORY,
                                teamService.findTeamById(userService.loadById(newContractSupplierRequest.playerId).getTeamId()).getFactoryId(),
                                gameCalendar.getCurrentDate().plusDays(i * 7L),
                                hasInsurance,
                                materialId,
                                amount);

                        contractSupplierDetailDto.setTransportId(transportDto.getId());
                        contractSupplierDetailDtos.add(contractSupplierDetailDto);
                    }

                    contractSupplierDto.setSupplierId(supplierId);
                    contractSupplierDto.setMaterialId(materialId);
                    contractSupplierDto.setTeamId(userService.loadById(newContractSupplierRequest.playerId).getTeamId());
                    // check whether or not contract is longterm
                    if(contractSupplierDetailDtos.size() > 1)
                        contractSupplierDto.setContractType(Enums.ContractType.LONGTERM);
                    else
                        contractSupplierDto.setContractType(Enums.ContractType.ONCE);

                    contractSupplierDto.setTerminated(false);
                    contractSupplierDto.setTerminatePenalty(100); //TODO
                    contractSupplierDto.setContractSupplierDetails(contractSupplierDetailDtos);
                    ContractSupplierDto saveContractSupplierDto = contractSupplierService.saveOrUpdate(contractSupplierDto);
                    newContractSupplierResponse = new NewContractSupplierResponse(ResponseTypeConstant.NEW_CONTRACT_WITH_SUPPLIER,
                            saveContractSupplierDto, totalMaterialPrice, "success");
                    teamService.findTeamById(newContractSupplierRequest.playerId).setCredit(teamCredit - totalMaterialPrice);
                    System.out.println("####NOW "+teamService.findTeamById(newContractSupplierRequest.playerId).getCredit());
                }else{
                    newContractSupplierResponse = new NewContractSupplierResponse(ResponseTypeConstant.NEW_CONTRACT_WITH_SUPPLIER,
                            null, totalMaterialPrice, "not_enough_money");
                }

            }
            else
            {
                newContractSupplierResponse = new NewContractSupplierResponse(ResponseTypeConstant.NEW_CONTRACT_WITH_SUPPLIER, null, (float)0, "supplier_material_not_matching");
            }
        }
        pushMessageManager.sendMessageBySession(request.session, gson.toJson(newContractSupplierResponse));
    }

    public void terminateLongtermContractSupplier(ProcessedRequest request, TerminateLongtermContractSupplierRequest terminateLongtermContractSupplierRequest)
    {
        ContractSupplierDto contractSupplierDto = contractSupplierService.findById(terminateLongtermContractSupplierRequest.getContractId());
        TerminateLongtermContractSupplierResponse terminateLongtermContractSupplierResponse;
        if (contractSupplierDto.getTeamId().equals(userService.loadById(terminateLongtermContractSupplierRequest.playerId).getTeamId()) &&
                contractSupplierDto.getContractType().equals(Enums.ContractType.LONGTERM))
        {
            contractSupplierDto.setTerminated(true);
            Integer penalty = contractSupplierDto.getTerminatePenalty();
            float teamCredit = teamService.findTeamById(userService.loadById(terminateLongtermContractSupplierRequest.playerId).getTeamId()).getCredit();
            System.out.println("######TEAM CREDIT" + ((Float)(teamCredit)));
            if(teamCredit < penalty)
                terminateLongtermContractSupplierResponse = new TerminateLongtermContractSupplierResponse(ResponseTypeConstant.TERMINATE_LONGTERM_CONTRACT_WITH_SUPPLIER, "not_enough_money", contractSupplierDto);
            else{
                // Terminating contract's transports
                Integer terminatedCounter = 0;
                for(ContractSupplierDetailDto contractSupplierDetailDto: contractSupplierDto.getContractSupplierDetails()){
                    TransportDto transportDto = transportService.loadById(contractSupplierDetailDto.getTransportId());
                    if (!transportDto.getTransportState().equals(Enums.TransportState.SUCCESSFUL)){
                        transportDto.setTransportState(Enums.TransportState.TERMINATED);
                        transportService.saveOrUpdate(transportDto);
                        terminatedCounter++;
                    }

                }
                // Maybe all transports have been sent and you're too late!!
                if(terminatedCounter == 0){
                    terminateLongtermContractSupplierResponse = new TerminateLongtermContractSupplierResponse(ResponseTypeConstant.TERMINATE_LONGTERM_CONTRACT_WITH_SUPPLIER, "nothing_to_terminate", contractSupplierDto);
                }else{
                    teamService.findTeamById(terminateLongtermContractSupplierRequest.playerId).setCredit(teamCredit - penalty);
                    terminateLongtermContractSupplierResponse = new TerminateLongtermContractSupplierResponse(ResponseTypeConstant.TERMINATE_LONGTERM_CONTRACT_WITH_SUPPLIER, "terminated", contractSupplierDto);
                }

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
        int playerId = getContractsSupplierRequest.playerId;
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
