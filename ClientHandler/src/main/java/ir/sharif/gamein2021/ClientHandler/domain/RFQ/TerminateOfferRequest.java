package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.core.view.RequestObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class TerminateOfferRequest extends RequestObject implements Serializable
{
    private final Integer offerId;
}
