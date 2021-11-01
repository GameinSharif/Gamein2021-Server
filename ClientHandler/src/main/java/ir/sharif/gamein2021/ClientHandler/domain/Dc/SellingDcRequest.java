package ir.sharif.gamein2021.ClientHandler.domain.Dc;

import ir.sharif.gamein2021.core.view.RequestObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class SellingDcRequest extends RequestObject implements Serializable {
    private final Integer dcId;

}
