package com.dfortch.javapad.prefs;

import java.awt.*;
import java.util.Locale;

public interface JavapadUserPreferencesProperties {

    Locale[] getAvailableLocales();

    Locale getDefaultLocale();

    JavapadTheme getDefaultTheme();

    Font getDefaultEditorFont();

    Color getDefaultEditorForegroundColor();

    Color getDefaultEditorBackgroundColor();
}
