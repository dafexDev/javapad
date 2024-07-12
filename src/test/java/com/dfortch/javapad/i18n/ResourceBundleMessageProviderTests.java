package com.dfortch.javapad.i18n;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

class ResourceBundleMessageProviderTests {

    private ResourceBundleMessageProvider messageProviderEn;
    private ResourceBundleMessageProvider messageProviderEs;

    @BeforeEach
    public void setUp() {
        messageProviderEn = new ResourceBundleMessageProvider("com.dfortch.javapad.messages-test", Locale.ENGLISH);
        messageProviderEs = new ResourceBundleMessageProvider("com.dfortch.javapad.messages-test", Locale.forLanguageTag("es"));
    }

    @DisplayName("Get Message: success (English)")
    @Test
    void testGetMessage_Success_English() {
        String key = "hello-world";
        String expectedMessage = "Hello World!";

        String message = messageProviderEn.getMessage(key);

        assertThat(expectedMessage).isEqualTo(message);
    }

    @DisplayName("Get Message: success (Spanish)")
    @Test
    void testGetMessage_Success_Spanish() {
        String key = "hello-world";
        String expectedMessage = "¡Hola Mundo!";

        String message = messageProviderEs.getMessage(key);

        assertThat(expectedMessage).isEqualTo(message);
    }

    @DisplayName("Get Message with params: success (English)")
    @Test
    void testGetMessageWithParams_Success_English() {
        String key = "hello-world-param";
        String expectedMessage = "Hello John!";

        String message = messageProviderEn.getMessage(key, "John");

        assertThat(expectedMessage).isEqualTo(message);
    }

    @DisplayName("Get Message with params: success (Spanish)")
    @Test
    void testGetMessageWithParams_Success_Spanish() {
        String key = "hello-world-param";
        String expectedMessage = "¡Hola John!";

        String message = messageProviderEs.getMessage(key, "John");

        assertThat(expectedMessage).isEqualTo(message);
    }

    @DisplayName("Get Message: key not found")
    @Test
    void testGetMessage_KeyNotFound() {
        String key = "non-existing-key";
        String expectedMessage = "Key not found: " + key;

        String message = messageProviderEn.getMessage(key);

        assertThat(expectedMessage).isEqualTo(message);
    }

    @DisplayName("Get Message with params: key not found")
    @Test
    void testGetMessageWithParams_KeyNotFound() {
        String key = "non-existing-key";
        String expectedMessage = "Key not found: " + key;

        String message = messageProviderEn.getMessage(key, "John");

        assertThat(expectedMessage).isEqualTo(message);
    }
}
