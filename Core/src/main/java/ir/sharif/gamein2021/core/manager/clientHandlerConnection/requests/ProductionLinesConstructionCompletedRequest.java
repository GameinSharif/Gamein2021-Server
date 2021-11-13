package ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests;

import ir.sharif.gamein2021.core.domain.entity.ProductionLine;

import java.util.List;

public class ProductionLinesConstructionCompletedRequest extends BaseClientHandlerRequest{
    private List<ProductionLine> savedProductionLines;

    public ProductionLinesConstructionCompletedRequest(List<ProductionLine> savedProductionLines, String message) {
        super(message);
        this.savedProductionLines = savedProductionLines;
    }

    public List<ProductionLine> getSavedProductionLines() {
        return savedProductionLines;
    }
}
