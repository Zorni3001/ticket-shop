package com.example.ticketshop.event;

import java.util.Optional;

public interface EventRepository {

    Optional<Event> findById(String id);
}
