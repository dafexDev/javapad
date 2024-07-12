package com.dfortch.javapad.prefs;

import java.awt.*;
import java.util.Locale;

public abstract class DynamicJavapadUserPreferences implements JavaPadUserPreferences {

    protected final JavapadUserPreferencesProperties properties;

    protected DynamicJavapadUserPreferences(JavapadUserPreferencesProperties properties) {
        this.properties = properties;
    }

    @Override
    public Locale getDefaultLocale() {
        return properties.getDefaultLocale();
    }

    @Override
    public Locale[] getAvailableLocales() {
        return properties.getAvailableLocales();
    }

    @Override
    public JavapadTheme getDefaultTheme() {
        return properties.getDefaultTheme();
    }

    @Override
    public Font getDefaultEditorFont() {
        return properties.getDefaultEditorFont();
    }

    @Override
    public Color getDefaultEditorForegroundColor() {
        return properties.getDefaultEditorForegroundColor();
    }

    @Override
    public Color getDefaultEditorBackgroundColor() {
        return properties.getDefaultEditorBackgroundColor();
    }
}
