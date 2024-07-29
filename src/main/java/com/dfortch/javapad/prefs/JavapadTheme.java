package com.dfortch.javapad.prefs;

import com.dfortch.javapad.i18n.MessageProvider;

import java.util.Arrays;
import java.util.stream.Stream;

public enum JavapadTheme {
    FLATLAF_LIGHT("com.formdev.flatlaf.FlatLightLaf", "theme.flatlaf-light.name"),
    FLATLAF_DARK("com.formdev.flatlaf.FlatDarkLaf", "theme.flatlaf-dark.name"),

    WINDOWS_MODERN("com.sun.java.swing.plaf.windows.WindowsLookAndFeel",
            "theme.windows-modern.name", Platform.WINDOWS),
    WINDOWS_CLASSIC("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel",
            "theme.windows-classic.name", Platform.WINDOWS),

    MAC_OS("com.apple.laf.AquaLookAndFeel", "theme.mac-os.name", Platform.MAC),

    LINUX_GTK("com.sun.java.swing.plaf.gtk.GTKLookAndFeel",
            "theme.linux-gtk.name", Platform.LINUX);

    private final String lookAndFeelClassName;

    private final String themeName;

    private final Platform platform;

    JavapadTheme(String lookAndFeelClassName, String themeName) {
        this(lookAndFeelClassName, themeName, Platform.ALL);
    }

    JavapadTheme(String lookAndFeelClassName, String themeName, Platform platform) {
        this.lookAndFeelClassName = lookAndFeelClassName;
        this.themeName = themeName;
        this.platform = platform;
    }

    public String getLookAndFeelClassName() {
        return lookAndFeelClassName;
    }

    public String getThemeName(MessageProvider messageProvider) {
        return messageProvider.getMessage(themeName);
    }

    public Platform getPlatform() {
        return platform;
    }

    public static JavapadTheme[] getMultiplatformThemes() {
        return Arrays.stream(values())
                .filter(theme -> theme.getPlatform() == Platform.ALL)
                .toArray(JavapadTheme[]::new);
    }

    public static JavapadTheme[] getNativeThemes() {
        String osName = System.getProperty("os.name").toLowerCase();
        Platform currentPlatform;

        if (osName.contains("win")) {
            currentPlatform = Platform.WINDOWS;
        } else if (osName.contains("mac")) {
            currentPlatform = Platform.MAC;
        } else if (osName.contains("nux") || osName.contains("nix")) {
            currentPlatform = Platform.LINUX;
        } else {
            currentPlatform = null;
        }

        return Arrays.stream(values())
                .filter(theme -> theme.getPlatform() == currentPlatform)
                .toArray(JavapadTheme[]::new);
    }

    public static JavapadTheme[] getSupportedThemes() {
        return Stream.concat(Arrays.stream(getMultiplatformThemes()), Arrays.stream(getNativeThemes()))
                .toArray(JavapadTheme[]::new);
    }

    public enum Platform {
        ALL,
        WINDOWS,
        MAC,
        LINUX
    }
}
