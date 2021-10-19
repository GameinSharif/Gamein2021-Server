package ir.sharif.gamein2021.ClientHandler.domain;

import ir.sharif.gamein2021.core.util.GameConstants;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;
import ir.sharif.gamein2021.core.domain.dto.GameinCustomerDto;
import ir.sharif.gamein2021.core.util.models.Product;

import java.io.Serializable;
import java.util.List;

public class GetGameDataResponse extends ResponseObject implements Serializable
{
    private List<GameinCustomerDto> gameinCustomers;

    private Product[] products;

    public GameConstants gameConstants;

    public GetGameDataResponse(ResponseTypeConstant responseTypeConstant, List<GameinCustomerDto> gameinCustomers, Product[] products)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.gameinCustomers = gameinCustomers;
        this.products = products;

        this.gameConstants = GameConstants.Instance;
    }
}
