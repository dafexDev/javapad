package com.dfortch.javapad.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {

    private static final Logger log = LogManager.getLogger(FileUtils.class);

    private FileUtils() {
    }

    public static void createDirectoryIfNotExists(Path path) throws IOException {
        log.trace("Checking if path exists: {}", path);
        try {
            if (Files.notExists(path)) {
                Files.createDirectories(path);
                log.info("Created directory: {}", path);
            }
        } catch (IOException e) {
            log.fatal("Failed to create directory: {}", path, e);
            throw e;
        }
    }
}
