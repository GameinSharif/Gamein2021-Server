package ir.sharif.gamein2021.core.util;

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
    GET_PROVIDERS(9),
    REMOVE_PROVIDER(10),
    NEW_PROVIDER_NEGOTIATION(11),
    BID_FOR_AUCTION(12),
    TERMINATE_OFFER(13),
    NEW_MESSAGE(14),
    GET_ALL_CHATS(15),
    GET_PRODUCTION_LINES(16),
    CONSTRUCT_PRODUCTION_LINE(17),
    SCRAP_PRODUCTION_LINE(18);

    private int value;

    RequestTypeConstant(int value)
    {
        this.value = value;
    }
}
