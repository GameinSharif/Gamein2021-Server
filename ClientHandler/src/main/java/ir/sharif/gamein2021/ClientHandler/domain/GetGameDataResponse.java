package ir.sharif.gamein2021.ClientHandler.domain;

import ir.sharif.gamein2021.core.manager.ReadJsonFilesManager;
import ir.sharif.gamein2021.core.util.GameConstants;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.util.models.Factory;
import ir.sharif.gamein2021.core.util.models.ProductionLineTemplate;
import ir.sharif.gamein2021.core.view.ResponseObject;
import ir.sharif.gamein2021.core.domain.dto.GameinCustomerDto;
import ir.sharif.gamein2021.core.util.models.Product;

import java.io.Serializable;
import java.util.List;

public class GetGameDataResponse extends ResponseObject implements Serializable
{
    private List<GameinCustomerDto> gameinCustomers;

    private Product[] products;
    private Factory[] factories;
    private ProductionLineTemplate[] productionLineTemplates;

    public GameConstants gameConstants;

    public GetGameDataResponse(ResponseTypeConstant responseTypeConstant, List<GameinCustomerDto> gameinCustomers)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.gameinCustomers = gameinCustomers;

        products = ReadJsonFilesManager.Products;
        factories = ReadJsonFilesManager.Factories;
        productionLineTemplates = ReadJsonFilesManager.ProductionLineTemplates;

        this.gameConstants = GameConstants.Instance;
    }
}
