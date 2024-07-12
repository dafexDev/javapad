package com.dfortch.javapad;

import com.dfortch.javapad.prefs.JavapadTheme;
import com.dfortch.javapad.prefs.JavapadUserPreferencesProperties;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

public class DefaultJavapadUserPreferencesProperties implements JavapadUserPreferencesProperties {

    @Override
    public Locale[] getAvailableLocales() {
        return JavapadConstants.AVAILABLE_LOCALES;
    }

    @Override
    public Locale getDefaultLocale() {
        return JavapadConstants.DEFAULT_LOCALE;
    }

    @Override
    public JavapadTheme getDefaultTheme() {
        return JavapadConstants.DEFAULT_THEME;
    }

    @Override
    public Font getDefaultEditorFont() {
        return UIManager.getFont("TextArea.font");
    }

    @Override
    public Color getDefaultEditorForegroundColor() {
        return null;
    }

    @Override
    public Color getDefaultEditorBackgroundColor() {
        return null;
    }
}
