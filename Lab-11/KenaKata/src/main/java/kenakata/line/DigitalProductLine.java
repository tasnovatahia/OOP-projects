package kenakata.line;

import kenakata.catalog.DigitalGood;
import kenakata.util.Validate;

public final class DigitalProductLine extends ProductLine {

    private final DigitalGood digitalGood;

    public DigitalProductLine(
            DigitalGood digitalGood,
            int quantity
    ) {
        super(
                Validate.notNull(
                        digitalGood,
                        "Digital good"
                ),
                quantity
        );

        this.digitalGood = digitalGood;
    }

    public DigitalGood getDigitalGood() {
        return digitalGood;
    }
}