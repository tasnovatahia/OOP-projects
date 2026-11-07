package kenakata.line;

import kenakata.catalog.StockedGood;
import kenakata.util.Validate;

public final class StockedProductLine
        extends ProductLine
        implements WeighableLine, InsurableLine, ReturnableLine {

    private final StockedGood stockedGood;

    public StockedProductLine(
            StockedGood stockedGood,
            int quantity
    ) {
        super(
                Validate.notNull(
                        stockedGood,
                        "Stocked good"
                ),
                quantity
        );

        this.stockedGood = stockedGood;
    }

    public StockedGood getStockedGood() {
        return stockedGood;
    }

    @Override
    public long getTotalWeightGrams() {
        return Math.multiplyExact(
                stockedGood.getWeightGrams(),
                getQuantity()
        );
    }

    @Override
    public long getInsurableValue() {
        return getCharge();
    }

    @Override
    public int getReturnWindowDays() {
        return stockedGood.getReturnWindowDays();
    }
}