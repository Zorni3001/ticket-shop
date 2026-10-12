package com.example.ticketshop.http;

import com.example.ticketshop.event.UnknownEventException;
import com.example.ticketshop.purchase.TicketPurchaseService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

public class TicketHttpHandler implements HttpHandler {

    private final TicketPurchaseService purchaseService;
    private final JsonMapper jsonMapper;

    public TicketHttpHandler(TicketPurchaseService purchaseService, JsonMapper jsonMapper) {
        this.purchaseService = purchaseService;
        this.jsonMapper = jsonMapper;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            sendJson(exchange, 405, new ErrorResponse("Only POST is supported"));
            return;
        }
        try {
            var request = jsonMapper.readValue(exchange.getRequestBody(), PurchaseRequest.class);
            var purchase = purchaseService.purchase(request.eventId(), request.attendees());
            sendJson(exchange, 201, purchase);
        } catch (JacksonException e) {
            sendJson(exchange, 400, new ErrorResponse("Invalid request: " + e.getOriginalMessage()));
        } catch (IllegalArgumentException e) {
            sendJson(exchange, 400, new ErrorResponse(e.getMessage()));
        } catch (UnknownEventException e) {
            sendJson(exchange, 404, new ErrorResponse(e.getMessage()));
        } catch (RuntimeException e) {
            sendJson(exchange, 500, new ErrorResponse("Internal server error"));
        }
    }

    private void sendJson(HttpExchange exchange, int status, Object body) throws IOException {
        var bytes = jsonMapper.writeValueAsBytes(body);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, bytes.length);
        try (var responseBody = exchange.getResponseBody()) {
            responseBody.write(bytes);
        }
    }
}
