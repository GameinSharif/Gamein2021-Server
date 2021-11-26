package ir.sharif.gamein2021.ClientHandler.domain.RFQ;

import ir.sharif.gamein2021.core.view.RequestObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class EditProviderRequest extends RequestObject implements Serializable {
    private final Integer providerId;
    private final Integer newCapacity;
    private final Float newPrice;
}
