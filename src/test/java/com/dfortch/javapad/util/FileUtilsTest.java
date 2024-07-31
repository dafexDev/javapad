package com.dfortch.javapad.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mockStatic;

class FileUtilsTest {

    private static final Logger log = LogManager.getLogger(FileUtilsTest.class);

    private MockedStatic<Files> filesMock;

    @BeforeEach
    void setUp() {
        filesMock = mockStatic(Files.class);
    }

    @AfterEach
    void tearDown() {
        filesMock.close();
    }

    @Test
    void createDirectoryIfNotExists_DirectoryDoesNotExist_ShouldCreateDirectory() throws IOException {
        Path path = Path.of("some/directory");

        filesMock.when(() -> Files.notExists(path)).thenReturn(true);
        filesMock.when(() -> Files.createDirectories(path)).thenReturn(path);

        FileUtils.createDirectoryIfNotExists(path);

        filesMock.verify(() -> Files.notExists(path));
        filesMock.verify(() -> Files.createDirectories(path));
        log.info("Test passed: Directory was created");
    }

    @Test
    void createDirectoryIfNotExists_DirectoryExists_ShouldNotCreateDirectory() throws IOException {
        Path path = Path.of("some/directory");

        filesMock.when(() -> Files.notExists(path)).thenReturn(false);

        FileUtils.createDirectoryIfNotExists(path);

        filesMock.verify(() -> Files.notExists(path));
        filesMock.verifyNoMoreInteractions();
        log.info("Test passed: Directory already exists, not created again");
    }

    @Test
    void createDirectoryIfNotExists_IOExceptionThrown_ShouldLogFatalAndThrow() {
        Path path = Path.of("some/directory");

        filesMock.when(() -> Files.notExists(path)).thenReturn(true);
        filesMock.when(() -> Files.createDirectories(path)).thenThrow(IOException.class);

        assertThatThrownBy(() -> FileUtils.createDirectoryIfNotExists(path))
                .isInstanceOf(IOException.class);

        filesMock.verify(() -> Files.notExists(path));
        filesMock.verify(() -> Files.createDirectories(path));
        log.info("Test passed: IOException thrown and logged");
    }
}
