package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.Config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

public class ConfigLoader {

    private static final Logger log = LogManager.getLogger(ConfigLoader.class);

    private static final String ENV_URL = "URL";
    private static final String ENV_INTERVAL_SECONDS = "INTERVAL_SECONDS";
    private static final String ENV_TIMEOUT_SECONDS = "TIMEOUT_SECONDS";
    private static final String ENV_DEVICE_ID = "DEVICE_ID";

    //  This file is only for testing
    private static final String PROPERTIES_FILE = "src/main/resources/config.properties";

    private static final String PROP_URL = "url";
    private static final String PROP_INTERVAL_SECONDS = "interval_seconds";
    private static final String PROP_TIMEOUT_SECONDS = "timeout_seconds";
    private static final String PROP_DEVICE_ID = "device_id";

    public Optional<Config> loadConfigFromEnvironment() {
        String envUrl = System.getenv(ENV_URL);
        if (envUrl == null) {
            return Optional.empty();
        }

        String envIntervalSecondsStr = System.getenv(ENV_INTERVAL_SECONDS);
        if (envIntervalSecondsStr == null) {
            return Optional.empty();
        }
        int envIntervalSeconds;
        try {
            envIntervalSeconds = Integer.parseInt(envIntervalSecondsStr);
        } catch (NumberFormatException e) {
            log.error("Invalid interval environment variable: {}", envIntervalSecondsStr);
            return Optional.empty();
        }

        String envTimeoutSecondsStr = System.getenv(ENV_TIMEOUT_SECONDS);
        if (envTimeoutSecondsStr == null) {
            return Optional.empty();
        }
        int envTimeoutSeconds;
        try {
            envTimeoutSeconds = Integer.parseInt(envTimeoutSecondsStr);
        } catch (NumberFormatException e) {
            log.error("Invalid timeout environment variable: {}", envTimeoutSecondsStr);
            return Optional.empty();
        }

        String envDeviceId = System.getenv(ENV_DEVICE_ID);
        if (envDeviceId == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(new Config(envUrl, envIntervalSeconds, envTimeoutSeconds, envDeviceId));
        } catch (IllegalArgumentException e) {
            log.error("environment variables - validation failed", e);
            return Optional.empty();
        }
    }

    public Optional<Config> loadConfigFromProperties() {
        String url;
        int intervalSeconds;
        int timeoutSeconds;
        String deviceId;

        try (FileInputStream fis = new FileInputStream(PROPERTIES_FILE)) {
            Properties props = new Properties();
            props.load(fis);

            if (props.containsKey(PROP_URL)) {
                url = props.getProperty(PROP_URL);
            } else {
                log.error("Missing url property in config properties file");
                return Optional.empty();
            }

            if (props.containsKey(PROP_INTERVAL_SECONDS)) {
                try {
                    intervalSeconds = Integer.parseInt(props.getProperty(PROP_INTERVAL_SECONDS));
                } catch (NumberFormatException e) {
                    log.error("Invalid interval property in config properties file: {}",
                            props.getProperty(PROP_INTERVAL_SECONDS));
                    return Optional.empty();
                }
            } else {
                log.error("Missing interval property in config properties file");
                return Optional.empty();
            }

            if (props.containsKey(PROP_TIMEOUT_SECONDS)) {
                try {
                    timeoutSeconds = Integer.parseInt(props.getProperty(PROP_TIMEOUT_SECONDS));
                } catch (NumberFormatException e) {
                    log.error("Invalid timeout property in config properties file: {}",
                            props.getProperty(PROP_TIMEOUT_SECONDS));
                    return Optional.empty();
                }
            } else {
                log.error("Missing timeout property in config properties file");
                return Optional.empty();
            }

            if (props.containsKey(PROP_DEVICE_ID)) {
                deviceId = props.getProperty(PROP_DEVICE_ID);
            } else {
                log.error("Missing device_id property in config properties file");
                return Optional.empty();
            }

        } catch (IOException e) {
            return Optional.empty();
        }

        try {
            return Optional.of(new Config(url, intervalSeconds, timeoutSeconds, deviceId));
        } catch (IllegalArgumentException e) {
            log.error("config.properties - validation failed", e);
            return Optional.empty();
        }
    }
}
