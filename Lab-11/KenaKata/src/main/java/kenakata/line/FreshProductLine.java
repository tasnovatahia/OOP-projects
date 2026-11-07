package kenakata.line;

import kenakata.catalog.FreshGood;
import kenakata.util.Validate;

public final class FreshProductLine
        extends ProductLine
        implements WeighableLine,
        ColdChainLine,
        InsurableLine,
        ReturnableLine {

    private final FreshGood freshGood;

    public FreshProductLine(
            FreshGood freshGood,
            int quantity
    ) {
        super(
                Validate.notNull(
                        freshGood,
                        "Fresh good"
                ),
                quantity
        );

        this.freshGood = freshGood;
    }

    public FreshGood getFreshGood() {
        return freshGood;
    }

    @Override
    public long getTotalWeightGrams() {
        return Math.multiplyExact(
                freshGood.getWeightGrams(),
                getQuantity()
        );
    }

    @Override
    public long getColdChainSurcharge() {
        return freshGood.getColdChainSurcharge();
    }

    @Override
    public long getInsurableValue() {
        return getCharge();
    }

    @Override
    public int getReturnWindowDays() {
        return freshGood.getReturnWindowDays();
    }
}
