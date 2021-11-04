package ir.sharif.gamein2021.ClientHandler.controller;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.controller.model.ProcessedRequest;
import ir.sharif.gamein2021.ClientHandler.domain.NewContractSupplierRequest;
import ir.sharif.gamein2021.ClientHandler.domain.NewContractSupplierResponse;
import ir.sharif.gamein2021.ClientHandler.domain.TerminateLongtermContractSupplierRequest;
import ir.sharif.gamein2021.ClientHandler.domain.TerminateLongtermContractSupplierResponse;
import ir.sharif.gamein2021.ClientHandler.manager.LocalPushMessageManager;
import ir.sharif.gamein2021.ClientHandler.transport.thread.ExecutorThread;
import ir.sharif.gamein2021.core.domain.dto.ContractSupplierDetailDto;
import ir.sharif.gamein2021.core.domain.dto.ContractSupplierDto;
import ir.sharif.gamein2021.core.domain.dto.TransportDto;
import ir.sharif.gamein2021.core.manager.GameCalendar;
import ir.sharif.gamein2021.core.service.*;
import ir.sharif.gamein2021.core.util.Enums;
import ir.sharif.gamein2021.core.util.GameConstants;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.util.models.Supplier;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private final GameCalendar gameCalendar;
    private final Gson gson = new Gson();

    public ContractSupplierController(LocalPushMessageManager pushMessageManager, ContractSupplierService contractSupplierService,
                                      UserService userService, WeekSupplyService weekSupplyService, TeamService teamService,
                                      TransportService transportService, GameCalendar gameCalendar)
    {
        this.pushMessageManager = pushMessageManager;
        this.contractSupplierService = contractSupplierService;
        this.userService = userService;
        this.weekSupplyService = weekSupplyService;
        this.teamService = teamService;
        this.transportService = transportService;
        this.gameCalendar = gameCalendar;
    }

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
        if(supplier == null){
            newContractSupplierResponse = new NewContractSupplierResponse(ResponseTypeConstant.NEW_CONTRACT_WITH_SUPPLIER,
                    null, "supplier_not_found");
        }else
        {
            if (supplier.getMaterials().contains(newContractSupplierRequest.getMaterialId())){
                //Float materialPrice = weekSupplyService.findSpecificWeekSupply(supplierId, materialId, currentWeek).getPrice();
                List<ContractSupplierDetailDto> contractSupplierDetailDtos = new ArrayList<>();
                for(int i = 0; i < weeks+1; i++){
                    ContractSupplierDetailDto contractSupplierDetailDto = new ContractSupplierDetailDto();
                    contractSupplierDetailDto.setContractDate(gameCalendar.getCurrentDate().plusDays(i* 7L));
                    contractSupplierDetailDto.setBoughtAmount(newContractSupplierRequest.getAmount());
                    //contractSupplierDetailDto.setPricePerUnit(materialPrice);
                    contractSupplierDetailDtos.add(contractSupplierDetailDto);
                    TransportDto transportDto = new TransportDto();
                    transportDto.setVehicleType(vehicleType);
                    transportDto.setSourceType(Enums.TransportNodeType.SUPPLIER);
                    transportDto.setSourceId(supplierId);
                    transportDto.setDestinationType(Enums.TransportNodeType.FACTORY);
                    transportDto.setDestinationId(teamService.findTeamById(userService.loadById(newContractSupplierRequest.playerId).getTeamId()).getFactoryId());
                    transportDto.setStartDate(LocalDate.now().plusDays(i* 7L));
                    transportDto.setEndDate(LocalDate.now().plusDays(i* 7L)); // TODO what's the concept of end-date anyways?
                    transportDto.setHasInsurance(hasInsurance);
                    transportDto.setTransportState(Enums.TransportState.PENDING); // TODO should we change it here for today's transports?
                    transportDto.setContentProductId(materialId);
                    transportDto.setContentProductAmount(amount);
                    transportService.save(transportDto);
                    // TODO compute cost for transportation

                }
                contractSupplierDto.setSupplierId(supplierId);
                contractSupplierDto.setMaterialId(materialId);
                contractSupplierDto.setTeamId(userService.loadById(newContractSupplierRequest.playerId).getTeamId());
                contractSupplierDto.setContractType(Enums.ContractType.LONGTERM);
                ContractSupplierDto saveContractSupplierDto = contractSupplierService.save(contractSupplierDto, contractSupplierDetailDtos);
                newContractSupplierResponse = new NewContractSupplierResponse(ResponseTypeConstant.NEW_CONTRACT_WITH_SUPPLIER,
                        saveContractSupplierDto, "success");
                // TODO overall cost of this contract

            }else {
                newContractSupplierResponse = new NewContractSupplierResponse(ResponseTypeConstant.NEW_CONTRACT_WITH_SUPPLIER,
                        null, "supplier_material_not_matching");
            }

        }
        pushMessageManager.sendMessageBySession(request.session, gson.toJson(newContractSupplierResponse));

    }

    public void terminateLongtermContractSupplier(ProcessedRequest request,
                                                  TerminateLongtermContractSupplierRequest terminateLongtermContractSupplierRequest)
    {
        ContractSupplierDto contractSupplierDto = contractSupplierService.findById(terminateLongtermContractSupplierRequest.getContractId());
        TerminateLongtermContractSupplierResponse terminateLongtermContractSupplierResponse;
        if(contractSupplierDto.getTeamId().equals(userService.loadById(terminateLongtermContractSupplierRequest.playerId).getTeamId()) &&
           contractSupplierDto.getContractType().equals(Enums.ContractType.LONGTERM))
        {
            contractSupplierDto.setTerminated(true);
            Integer penalty = contractSupplierDto.getTerminatePenalty();
            terminateLongtermContractSupplierResponse = new TerminateLongtermContractSupplierResponse(ResponseTypeConstant.TERMINATE_LONGTERM_CONTRACT_WITH_SUPPLIER,
                    "terminated", penalty);
            // TODO reduce penalty from client's money
        }else {
            terminateLongtermContractSupplierResponse = new TerminateLongtermContractSupplierResponse(ResponseTypeConstant.TERMINATE_LONGTERM_CONTRACT_WITH_SUPPLIER,
                    "not_terminated", 0);
        }
        pushMessageManager.sendMessageBySession(request.session, gson.toJson(terminateLongtermContractSupplierResponse));
    }
}
