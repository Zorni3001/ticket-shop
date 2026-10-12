package com.example.ticketshop.pricing;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class CoverageIsNotQualityTest {

    private static final BigDecimal BASE_PRICE = new BigDecimal("49.90");

    private final TicketPriceCalculator calculator = new TicketPriceCalculator();

    @Test
    void calculatesPricesForAllKindsOfOrders() {
        calculator.calculate(BASE_PRICE, List.of(new Attendee(70, false), new Attendee(22, true), new Attendee(30, false)));
        calculator.calculate(BASE_PRICE, Collections.nCopies(12, new Attendee(30, false)));

        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate(BASE_PRICE, Collections.nCopies(21, new Attendee(30, false))));
    }
}
