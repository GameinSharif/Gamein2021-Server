package ir.sharif.gamein2021.ClientHandler.util;

public enum ResponseTypeConstant
{
    CONNECTION(0),
    LOGIN(1),
    NEW_OFFER(2),
    GET_OFFERS(3),
    NEW_NEGOTIATION(4),
    GET_NEGOTIATIONS(5),
    GET_GAME_DATA(6);

    private int value;

    ResponseTypeConstant(int value)
    {
        this.value = value;
    }
}
