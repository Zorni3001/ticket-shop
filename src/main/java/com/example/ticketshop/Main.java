package com.example.ticketshop;

import com.example.ticketshop.http.TicketServer;
import java.io.IOException;

public class Main {

    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        var server = TicketServer.start(PORT);
        System.out.println("Ticket shop is running on http://localhost:" + server.port() + "/tickets");
    }
}
