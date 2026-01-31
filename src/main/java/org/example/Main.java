package org.example;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class Main {

    private static final Logger log = LogManager.getLogger(Main.class);


    static void main() {
        log.info("Starting ClientDacha");

        Optional<Config> config = loadConfig();
        if (config.isEmpty()) {
            log.error("can't load config");
            return;
        }
        log.info("Loaded configuration: {}", config);

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
