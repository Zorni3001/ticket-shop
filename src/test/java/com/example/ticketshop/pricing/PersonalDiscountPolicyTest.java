package com.example.ticketshop.pricing;

import static com.example.ticketshop.MoneyAssertions.assertAmount;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PersonalDiscountPolicyTest {

    private final PersonalDiscountPolicy policy = new PersonalDiscountPolicy();

    @Test
    void regularAdultGetsNoDiscount() {
        var attendee = new Attendee(30, false);

        var discountRate = policy.discountRateFor(attendee);

        assertAmount("0", discountRate);
    }

    @Test
    void studentGetsTwentyPercentDiscount() {
        var attendee = new Attendee(22, true);

        var discountRate = policy.discountRateFor(attendee);

        assertAmount("0.20", discountRate);
    }

    @ParameterizedTest
    @CsvSource({
            "64, 0",
            "65, 0.30",
            "66, 0.30"
    })
    void seniorDiscountStartsAtAge65(int age, String expectedDiscountRate) {
        var attendee = new Attendee(age, false);

        var discountRate = policy.discountRateFor(attendee);

        assertAmount(expectedDiscountRate, discountRate);
    }

    @Test
    void seniorStudentGetsOnlyTheHigherDiscount() {
        var attendee = new Attendee(70, true);

        var discountRate = policy.discountRateFor(attendee);

        assertAmount("0.30", discountRate);
    }
}
