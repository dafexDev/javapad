package com.dfortch.javapad.prefs;

import com.dfortch.javapad.i18n.MessageProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class JavapadThemeTest {

    @Test
    void testGetLookAndFeelClassName() {
        assertThat(JavapadTheme.FLATLAF_LIGHT.getLookAndFeelClassName())
                .isEqualTo("com.formdev.flatlaf.FlatLightLaf");
        assertThat(JavapadTheme.FLATLAF_DARK.getLookAndFeelClassName())
                .isEqualTo("com.formdev.flatlaf.FlatDarkLaf");
    }

    @Test
    void testGetThemeName() {
        MessageProvider messageProvider = Mockito.mock(MessageProvider.class);
        when(messageProvider.getMessage("theme.flatlaf-light.name")).thenReturn("FlatLaf Light");
        when(messageProvider.getMessage("theme.flatlaf-dark.name")).thenReturn("FlatLaf Dark");

        assertThat(JavapadTheme.FLATLAF_LIGHT.getThemeName(messageProvider))
                .isEqualTo("FlatLaf Light");
        assertThat(JavapadTheme.FLATLAF_DARK.getThemeName(messageProvider))
                .isEqualTo("FlatLaf Dark");
    }

    @Test
    void testGetPlatform() {
        assertThat(JavapadTheme.FLATLAF_LIGHT.getPlatform())
                .isEqualTo(JavapadTheme.Platform.ALL);
        assertThat(JavapadTheme.WINDOWS_MODERN.getPlatform())
                .isEqualTo(JavapadTheme.Platform.WINDOWS);
    }

    @Test
    void testGetMultiplatformThemes() {
        JavapadTheme[] expectedThemes = {
                JavapadTheme.FLATLAF_LIGHT,
                JavapadTheme.FLATLAF_DARK
        };
        assertThat(JavapadTheme.getMultiplatformThemes())
                .containsExactlyInAnyOrder(expectedThemes);
    }

    @Test
    void testGetNativeThemesWindows() {
        System.setProperty("os.name", "Windows 10");
        JavapadTheme[] expectedThemes = {
                JavapadTheme.WINDOWS_MODERN,
                JavapadTheme.WINDOWS_CLASSIC
        };
        assertThat(JavapadTheme.getNativeThemes())
                .containsExactlyInAnyOrder(expectedThemes);
    }

    @Test
    void testGetNativeThemesMac() {
        System.setProperty("os.name", "Mac OS X");
        JavapadTheme[] expectedThemes = {
                JavapadTheme.MAC_OS
        };
        assertThat(JavapadTheme.getNativeThemes())
                .containsExactlyInAnyOrder(expectedThemes);
    }

    @Test
    void testGetNativeThemesLinux() {
        System.setProperty("os.name", "Linux");
        JavapadTheme[] expectedThemes = {
                JavapadTheme.LINUX_GTK
        };
        assertThat(JavapadTheme.getNativeThemes())
                .containsExactlyInAnyOrder(expectedThemes);
    }

    @Test
    void testGetSupportedThemes() {
        System.setProperty("os.name", "Windows 10");
        JavapadTheme[] expectedThemes = {
                JavapadTheme.FLATLAF_LIGHT,
                JavapadTheme.FLATLAF_DARK,
                JavapadTheme.WINDOWS_MODERN,
                JavapadTheme.WINDOWS_CLASSIC
        };
        assertThat(JavapadTheme.getSupportedThemes())
                .containsExactlyInAnyOrder(expectedThemes);
    }
}
