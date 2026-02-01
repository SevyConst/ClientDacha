package org.example;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.Config;
import org.example.model.Event;
import org.example.model.EventRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Optional;

public class Main {

    private static final Logger log = LogManager.getLogger(Main.class);

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static void main() {
        log.info("Starting ClientDacha");

        Optional<Config> config = loadConfig();
        if (config.isEmpty()) {
            log.error("can't load config");
            return;
        }
        log.info("Loaded configuration: {}", config);

        Event event = new Event(1, "start", LocalDateTime.now().format(dateTimeFormatter));
        EventRequest eventRequest = new EventRequest(Collections.singletonList(event), config.get().deviceId());

        HttpSender httpSender = new HttpSender(config.get().url(), config.get().timeoutSeconds());
        httpSender.send(eventRequest);
    }

    private static Optional<Config> loadConfig() {
        ConfigLoader configLoader = new ConfigLoader();

        Optional<Config> config = configLoader.loadConfigFromEnvironment();
        if (config.isEmpty()) {
            log.warn("Can't load config from environment - trying load config from config.properties");
            config = configLoader.loadConfigFromProperties();
        }
        return config;
    }
}
