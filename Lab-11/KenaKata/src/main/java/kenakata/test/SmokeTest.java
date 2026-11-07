package kenakata.test;
import kenakata.coupon.Coupon;
import kenakata.delivery.DeliveryZone;
import kenakata.exceptions.InsufficientWalletBalanceException;
import kenakata.exceptions.NonReturnableException;
import kenakata.exceptions.NotInsurableException;
import kenakata.catalog.DigitalGood;
import kenakata.catalog.FreshGood;
import kenakata.catalog.StockedGood;
import kenakata.line.ProductLine;
import kenakata.order.Order;
import kenakata.order.PriceBreakdown;
import kenakata.payment.Wallet;
import kenakata.seller.Seller;
import kenakata.settlement.Marketplace;
import kenakata.settlement.SettlementReport;

public class SmokeTest {
    private SmokeTest() {
        throw new AssertionError("SmokeTest cannot be instantiated.");
    }

    public static void main(String[] args) throws Exception {
        testWorkedExample();
        testAtomicPaymentFailure();
        testOptionalCapabilities();
        testSettlement();

        System.out.println();
        System.out.println("All Kenakata smoke tests passed successfully.");
    }

    public static void testWorkedExample() throws Exception {
        Seller electronicsSeller = new Seller("Seller-001", "Dhaka Electronics");


        Seller bookSeller = new Seller("Seller-002", "Digital Book House");

        Seller fishSeller = new Seller("Seller-O03", "Fresh Fish Market");

        StockedGood tableLamp = new StockedGood("LAMP-001", "Table Lamp", 1200, 10, electronicsSeller, 1500, 7);
        DigitalGood eBook = new DigitalGood("BOOK-001", "Java E-book", 300, 20, bookSeller);
        FreshGood hilsa = new FreshGood("FISH-001", "Hilsa", 1600, 5, fishSeller, 1000, 2);

        Coupon coupon = new Coupon("EID10",  200,  150);

        Order order = new Order("ORDER_WORKED_EXAMPLE");
        order.addProductLine(tableLamp, 2);
        order.addProductLine(eBook, 1);


        order.applyCoupon(coupon);

        PriceBreakdown quote = order.quote(DeliveryZone.INSIDE_DHAKA, 100);
        assertEquals(4350L, quote.getSubtotal(), "Worked-example subtotal");

        assertEquals(190L, quote.getDelivery(), "Worked-example delivery");

        assertEquals(199L, quote.getTax(), "Worked-example tax");

        assertEquals(0L, quote.getInsurance(), "Worked-example insurance");

        assertEquals(44L, quote.getServiceFee(), "Worked-example grand total");

        assertEquals(
                4583L,
                quote.getGrandTotal(),
                "Worked-example grand total"
        );
        assertEquals(
                10,
                tableLamp.getStockRemaining(),
                "Lamp stock after quote"
        );
        assertEquals(
                20,
                eBook.getStockRemaining(),
                "E-book stock after quote"
        );

        assertEquals(
                5,
                hilsa.getStockRemaining(),
                "Hilsa stock after quote"
        );
        System.out.println(
                "PASS: worked-example pricing"
        );
    }

    private static void testAtomicPaymentFailure()
            throws Exception {

        Seller seller = new Seller(
                "SELLER-ATOMIC",
                "Atomic Seller"
        );

        StockedGood product = new StockedGood(
                "PRODUCT-ATOMIC",
                "Expensive Product",
                5000,
                3,
                seller,
                500,
                7
        );
        Order order = new Order(
                "ORDER-ATOMIC"
        );

        order.addProductLine(
                product,
                1
        );

        Wallet wallet = new Wallet(
                100
        );

        int stockBefore =
                product.getStockRemaining();
        long balanceBefore =
                wallet.getBalance();

        expectThrows(
                InsufficientWalletBalanceException.class,
                () -> order.place(
                        wallet,
                        DeliveryZone.INSIDE_DHAKA,
                        100
                ),
                "Insufficient wallet payment"
        );

        assertEquals(
                stockBefore,
                product.getStockRemaining(),
                "Stock after declined payment"
        );
        assertEquals(
                balanceBefore,
                wallet.getBalance(),
                "Wallet balance after declined payment"
        );

        assertFalse(
                order.isPlaced(),
                "Order must remain unplaced"
        );

        System.out.println(
                "PASS: atomic payment failure"
        );
    }

