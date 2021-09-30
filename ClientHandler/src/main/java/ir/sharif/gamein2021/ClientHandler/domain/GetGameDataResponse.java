package ir.sharif.gamein2021.ClientHandler.domain;

import ir.sharif.gamein2021.ClientHandler.util.ResponseTypeConstant;
import ir.sharif.gamein2021.ClientHandler.view.ResponseObject;
import ir.sharif.gamein2021.core.domain.dto.GameinCustomerDto;
import ir.sharif.gamein2021.core.domain.dto.WeekDemandDto;
import ir.sharif.gamein2021.core.util.Product;

import java.io.Serializable;
import java.util.List;

public class GetGameDataResponse extends ResponseObject implements Serializable
{
    private List<GameinCustomerDto> gameinCustomers;
    private List<WeekDemandDto> thisWeekDemands;

    private Product[] products;

    public GetGameDataResponse(ResponseTypeConstant responseTypeConstant, List<GameinCustomerDto> gameinCustomers, List<WeekDemandDto> thisWeekDemands, Product[] products)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.gameinCustomers = gameinCustomers;
        this.thisWeekDemands = thisWeekDemands;
        this.products = products;
    }
}
