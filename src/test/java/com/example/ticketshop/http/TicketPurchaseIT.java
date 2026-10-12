package com.example.ticketshop.http;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.ticketshop.pricing.Attendee;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

class TicketPurchaseIT {

    private static final JsonMapper JSON_MAPPER = JsonMapper.builder().build();

    private static TicketServer server;
    private static HttpClient httpClient;

    @BeforeAll
    static void startServer() throws IOException {
        server = TicketServer.start(0);
        httpClient = HttpClient.newHttpClient();
    }

    @AfterAll
    static void stopServer() {
        httpClient.close();
        server.stop();
    }

    @Test
    void rejectsOrderWithMoreThanTwentyAttendees() throws Exception {
        var request = new PurchaseRequest("rock-night", Collections.nCopies(21, new Attendee(30, false)));

        var response = postTickets(toJson(request));

        assertEquals(400, response.statusCode());
    }

    @Test
    @Disabled("Exercise 5.1")
    void purchasesTicketsForKnownEvent() {
        // TODO Exercise 5.1: a purchase for a known event returns 201, application/json and the correct total
    }

    @Test
    @Disabled("Exercise 5.2")
    void returnsNotFoundForUnknownEvent() {
        // TODO Exercise 5.2: an unknown event returns 404
    }

    @Test
    @Disabled("Exercise 5.3")
    void returnsBadRequestForOrderWithoutAttendees() {
        // TODO Exercise 5.3: an order without attendees returns 400
    }

    @Test
    @Disabled("Exercise 5.4")
    void returnsBadRequestForMalformedJson() {
        // TODO Exercise 5.4: malformed JSON returns 400
    }

    private static HttpResponse<String> postTickets(String body) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder(URI.create("http://localhost:" + server.port() + "/tickets"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static String toJson(Object value) {
        return JSON_MAPPER.writeValueAsString(value);
    }
}
