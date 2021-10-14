package ir.sharif.gamein2021.ClientHandler.util;

public enum RequestTypeConstant
{
    LOGIN(0),
    NEW_OFFER(1),
    GET_OFFERS(2),
    GET_GAME_DATA(3),
    GET_RANDOM_COUNTRY(4),
    GET_CURRENT_WEEK_DEMANDS(5),
    BID_FOR_AUCTION(6);

    private int value;

    RequestTypeConstant(int value)
    {
        this.value = value;
    }
}
