package ir.sharif.gamein2021.ClientHandler.domain.productionLine;

import ir.sharif.gamein2021.core.domain.entity.ProductionLine;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.io.Serializable;

public class ProductionLineConstructionCompletedResponse extends ResponseObject implements Serializable {
    private ProductionLine productionLine;

    public ProductionLineConstructionCompletedResponse(ProductionLine productionLine) {
        responseTypeConstant = ResponseTypeConstant.PRODUCTION_LINE_CONSTRUCTION_COMPLETED.ordinal();
        this.productionLine = productionLine;
    }
}
