package com.example.ticketshop.pricing;

import static com.example.ticketshop.MoneyAssertions.assertAmount;

import java.math.BigDecimal;
import java.util.Collections;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class BulkOrderPricingTest {

    private static final BigDecimal BASE_PRICE = new BigDecimal("49.90");

    private final TicketPriceCalculator calculator = new TicketPriceCalculator();

    @Test
    void nineAttendeesGetNoBulkDiscount() {
        var attendees = Collections.nCopies(9, new Attendee(30, false));

        var price = calculator.calculate(BASE_PRICE, attendees);

        assertAmount("0.00", price.bulkDiscount());
        assertAmount("451.10", price.total());
    }

    @Test
    @Disabled("Exercise 3.1")
    void tenAttendeesGetTenPercentBulkDiscount() {
        // TODO Exercise 3.1: ten attendees get 10 % off the subtotal
    }

    @Test
    @Disabled("Exercise 3.2")
    void serviceFeeIsNotDiscounted() {
        // TODO Exercise 3.2: the service fee is not part of the bulk discount
    }

    @Test
    @Disabled("Exercise 3.3")
    void bulkDiscountAppliesOnTopOfPersonalDiscounts() {
        // TODO Exercise 3.3: the bulk discount applies on top of personal discounts, write helpers such as adults(n) and students(n)
    }
}
