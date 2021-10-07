package ir.sharif.gamein2021.ClientHandler.util;

public enum RequestTypeConstant
{
    LOGIN(0),
    NEW_OFFER(1),
    GET_OFFERS(2),
    GET_GAME_DATA(3),
    GET_CONTRACTS(4),
    NEW_NEGOTIATION(5),
    GET_NEGOTIATIONS(6),
    EDIT_NEGOTIATION_COST_PER_UNIT(7),
    NEW_PROVIDER(8),
    GET_PROVIDERS(9);

    private int value;

    RequestTypeConstant(int value)
    {
        this.value = value;
    }
}
