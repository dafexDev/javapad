package com.dfortch.javapad.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

public class FileOperationsImpl implements FileOperations {

    private static final Logger log = LogManager.getLogger(FileOperationsImpl.class);

    @Override
    public String readFromFile(File file) throws IOException {
        Objects.requireNonNull(file, "File must not be null");
        log.trace("Entering readFromFile method");
        log.info("Attempting to read from file: {}", file.getAbsolutePath());

        try {
            String content = Files.readString(file.toPath());
            log.debug("File read successfully: {}", file.getAbsolutePath());
            log.trace("Exiting readFromFile method");
            return content;
        } catch (IOException e) {
            log.error("Failed to read from file: {}", file.getAbsolutePath(), e);
            log.fatal("Critical error occurred while reading file: {}", file.getAbsolutePath(), e);
            throw e;
        }
    }

    @Override
    public void saveToFile(File file, String content, boolean createFileIfNotExists) throws IOException {
        Objects.requireNonNull(file, "File must not be null");
        Objects.requireNonNull(content, "Content must not be null");
        log.trace("Entering saveToFile method");

        if (createFileIfNotExists && !file.exists()) {
            try {
                if (file.createNewFile()) {
                    log.info("File created successfully: {}", file.getAbsolutePath());
                } else {
                    log.warn("File already exists and was not created: {}", file.getAbsolutePath());
                }
            } catch (IOException e) {
                log.error("Failed to create file: {}", file.getAbsolutePath(), e);
                throw e;
            }
        }

        log.info("Attempting to write to file: {}", file.getAbsolutePath());

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(content.getBytes());
            log.debug("File written successfully: {}", file.getAbsolutePath());
        } catch (IOException e) {
            log.error("Failed to write to file: {}", file.getAbsolutePath(), e);
            log.fatal("Critical error occurred while writing to file: {}", file.getAbsolutePath(), e);
            throw e;
        } finally {
            log.trace("Exiting saveToFile method");
        }
    }
}
