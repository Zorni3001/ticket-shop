package com.example.ticketshop.pricing;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BulkDiscountPolicy {

    private static final int MIN_ATTENDEES = 10;
    private static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.10");

    public BigDecimal discountFor(int attendeeCount, BigDecimal amount) {
        if (attendeeCount < MIN_ATTENDEES) {
            return BigDecimal.ZERO;
        }
        return amount.multiply(DISCOUNT_RATE).setScale(2, RoundingMode.HALF_UP);
    }
}
