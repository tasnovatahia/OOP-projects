package kenakata.settlement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kenakata.delivery.DeliveryZone;
import kenakata.exceptions.BusinessException;
import kenakata.line.OrderLine;
import kenakata.line.ProductLine;
import kenakata.order.Order;
import kenakata.order.PriceBreakdown;
import kenakata.order.ReturnRecord;
import kenakata.payment.PaymentMethod;
import kenakata.seller.Seller;
import kenakata.util.Validate;

public final class Marketplace {

    private final Set<Seller> sellers;
    private final List<Order> placedOrders;

    public Marketplace() {
        this.sellers = new LinkedHashSet<>();
        this.placedOrders = new ArrayList<>();
    }

    public void registerSeller(Seller seller) {
        Seller validSeller = Validate.notNull(
                seller,
                "Seller"
        );

        sellers.add(validSeller);
    }

    public Set<Seller> getSellers() {
        return Collections.unmodifiableSet(sellers);
    }

    public List<Order> getPlacedOrders() {
        return Collections.unmodifiableList(placedOrders);
    }

    /*
     * Compatibility method for your previous name.
     */
    public List<Order> getPlaceOrders() {
        return getPlacedOrders();
    }

    public void recordPlacedOrder(Order order) {
        Validate.notNull(
                order,
                "Order"
        );

        if (!order.isPlaced()) {
            throw new IllegalArgumentException(
                    "Only a successfully placed order can be recorded."
            );
        }

        if (!placedOrders.contains(order)) {
            placedOrders.add(order);
        }
    }

    /*
     * Compatibility method for your previous name.
     */
    public void recordPlaceOrder(Order order) {
        recordPlacedOrder(order);
    }

    public PriceBreakdown placeOrder(
            Order order,
            PaymentMethod paymentMethod,
            DeliveryZone deliveryZone,
            int currentDay
    ) throws BusinessException {

        Validate.notNull(
                order,
                "Order"
        );

        Validate.notNull(
                paymentMethod,
                "Payment method"
        );

        Validate.notNull(
                deliveryZone,
                "Delivery zone"
        );

        Validate.nonNegative(
                currentDay,
                "Current day"
        );

        PriceBreakdown price = order.place(
                paymentMethod,
                deliveryZone,
                currentDay
        );

        recordPlacedOrder(order);

        return price;
    }

    public PriceBreakdown placeOrder(
            Order order,
            DeliveryZone deliveryZone,
            int currentDay,
            PaymentMethod paymentMethod
    ) throws BusinessException {

        return placeOrder(
                order,
                paymentMethod,
                deliveryZone,
                currentDay
        );
    }

    public SettlementReport settleDay(int day) {
        Validate.nonNegative(
                day,
                "Settlement day"
        );

        Map<Seller, Long> payouts =
                createEmptyPayoutMap();

        long platformRevenue = 0;

        /*
         * Calculate sales and platform revenue
         * for orders placed on this day.
         */
        for (Order order : placedOrders) {

            if (order.getPlacedDay() != day) {
                continue;
            }

            long commissions = 0;
            long addOnRevenue = 0;

            for (OrderLine line : order.getLines()) {

                if (line instanceof ProductLine productLine) {

                    Seller seller =
                            productLine.getSeller();

                    payouts.putIfAbsent(
                            seller,
                            0L
                    );

                    long productCharge =
                            productLine.getCharge();

                    long commission =
                            productLine.getCommission();

                    commissions = Math.addExact(
                            commissions,
                            commission
                    );

                    long sellerNetAmount =
                            Math.subtractExact(
                                    productCharge,
                                    commission
                            );

                    payouts.merge(
                            seller,
                            sellerNetAmount,
                            Math::addExact
                    );

                } else {
                    /*
                     * Non-product lines are add-ons.
                     */
                    addOnRevenue = Math.addExact(
                            addOnRevenue,
                            line.getCharge()
                    );
                }
            }

            PriceBreakdown price =
                    order.getPlacedPrice();

            long orderPlatformRevenue =
                    commissions;

            orderPlatformRevenue = Math.addExact(
                    orderPlatformRevenue,
                    addOnRevenue
            );

            orderPlatformRevenue = Math.addExact(
                    orderPlatformRevenue,
                    price.getDelivery()
            );

            orderPlatformRevenue = Math.addExact(
                    orderPlatformRevenue,
                    price.getTax()
            );

            orderPlatformRevenue = Math.addExact(
                    orderPlatformRevenue,
                    price.getInsurance()
            );

            orderPlatformRevenue = Math.addExact(
                    orderPlatformRevenue,
                    price.getServiceFee()
            );

            /*
             * Subtract the coupon discount only once.
             */
            orderPlatformRevenue = Math.subtractExact(
                    orderPlatformRevenue,
                    price.getDiscount()
            );

            platformRevenue = Math.addExact(
                    platformRevenue,
                    orderPlatformRevenue
            );
        }

        /*
         * Process refunds made on this settlement day.
         */
        for (Order order : placedOrders) {

            for (ReturnRecord returnRecord
                    : order.getReturnRecords()) {

                if (returnRecord.getReturnDay() != day) {
                    continue;
                }

                Seller seller =
                        returnRecord.getSeller();

                long refund =
                        returnRecord.getRefundAmount();

                payouts.putIfAbsent(
                        seller,
                        0L
                );

                payouts.merge(
                        seller,
                        -refund,
                        Math::addExact
                );
            }
        }

        return new SettlementReport(
                day,
                payouts,
                platformRevenue
        );
    }

    public SettlementReport runSettlement(int day) {
        return settleDay(day);
    }

    public SettlementReport settle(int day) {
        return settleDay(day);
    }

    private Map<Seller, Long> createEmptyPayoutMap() {
        Map<Seller, Long> payouts =
                new LinkedHashMap<>();

        for (Seller seller : sellers) {
            payouts.put(
                    seller,
                    0L
            );
        }

        return payouts;
    }
}
