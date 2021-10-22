package ir.sharif.gamein2021.ClientHandler.domain.productionLine;

import ir.sharif.gamein2021.core.domain.dto.ProductionLineDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.io.Serializable;

public class UpgradeProductionLineQualityResponse extends ResponseObject implements Serializable {
    private ProductionLineDto productionLine;

    public UpgradeProductionLineQualityResponse(ProductionLineDto productionLine) {
        responseTypeConstant = ResponseTypeConstant.UPGRADE_PRODUCTION_LINE_QUALITY.ordinal();
        this.productionLine = productionLine;
    }
}
