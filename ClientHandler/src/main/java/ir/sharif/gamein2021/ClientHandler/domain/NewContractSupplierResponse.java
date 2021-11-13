package ir.sharif.gamein2021.ClientHandler.domain;

import ir.sharif.gamein2021.core.domain.dto.ContractSupplierDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.io.Serializable;


public class NewContractSupplierResponse extends ResponseObject implements Serializable
{
    private ContractSupplierDto contractSupplier;
    private Float price;
    private String result;

    public NewContractSupplierResponse(ResponseTypeConstant responseTypeConstant, ContractSupplierDto contractSupplier,
                                       Float price, String result)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.contractSupplier = contractSupplier;
        this.price = price;
        this.result = result;
    }


}
