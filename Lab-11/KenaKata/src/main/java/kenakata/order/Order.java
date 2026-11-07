package kenakata.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import kenakata.coupon.Coupon;
import kenakata.delivery.DeliveryCalculator;
import kenakata.delivery.DeliveryZone;
import kenakata.exceptions.BusinessException;
import kenakata.exceptions.NonReturnableException;
import kenakata.exceptions.NotInsurableException;
import kenakata.exceptions.OrderStateException;
import kenakata.exceptions.ReturnWindowExpiredException;
import kenakata.catalog.CatalogueItem;
import kenakata.line.InsurableLine;
import kenakata.line.OrderLine;
import kenakata.line.ProductLine;
import kenakata.line.ReturnableLine;
import kenakata.payment.PaymentMethod;
import kenakata.util.MoneyMath;
import kenakata.util.Validate;

public final class Order {

    private static final double INSURANCE_PERCENTAGE = 1.0;
    private static final long MINIMUM_INSURANCE_FEE = 20;

    private static final double SERVICE_FEE_PERCENTAGE = 1.0;
    private static final long SERVICE_FEE_CAP = 100;

    private static final AtomicLong NEXT_ORDER_NUMBER =
            new AtomicLong(1);

    private final String orderId;
    private final List<OrderLine> lines;
    private final Set<OrderLine> insuredLines;
    private final List<ReturnRecord> returnRecords;

    private Coupon coupon;

    private boolean placed;
    private int placedDay;

    private DeliveryZone placedZone;
    private PriceBreakdown placedPrice;

    public Order() {
        this(
                "ORDER-"
                        + NEXT_ORDER_NUMBER.getAndIncrement()
        );
    }

