package org.qlink;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class App {
    private static Map<String, String> queryToMap(String query) {
        if (query == null)
            return Collections.emptyMap();
        return Arrays.stream(query.split("&"))
                .map(param -> param.split("=", 2))
                .collect(Collectors.toMap(
                        arr -> arr[0],
                        arr -> arr.length > 1 ? arr[1] : ""));
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        TokenManager tokenManager = new TokenManager();

        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String path = exchange.getRequestURI().getPath();
                // System.out.println(params.toString());

                String res;
                switch (path) {
                    case "/":
                        res = tokenManager.generateToken();
                        break;
                    case "/remove":
                        String query = exchange.getRequestURI().getQuery();
                        Map<String, String> params = queryToMap(query);
                        String token = params.getOrDefault("token", null);
                        if (token == null) {
                            res = "404 Token Not Found";
                        } else {
                            if (tokenManager.contains(token)) {
                                tokenManager.removeToken(token);
                                res = "Token " + token + " Removed Successfully";
                            } else {
                                res = "404 Token Not Found";
                            }
                        }
                        break;
                    default:
                        res = "404 Not Found: " + path;
                        break;
                }
                exchange.sendResponseHeaders(200, res.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(res.getBytes());
                }
            }
        });

        server.setExecutor(null);
        server.start();
        System.out.println("Server running on port 8000");
    }
}
