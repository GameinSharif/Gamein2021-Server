package ir.sharif.gamein2021.core.exception;

public class ChatNotFoundException extends RuntimeException {
    public ChatNotFoundException() {
    }

    public ChatNotFoundException(String message) {
        super(message);
    }
}
