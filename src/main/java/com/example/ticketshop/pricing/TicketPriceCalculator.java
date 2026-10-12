package com.example.ticketshop.pricing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class TicketPriceCalculator {

    private static final BigDecimal SERVICE_FEE = new BigDecimal("2.00");
    private static final int MAX_ATTENDEES = 20;

    private final PersonalDiscountPolicy personalDiscountPolicy = new PersonalDiscountPolicy();
    private final BulkDiscountPolicy bulkDiscountPolicy = new BulkDiscountPolicy();

    public PriceBreakdown calculate(BigDecimal basePrice, List<Attendee> attendees) {
        if (attendees.isEmpty() || attendees.size() > MAX_ATTENDEES) {
            throw new IllegalArgumentException("An order must contain between 1 and " + MAX_ATTENDEES + " attendees");
        }
        var subtotal = attendees.stream()
                .map(attendee -> ticketPrice(basePrice, attendee))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        var bulkDiscount = bulkDiscountPolicy.discountFor(attendees.size(), subtotal.add(SERVICE_FEE));
        var total = subtotal.subtract(bulkDiscount).add(SERVICE_FEE);
        return new PriceBreakdown(subtotal, bulkDiscount, SERVICE_FEE, total);
    }

    private BigDecimal ticketPrice(BigDecimal basePrice, Attendee attendee) {
        var discountRate = personalDiscountPolicy.discountRateFor(attendee);
        return basePrice.multiply(BigDecimal.ONE.subtract(discountRate)).setScale(2, RoundingMode.HALF_UP);
    }
}
