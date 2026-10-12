package com.example.ticketshop.purchase;

import com.example.ticketshop.event.EventRepository;
import com.example.ticketshop.event.UnknownEventException;
import com.example.ticketshop.pricing.Attendee;
import com.example.ticketshop.pricing.TicketPriceCalculator;
import java.util.List;
import java.util.UUID;

public class TicketPurchaseService {

    private final EventRepository eventRepository;
    private final PurchaseRepository purchaseRepository;
    private final TicketPriceCalculator priceCalculator;

    public TicketPurchaseService(EventRepository eventRepository, PurchaseRepository purchaseRepository,
            TicketPriceCalculator priceCalculator) {
        this.eventRepository = eventRepository;
        this.purchaseRepository = purchaseRepository;
        this.priceCalculator = priceCalculator;
    }

    public Purchase purchase(String eventId, List<Attendee> attendees) {
        var event = eventRepository.findById(eventId)
                .orElseThrow(() -> new UnknownEventException(eventId));
        var price = priceCalculator.calculate(event.basePrice(), attendees);
        var purchase = new Purchase(UUID.randomUUID().toString(), event.id(), attendees.size(), price);
        purchaseRepository.save(purchase);
        return purchase;
    }
}
