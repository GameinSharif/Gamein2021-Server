package ir.sharif.gamein2021.ClientHandler.domain;

import ir.sharif.gamein2021.core.domain.dto.ContractSupplierDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.io.Serializable;

public class TerminateLongtermContractSupplierResponse extends ResponseObject implements Serializable
{
    private String result;

    public TerminateLongtermContractSupplierResponse(ResponseTypeConstant responseTypeConstant, String result)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.result = result;
    }
}
