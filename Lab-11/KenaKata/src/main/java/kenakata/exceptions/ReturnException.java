package kenakata.exceptions;

public class ReturnException extends BusinessException {

    public ReturnException(String message) {
        super(message);
    }

    public ReturnException(String message, Throwable cause) {
        super(message, cause);
    }
}