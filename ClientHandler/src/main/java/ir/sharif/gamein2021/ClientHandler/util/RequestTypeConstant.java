package ir.sharif.gamein2021.ClientHandler.util;

public enum RequestTypeConstant
{
    LOGIN(0),
    NEW_OFFER(1),
    GET_OFFERS(2),
    NEW_NEGOTIATION(3),
    GET_NEGOTIATIONS(4);

    private int value;

    RequestTypeConstant(int value)
    {
        this.value = value;
    }
}
