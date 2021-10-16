package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.ClientHandler.util.ResponseTypeConstant;
import ir.sharif.gamein2021.ClientHandler.view.RequestObject;
import ir.sharif.gamein2021.core.domain.dto.NegotiationDto;
import ir.sharif.gamein2021.core.domain.entity.Team;
import ir.sharif.gamein2021.core.util.Enums.NegotiationState;
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
