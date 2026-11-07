package kenakata.exceptions;

public class MinimumSpendException
        extends CouponException {

    public MinimumSpendException(String message) {
        super(message);
    }

    public MinimumSpendException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
    }
}