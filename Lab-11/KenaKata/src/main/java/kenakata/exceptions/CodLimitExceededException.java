package kenakata.exceptions;

public class CodLimitExceededException extends PaymentException {

    public CodLimitExceededException(String message) {
        super(message);
    }

    public CodLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}