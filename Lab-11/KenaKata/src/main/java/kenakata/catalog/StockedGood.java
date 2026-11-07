package kenakata.catalog;

import kenakata.seller.Seller;
import kenakata.util.MoneyMath;
import kenakata.util.Validate;

public final class StockedGood extends CatalogueItem {

    private final long weightGrams;
    private final int returnWindowDays;

    public StockedGood(String sku, String title, long unitPrice, int stock, Seller seller, long weightGrams, int returnWindowDays
    ) {
        super(sku, title, unitPrice, stock, seller);

        this.weightGrams = Validate.positive(
                weightGrams,
                "Weight in grams"
        );

        this.returnWindowDays = Validate.positive(returnWindowDays, "Return window days");
    }

    public long getWeightGrams() {
        return weightGrams;
    }

    public int getReturnWindowDays() {
        return returnWindowDays;
    }

    @Override
    public long getVat() {
        return MoneyMath.ceilPercentage(
                getUnitPrice(),
                7.5
        );
    }

    @Override
    public long getCommission(long lineCharge) {
        return MoneyMath.ceilPercentage(
                lineCharge,
                8.0
        );
    }

    @Override
    public boolean isDiscountable() {
        return true;
    }
}