package com.example.ticketshop.pricing;

import static com.example.ticketshop.MoneyAssertions.assertAmount;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class TicketPriceCalculatorTest {

    private static final BigDecimal BASE_PRICE = new BigDecimal("49.90");

    private final TicketPriceCalculator calculator = new TicketPriceCalculator();

    @Test
    void calculatesTotalForSingleAdult() {
        var attendees = List.of(new Attendee(30, false));

        var price = calculator.calculate(BASE_PRICE, attendees);

        assertAmount("51.90", price.total());
    }

    @Test
    @Disabled("Exercise 1.1")
    void chargesServiceFeeOncePerOrder() {
        // TODO Exercise 1.1: several adults, the service fee is added only once
    }

    @Test
    @Disabled("Exercise 1.2")
    void acceptsTwentyAttendees() {
        // TODO Exercise 1.2: an order with twenty attendees is accepted
    }

    @Test
    @Disabled("Exercise 1.3")
    void rejectsMoreThanTwentyAttendees() {
        // TODO Exercise 1.3: an order with more than twenty attendees is rejected, use assertThrows
    }

    @Test
    @Disabled("Exercise 1.4")
    void rejectsOrderWithoutAttendees() {
        // TODO Exercise 1.4: an order without attendees is rejected
    }

    @Test
    @Disabled("Exercise 2.4")
    void roundsDiscountedTicketPriceToCents() {
        // TODO Exercise 2.4: discounted ticket prices are rounded to cents (half up), pick a base price with more than two decimals after the discount
    }
}
