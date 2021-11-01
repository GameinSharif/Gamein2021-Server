package ir.sharif.gamein2021.ClientHandler.domain.productionLine;

import ir.sharif.gamein2021.core.domain.dto.ProductionLineDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.io.Serializable;

public class ScrapProductionLineResponse extends ResponseObject implements Serializable {
    private ProductionLineDto productionLine;

    public ScrapProductionLineResponse(ProductionLineDto productionLine) {
        responseTypeConstant = ResponseTypeConstant.SCRAP_PRODUCTION_LINE.ordinal();
        this.productionLine = productionLine;
    }
}
