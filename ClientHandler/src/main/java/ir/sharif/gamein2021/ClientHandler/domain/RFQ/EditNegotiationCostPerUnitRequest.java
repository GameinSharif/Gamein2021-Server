package ir.sharif.gamein2021.ClientHandler.domain.RFQ;
import ir.sharif.gamein2021.ClientHandler.view.RequestObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class EditNegotiationCostPerUnitRequest extends RequestObject implements Serializable{
    Integer negotiationId;
    Integer newCostPerUnit;
}
