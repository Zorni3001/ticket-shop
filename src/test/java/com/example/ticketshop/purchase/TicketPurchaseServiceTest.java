package com.example.ticketshop.purchase;

import static com.example.ticketshop.MoneyAssertions.assertAmount;
import static org.mockito.Mockito.when;

import com.example.ticketshop.event.Event;
import com.example.ticketshop.event.EventRepository;
import com.example.ticketshop.pricing.Attendee;
import com.example.ticketshop.pricing.TicketPriceCalculator;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
    @Disabled("Exercise 6.1")
    void storesThePurchase() {
        // TODO Exercise 6.1: verify that the purchase is saved
    }

    @Test
    @Disabled("Exercise 6.2")
    void rejectsUnknownEvent() {
        // TODO Exercise 6.2: stub findById with Optional.empty() and expect an UnknownEventException
    }

    @Test
    @Disabled("Exercise 6.3")
    void storesNothingForUnknownEvent() {
        // TODO Exercise 6.3: nothing is saved for an unknown event, use verifyNoInteractions
    }

    @Test
    @Disabled("Exercise 6.4")
    void storedPurchaseCanBeFoundWithInMemoryFakes() {
        // TODO Exercise 6.4: use InMemoryEventRepository and InMemoryPurchaseRepository instead of mocks
    }
}
