package ir.sharif.gamein2021.ClientHandler.util;

public enum RequestTypeConstant
{
    LOGIN(0),
    NEW_OFFER(1),
    GET_OFFERS(2),
    GET_GAME_DATA(3),
    GET_CONTRACTS(4);
    NEW_NEGOTIATION(3),
    GET_NEGOTIATIONS(4),
    GET_GAME_DATA(5),
    EDIT_NEGOTIATION_COST_PER_UNIT(6);

    private int value;

    RequestTypeConstant(int value)
    {
        this.value = value;
    }
}
