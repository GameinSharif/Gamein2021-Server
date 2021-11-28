package ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests;

import ir.sharif.gamein2021.core.domain.dto.ProductionLineDto;

import java.util.List;

public class ProductionLinesConstructionCompletedRequest extends BaseClientHandlerRequest{
    private List<ProductionLineDto> savedProductionLines;

    public ProductionLinesConstructionCompletedRequest(List<ProductionLineDto> savedProductionLines, String message) {
        super(message);
        this.savedProductionLines = savedProductionLines;
    }

    public List<ProductionLineDto> getSavedProductionLines() {
        return savedProductionLines;
    }
}