    private static void testOptionalCapabilities()
            throws Exception {

        Seller seller = new Seller(
                "SELLER-DIGITAL",
                "Digital Seller"
        );

        DigitalGood digitalGood = new DigitalGood(
                "DIGITAL-001",
                "Programming Guide",
                500,
                10,
                seller
        );

        Order order = new Order(
                "ORDER-CAPABILITY"
        );
        ProductLine digitalLine =
                order.addProductLine(
                        digitalGood,
                        1
                );

        expectThrows(
                NotInsurableException.class,
                () -> order.insureLine(digitalLine),
                "Digital product insurance"
        );

        Wallet wallet = new Wallet(
                5000
        );

        order.place(
                wallet,
                DeliveryZone.INSIDE_DHAKA,
                100
        );

        expectThrows(
                NonReturnableException.class,
                () -> order.returnLine(
                        digitalLine,
                        101
                ),
                "Digital product return"
        );

        System.out.println(
                "PASS: optional line capabilities"
        );
    }

    private static void testSettlement()
            throws Exception {

        Seller electronicsSeller = new Seller(
                "SETTLE-SELLER-001",
                "Electronics Seller"
        );

        Seller fishSeller = new Seller(
                "SETTLE-SELLER-002",
                "Fish Seller"
        );

        StockedGood tableLamp = new StockedGood(
                "SETTLE-LAMP",
                "Table Lamp",
                1200,
                10,
                electronicsSeller,
                1500,
                7
        );
        DigitalGood eBook = new DigitalGood(
                "SETTLE-BOOK",
                "E-book",
                300,
                10,
                electronicsSeller
        );

        FreshGood hilsa = new FreshGood(
                "SETTLE-FISH",
                "Hilsa",
                1600,
                5,
                fishSeller,
                1000,
                2
        );
        Coupon coupon = new Coupon(
                "EID10",
                1000,
                150
        );

        Order order = new Order(
                "ORDER-SETTLEMENT"
        );

        order.addProductLine(
                tableLamp,
                2
        );
        order.addProductLine(
                eBook,
                1
        );

        order.addProductLine(
                hilsa,
                1
        );



        order.applyCoupon(coupon);
        Wallet wallet = new Wallet(
                10000
        );

        Marketplace marketplace =
                new Marketplace();

        marketplace.registerSeller(
                electronicsSeller
        );

        marketplace.registerSeller(
                fishSeller
        );

        PriceBreakdown placedPrice =
                marketplace.placeOrder(
                        order,
                        wallet,
                        DeliveryZone.INSIDE_DHAKA,
                        100
                );


        marketplace.registerSeller(
                electronicsSeller
        );

        marketplace.registerSeller(
                fishSeller
        );


        assertEquals(
                4583L,
                placedPrice.getGrandTotal(),
                "Placed-order grand total"
        );

        assertEquals(
                5417L,
                wallet.getBalance(),
                "Wallet balance after placement"
        );

        assertEquals(
                8,
                tableLamp.getStockRemaining(),
                "Lamp stock after placement"
        );

        assertEquals(
                9,
                eBook.getStockRemaining(),
                "E-book stock after placement"
        );

        assertEquals(
                4,
                hilsa.getStockRemaining(),
                "Hilsa stock after placement"
        );

        SettlementReport report =
                marketplace.settleDay(100);


        assertEquals(
                2448L,
                report.getSellerPayout(
                        electronicsSeller
                ),
                "Electronics seller payout"
        );

        /*
         * Fish seller:
         *
         * Hilsa charge = 1600
         * Commission = 5% = 80
         * Payout = 1520
         */
        assertEquals(
                1520L,
                report.getSellerPayout(
                        fishSeller
                ),
                "Fish seller payout"
        );


        assertEquals(
                615L,
                report.getPlatformRevenue(),
                "Platform revenue"
        );

        System.out.println(
                "PASS: placement and settlement"
        );
    }

    private static void assertEquals(
            long expected,
            long actual,
            String testName
    ) {
        if (expected != actual) {
            throw new AssertionError(
                    testName
                            + " failed. Expected: "
                            + expected
                            + ", actual: "
                            + actual
            );
        }
    }

    private static void assertEquals(
            int expected,
            int actual,
            String testName
    ) {
        if (expected != actual) {
            throw new AssertionError(
                    testName
                            + " failed. Expected: "
                            + expected
                            + ", actual: "
                            + actual
            );
        }
    }

    private static void assertFalse(
            boolean condition,
            String testName
    ) {
        if (condition) {
            throw new AssertionError(
                    testName + " failed."
            );
        }
    }

    private static <T extends Throwable> void expectThrows(
            Class<T> expectedException,
            ThrowingAction action,
            String testName
    ) throws Exception {

        try {
            action.run();

        } catch (Throwable exception) {

            if (expectedException.isInstance(exception)) {
                return;
            }

            throw new AssertionError(testName + " threw " + exception.getClass().getSimpleName() + " instead of " + expectedException.getSimpleName(),
                    exception
            );
        }

        throw new AssertionError(
                testName
                        + " failed because "
                        + expectedException.getSimpleName()
                        + " was not thrown."
        );
    }

    @FunctionalInterface
    private interface ThrowingAction {

        void run() throws Exception;
    }
}
