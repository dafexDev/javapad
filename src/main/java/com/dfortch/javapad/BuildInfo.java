package com.dfortch.javapad;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BuildInfo {

    private static final Logger log = LogManager.getLogger(BuildInfo.class);

    private static final String BUILD_VERSION;
    private static final String BUILD_DATE;
    private static final String BUILD_URL;

    static {
        Properties properties = new Properties();
        try (InputStream input = BuildInfo.class.getResourceAsStream("build.properties")) {
            if (input == null) {
                log.error("Could not find build.properties");
                throw new IllegalStateException("Could not find build.properties");
            }
            log.debug("Loading build.properties");
            properties.load(input);
        } catch (IOException e) {
            log.error("Failed to load build.properties", e);
            throw new RuntimeException("Failed to load build.properties", e);
        }

        BUILD_VERSION = properties.getProperty("build.version");
        log.info("Build version retrieved: {}", BUILD_VERSION);

        BUILD_URL = properties.getProperty("build.url");
        log.info("Build URL retrieved: {}", BUILD_URL);

        BUILD_DATE = properties.getProperty("build.date");
        log.info("Build date retrieved: {}", BUILD_DATE);
    }

    private BuildInfo() {
    }

    public static String getBuildVersion() {
        log.trace("getBuildVersion() called");
        return BUILD_VERSION;
    }

    public static String getBuildUrl() {
        log.trace("getBuildUrl() called");
        return BUILD_URL;
    }

    public static String getBuildDate() {
        log.trace("getBuildDate() called");
        return BUILD_DATE;
    }
}
