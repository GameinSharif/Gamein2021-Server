package ir.sharif.gamein2021.core.util;

public enum ResponseTypeConstant {
    CONNECTION(0),
    LOGIN(1),
    NEW_OFFER(2),
    GET_OFFERS(3),
    GET_GAME_DATA(4),
    GET_CURRENT_WEEK_DEMANDS(5),
    GET_CONTRACTS(6),
    ACCEPT_OFFER(7),
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
    CONSTRUCT_PRODUCTION_LINE(20),
    SCRAP_PRODUCTION_LINE(21),
    START_PRODUCTION(22),
    UPGRADE_PRODUCTION_LINE_QUALITY(23),
    UPGRADE_PRODUCTION_LINE_EFFICIENCY(24),
    GET_CURRENT_WEEK_SUPPLIES(25),
    NEW_CONTRACT_WITH_SUPPLIER(26),
    TERMINATE_LONGTERM_CONTRACT_WITH_SUPPLIER(27),
    BUY_DC(28),
    SELL_DC(29),
    GET_TEAM_TRANSPORTS(30),
    TRANSPORT_STATE_CHANGED(31),
    GET_CONTRACTS_WITH_SUPPLIER(32),
    AUCTION_FINISHED(33),
    GET_ALL_ACTIVE_DC(34),
    ADD_PRODUCT(35),
    REMOVE_PRODUCT(36),
    GET_STORAGES(37),
    NEW_CONTRACT(38),
    TERMINATE_CONTRACT(39),
    SERVER_TIME(40),
    GAME_TIME(41),
    MONEY_UPDATE(42),
    PRODUCTION_LINE_CONSTRUCTION_COMPLETED(43),
    PRODUCT_CREATION_COMPLETED (44),
    UPDATE_GAME_STATUS(45),
    ACCESS_DENIED(46),
    TRANSPORT_TO_STORAGE(47),
    GET_ALL_WEEKLY_REPORTS(48),
    UPDATE_WEEKLY_REPORT(49),
    REJECT_NEGOTIATION(50),
    EDIT_PROVIDER_RESPONSE(51);

    private int value;

    ResponseTypeConstant(int value) {
        this.value = value;
    }
}
