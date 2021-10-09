package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.ClientHandler.view.RequestObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class NewProviderNegotiationRequest extends RequestObject implements Serializable {
    private Integer productId;
    private Integer amount;
    private Integer costPerUnitDemander;
    private Integer costPerUnitSupplier;
    //LocalDateTime earliestExpectedArrival;
    //LocalDateTime latestExpectedArrival;
    private Integer providerId;
}
