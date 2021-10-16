package ir.sharif.gamein2021.ClientHandler.util;

public enum RequestTypeConstant
{
    LOGIN(0),
    NEW_OFFER(1),
    GET_OFFERS(2),
    GET_GAME_DATA(3),
    BID_FOR_AUCTION(4);

    private int value;

    RequestTypeConstant(int value)
    {
        this.value = value;
    }
}
