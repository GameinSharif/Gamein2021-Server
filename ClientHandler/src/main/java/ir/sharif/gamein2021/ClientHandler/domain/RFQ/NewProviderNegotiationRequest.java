package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.core.view.RequestObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class NewProviderNegotiationRequest extends RequestObject implements Serializable
{
    private Integer providerId;
    private Integer amount;
    private Integer costPerUnitDemander;
}
