package ir.sharif.gamein2021.ClientHandler.domain.productionLine;

import ir.sharif.gamein2021.core.domain.dto.ProductionLineDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.io.Serializable;

public class StartProductionResponse extends ResponseObject implements Serializable {
    private ProductionLineDto productionLine;

    public StartProductionResponse() {
        responseTypeConstant = ResponseTypeConstant.START_PRODUCTION.ordinal();
    }

    public StartProductionResponse(ProductionLineDto productionLine) {
        responseTypeConstant = ResponseTypeConstant.START_PRODUCTION.ordinal();
        this.productionLine = productionLine;
    }

    public void setProductionLine(ProductionLineDto productionLine) {
        this.productionLine = productionLine;
    }
}
