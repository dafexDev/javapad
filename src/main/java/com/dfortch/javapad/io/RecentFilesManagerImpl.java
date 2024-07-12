package com.dfortch.javapad.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RecentFilesManagerImpl implements RecentFilesManager {

    private static final Logger log = LogManager.getLogger(RecentFilesManagerImpl.class);
    private final Path cachePath;

    private final String recentFilesCache;

    private final int maxRecentFiles;

    public RecentFilesManagerImpl(Path cachePath, String recentFilesCache, int maxRecentFiles) {
        this.cachePath = cachePath;
        this.recentFilesCache = recentFilesCache;
        this.maxRecentFiles = maxRecentFiles;
    }

    @Override
    public void addRecentFile(File file) throws IOException {
        log.trace("Entering addRecentFile method");
        Objects.requireNonNull(file, "File must not be null");
        List<File> recentFiles = getRecentFiles();
        recentFiles.remove(file);
        recentFiles.addFirst(file);

        if (recentFiles.size() > maxRecentFiles) {
            recentFiles = recentFiles.subList(0, maxRecentFiles);
        }

        saveRecentFiles(recentFiles);
        log.info("Added recent file: {}", file.getAbsolutePath());
        log.trace("Exiting addRecentFile method");
    }

    @Override
    public List<File> getRecentFiles() throws IOException {
        log.trace("Entering getRecentFiles method");
        List<File> recentFiles;

        if (!Files.exists(getRecentFilesCachePath())) {
            Files.createFile(getRecentFilesCachePath());
        }

        try (Stream<String> linesStream = Files.lines(getRecentFilesCachePath())) {
            recentFiles = linesStream
                    .map(Path::of)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
            log.debug("Retrieved recent files: {}", recentFiles);
        } catch (IOException e) {
            log.error("Failed to read recent files cache", e);
            throw e;
        }
        log.trace("Exiting getRecentFiles method");
        return recentFiles;
    }

    @Override
    public void removeRecentFile(File file) throws IOException {
        log.trace("Entering removeRecentFile method");
        Objects.requireNonNull(file, "File must not be null");
        List<File> recentFiles = getRecentFiles();
        recentFiles.remove(file);
        saveRecentFiles(recentFiles);
        log.info("Removed recent file: {}", file.getAbsolutePath());
        log.trace("Exiting removeRecentFile method");
    }

    @Override
    public void clearRecentFiles() throws IOException {
        log.trace("Entering clearRecentFiles method");
        try {
            Files.deleteIfExists(getRecentFilesCachePath());
            log.info("Cleared recent files cache");
        } catch (IOException e) {
            log.error("Failed to clear recent files cache", e);
            throw e;
        }
        log.trace("Exiting clearRecentFiles method");
    }

    private void saveRecentFiles(List<File> recentFiles) throws IOException {
        log.trace("Entering saveRecentFiles method");
        try {
            Files.write(getRecentFilesCachePath(), recentFiles.stream()
                    .map(File::getAbsolutePath)
                    .collect(Collectors.toList()));
            log.debug("Saved recent files cache: {}", recentFiles);
        } catch (IOException e) {
            log.error("Failed to save recent files cache", e);
            throw e;
        }
        log.trace("Exiting saveRecentFiles method");
    }

    private Path getRecentFilesCachePath() {
        return cachePath.resolve(recentFilesCache);
    }
}
