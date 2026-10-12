package com.example.ticketshop.event;

public class UnknownEventException extends RuntimeException {

    public UnknownEventException(String eventId) {
        super("Unknown event: " + eventId);
    }
}
