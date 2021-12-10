package ir.sharif.gamein2021.core.response;

import ir.sharif.gamein2021.core.domain.dto.ContractDto;
import ir.sharif.gamein2021.core.domain.dto.ContractSupplierDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.io.Serializable;

public class ContractSupplierFinalizedResponse extends ResponseObject implements Serializable
{
    private ContractSupplierDto contractSupplier;

    public ContractSupplierFinalizedResponse(ResponseTypeConstant responseTypeConstant, ContractSupplierDto contractSupplier)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.contractSupplier = contractSupplier;
    }
}
