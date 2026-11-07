package kenakata.catalog;

import kenakata.seller.Seller;
import kenakata.util.MoneyMath;
import kenakata.util.Validate;

public final class FreshGood extends CatalogueItem {

    private static final long COLD_CHAIN_SURCHARGE = 50;

    private final long weightGrams;
    private final int returnWindowDays;

    public FreshGood(
            String sku,
            String title,
            long unitPrice,
            int stock,
            Seller seller,
            long weightGrams,
            int returnWindowDays
    ) {
        super(
                sku,
                title,
                unitPrice,
                stock,
                seller
        );

        this.weightGrams = Validate.positive(
                weightGrams,
                "Weight in grams"
        );

        this.returnWindowDays = Validate.positive(
                returnWindowDays,
                "Return window days"
        );
    }

    public long getWeightGrams() {
        return weightGrams;
    }

    public int getReturnWindowDays() {
        return returnWindowDays;
    }

    public long getColdChainSurcharge() {
        return COLD_CHAIN_SURCHARGE;
    }

    @Override
    public long getVat() {
        return 0;
    }

    @Override
    public long getCommission(long lineCharge) {
        return MoneyMath.ceilPercentage(
                lineCharge,
                5.0
        );
    }

    @Override
    public boolean isDiscountable() {
        return false;
    }
}