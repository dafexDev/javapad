package com.dfortch.javapad.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RecentFilesManagerImplTests {

    private RecentFilesManager recentFilesManager;

    @TempDir
    private Path tempDir;

    private static final String CACHE_FILE = "recent_files.txt";

    private static final int MAX_RECENT_FILES = 6;

    @BeforeEach
    public void setUp() {
        recentFilesManager = new RecentFilesManagerImpl(tempDir, CACHE_FILE, MAX_RECENT_FILES);
    }

    @DisplayName("Add and retrieve recent file: success")
    @Test
    void testAddAndRetrieveRecentFile_Success() throws IOException {
        File tempFile = tempDir.resolve("test-file.txt").toFile();
        Files.writeString(tempFile.toPath(), "Hello World!");

        recentFilesManager.addRecentFile(tempFile);
        List<File> recentFiles = recentFilesManager.getRecentFiles();

        assertThat(recentFiles).containsExactly(tempFile);
    }

    @DisplayName("Add multiple files and check order")
    @Test
    void testAddMultipleFiles_CheckOrder() throws IOException {
        File tempFile1 = tempDir.resolve("test-file1.txt").toFile();
        File tempFile2 = tempDir.resolve("test-file2.txt").toFile();
        Files.writeString(tempFile1.toPath(), "Content 1");
        Files.writeString(tempFile2.toPath(), "Content 2");

        recentFilesManager.addRecentFile(tempFile1);
        recentFilesManager.addRecentFile(tempFile2);
        List<File> recentFiles = recentFilesManager.getRecentFiles();

        assertThat(recentFiles).containsExactly(tempFile2, tempFile1);
    }

    @DisplayName("Remove recent file: success")
    @Test
    void testRemoveRecentFile_Success() throws IOException {
        File tempFile = tempDir.resolve("test-file.txt").toFile();
        Files.writeString(tempFile.toPath(), "Hello World!");
        recentFilesManager.addRecentFile(tempFile);

        recentFilesManager.removeRecentFile(tempFile);
        List<File> recentFiles = recentFilesManager.getRecentFiles();

        assertThat(recentFiles)
                .isEmpty();
    }

    @DisplayName("Clear recent files: success")
    @Test
    void testClearRecentFiles_Success() throws IOException {
        File tempFile = tempDir.resolve("test-file.txt").toFile();
        Files.writeString(tempFile.toPath(), "Hello World!");
        recentFilesManager.addRecentFile(tempFile);

        recentFilesManager.clearRecentFiles();
        List<File> recentFiles = recentFilesManager.getRecentFiles();

        assertThat(recentFiles).isEmpty();
    }

    @DisplayName("Add null file: throws NullPointerException")
    @Test
    void testAddNullFile_ThrowsNullPointerException() {
        assertThatThrownBy(() -> recentFilesManager.addRecentFile(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("File must not be null");
    }

    @DisplayName("Remove null file: throws NullPointerException")
    @Test
    void testRemoveNullFile_ThrowsNullPointerException() {
        assertThatThrownBy(() -> recentFilesManager.removeRecentFile(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("File must not be null");
    }

    @DisplayName("Add more than max recent files: trims list")
    @Test
    void testAddMoreThanMaxRecentFiles_TrimsList() throws IOException {
        for (int i = 0; i < MAX_RECENT_FILES + 1; i++) {
            File tempFile = tempDir.resolve("testFile" + i + ".txt").toFile();
            Files.writeString(tempFile.toPath(), "Content" + i);
            recentFilesManager.addRecentFile(tempFile);
        }

        List<File> recentFiles = recentFilesManager.getRecentFiles();

        assertThat(recentFiles).hasSize(MAX_RECENT_FILES);
    }
}
