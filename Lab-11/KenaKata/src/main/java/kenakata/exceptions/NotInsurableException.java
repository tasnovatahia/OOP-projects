package kenakata.exceptions;

public class NotInsurableException
        extends InsuranceException {

    public NotInsurableException(String message) {
        super(message);
    }

    public NotInsurableException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
    }
}