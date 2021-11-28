package ir.sharif.gamein2021.ClientHandler.domain.Product;

import ir.sharif.gamein2021.core.view.RequestObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class AddProductRequest extends RequestObject implements Serializable {
    private final boolean isDc;
    private final Integer buildingId;
    private final Integer productId;
    private final int amount;
}
