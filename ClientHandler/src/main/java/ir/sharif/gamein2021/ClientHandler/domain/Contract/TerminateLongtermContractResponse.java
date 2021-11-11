package ir.sharif.gamein2021.ClientHandler.domain.Contract;

import ir.sharif.gamein2021.core.domain.dto.ContractDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.io.Serializable;

public class TerminateLongtermContractResponse extends ResponseObject implements Serializable
{
    private ContractDto terminatedContract;

    public TerminateLongtermContractResponse(ResponseTypeConstant responseTypeConstant, ContractDto terminatedContract)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.terminatedContract = terminatedContract;
    }
}
