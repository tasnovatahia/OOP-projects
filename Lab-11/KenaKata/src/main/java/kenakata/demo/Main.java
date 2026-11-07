/*package kenakata.demo;


import kenakata.addon.GiftWrapping;
import kenakata.coupon.Coupon;
import kenakata.delivery.DeliveryZone;
import kenakata.exception.BusinessException;
import kenakata.item.DigitalGood;
import kenakata.item.FreshGood;
import kenakata.item.StockedGood;
import kenakata.line.ProductLine;
import kenakata.order.Order;
import kenakata.order.PriceBreakdown;
import kenakata.payment.CardPayment;
import kenakata.payment.CashOnDelivery;
import kenakata.payment.PaymentMethod;
import kenakata.payment.Wallet;
import kenakata.seller.Seller;
import kenakata.settlement.Marketplace;
import kenakata.settlement.SettlementReport;

public final class Main {

    private Main() {
        throw new AssertionError(
                "Main class cannot be instantiated."
        );
    }

    public static void main(String[] args) {

        try {
            Marketplace marketplace = new Marketplace();
            Seller electronicsSeller = new Seller("SELLER-001", "Dhaka Electronics"
            );

            Seller bookSeller = new Seller("SELLER-002", "Digital Book House"
            );

            Seller freshSeller = new Seller("SELLER-003", "Fresh Food Market"
            );


            marketplace.registerSeller(electronicsSeller);
            marketplace.registerSeller(bookSeller);
            marketplace.registerSeller(freshSeller);




            DigitalGood javaEBook = new DigitalGood(
                    "BOOK-001",
                    "Java Programming E-book",
                    300,
                    100,
                    bookSeller
            );

            DigitalGood designCourse = new DigitalGood(
                    "COURSE-001",
                    "Graphic Design Course",
                    800,
                    50,
                    bookSeller
            );


            FreshGood hilsa = new FreshGood(
                    "FISH-001",
                    "Hilsa Fish",
                    1600,
                    10,
                    freshSeller,
                    1000,
                    2
            );

            FreshGood mangoBox = new FreshGood(
                    "MANGO-001",
                    "Fresh Mango Box",
                    1800,
                    15,
                    freshSeller,
                    3000,
                    2
            );


            Coupon eidCoupon = new Coupon(
                    "EID10",
                    10.0,
                    200,
                    1000,
                    150
            );

            PaymentMethod paymentMethod;


            System.out.println(
                    "========================================"
            );

            System.out.println("ORDER 1: WALLET PAYMENT");

            System.out.println(
                    "========================================"
            );

            Order firstOrder = new Order(
                    "ORDER-001"
            );



            firstOrder.addProductLine(javaEBook, 1
            );

            ProductLine hilsaLine = firstOrder.addProductLine(hilsa, 1);


            firstOrder.addAddOn(
                    new GiftWrapping()
            );





            firstOrder.applyCoupon(
                    eidCoupon
            );







            System.out.println();
            System.out.println("Order 1 quotation:");


            Wallet wallet = new Wallet(
                    "01700000000",
                    20_000
            );

            paymentMethod = wallet;

            try {
                PriceBreakdown firstPlacedPrice =
                        marketplace.placeOrder(
                                firstOrder,
                                paymentMethod,
                                DeliveryZone.INSIDE_DHAKA,
                                100
                        );

                System.out.println();
                System.out.println(
                        "Order 1 placed successfully."
                );

                System.out.println(
                        "Grand total: "
                                + firstPlacedPrice.getGrandTotal()
                                + " Taka"
                );

                System.out.println(
                        "Wallet balance after payment: "
                                + wallet.getBalance()
                                + " Taka"
                );



                System.out.println(
                        "Hilsa stock remaining: "
                                + hilsa.getStockRemaining()
                );

            } catch (BusinessException exception) {
                System.out.println(
                        "Order 1 was refused: "
                                + exception.getMessage()
                );
            }/*

            /*
             * -------------------------------------------------
             * ORDER 2
             * Deliberately refused card payment
             * -------------------------------------------------
             */
            /*System.out.println();
            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "ORDER 2: DECLINED CARD PAYMENT"
            );

            System.out.println(
                    "========================================"
            );

            Order secondOrder = new Order(
                    "ORDER-002"
            );



            secondOrder.addProductLine(
                    designCourse,
                    1
            );






            CardPayment card = new CardPayment(
                    "1111-2222-3333-4444",
                    1000
            );

            paymentMethod = card;

            try {
                marketplace.placeOrder(
                        secondOrder,
                        paymentMethod,
                        DeliveryZone.OUTSIDE_DHAKA,
                        100
                );

                System.out.println(
                        "Order 2 placed successfully."
                );

            } catch (BusinessException exception) {
                System.out.println();
                System.out.println(
                        "Order 2 was refused as expected."
                );

                System.out.println(
                        "Reason: "
                                + exception.getMessage()
                );



                System.out.println(
                        "Card limit after refusal: "
                                + card.getRemainingLimit()
                );
            }


            System.out.println();
            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "ORDER 3: CASH ON DELIVERY"
            );

            System.out.println(
                    "========================================"
            );

            Order thirdOrder = new Order(
                    "ORDER-003"
            );

            ProductLine mangoLine =
                    thirdOrder.addProductLine(
                            mangoBox,
                            2
                    );

            thirdOrder.addProductLine(
                    javaEBook,
                    2
            );




            thirdOrder.insureLine(
                    mangoLine
            );



            System.out.println();
            System.out.println("Order 3 quotation:");


            CashOnDelivery cashOnDelivery =
                    new CashOnDelivery();

            paymentMethod = cashOnDelivery;

            try {
                PriceBreakdown thirdPlacedPrice =
                        marketplace.placeOrder(
                                thirdOrder,
                                paymentMethod,
                                DeliveryZone.OUTSIDE_DHAKA,
                                100
                        );

                System.out.println();
                System.out.println(
                        "Order 3 placed successfully."
                );

                System.out.println(
                        "Grand total: "
                                + thirdPlacedPrice.getGrandTotal()
                                + " Taka"
                );

                System.out.println(
                        "Mango stock remaining: "
                                + mangoBox.getStockRemaining()
                );

            } catch (BusinessException exception) {
                System.out.println(
                        "Order 3 was refused: "
                                + exception.getMessage()
                );
            }*/

            /*
             * -------------------------------------------------
             * 5. Process one return
             * -------------------------------------------------
             */
            /*System.out.println();
            System.out.println(
                    "========================================"
            );

            System.out.println("PRODUCT RETURN");

            System.out.println(
                    "========================================"
            );

            try {



                System.out.println(
                        "Hilsa line returned successfully."
                );

                System.out.println(
                        "Refund amount: "
                                + hilsaLine.getCharge()
                                + " Taka"
                );

            } catch (BusinessException exception) {
                System.out.println(
                        "Return refused: "
                                + exception.getMessage()
                );
            }*/

            /*
             * -------------------------------------------------
             * 6. Run nightly settlement
             * -------------------------------------------------
             */
            /*System.out.println();
            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "NIGHTLY SETTLEMENT: DAY 100"
            );

            System.out.println(
                    "========================================"
            );

            SettlementReport settlementReport =
                    marketplace.settleDay(100);

            System.out.println();
            System.out.println(settlementReport);*/

            /*
             * -------------------------------------------------
             * 7. Final stock report
             * -------------------------------------------------
             */
            /*System.out.println();
            System.out.println(
                    "========================================"
            );

            System.out.println("FINAL STOCK REPORT");

            System.out.println(
                    "========================================"
            );



            System.out.println(
                    "Java E-book: "
                            + javaEBook.getStockRemaining()
            );

            System.out.println(
                    "Graphic Design Course: "
                            + designCourse.getStockRemaining()
            );

            System.out.println(
                    "Hilsa Fish: "
                            + hilsa.getStockRemaining()
            );

            System.out.println(
                    "Fresh Mango Box: "
                            + mangoBox.getStockRemaining()
            );

        } catch (BusinessException exception) {*/
            /*
             * Catches a business refusal occurring while
             * preparing an order, such as insurance refusal
             * or coupon rejection.
             */
            /*System.out.println(
                    "Business operation refused: "
                            + exception.getMessage()
            );

        } catch (IllegalArgumentException exception) {*/
            /*
             * Catches malformed object construction.
             */
            /*System.out.println(
                    "Invalid data: "
                            + exception.getMessage()
            );

        } catch (ArithmeticException exception) {*/
            /*
             * Catches an unlikely whole-number overflow.
             */
            /*System.out.println(
                    "Money calculation overflow: "
                            + exception.getMessage()
            );
        }
    }
}*/