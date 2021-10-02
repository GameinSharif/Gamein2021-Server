package ir.sharif.gamein2021.ClientHandler.util;

public enum RequestTypeConstant
{
    LOGIN(0),
    NEW_OFFER(1),
    GET_OFFERS(2),
    NEW_PROVIDER(5),
    GET_PROVIDERS(6);

    private int value;

    RequestTypeConstant(int value)
    {
        this.value = value;
    }
}
