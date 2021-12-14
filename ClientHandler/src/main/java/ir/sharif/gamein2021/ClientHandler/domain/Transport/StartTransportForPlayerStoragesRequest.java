package ir.sharif.gamein2021.ClientHandler.domain.Transport;

import ir.sharif.gamein2021.core.util.Enums;
import ir.sharif.gamein2021.core.view.RequestObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class StartTransportForPlayerStoragesRequest extends RequestObject implements Serializable {
    private final int sourceId;
    private final int sourceType;
    private final int destinationId;
    private final int destinationType;
    private final Integer productId;
    private final Integer amount;
    private boolean hasInsurance;
    private Enums.VehicleType vehicleType;
}
