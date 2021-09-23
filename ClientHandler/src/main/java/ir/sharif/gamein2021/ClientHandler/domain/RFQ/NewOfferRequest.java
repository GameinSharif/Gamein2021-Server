package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.ClientHandler.view.RequestObject;
import ir.sharif.gamein2021.core.domain.dto.OfferDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class NewOfferRequest extends RequestObject implements Serializable {
    OfferDto offerDto;
}
