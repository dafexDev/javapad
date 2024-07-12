package com.dfortch.javapad.prefs;

import com.dfortch.javapad.i18n.MessageProvider;

public enum JavapadTheme {
    LIGHT("com.formdev.flatlaf.FlatLightLaf", "theme.light.name"),
    DARK("com.formdev.flatlaf.FlatDarkLaf", "theme.dark.name");

    private final String lookAndFeelClassName;

    private final String themeName;

    JavapadTheme(String lookAndFeelClassName, String themeName) {
        this.lookAndFeelClassName = lookAndFeelClassName;
        this.themeName = themeName;
    }

    public String getLookAndFeelClassName() {
        return lookAndFeelClassName;
    }

    public String getThemeName(MessageProvider messageProvider) {
        return messageProvider.getMessage(themeName);
    }
}
