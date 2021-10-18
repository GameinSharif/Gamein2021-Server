package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.core.view.RequestObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class NewNegotiationRequest extends RequestObject implements Serializable
{
    private Integer supplierId;
    private Integer productId;
    private Integer amount;
    private Integer costPerUnitDemander;
    private LocalDate earliestExpectedArrival;
    private LocalDate latestExpectedArrival;
}
