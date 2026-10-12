package com.example.ticketshop.event;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

public class InMemoryEventRepository implements EventRepository {

    private final Map<String, Event> events = Map.of(
            "rock-night", new Event("rock-night", "Rock Night", new BigDecimal("49.90")),
            "jazz-brunch", new Event("jazz-brunch", "Jazz Brunch", new BigDecimal("24.50")),
            "comedy-evening", new Event("comedy-evening", "Comedy Evening", new BigDecimal("19.99")));

    @Override
    public Optional<Event> findById(String id) {
        return Optional.ofNullable(events.get(id));
    }
}
