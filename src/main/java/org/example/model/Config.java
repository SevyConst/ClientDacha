package org.example.model;

public record Config(
        String url,
        int intervalSeconds,
        int timeoutSeconds,
        String deviceId
) {

    private static final int minIntervalSeconds = 10;
    private static final int minTimeoutSeconds = 30;

    public Config {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("url must be defined");
        }

        if (intervalSeconds < minIntervalSeconds) {
            throw new IllegalArgumentException("intervalSeconds must be at least " + minIntervalSeconds);
        }

        if (timeoutSeconds < minTimeoutSeconds) {
            throw new IllegalArgumentException("timeoutSeconds must be at least " +  minTimeoutSeconds);
        }

        if (deviceId == null || deviceId.isBlank()) {
            throw new IllegalArgumentException("url must be defined");
        }

    }
}
