package ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests;

import ir.sharif.gamein2021.core.domain.dto.ProductionLineDto;
import ir.sharif.gamein2021.core.domain.dto.ProductionLineProductDto;

public class ProductCreationCompletedRequest extends BaseClientHandlerRequest {
    private final ProductionLineDto productionLine;
    private final ProductionLineProductDto product;
    private final Integer teamId;

    public ProductCreationCompletedRequest(ProductionLineDto productionLine, ProductionLineProductDto product, String message) {
        super(message);
        this.productionLine = productionLine;
        this.product = product;
        this.teamId = productionLine.getTeamId();
    }

    public ProductionLineDto getProductionLine() {
        return productionLine;
    }

    public ProductionLineProductDto getProduct() {
        return product;
    }

    public Integer getTeamId() {
        return teamId;
    }
}
