package ir.sharif.gamein2021.ClientHandler.domain;

import ir.sharif.gamein2021.core.domain.dto.NewsDto;
import ir.sharif.gamein2021.core.domain.dto.CoronaInfoDto;
import ir.sharif.gamein2021.core.domain.dto.GameinCustomerDto;
import ir.sharif.gamein2021.core.domain.dto.TeamDto;
import ir.sharif.gamein2021.core.manager.ReadJsonFilesManager;
import ir.sharif.gamein2021.core.util.GameConstants;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.util.models.*;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.io.Serializable;
import java.util.List;

public class GetGameDataResponse extends ResponseObject implements Serializable {
    private List<TeamDto> teams;
    private List<GameinCustomerDto> gameinCustomers;
    private List<NewsDto> newsDtos;
    private List<CoronaInfoDto> coronaInfos;

    private Product[] products;
    private Factory[] factories;
    private Supplier[] suppliers;
    private ProductionLineTemplate[] productionLineTemplates;
    private Vehicle[] vehicles;

    public GameConstants gameConstants;

    public GetGameDataResponse(ResponseTypeConstant responseTypeConstant, List<TeamDto> teams, List<GameinCustomerDto> gameinCustomers, List<NewsDto> newsDtos)
    {
        this.teams = teams;
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.gameinCustomers = gameinCustomers;
        this.newsDtos = newsDtos;
        this.coronaInfos = coronaInfos;
                               List<CoronaInfoDto> coronaInfos) {

        products = ReadJsonFilesManager.Products;
        factories = ReadJsonFilesManager.Factories;
        suppliers = ReadJsonFilesManager.Suppliers;
        productionLineTemplates = ReadJsonFilesManager.ProductionLineTemplates;
        vehicles = ReadJsonFilesManager.Vehicles;

        this.gameConstants = GameConstants.Instance;
    }
}
