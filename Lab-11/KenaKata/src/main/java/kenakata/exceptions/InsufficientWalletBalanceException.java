package kenakata.exceptions;

public class InsufficientWalletBalanceException
        extends PaymentException {

    public InsufficientWalletBalanceException(String message) {
        super(message);
    }

    public InsufficientWalletBalanceException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
    }
}
