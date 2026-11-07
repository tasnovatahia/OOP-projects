package kenakata.exceptions;

public class OutOfStockException extends BusinessException {

    public OutOfStockException(String message) {
        super(message);
    }

    public OutOfStockException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
    }
}
