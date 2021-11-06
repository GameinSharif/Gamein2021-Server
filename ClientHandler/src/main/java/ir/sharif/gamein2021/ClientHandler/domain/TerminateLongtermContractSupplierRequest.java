package ir.sharif.gamein2021.ClientHandler.domain;

import ir.sharif.gamein2021.core.view.RequestObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class TerminateLongtermContractSupplierRequest extends RequestObject implements Serializable
{
    private int contractId;
}
