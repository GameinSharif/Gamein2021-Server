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
import ir.sharif.gamein2021.core.service.ContractSupplierService;
import ir.sharif.gamein2021.core.service.UserService;
import ir.sharif.gamein2021.core.util.Enums;
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
    private final Gson gson = new Gson();

    public ContractSupplierController(LocalPushMessageManager pushMessageManager, ContractSupplierService contractSupplierService, UserService userService)
    {
        this.pushMessageManager = pushMessageManager;
        this.contractSupplierService = contractSupplierService;
        this.userService = userService;
    }

    public void newContractSupplier(ProcessedRequest request, NewContractSupplierRequest newContractSupplierRequest)
    {
        ContractSupplierDto contractSupplierDto = newContractSupplierRequest.getContractSupplierDto();
        Supplier supplier = contractSupplierService.SupplierIdValidation(contractSupplierDto.getSupplierId());
        NewContractSupplierResponse newContractSupplierResponse;
        if(supplier == null || contractSupplierDto.isTerminated()){
            newContractSupplierResponse = new NewContractSupplierResponse(ResponseTypeConstant.NEW_CONTRACT_WITH_SUPPLIER,
                    contractSupplierDto, "supplier_not_found");
        }else
        {
            if (supplier.getMaterials().contains(contractSupplierDto.getMaterialId())){
                Integer materialPrice = supplier.getPricePerUnit().get(supplier.getMaterials().indexOf(contractSupplierDto.getMaterialId()));
                List<ContractSupplierDetailDto> contractSupplierDetailDtos = new ArrayList<>();
                for(int i = 0; i < newContractSupplierRequest.getWeeks()+1; i++){
                    ContractSupplierDetailDto contractSupplierDetailDto = new ContractSupplierDetailDto();
                    contractSupplierDetailDto.setContractDate(LocalDate.now().plusDays(i* 7L));
                    contractSupplierDetailDto.setBoughtAmount(newContractSupplierRequest.getAmount());
                    contractSupplierDetailDto.setPricePerUnit(materialPrice);
                    contractSupplierDetailDtos.add(contractSupplierDetailDto);
                }
                contractSupplierDto.setTeamId(userService.loadById(newContractSupplierRequest.playerId).getTeam().getId());
                ContractSupplierDto saveContractSupplierDto = contractSupplierService.save(contractSupplierDto, contractSupplierDetailDtos);
                newContractSupplierResponse = new NewContractSupplierResponse(ResponseTypeConstant.NEW_CONTRACT_WITH_SUPPLIER,
                        saveContractSupplierDto, "success");
            }else {
                newContractSupplierResponse = new NewContractSupplierResponse(ResponseTypeConstant.NEW_CONTRACT_WITH_SUPPLIER,
                        contractSupplierDto, "supplier_material_not_matching");
            }

        }
        pushMessageManager.sendMessageBySession(request.session, gson.toJson(newContractSupplierResponse));

    }

    public void terminateLongtermContractSupplier(ProcessedRequest request,
                                                  TerminateLongtermContractSupplierRequest terminateLongtermContractSupplierRequest)
    {
        ContractSupplierDto contractSupplierDto = contractSupplierService.findById(terminateLongtermContractSupplierRequest.getContractId());
        TerminateLongtermContractSupplierResponse terminateLongtermContractSupplierResponse;
        if(contractSupplierDto.getTeamId().equals(userService.loadById(terminateLongtermContractSupplierRequest.playerId).getTeam().getId()) &&
           contractSupplierDto.getContractType().equals(Enums.ContractType.LONGTERM))
        {
            contractSupplierDto.setTerminated(true);
            Integer penalty = contractSupplierDto.getTerminatePenalty();
            terminateLongtermContractSupplierResponse = new TerminateLongtermContractSupplierResponse(ResponseTypeConstant.TERMINATE_LONGTERM_CONTRACT_WITH_SUPPLIER,
                    "terminated", penalty);
        }else {
            terminateLongtermContractSupplierResponse = new TerminateLongtermContractSupplierResponse(ResponseTypeConstant.TERMINATE_LONGTERM_CONTRACT_WITH_SUPPLIER,
                    "not_terminated", 0);
        }
        pushMessageManager.sendMessageBySession(request.session, gson.toJson(terminateLongtermContractSupplierResponse));
    }
}
