package dev.janosch_r.station_network.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void handle_put (HttpExchange exchange) throws IOException {
        try {
            System.out.println("Got request from " + exchange.getRemoteAddress());

            FileWriter fw = new FileWriter("raw_data.xml", true);
            BufferedWriter bw = new BufferedWriter(fw);

            byte[] bytes = exchange.getRequestBody().readAllBytes();
            bw.write(new String(bytes, StandardCharsets.UTF_8));

            bw.newLine();
            bw.newLine();

            bw.close();

            exchange.sendResponseHeaders(200, 0);
            exchange.getResponseBody().close();

        } catch (Exception ex) {
            throw new IOException(ex.getClass().getName() + ": " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(16415), 0);
            server.createContext("/put", Main::handle_put);

            server.setExecutor(null);
            server.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
