package com.example.ticketshop.pricing;

public record Attendee(int age, boolean student) {

    public Attendee {
        if (age < 0) {
            throw new IllegalArgumentException("Age must not be negative but was " + age);
        }
    }
}
