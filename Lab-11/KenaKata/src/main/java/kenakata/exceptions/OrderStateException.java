package kenakata.exceptions;

public class OrderStateException
        extends BusinessException {

    public OrderStateException(String message) {
        super(message);
    }

    public OrderStateException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
    }
}