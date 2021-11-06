package ir.sharif.gamein2021.core.exception;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(String message) {
        super(message);
    }
}
