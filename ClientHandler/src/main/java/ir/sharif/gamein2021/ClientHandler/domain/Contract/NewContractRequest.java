package ir.sharif.gamein2021.ClientHandler.domain.Contract;

import ir.sharif.gamein2021.core.util.Enums;
import ir.sharif.gamein2021.core.view.RequestObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class NewContractRequest extends RequestObject implements Serializable
{
    private Integer gameinCustomerId;
    private Integer productId;
    private Integer amount;
    private Float pricePerUnit;
    private Integer weeks;
}