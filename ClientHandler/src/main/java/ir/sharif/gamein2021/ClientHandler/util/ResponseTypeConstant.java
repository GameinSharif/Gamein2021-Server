package ir.sharif.gamein2021.ClientHandler.util;

public enum ResponseTypeConstant
{
    CONNECTION(0),
    LOGIN(1),
    NEW_OFFER(2),
    GET_OFFERS(3),
    GET_GAME_DATA(4),
    NEW_PROVIDER(5),
    GET_PROVIDERS(6);

    private int value;

    ResponseTypeConstant(int value)
    {
        this.value = value;
    }
}
