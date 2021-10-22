package ir.sharif.gamein2021.ClientHandler.domain.productionLine;

import ir.sharif.gamein2021.core.view.RequestObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class StartProductionRequest extends RequestObject implements Serializable {
    private Integer productionLineId;
    private Integer productId;
    private Integer amount;
}
