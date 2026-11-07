package kenakata.payment;

import kenakata.exceptions.InsufficientWalletBalanceException;
import kenakata.util.Validate;

public class Wallet implements PaymentMethod {

    private final String walletNumber;
    private long balance;

    public Wallet(long initialBalance) {
        this("WALLET", initialBalance);
    }

    public Wallet(
            String walletNumber,
            long initialBalance
    ) {
        this.walletNumber = Validate.notBlank(
                walletNumber,
                "Wallet number"
        );

        this.balance = Validate.nonNegative(
                initialBalance,
                "Initial wallet balance"
        );
    }

    public String getWalletNumber() {
        return walletNumber;
    }

    public String getNumber() {
        return walletNumber;
    }

    public long getBalance() {
        return balance;
    }

    public long balance() {
        return balance;
    }

    public void debit(long amount)
            throws InsufficientWalletBalanceException {

        Validate.nonNegative(
                amount,
                "Debit amount"
        );

        if (amount > balance) {
            throw new InsufficientWalletBalanceException(
                    "Insufficient wallet balance. Required: "
                            + amount
                            + ", available: "
                            + balance
            );
        }

        balance -= amount;
    }

    public void credit(long amount) {
        Validate.positive(
                amount,
                "Credit amount"
        );

        balance += amount;
    }

    @Override
    public void authorize(long amount)
            throws InsufficientWalletBalanceException {

        debit(amount);
    }

    @Override
    public String toString() {
        return "Wallet{walletNumber='" + walletNumber + "', balance=" + balance + "}";
    }
}