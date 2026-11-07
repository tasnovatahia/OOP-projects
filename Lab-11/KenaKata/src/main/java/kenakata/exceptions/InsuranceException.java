package kenakata.exceptions;

public class InsuranceException extends BusinessException {

    public InsuranceException(String message) {
        super(message);
    }

    public InsuranceException(String message, Throwable cause) {
        super(message, cause);
    }
}