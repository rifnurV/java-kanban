package managers.http.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.http.DurationTypeAdapter;
import managers.http.LocalDateTimeTypeAdapter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class BaseHttpHandler implements HttpHandler {

    protected final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .create();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response;

        // извлеките метод из запроса
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                getRequest(exchange);
                break;
            case "POST":
                postRequest(exchange);
                break;
            case "DELETE":
                deleteRequest(exchange);
                break;
            default:
                sendNotFound(exchange);
                break;
        }

        sendNotFound(exchange);
    }

    public void getRequest(HttpExchange exchange) throws IOException {
        sendHasInteractions(exchange);
    }

    public void postRequest(HttpExchange exchange) throws IOException {
        sendHasInteractions(exchange);
    }

    public void deleteRequest(HttpExchange exchange) throws IOException {
        sendHasInteractions(exchange);
    }

    protected void sendSuccess(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, 0);
        exchange.close();
    }

    protected void sendCreated(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(201, 0);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(404, 0);
        exchange.close();
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(406, 0);
        exchange.close();
    }

    protected void sendInternalError(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(500, 0);
        exchange.close();
    }

    public void sendStatus(HttpExchange httpExchange, int status) throws IOException {
        httpExchange.sendResponseHeaders(status, 0);
        httpExchange.close();
    }

    public <T> void sendJson(HttpExchange httpExchange, T obj) throws IOException {
        Gson gson = getGson();
        String json = gson.toJson(obj);
        byte[] resp = json.getBytes(StandardCharsets.UTF_8);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        httpExchange.sendResponseHeaders(200, resp.length);
        httpExchange.getResponseBody().write(resp);
        httpExchange.close();
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalDateTimeTypeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationTypeAdapter());
        Gson gson = gsonBuilder.create();
        return gson;
    }
}