package org.example;

import java.util.Objects;

public record Config(
        String url,
        int intervalSeconds,
        int timeoutSeconds
) {

    private static final int minIntervalSeconds = 10;
    private static final int minTimeoutSeconds = 30;

    public Config {
        if (url == null) {
            throw new IllegalArgumentException("url cannot be null");
        }
        if (url.isBlank()) {
            throw new IllegalArgumentException("url cannot be blank");
        }
        if (intervalSeconds < minIntervalSeconds) {
            throw new IllegalArgumentException("intervalSeconds must be at least " + minIntervalSeconds);
        }
        if (timeoutSeconds < minTimeoutSeconds) {
            throw new IllegalArgumentException("timeoutSeconds must be at least " +  minTimeoutSeconds);
        }

    }
}
