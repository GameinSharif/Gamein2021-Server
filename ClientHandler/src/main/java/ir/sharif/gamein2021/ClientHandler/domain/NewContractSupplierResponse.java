package ir.sharif.gamein2021.ClientHandler.domain;

import ir.sharif.gamein2021.core.domain.dto.ContractSupplierDto;
import ir.sharif.gamein2021.core.domain.dto.WeekSupplyDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


public class NewContractSupplierResponse extends ResponseObject implements Serializable
{
    private ContractSupplierDto contractSupplierDto;
    private String result;

    public NewContractSupplierResponse(ResponseTypeConstant responseTypeConstant, ContractSupplierDto contractSupplierDto,
                                       String result)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.contractSupplierDto = contractSupplierDto;
        this.result = result;
    }


}
