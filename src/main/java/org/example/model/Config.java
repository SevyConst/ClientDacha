package org.example.model;

public record Config(
        String url,
        int intervalSeconds,
        int timeoutSeconds,
        String deviceId
) {

    private static final int MINIMUM_INTERVAL_SECONDS = 30;
    private static final int MINIMUM_TIMEOUT_SECONDS = 30;

    public Config {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("url must be defined");
        }

        if (intervalSeconds < MINIMUM_INTERVAL_SECONDS) {
            throw new IllegalArgumentException("intervalSeconds must be at least " + MINIMUM_INTERVAL_SECONDS);
        }

        if (timeoutSeconds < MINIMUM_TIMEOUT_SECONDS) {
            throw new IllegalArgumentException("timeoutSeconds must be at least " + MINIMUM_TIMEOUT_SECONDS);
        }

        if (deviceId == null || deviceId.isBlank()) {
            throw new IllegalArgumentException("deviceId must be defined");
        }

    }
}
