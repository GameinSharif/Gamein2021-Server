package ir.sharif.gamein2021.ClientHandler.domain.Product;

import ir.sharif.gamein2021.core.view.RequestObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class AddProductRequest extends RequestObject implements Serializable {
    private boolean isDc;
    private Integer buildingId;
    private Integer productId;
    private int amount;
}
