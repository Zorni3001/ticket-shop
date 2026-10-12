package com.example.ticketshop.event;

import java.math.BigDecimal;

public record Event(String id, String name, BigDecimal basePrice) {
}
