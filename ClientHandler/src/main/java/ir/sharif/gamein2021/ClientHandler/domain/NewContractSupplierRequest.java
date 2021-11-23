package ir.sharif.gamein2021.ClientHandler.domain;

import ir.sharif.gamein2021.core.view.RequestObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class NewContractSupplierRequest extends RequestObject implements Serializable
{
    private Integer supplierId;
    private Integer materialId;
    private Integer vehicleId;
    private Boolean hasInsurance;
    private Integer amount;
    private Integer weeks;
}
