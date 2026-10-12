package com.example.ticketshop.pricing;

import static com.example.ticketshop.MoneyAssertions.assertAmount;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Collections;
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
    void chargesServiceFeeOncePerOrder() {
        var attendees = List.of(new Attendee(30, false), new Attendee(40, false), new Attendee(50, false));

        var price = calculator.calculate(BASE_PRICE, attendees);

        assertAmount("2.00", price.serviceFee());
        assertAmount("151.70", price.total());
    }

    @Test
    void acceptsTwentyAttendees() {
        var attendees = Collections.nCopies(20, new Attendee(30, false));

        var price = calculator.calculate(BASE_PRICE, attendees);

        assertAmount("998.00", price.subtotal());
    }

    @Test
    void rejectsMoreThanTwentyAttendees() {
        var attendees = Collections.nCopies(21, new Attendee(30, false));

        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(BASE_PRICE, attendees));
    }

    @Test
    void rejectsOrderWithoutAttendees() {
        var attendees = List.<Attendee>of();

        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(BASE_PRICE, attendees));
    }

    @Test
    @Disabled("Exercise 2.4")
    void roundsDiscountedTicketPriceToCents() {
        // TODO Exercise 2.4: discounted ticket prices are rounded to cents (half up), pick a base price with more than two decimals after the discount
    }
}
