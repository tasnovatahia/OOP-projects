package kenakata.exceptions;

public class ReturnWindowExpiredException
        extends ReturnException {

    public ReturnWindowExpiredException(String message) {
        super(message);
    }

    public ReturnWindowExpiredException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
    }
}