    public Order(String orderId) {
        this.orderId = Validate.notBlank(
                orderId,
                "Order ID"
        );

        this.lines = new ArrayList<>();

        this.insuredLines =
                Collections.newSetFromMap(
                        new IdentityHashMap<>()
                );

        this.returnRecords = new ArrayList<>();

        this.placed = false;
        this.placedDay = -1;
        this.placedZone = null;
        this.placedPrice = null;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getId() {
        return orderId;
    }

    public String id() {
        return orderId;
    }

    public ProductLine addProductLine(
            CatalogueItem item,
            int quantity
    ) {
        ensureOrderCanBeChanged();

        Validate.notNull(
                item,
                "Catalogue item"
        );

        Validate.positive(
                quantity,
                "Quantity"
        );

        ProductLine line = ProductLine.create(
                item,
                quantity
        );

        lines.add(line);

        return line;
    }

    public ProductLine addProduct(
            CatalogueItem item,
            int quantity
    ) {
        return addProductLine(
                item,
                quantity
        );
    }

    public ProductLine addItem(
            CatalogueItem item,
            int quantity
    ) {
        return addProductLine(
                item,
                quantity
        );
    }

    public OrderLine addAddOn(OrderLine addOn) {
        ensureOrderCanBeChanged();

        Validate.notNull(
                addOn,
                "Add-on"
        );

        if (addOn instanceof ProductLine) {
            throw new IllegalArgumentException(
                    "A product line must be added using "
                            + "addProductLine()."
            );
        }

        lines.add(addOn);

        return addOn;
    }

    public OrderLine addAddon(OrderLine addOn) {
        return addAddOn(addOn);
    }

    public OrderLine addLine(OrderLine addOn) {
        return addAddOn(addOn);
    }

    public void applyCoupon(Coupon coupon) {
        ensureOrderCanBeChanged();

        this.coupon = Validate.notNull(
                coupon,
                "Coupon"
        );
    }

    public void setCoupon(Coupon coupon) {
        applyCoupon(coupon);
    }

    public void removeCoupon() {
        ensureOrderCanBeChanged();
        this.coupon = null;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void markLineToInsure(OrderLine line)
            throws NotInsurableException {

        ensureOrderCanBeChanged();

        Validate.notNull(
                line,
                "Line"
        );

        ensureLineBelongsToOrder(line);

        if (!(line instanceof InsurableLine)) {
            throw new NotInsurableException(
                    line.getReceiptLabel()
                            + " is not insurable."
            );
        }

        insuredLines.add(line);
    }

    public void insureLine(OrderLine line)
            throws NotInsurableException {

        markLineToInsure(line);
    }

    public void markLineToInsure(int lineIndex)
            throws NotInsurableException {

        markLineToInsure(
                getLine(lineIndex)
        );
    }

    public void insureLine(int lineIndex)
            throws NotInsurableException {

        markLineToInsure(lineIndex);
    }

    public void removeInsurance(OrderLine line) {
        ensureOrderCanBeChanged();

        Validate.notNull(
                line,
                "Line"
        );

        ensureLineBelongsToOrder(line);

        insuredLines.remove(line);
    }

    public boolean isInsured(OrderLine line) {
        return insuredLines.contains(line);
    }

    public PriceBreakdown quote(
            DeliveryZone deliveryZone,
            int currentDay
    ) throws BusinessException {

        Validate.notNull(
                deliveryZone,
                "Delivery zone"
        );

        Validate.nonNegative(
                currentDay,
                "Current day"
        );

        ensureOrderHasLines();

        long subtotal = calculateSubtotal();

        long discount = calculateDiscount(
                currentDay
        );

        long delivery = DeliveryCalculator.calculate(
                lines,
                deliveryZone
        );

        long tax = calculateTax();

        long insurance = calculateInsurance();

        long serviceFee = calculateServiceFee(
                subtotal
        );
return null;

    }

    public PriceBreakdown quote(
            int currentDay,
            DeliveryZone deliveryZone
    ) throws BusinessException {

        return quote(
                deliveryZone,
                currentDay
        );
    }

    public PriceBreakdown getQuote(
            DeliveryZone deliveryZone,
            int currentDay
    ) throws BusinessException {

        return quote(
                deliveryZone,
                currentDay
        );
    }

    public PriceBreakdown place(
            PaymentMethod paymentMethod,
            DeliveryZone deliveryZone,
            int currentDay
    ) throws BusinessException {

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

        if (placed) {
            throw new OrderStateException(
                    "Order "
                            + orderId
                            + " has already been placed."
            );
        }

        /*
         * Calculate the price before changing
         * stock or payment balance.
         */
        PriceBreakdown price = quote(
                deliveryZone,
                currentDay
        );

        List<ProductLine> productLines =
                getProductLinesInternal();

        List<ProductLine> reservedLines =
                new ArrayList<>();

        /*
         * Check all products before reserving any stock.
         */
        for (ProductLine productLine : productLines) {
            productLine.checkStock();
        }

        try {
            /*
             * Reserve stock.
             */
            for (ProductLine productLine : productLines) {
                productLine.reserveStock();
                reservedLines.add(productLine);
            }

            /*
             * Authorize the complete grand total.
             */
            paymentMethod.authorize(
                    price.getGrandTotal()
            );

        } catch (BusinessException exception) {

            /*
             * Restore stock if placement fails.
             */
            releaseReservedLines(
                    reservedLines
            );

            throw exception;

        } catch (RuntimeException exception) {

            releaseReservedLines(
                    reservedLines
            );

            throw exception;
        }

        placed = true;
        placedDay = currentDay;
        placedZone = deliveryZone;
        placedPrice = price;

        return price;
    }

    public PriceBreakdown place(
            DeliveryZone deliveryZone,
            int currentDay,
            PaymentMethod paymentMethod
    ) throws BusinessException {

        return place(
                paymentMethod,
                deliveryZone,
                currentDay
        );
    }

    public ReturnRecord acceptReturn(
            ProductLine productLine,
            int returnDay
    ) throws BusinessException {

        Validate.notNull(
                productLine,
                "Product line"
        );

        Validate.nonNegative(
                returnDay,
                "Return day"
        );

        if (!placed) {
            throw new OrderStateException(
                    "The order must be placed before "
                            + "a return can be accepted."
            );
        }

        ensureLineBelongsToOrder(
                productLine
        );



        if (!(productLine instanceof ReturnableLine)) {
            throw new NonReturnableException(
                    productLine.getReceiptLabel()
                            + " is not returnable."
            );
        }

        if (returnDay < placedDay) {
            throw new IllegalArgumentException(
                    "Return day cannot be before "
                            + "the order placement day."
            );
        }

        ReturnableLine returnableLine =
                (ReturnableLine) productLine;

        int elapsedDays =
                returnDay - placedDay;

        if (elapsedDays
                > returnableLine.getReturnWindowDays()) {

            throw new ReturnWindowExpiredException(
                    "The return window for "
                            + productLine.getReceiptLabel()
                            + " has expired."
            );
        }

        long refundAmount =
                productLine.getCharge();

        productLine.markReturned();

        ReturnRecord returnRecord =
                new ReturnRecord(
                        productLine,
                        returnDay,
                        refundAmount
                );

        returnRecords.add(returnRecord);

        return returnRecord;
    }

    public ReturnRecord acceptReturn(
            OrderLine line,
            int returnDay
    ) throws BusinessException {

        Validate.notNull(
                line,
                "Line"
        );

        if (!(line instanceof ProductLine)) {
            throw new NonReturnableException(
                    line.getReceiptLabel()
                            + " is not returnable."
            );
        }

        return acceptReturn(
                (ProductLine) line,
                returnDay
        );
    }

    public ReturnRecord acceptReturn(
            int lineIndex,
            int returnDay
    ) throws BusinessException {

        return acceptReturn(
                getLine(lineIndex),
                returnDay
        );
    }

    public ReturnRecord returnLine(
            ProductLine productLine,
            int returnDay
    ) throws BusinessException {

        return acceptReturn(
                productLine,
                returnDay
        );
    }

    public ReturnRecord returnLine(
            OrderLine line,
            int returnDay
    ) throws BusinessException {

        return acceptReturn(
                line,
                returnDay
        );
    }

    public ReturnRecord returnLine(
            int lineIndex,
            int returnDay
    ) throws BusinessException {

        return acceptReturn(
                lineIndex,
                returnDay
        );
    }

    public List<OrderLine> getLines() {
        return Collections.unmodifiableList(
                lines
        );
    }

    public List<ProductLine> getProductLines() {
        return Collections.unmodifiableList(
                getProductLinesInternal()
        );
    }

    public List<ReturnRecord> getReturnRecords() {
        return Collections.unmodifiableList(
                returnRecords
        );
    }

    public Set<OrderLine> getInsuredLines() {
        Set<OrderLine> copy =
                Collections.newSetFromMap(
                        new IdentityHashMap<>()
                );

        copy.addAll(insuredLines);

        return Collections.unmodifiableSet(copy);
    }

    public OrderLine getLine(int lineIndex) {
        if (
                lineIndex < 0
                        || lineIndex >= lines.size()
        ) {
            throw new IndexOutOfBoundsException(
                    "Invalid line index: "
                            + lineIndex
            );
        }

        return lines.get(lineIndex);
    }

    public int getLineCount() {
        return lines.size();
    }

    public boolean isPlaced() {
        return placed;
    }

    public int getPlacedDay() {
        return placedDay;
    }

    public DeliveryZone getPlacedZone() {
        return placedZone;
    }

    public PriceBreakdown getPlacedPrice() {
        if (placedPrice == null) {
            throw new IllegalStateException(
                    "The order has not been placed yet."
            );
        }

        return placedPrice;
    }

    public PriceBreakdown getPriceBreakdown() {
        return getPlacedPrice();
    }

    public long getSubtotal() {
        return getPlacedPrice().getSubtotal();
    }

    public long getDiscount() {
        return getPlacedPrice().getDiscount();
    }

    public long getDelivery() {
        return getPlacedPrice().getDelivery();
    }

    public long getTax() {
        return getPlacedPrice().getTax();
    }

    public long getInsurance() {
        return getPlacedPrice().getInsurance();
    }

    public long getServiceFee() {
        return getPlacedPrice().getServiceFee();
    }

    public long getGrandTotal() {
        return getPlacedPrice().getGrandTotal();
    }

    private void ensureOrderCanBeChanged() {
        if (placed) {
            throw new IllegalStateException(
                    "A placed order cannot be changed."
            );
        }
    }

    private void ensureOrderHasLines()
            throws OrderStateException {

        if (lines.isEmpty()) {
            throw new OrderStateException(
                    "The order must contain at least one line."
            );
        }
    }

    private void ensureLineBelongsToOrder(
            OrderLine line
    ) {
        for (OrderLine existingLine : lines) {
            if (existingLine == line) {
                return;
            }
        }

        throw new IllegalArgumentException(
                "The supplied line does not belong "
                        + "to this order."
        );
    }

    private long calculateSubtotal() {
        long subtotal = 0;

        for (OrderLine line : lines) {
            subtotal = Math.addExact(
                    subtotal,
                    line.getCharge()
            );
        }

        return subtotal;
    }

    private long calculateDiscount(int currentDay)
            throws BusinessException {

        if (coupon == null) {
            return 0;
        }

        long discountableBase = 0;

        for (OrderLine line : lines) {
            if (line instanceof ProductLine) {
                ProductLine productLine =
                        (ProductLine) line;

                if (productLine.isDiscountable()) {
                    discountableBase = Math.addExact(
                            discountableBase,
                            productLine.getCharge()
                    );
                }
            }
        }

        return coupon.computeDiscount(
                discountableBase,
                currentDay
        );
    }

    private long calculateTax() {
        long tax = 0;

        for (OrderLine line : lines) {
            tax = Math.addExact(
                    tax,
                    line.getVat()
            );
        }

        return tax;
    }

    private long calculateInsurance() {
        long insuranceTotal = 0;

        for (OrderLine line : insuredLines) {
            InsurableLine insurableLine =
                    (InsurableLine) line;

            long percentageFee =
                    MoneyMath.ceilPercentage(
                            insurableLine.getInsurableValue(),
                            INSURANCE_PERCENTAGE
                    );

            long lineInsurance =
                    Math.max(
                            percentageFee,
                            MINIMUM_INSURANCE_FEE
                    );

            insuranceTotal = Math.addExact(
                    insuranceTotal,
                    lineInsurance
            );
        }

        return insuranceTotal;
    }

    private long calculateServiceFee(long subtotal) {
        long calculatedFee =
                MoneyMath.ceilPercentage(
                        subtotal,
                        SERVICE_FEE_PERCENTAGE
                );

        return Math.min(
                calculatedFee,
                SERVICE_FEE_CAP
        );
    }

    private List<ProductLine> getProductLinesInternal() {
        List<ProductLine> productLines =
                new ArrayList<>();

        for (OrderLine line : lines) {
            if (line instanceof ProductLine) {
                productLines.add(
                        (ProductLine) line
                );
            }
        }

        return productLines;
    }

    private void releaseReservedLines(
            List<ProductLine> reservedLines
    ) {
        for (
                int index = reservedLines.size() - 1;
                index >= 0;
                index--
        ) {
            reservedLines
                    .get(index)
                    .releaseStock();
        }
    }
}