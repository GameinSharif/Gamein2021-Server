package ir.sharif.gamein2021.core.exception;

public class MessageNotFoundInChatException extends RuntimeException {

    public MessageNotFoundInChatException() {}

    public MessageNotFoundInChatException(String message) {
        super(message);
    }
    
}