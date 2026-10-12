package com.example.ticketshop.purchase;

import com.example.ticketshop.pricing.PriceBreakdown;

public record Purchase(String purchaseId, String eventId, int ticketCount, PriceBreakdown price) {
}
