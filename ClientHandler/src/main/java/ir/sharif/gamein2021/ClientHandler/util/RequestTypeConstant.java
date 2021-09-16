package ir.sharif.gamein2021.ClientHandler.util;

public enum RequestTypeConstant {
    // Requests
    LOGIN(0),
    NEW_OFFER(1),
    GET_OFFERS(2);

    private int value;

    RequestTypeConstant(int value) {
        this.value = value;
    }
}
