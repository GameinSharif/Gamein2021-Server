package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.ClientHandler.view.RequestObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class EditNegotiationCostPerUnitRequest extends RequestObject implements Serializable
{
    private Integer negotiationId;
    private Integer newCostPerUnit;
}
