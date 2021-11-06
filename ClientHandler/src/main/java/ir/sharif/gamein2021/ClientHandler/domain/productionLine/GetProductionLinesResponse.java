package ir.sharif.gamein2021.ClientHandler.domain.productionLine;

import ir.sharif.gamein2021.core.domain.dto.ProductionLineDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.io.Serializable;
import java.util.List;

public class GetProductionLinesResponse extends ResponseObject implements Serializable {
    private List<ProductionLineDto> productionLines;

    public GetProductionLinesResponse(List<ProductionLineDto> productionLines) {
        responseTypeConstant = ResponseTypeConstant.GET_PRODUCTION_LINES.ordinal();
        this.productionLines = productionLines;
    }
}
