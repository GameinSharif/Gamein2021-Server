package ir.sharif.gamein2021.core.exception;

public class OfferNotFoundException extends RuntimeException {
    public OfferNotFoundException() {
    }

    public OfferNotFoundException(String message) {
        super(message);
    }
}
