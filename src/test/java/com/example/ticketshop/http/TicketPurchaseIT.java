package com.example.ticketshop.http;

import static com.example.ticketshop.MoneyAssertions.assertAmount;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.ticketshop.pricing.Attendee;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
    void purchasesTicketsForKnownEvent() throws Exception {
        var request = new PurchaseRequest("rock-night", List.of(new Attendee(30, false), new Attendee(22, true)));

        var response = postTickets(toJson(request));

        assertEquals(201, response.statusCode());
        assertEquals(Optional.of("application/json"), response.headers().firstValue("Content-Type"));
        var body = JSON_MAPPER.readTree(response.body());
        assertEquals(2, body.at("/ticketCount").intValue());
        assertAmount("91.82", body.at("/price/total").decimalValue());
    }

    @Test
    void returnsNotFoundForUnknownEvent() throws Exception {
        var request = new PurchaseRequest("opera-gala", List.of(new Attendee(30, false)));

        var response = postTickets(toJson(request));

        assertEquals(404, response.statusCode());
    }

    @Test
    void returnsBadRequestForOrderWithoutAttendees() throws Exception {
        var request = new PurchaseRequest("rock-night", List.of());

        var response = postTickets(toJson(request));

        assertEquals(400, response.statusCode());
    }

    @Test
    void returnsBadRequestForMalformedJson() throws Exception {
        var response = postTickets("{ this is not json");

        assertEquals(400, response.statusCode());
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
