package kenakata.exceptions;

public class NonReturnableException
        extends ReturnException {

    public NonReturnableException(String message) {
        super(message);
    }

    public NonReturnableException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
    }
}