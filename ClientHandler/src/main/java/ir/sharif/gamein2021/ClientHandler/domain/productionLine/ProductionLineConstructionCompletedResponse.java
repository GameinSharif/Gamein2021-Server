package ir.sharif.gamein2021.ClientHandler.domain.productionLine;

import ir.sharif.gamein2021.core.domain.dto.ProductionLineDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class ProductionLineConstructionCompletedResponse extends ResponseObject implements Serializable {
    private ProductionLineDto productionLine;

    public ProductionLineConstructionCompletedResponse(ProductionLineDto productionLine) {
        responseTypeConstant = ResponseTypeConstant.PRODUCTION_LINE_CONSTRUCTION_COMPLETED.ordinal();
        this.productionLine = productionLine;
    }
}
