package ir.sharif.gamein2021.core.exception;

public class TeamNotFoundException extends RuntimeException {
    public TeamNotFoundException() {
    }

    public TeamNotFoundException(String message) {
        super(message);
    }
}
