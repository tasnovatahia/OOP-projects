package kenakata.catalog;

import kenakata.seller.Seller;
import kenakata.util.MoneyMath;

public final class DigitalGood extends CatalogueItem {

    public DigitalGood(
            String sku, String title, long unitPrice, int stock, Seller seller
    ) {
        super(sku, title, unitPrice, stock, seller
        );
    }

    @Override
    public long getVat() {
        return MoneyMath.ceilPercentage(getUnitPrice(), 5.0
        );
    }
    @Override
    public long getCommission(long lineCharge) {
        return MoneyMath.ceilPercentage(
                lineCharge,
                20.0
        );
    }
    @Override
    public boolean isDiscountable() {
        return false;
    }
}