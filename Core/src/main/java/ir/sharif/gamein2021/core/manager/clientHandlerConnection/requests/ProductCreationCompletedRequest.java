package ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests;

import ir.sharif.gamein2021.core.domain.entity.ProductionLine;
import ir.sharif.gamein2021.core.domain.entity.ProductionLineProduct;

public class ProductCreationCompletedRequest extends BaseClientHandlerRequest {
    private final ProductionLine productionLine;
    private final ProductionLineProduct product;
    private final Integer teamId;

    public ProductCreationCompletedRequest(ProductionLine productionLine, ProductionLineProduct product, String message) {
        super(message);
        this.productionLine = productionLine;
        this.product = product;
        this.teamId = productionLine.getTeam().getId();
    }

    public ProductionLine getProductionLine() {
        return productionLine;
    }

    public ProductionLineProduct getProduct() {
        return product;
    }

    public Integer getTeamId() {
        return teamId;
    }
}
