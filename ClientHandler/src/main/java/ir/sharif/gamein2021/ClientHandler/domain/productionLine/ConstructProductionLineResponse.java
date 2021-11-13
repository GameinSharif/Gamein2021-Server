package ir.sharif.gamein2021.ClientHandler.domain.productionLine;

import ir.sharif.gamein2021.core.domain.dto.ProductionLineDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.io.Serializable;

public class ConstructProductionLineResponse extends ResponseObject implements Serializable {
    ProductionLineDto productionLine;

    public ConstructProductionLineResponse() {
        responseTypeConstant = ResponseTypeConstant.CONSTRUCT_PRODUCTION_LINE.ordinal();
    }

    public ConstructProductionLineResponse(ProductionLineDto productionLine) {
        responseTypeConstant = ResponseTypeConstant.CONSTRUCT_PRODUCTION_LINE.ordinal();
        this.productionLine = productionLine;
    }

    public ProductionLineDto getProductionLine() {
        return productionLine;
    }

    public void setProductionLine(ProductionLineDto productionLine) {
        this.productionLine = productionLine;
    }
}
