package ir.sharif.gamein2021.ClientHandler.util;

public enum ResponseTypeConstant
{
    CONNECTION(0),
    LOGIN(1),
    NEW_OFFER(2),
    GET_OFFERS(3),
    GET_GAME_DATA(4),
    GET_CURRENT_WEEK_DEMANDS(5),
    GET_CONTRACTS(6),
    NEW_NEGOTIATION(7),
    GET_NEGOTIATIONS(8),
    EDIT_NEGOTIATION_COST_PER_UNIT(9),
    NEW_PROVIDER(10),
    GET_PROVIDERS(11),
    REMOVE_PROVIDER(12),
    NEW_PROVIDER_NEGOTIATION(13);

    private int value;

    ResponseTypeConstant(int value)
    {
        this.value = value;
    }
}
