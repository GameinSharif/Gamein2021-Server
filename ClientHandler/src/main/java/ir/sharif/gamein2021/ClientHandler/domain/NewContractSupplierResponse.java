package ir.sharif.gamein2021.ClientHandler.domain;

import ir.sharif.gamein2021.core.domain.dto.ContractSupplierDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.io.Serializable;
import java.util.List;


public class NewContractSupplierResponse extends ResponseObject implements Serializable
{
    private List<ContractSupplierDto> contractSuppliers;
    private Float price;
    private String result;

    public NewContractSupplierResponse(ResponseTypeConstant responseTypeConstant, List<ContractSupplierDto> contractSuppliers,
                                       Float price, String result)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.contractSuppliers = contractSuppliers;
        this.price = price;
        this.result = result;
    }


}
