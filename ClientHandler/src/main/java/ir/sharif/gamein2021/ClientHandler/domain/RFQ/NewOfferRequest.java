package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.ClientHandler.view.RequestObject;
import ir.sharif.gamein2021.core.domain.dto.OfferDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Getter
public class NewOfferRequest extends RequestObject implements Serializable {

    private OfferDto offerDto;

}
