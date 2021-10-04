package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.ClientHandler.view.RequestObject;
import ir.sharif.gamein2021.core.domain.dto.NegotiationDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class NewNegotiationRequest extends RequestObject implements Serializable {
    // TODO or send me the essential stuff not a whole dto?
    NegotiationDto negotiation;
}
