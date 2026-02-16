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
    private final URI uri;
    private final int timeoutSeconds;
    private final Gson gson = new Gson();

    public HttpSender(String url, int timeoutSeconds) {
        this.uri = URI.create(url);
        this.timeoutSeconds = timeoutSeconds;
        httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(timeoutSeconds))
                .build();
    }

    public void send(EventRequest eventRequest) {
        String json = gson.toJson(eventRequest);

        log.info("Sending json: {}", json);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json)).build();

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            log.error(e);
            return;
        }

        if (response.statusCode() != 200) {
            if (response.body() != null) {
                log.error("HTTP response code '{}', json: {}", response.statusCode(), response.body());
            } else {
                log.error("HTTP response code {}", response.statusCode());
            }
            return;
        }

        log.info("response: {}", response.body());
    }
}
