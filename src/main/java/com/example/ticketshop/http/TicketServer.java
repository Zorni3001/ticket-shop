package com.example.ticketshop.http;

import com.example.ticketshop.event.InMemoryEventRepository;
import com.example.ticketshop.pricing.TicketPriceCalculator;
import com.example.ticketshop.purchase.InMemoryPurchaseRepository;
import com.example.ticketshop.purchase.TicketPurchaseService;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import tools.jackson.databind.json.JsonMapper;

public class TicketServer {

    private final HttpServer httpServer;

    private TicketServer(HttpServer httpServer) {
        this.httpServer = httpServer;
    }

    public static TicketServer start(int port) throws IOException {
        var purchaseService = new TicketPurchaseService(
                new InMemoryEventRepository(),
                new InMemoryPurchaseRepository(),
                new TicketPriceCalculator());
        var httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        httpServer.createContext("/tickets", new TicketHttpHandler(purchaseService, JsonMapper.builder().build()));
        httpServer.start();
        return new TicketServer(httpServer);
    }

    public int port() {
        return httpServer.getAddress().getPort();
    }

    public void stop() {
        httpServer.stop(0);
    }
}
