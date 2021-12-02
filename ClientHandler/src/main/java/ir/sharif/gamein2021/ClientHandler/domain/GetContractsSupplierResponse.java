package ir.sharif.gamein2021.ClientHandler.domain;

import ir.sharif.gamein2021.core.domain.dto.ContractSupplierDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.util.List;
import java.io.Serializable;

public class GetContractsSupplierResponse extends ResponseObject implements Serializable
{
    private List<ContractSupplierDto> contractsSupplier;

    public GetContractsSupplierResponse(ResponseTypeConstant responseTypeConstant, List<ContractSupplierDto> contractsSupplier)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.contractsSupplier = contractsSupplier;
    }

}
