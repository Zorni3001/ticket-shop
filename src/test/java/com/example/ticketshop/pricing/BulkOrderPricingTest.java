package com.example.ticketshop.pricing;

import static com.example.ticketshop.MoneyAssertions.assertAmount;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class BulkOrderPricingTest {

    private static final BigDecimal BASE_PRICE = new BigDecimal("49.90");

    private final TicketPriceCalculator calculator = new TicketPriceCalculator();

    @Test
    void nineAttendeesGetNoBulkDiscount() {
        var attendees = adults(9);

        var price = calculator.calculate(BASE_PRICE, attendees);

        assertAmount("0.00", price.bulkDiscount());
        assertAmount("451.10", price.total());
    }

    @Test
    void tenAttendeesGetTenPercentBulkDiscount() {
        var attendees = adults(10);

        var price = calculator.calculate(BASE_PRICE, attendees);

        assertAmount("499.00", price.subtotal());
        assertAmount("49.90", price.bulkDiscount());
        assertAmount("451.10", price.total());
    }

    @Test
    void serviceFeeIsNotDiscounted() {
        var attendees = adults(12);

        var price = calculator.calculate(BASE_PRICE, attendees);

        assertAmount("59.88", price.bulkDiscount());
        assertAmount("2.00", price.serviceFee());
        assertAmount("540.92", price.total());
    }

    @Test
    void bulkDiscountAppliesOnTopOfPersonalDiscounts() {
        var attendees = Stream.of(adults(6), students(2), seniors(2))
                .flatMap(List::stream)
                .toList();

        var price = calculator.calculate(BASE_PRICE, attendees);

        assertAmount("449.10", price.subtotal());
        assertAmount("44.91", price.bulkDiscount());
        assertAmount("406.19", price.total());
    }

    private static List<Attendee> adults(int count) {
        return Collections.nCopies(count, new Attendee(30, false));
    }

    private static List<Attendee> students(int count) {
        return Collections.nCopies(count, new Attendee(22, true));
    }

    private static List<Attendee> seniors(int count) {
        return Collections.nCopies(count, new Attendee(70, false));
    }
}
