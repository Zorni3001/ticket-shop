package com.example.ticketshop.pricing;

import static com.example.ticketshop.MoneyAssertions.assertAmount;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class PersonalDiscountPolicyTest {

    private final PersonalDiscountPolicy policy = new PersonalDiscountPolicy();

    @Test
    void regularAdultGetsNoDiscount() {
        var attendee = new Attendee(30, false);

        var discountRate = policy.discountRateFor(attendee);

        assertAmount("0", discountRate);
    }

    @Test
    @Disabled("Exercise 2.1")
    void studentGetsTwentyPercentDiscount() {
        // TODO Exercise 2.1: students get a 20 % discount
    }

    @Test
    @Disabled("Exercise 2.2")
    void seniorDiscountStartsAtAge65() {
        // TODO Exercise 2.2: seniors get 30 % from age 65, test the ages around the boundary with @ParameterizedTest and @CsvSource
    }

    @Test
    @Disabled("Exercise 2.3")
    void seniorStudentGetsOnlyTheHigherDiscount() {
        // TODO Exercise 2.3: a senior student gets only the higher discount
    }
}
