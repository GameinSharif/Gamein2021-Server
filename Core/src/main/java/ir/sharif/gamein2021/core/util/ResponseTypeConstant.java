package ir.sharif.gamein2021.core.util;

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
    NEW_PROVIDER_NEGOTIATION(13),
    GET_ALL_AUCTIONS(14),
    BID_FOR_AUCTION(15),
    TERMINATE_OFFER(16),
    NEW_MESSAGE(17),
    GET_ALL_CHATS(18),
    GET_PRODUCTION_LINES(19),
    CONSTRUCT_PRODUCTION_LINE(20);

    private int value;

    ResponseTypeConstant(int value)
    {
        this.value = value;
    }
}
