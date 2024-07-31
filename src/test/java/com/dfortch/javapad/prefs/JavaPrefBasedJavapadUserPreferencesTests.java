package com.dfortch.javapad.prefs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class JavaPrefBasedJavapadUserPreferencesTests {

    private JavapadUserPreferencesProperties properties;
    private JavaPrefBasedJavapadUserPreferences preferences;

    @BeforeEach
    public void setUp() {
        properties = mock(JavapadUserPreferencesProperties.class);
        when(properties.getAvailableLocales()).thenReturn(new Locale[]{Locale.ENGLISH, Locale.FRENCH});
        when(properties.getDefaultLocale()).thenReturn(Locale.ENGLISH);
        when(properties.getDefaultTheme()).thenReturn(JavapadTheme.FLATLAF_DARK);
        when(properties.getDefaultEditorFont()).thenReturn(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        when(properties.getDefaultEditorForegroundColor()).thenReturn(Color.BLACK);
        when(properties.getDefaultEditorBackgroundColor()).thenReturn(Color.WHITE);

        preferences = new JavaPrefBasedJavapadUserPreferences(properties);
    }

    @DisplayName("Get and set locale: success")
    @Test
    void testGetAndSetLocale_Success() {
        Locale newLocale = Locale.FRENCH;

        preferences.setLocale(newLocale);
        Locale retrievedLocale = preferences.getLocale();

        assertThat(retrievedLocale).isEqualTo(newLocale);
    }

    @DisplayName("Set non available locale: throws IllegalArgumentException")
    @Test
    void testSetNonAvailableLocale_ThrowsIllegalArgumentException() {
        Locale nonAvailableLocale = Locale.ITALIAN;

        assertThatThrownBy(() -> preferences.setLocale(nonAvailableLocale))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Get and set theme: success")
    @Test
    void testGetAndSetTheme_Success() {
        JavapadTheme newTheme = JavapadTheme.FLATLAF_LIGHT;

        preferences.setTheme(newTheme);
        JavapadTheme retrievedTheme = preferences.getTheme();

        assertThat(retrievedTheme).isEqualTo(newTheme);
    }

    @DisplayName("Get and set editor font: success")
    @Test
    void testGetAndSetEditorFont_Success() {
        Font newFont = new Font("Serif", Font.BOLD, 14);

        preferences.setEditorFont(newFont);
        Font retrievedFont = preferences.getEditorFont();

        assertThat(retrievedFont).isEqualTo(newFont);
    }

    @DisplayName("Get and set editor foreground color: success")
    @Test
    void testGetAndSetEditorForegroundColor_Success() {
        Color newColor = Color.RED;

        preferences.setEditorForegroundColor(newColor);
        Color retrievedColor = preferences.getEditorForegroundColor();

        assertThat(retrievedColor).isEqualTo(newColor);
    }

    @DisplayName("Get and set editor background color: success")
    @Test
    void testGetAndSetEditorBackgroundColor_Success() {
        Color newColor = Color.GRAY;

        preferences.setEditorBackgroundColor(newColor);
        Color retrievedColor = preferences.getEditorBackgroundColor();

        assertThat(retrievedColor).isEqualTo(newColor);
    }

    @DisplayName("Reset locale: success")
    @Test
    void testResetLocale_Success() {
        preferences.resetLocale();
        Locale retrievedLocale = preferences.getLocale();

        assertThat(retrievedLocale).isEqualTo(properties.getDefaultLocale());
    }

    @DisplayName("Reset theme: success")
    @Test
    void testResetTheme_Success() {
        preferences.resetTheme();
        JavapadTheme retrievedTheme = preferences.getTheme();

        assertThat(retrievedTheme).isEqualTo(properties.getDefaultTheme());
    }

    @DisplayName("Reset editor font: success")
    @Test
    void testResetEditorFont_Success() {
        preferences.resetEditorFont();
        Font retrievedFont = preferences.getEditorFont();

        assertThat(retrievedFont).isEqualTo(properties.getDefaultEditorFont());
    }

    @DisplayName("Reset editor foreground color: success")
    @Test
    void testResetEditorForegroundColor_Success() {
        preferences.resetEditorForegroundColor();
        Color retrievedColor = preferences.getEditorForegroundColor();

        assertThat(retrievedColor).isEqualTo(properties.getDefaultEditorForegroundColor());
    }

    @DisplayName("Reset editor background color: success")
    @Test
    void testResetEditorBackgroundColor_Success() {
        preferences.resetEditorBackgroundColor();
        Color retrievedColor = preferences.getEditorBackgroundColor();

        assertThat(retrievedColor).isEqualTo(properties.getDefaultEditorBackgroundColor());
    }

    @DisplayName("Reset all preferences: success")
    @Test
    void testResetAllPreferences_Success() {
        preferences.resetAll();

        assertThat(preferences.getLocale()).isEqualTo(properties.getDefaultLocale());
        assertThat(preferences.getTheme()).isEqualTo(properties.getDefaultTheme());
        assertThat(preferences.getEditorFont()).isEqualTo(properties.getDefaultEditorFont());
        assertThat(preferences.getEditorForegroundColor()).isEqualTo(properties.getDefaultEditorForegroundColor());
        assertThat(preferences.getEditorBackgroundColor()).isEqualTo(properties.getDefaultEditorBackgroundColor());
    }

    @DisplayName("Add and remove preferences change listener: success")
    @Test
    void testAddAndRemovePreferencesChangeListener_Success() {
        PreferencesChangeListener listener = mock(PreferencesChangeListener.class);
        preferences.addPreferencesChangeListener(listener);

        preferences.setLocale(Locale.FRENCH);
        preferences.removePreferencesChangeListener(listener);
        preferences.setLocale(Locale.ENGLISH);

        verify(listener, times(1)).onPreferencesChanged();
    }
}
