package com.example.ticketshop.pricing;

import java.math.BigDecimal;

public class PersonalDiscountPolicy {

    private static final int SENIOR_MIN_AGE = 65;
    private static final BigDecimal SENIOR_DISCOUNT_RATE = new BigDecimal("0.30");
    private static final BigDecimal STUDENT_DISCOUNT_RATE = new BigDecimal("0.20");

    public BigDecimal discountRateFor(Attendee attendee) {
        var seniorRate = attendee.age() >= SENIOR_MIN_AGE ? SENIOR_DISCOUNT_RATE : BigDecimal.ZERO;
        var studentRate = attendee.student() ? STUDENT_DISCOUNT_RATE : BigDecimal.ZERO;
        return seniorRate.max(studentRate);
    }
}
