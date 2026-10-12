package com.example.ticketshop.http;

import com.example.ticketshop.pricing.Attendee;
import java.util.List;

public record PurchaseRequest(String eventId, List<Attendee> attendees) {

    public PurchaseRequest {
        if (eventId == null || attendees == null) {
            throw new IllegalArgumentException("eventId and attendees are required");
        }
    }
}
