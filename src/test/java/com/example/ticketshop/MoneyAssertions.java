package com.example.ticketshop;

import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;

public final class MoneyAssertions {

    private MoneyAssertions() {
    }

    public static void assertAmount(String expected, BigDecimal actual) {
        if (new BigDecimal(expected).compareTo(actual) != 0) {
            fail("expected " + expected + " but was " + actual);
        }
    }
}
