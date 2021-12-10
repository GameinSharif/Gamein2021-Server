package ir.sharif.gamein2021.core.response;

import ir.sharif.gamein2021.core.domain.dto.ContractDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.io.Serializable;

public class ContractFinalizedResponse extends ResponseObject implements Serializable
{
    private ContractDto contract;

    public ContractFinalizedResponse(ResponseTypeConstant responseTypeConstant, ContractDto contract)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.contract = contract;
    }
}
