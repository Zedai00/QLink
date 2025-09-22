package org.qlink;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class App {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String res = "Hello from qlink";
                exchange.sendResponseHeaders(200, res.length());
                OutputStream os = exchange.getResponseBody();
                os.write(res.getBytes());
                os.close();
            }
        });

        server.setExecutor(null);
        server.start();
        System.out.println("Server running on port 8000");
    }
}
