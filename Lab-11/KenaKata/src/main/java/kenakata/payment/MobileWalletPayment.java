package kenakata.payment;
public class MobileWalletPayment extends Wallet {

    public MobileWalletPayment(long initialBalance) {
        super(initialBalance);
    }

    public MobileWalletPayment(
            String walletNumber,
            long initialBalance
    ) {
        super(
                walletNumber,
                initialBalance
        );
    }
}