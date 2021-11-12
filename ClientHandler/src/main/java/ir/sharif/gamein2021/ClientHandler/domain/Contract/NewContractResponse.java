package ir.sharif.gamein2021.ClientHandler.domain.Contract;

import ir.sharif.gamein2021.core.domain.dto.ContractDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.io.Serializable;

public class NewContractResponse extends ResponseObject implements Serializable
{
    public ContractDto contract;

    public NewContractResponse(ResponseTypeConstant responseTypeConstant, ContractDto contract)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.contract = contract;
    }
}
