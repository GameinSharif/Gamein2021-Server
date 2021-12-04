package ir.sharif.gamein2021.ClientHandler.domain;

import ir.sharif.gamein2021.core.domain.dto.ContractSupplierDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.io.Serializable;

public class TerminateLongtermContractSupplierResponse extends ResponseObject implements Serializable
{
    private String result;
    private ContractSupplierDto contractSupplier;

    public TerminateLongtermContractSupplierResponse(ResponseTypeConstant responseTypeConstant, String result, ContractSupplierDto contractSupplier)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.result = result;
        this.contractSupplier = contractSupplier;
    }
}
