package org.example;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.EventRequest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HttpSender {
    private static final Logger log = LogManager.getLogger(HttpSender.class);
    private final HttpClient httpClient;
    private final String url;
    private final int timeoutSeconds;
    private final Gson gson = new Gson();

    public HttpSender(String url, int timeoutSeconds) {
        this.url = url;
        this.timeoutSeconds = timeoutSeconds;
        httpClient = HttpClient.newHttpClient();

    }

    public void send(EventRequest eventRequest) {
        String json = gson.toJson(eventRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json)).build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            log.error(e);
        }
    }
}
