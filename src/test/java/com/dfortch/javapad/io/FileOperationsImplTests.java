package com.dfortch.javapad.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileOperationsImplTests {

    private FileOperations fileOperations;

    @TempDir
    private Path tempDir;

    @BeforeEach
    public void setUp() {
        fileOperations = new FileOperationsImpl();
    }

    @DisplayName("Read from file: success")
    @Test
    void testReadFromFile_Success() throws IOException {
        File tempFile = tempDir.resolve("test-read.txt").toFile();
        String expectedContent = "Hello World!";
        Files.writeString(tempFile.toPath(), expectedContent);

        String content = fileOperations.readFromFile(tempFile);

        assertThat(content).isEqualTo(expectedContent);
    }

    @DisplayName("Read from non existing file: throws NoSuchFileException")
    @Test
    void testReadFromNonExistingFile_ThrowsNoSuchFileException() {
        File tempFile = tempDir.resolve("non-existing-file.txt").toFile();

        assertThatThrownBy(() -> fileOperations.readFromFile(tempFile))
                .isInstanceOf(NoSuchFileException.class)
                .hasMessageContaining("non-existing-file.txt");
    }

    @DisplayName("Save to file: success")
    @Test
    void testSaveToFile_Success() throws IOException {
        File tempFile = tempDir.resolve("test-write.txt").toFile();
        String content = "Hello World!";

        fileOperations.saveToFile(tempFile, content, true);

        String readContent = Files.readString(tempFile.toPath());
        assertThat(readContent).isEqualTo(content);
    }

    @DisplayName("Save to file with null file: throws NullPointerException")
    @Test
    void testSaveToFileWithNullFile_ThrowsNullPointerException() {
        String content = "Hello World!";

        assertThatThrownBy(() -> fileOperations.saveToFile(null, content, true))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("File must not be null");
    }

    @DisplayName("Save to file with null content: throws NullPointerException")
    @Test
    void testSaveToFileWithNullContent_ThrowsNullPointerException() {
        File tempFile = tempDir.resolve("test-write.txt").toFile();

        assertThatThrownBy(() -> fileOperations.saveToFile(tempFile, null, true))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Content must not be null");
    }
}
