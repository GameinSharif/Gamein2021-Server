package ir.sharif.gamein2021.ClientHandler.domain.Contract;

import ir.sharif.gamein2021.core.view.RequestObject;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class TerminateLongtermContractRequest extends RequestObject implements Serializable
{
    private int contractId;
}
