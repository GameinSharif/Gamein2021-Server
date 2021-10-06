package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.ClientHandler.util.ResponseTypeConstant;
import ir.sharif.gamein2021.ClientHandler.view.RequestObject;
import ir.sharif.gamein2021.core.domain.dto.NegotiationDto;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.util.Enums.NegotiationState;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NewNegotiationRequest extends RequestObject implements Serializable {
    //NegotiationDto negotiationDto; // the demander and supplier field is empty
    private Integer productId;
    private Integer amount;
    private Integer costPerUnitDemander;
    private Integer costPerUnitSupplier;
    //LocalDateTime earliestExpectedArrival;
    //LocalDateTime latestExpectedArrival;
    private Integer supplierId;
}
