package com.example.ticketshop.purchase;

import static com.example.ticketshop.MoneyAssertions.assertAmount;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.example.ticketshop.event.Event;
import com.example.ticketshop.event.EventRepository;
import com.example.ticketshop.event.InMemoryEventRepository;
import com.example.ticketshop.event.UnknownEventException;
import com.example.ticketshop.pricing.Attendee;
import com.example.ticketshop.pricing.TicketPriceCalculator;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TicketPurchaseServiceTest {

    private static final Event SUMMER_FESTIVAL = new Event("summer-festival", "Summer Festival", new BigDecimal("10.00"));

    @Mock
    private EventRepository eventRepository;

    @Mock
    private PurchaseRepository purchaseRepository;

    private TicketPurchaseService purchaseService;

    @BeforeEach
    void createService() {
        purchaseService = new TicketPurchaseService(eventRepository, purchaseRepository, new TicketPriceCalculator());
    }

    @Test
    void calculatesPriceFromBasePriceOfTheEvent() {
        when(eventRepository.findById("summer-festival")).thenReturn(Optional.of(SUMMER_FESTIVAL));

        var purchase = purchaseService.purchase("summer-festival", List.of(new Attendee(30, false)));

        assertAmount("12.00", purchase.price().total());
    }

    @Test
    void storesThePurchase() {
        when(eventRepository.findById("summer-festival")).thenReturn(Optional.of(SUMMER_FESTIVAL));

        var purchase = purchaseService.purchase("summer-festival", List.of(new Attendee(30, false)));

        verify(purchaseRepository).save(purchase);
    }

    @Test
    void rejectsUnknownEvent() {
        when(eventRepository.findById("opera-gala")).thenReturn(Optional.empty());
        var attendees = List.of(new Attendee(30, false));

        assertThrows(UnknownEventException.class, () -> purchaseService.purchase("opera-gala", attendees));
    }

    @Test
    void storesNothingForUnknownEvent() {
        when(eventRepository.findById("opera-gala")).thenReturn(Optional.empty());
        var attendees = List.of(new Attendee(30, false));

        assertThrows(UnknownEventException.class, () -> purchaseService.purchase("opera-gala", attendees));

        verifyNoInteractions(purchaseRepository);
    }

    @Test
    void storedPurchaseCanBeFoundWithInMemoryFakes() {
        var inMemoryPurchaseRepository = new InMemoryPurchaseRepository();
        var serviceWithFakes = new TicketPurchaseService(
                new InMemoryEventRepository(),
                inMemoryPurchaseRepository,
                new TicketPriceCalculator());

        var purchase = serviceWithFakes.purchase("jazz-brunch", List.of(new Attendee(30, false)));

        assertEquals(Optional.of(purchase), inMemoryPurchaseRepository.findById(purchase.purchaseId()));
    }
}
