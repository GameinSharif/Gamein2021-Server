package ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests;

import ir.sharif.gamein2021.core.domain.entity.ProductionLine;
import ir.sharif.gamein2021.core.domain.entity.ProductionLineProduct;

public class ProductCreationCompletedRequest extends BaseClientHandlerRequest {
    private final Integer productionLineId;
    private final ProductionLineProduct product;
    private final Integer teamId;

    public ProductCreationCompletedRequest(ProductionLine productionLine, ProductionLineProduct product, String message) {
        super(message);
        this.productionLineId = productionLine.getId();
        this.product = product;
        this.teamId = productionLine.getTeam().getId();
    }

    public Integer getProductionLineId() {
        return productionLineId;
    }

    public ProductionLineProduct getProduct() {
        return product;
    }

    public Integer getTeamId() {
        return teamId;
    }
}
