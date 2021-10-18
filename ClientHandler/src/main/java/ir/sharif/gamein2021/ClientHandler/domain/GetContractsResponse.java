package ir.sharif.gamein2021.ClientHandler.domain;

import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;
import ir.sharif.gamein2021.core.domain.dto.ContractDto;

import java.io.Serializable;
import java.util.List;

public class GetContractsResponse extends ResponseObject implements Serializable
{
    private List<ContractDto> contracts;

    public GetContractsResponse(ResponseTypeConstant responseTypeConstant, List<ContractDto> contracts)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.contracts = contracts;
    }
}
