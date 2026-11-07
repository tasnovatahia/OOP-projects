package kenakata.payment;
import kenakata.exceptions.PaymentException;

public interface  PaymentMethod {


    void authorize(long amount) throws PaymentException;

    default void authorise(long amount) throws PaymentException{
        authorise(amount);
    }

}
