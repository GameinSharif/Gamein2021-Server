package ir.sharif.gamein2021.ClientHandler.domain.productionLine;

import ir.sharif.gamein2021.core.domain.entity.ProductionLineProduct;
import ir.sharif.gamein2021.core.manager.clientHandlerConnection.requests.ProductCreationCompletedRequest;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.io.Serializable;

public class ProductCreationCompletedResponse extends ResponseObject implements Serializable {

    private final ProductionLineProduct product;
    private final Integer productLineId;

    public ProductCreationCompletedResponse(ProductCreationCompletedRequest productCreationCompletedRequest) {
        responseTypeConstant = ResponseTypeConstant.PRODUCT_CREATION_COMPLETED.ordinal();
        product =  productCreationCompletedRequest.getProduct();
        productLineId=  productCreationCompletedRequest.getProductionLineId();
    }

    public ProductionLineProduct getProduct() {
        return product;
    }

    public Integer getProductLineId() {
        return productLineId;
    }
}
