package com.dfortch.javapad.prefs;

import java.awt.*;
import java.util.Locale;

public interface JavaPadUserPreferences {

    Locale getLocale();

    Locale[] getAvailableLocales();

    void setLocale(Locale locale);

    JavapadTheme getTheme();

    void setTheme(JavapadTheme theme);

    Font getEditorFont();

    void setEditorFont(Font font);

    Color getEditorForegroundColor();

    void setEditorForegroundColor(Color color);

    Color getEditorBackgroundColor();

    void setEditorBackgroundColor(Color color);

    Locale getDefaultLocale();

    void resetLocale();

    JavapadTheme getDefaultTheme();

    void resetTheme();

    Font getDefaultEditorFont();

    void resetEditorFont();

    Color getDefaultEditorForegroundColor();

    void resetEditorForegroundColor();

    Color getDefaultEditorBackgroundColor();

    void resetEditorBackgroundColor();

    void resetAll();

    void addPreferencesChangeListener(PreferencesChangeListener listener);

    void removePreferencesChangeListener(PreferencesChangeListener listener);
}
