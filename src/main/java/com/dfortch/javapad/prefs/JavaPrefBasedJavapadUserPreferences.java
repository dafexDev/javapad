package com.dfortch.javapad.prefs;


import com.dfortch.javapad.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.prefs.Preferences;

import static com.dfortch.javapad.util.ColorUtils.colorToString;
import static com.dfortch.javapad.util.ColorUtils.stringToColor;

/**
 * Uses {@link java.util.prefs.Preferences} to store preferences
 */
public class JavaPrefBasedJavapadUserPreferences extends DynamicJavapadUserPreferences {

    private static final Logger log = LogManager.getLogger(JavaPrefBasedJavapadUserPreferences.class);

    private static final Preferences preferences = Preferences.userNodeForPackage(Main.class);

    private final List<PreferencesChangeListener> listeners = new ArrayList<>();

    private static final String KEY_LOCALE = "locale";
    private static final String KEY_THEME = "theme";
    private static final String KEY_EDITOR_FONT_FAMILY = "editor_font_family";
    private static final String KEY_EDITOR_FONT_STYLE = "editor_font_style";
    private static final String KEY_EDITOR_FONT_SIZE = "editor_font_size";
    private static final String KEY_EDITOR_FOREGROUND_COLOR = "editor_foreground";
    private static final String KEY_EDITOR_BACKGROUND_COLOR = "editor_background";

    public JavaPrefBasedJavapadUserPreferences(JavapadUserPreferencesProperties properties) {
        super(properties);
        log.info("Initialized JavaPrefBasedJavapadUserPreferences with properties: {}", properties);
    }

    @Override
    public Locale getLocale() {
        log.trace("Retrieving locale");
        String localeString = preferences.get(KEY_LOCALE, getDefaultLocale().toLanguageTag());
        Locale locale = Locale.forLanguageTag(localeString);

        if (Arrays.asList(properties.getAvailableLocales()).contains(locale)) {
            log.debug("Locale found: {}", locale);
        } else {
            log.warn("Locale not supported, reverting to default locale: {}", properties.getDefaultLocale());
            locale = properties.getDefaultLocale();
        }
        log.trace("Locale retrieved: {}", locale);
        return locale;
    }

    @Override
    public void setLocale(Locale locale) {
        log.trace("Setting locale: {}", locale);
        if (Arrays.asList(getAvailableLocales()).contains(locale)) {
            preferences.put(KEY_LOCALE, locale.toLanguageTag());
            log.info("Locale set to: {}", locale);
            notifyPreferencesChanged();
        } else {
            log.error("Unsupported locale: {}", locale);
            throw new IllegalArgumentException("Unsupported locale: " + locale);
        }
    }

    @Override
    public JavapadTheme getTheme() {
        log.trace("Retrieving theme");
        String themeString = preferences.get(KEY_THEME, getDefaultTheme().name());
        JavapadTheme theme = JavapadTheme.valueOf(themeString);
        log.debug("Theme retrieved: {}", theme);
        return theme;
    }

    @Override
    public void setTheme(JavapadTheme theme) {
        log.trace("Setting theme: {}", theme);
        preferences.put(KEY_THEME, theme.name());
        log.info("Theme set to: {}", theme);
        notifyPreferencesChanged();
    }

    @Override
    public Font getEditorFont() {
        log.trace("Retrieving editor font");
        String fontFamily = preferences.get(KEY_EDITOR_FONT_FAMILY, getDefaultEditorFont().getFamily());
        int fontStyle = preferences.getInt(KEY_EDITOR_FONT_STYLE, getDefaultEditorFont().getStyle());
        int fontSize = preferences.getInt(KEY_EDITOR_FONT_SIZE, getDefaultEditorFont().getSize());
        Font font = new Font(fontFamily, fontStyle, fontSize);
        log.debug("Editor font retrieved: {}", font);
        return font;
    }

    @Override
    public void setEditorFont(Font font) {
        log.trace("Setting editor font: {}", font);
        preferences.put(KEY_EDITOR_FONT_FAMILY, font.getFamily());
        preferences.putInt(KEY_EDITOR_FONT_STYLE, font.getStyle());
        preferences.putInt(KEY_EDITOR_FONT_SIZE, font.getSize());
        log.info("Editor font set to: {}", font);
        notifyPreferencesChanged();
    }

    @Override
    public Color getEditorForegroundColor() {
        log.trace("Retrieving editor foreground color");
        Color color = getColorFromPreferences(KEY_EDITOR_FOREGROUND_COLOR, getDefaultEditorForegroundColor());
        log.debug("Editor foreground color retrieved: {}", color);
        return color;
    }

    @Override
    public void setEditorForegroundColor(Color color) {
        log.trace("Setting editor foreground color: {}", color);
        setColorInPreferences(KEY_EDITOR_FOREGROUND_COLOR, color);
        log.info("Editor foreground color set to: {}", color);
        notifyPreferencesChanged();
    }

    @Override
    public Color getEditorBackgroundColor() {
        log.trace("Retrieving editor background color");
        Color color = getColorFromPreferences(KEY_EDITOR_BACKGROUND_COLOR, getDefaultEditorBackgroundColor());
        log.debug("Editor background color retrieved: {}", color);
        return color;
    }

    @Override
    public void setEditorBackgroundColor(Color color) {
        log.trace("Setting editor background color: {}", color);
        setColorInPreferences(KEY_EDITOR_BACKGROUND_COLOR, color);
        log.info("Editor background color set to: {}", color);
        notifyPreferencesChanged();
    }

    @Override
    public void resetLocale() {
        log.trace("Resetting locale to default");
        preferences.put(KEY_LOCALE, getDefaultLocale().toLanguageTag());
        notifyPreferencesChanged();
        log.info("Locale reset to default: {}", getDefaultLocale());
    }

    @Override
    public void resetTheme() {
        log.trace("Resetting theme to default");
        preferences.put(KEY_THEME, getDefaultTheme().name());
        notifyPreferencesChanged();
        log.info("Theme reset to default: {}", getDefaultTheme());
    }

    @Override
    public void resetEditorFont() {
        log.trace("Resetting editor font to default");
        preferences.put(KEY_EDITOR_FONT_FAMILY, getDefaultEditorFont().getFamily());
        preferences.putInt(KEY_EDITOR_FONT_STYLE, getDefaultEditorFont().getStyle());
        preferences.putInt(KEY_EDITOR_FONT_SIZE, getDefaultEditorFont().getSize());
        notifyPreferencesChanged();
        log.info("Editor font reset to default: {}", getDefaultEditorFont());
    }

    @Override
    public void resetEditorForegroundColor() {
        log.trace("Resetting editor foreground color to default");
        preferences.put(KEY_EDITOR_FOREGROUND_COLOR, colorToString(getDefaultEditorForegroundColor()));
        notifyPreferencesChanged();
        log.info("Editor foreground color reset to default: {}", getDefaultEditorForegroundColor());
    }

    @Override
    public void resetEditorBackgroundColor() {
        log.trace("Resetting editor background color to default");
        preferences.put(KEY_EDITOR_BACKGROUND_COLOR, colorToString(getDefaultEditorBackgroundColor()));
        notifyPreferencesChanged();
        log.info("Editor background color reset to default: {}", getDefaultEditorBackgroundColor());
    }

    @Override
    public void resetAll() {
        log.trace("Resetting all preferences to default");
        resetLocale();
        resetTheme();
        resetEditorFont();
        resetEditorForegroundColor();
        resetEditorBackgroundColor();
        notifyPreferencesChanged();
        log.info("All preferences reset to default");
    }

    @Override
    public void addPreferencesChangeListener(PreferencesChangeListener listener) {
        log.trace("Adding preferences change listener: {}", listener);
        listeners.add(listener);
        log.debug("Preferences change listener added: {}", listener);
    }

    @Override
    public void removePreferencesChangeListener(PreferencesChangeListener listener) {
        log.trace("Removing preferences change listener: {}", listener);
        listeners.remove(listener);
        log.debug("Preferences change listener removed: {}", listener);
    }

    private Color getColorFromPreferences(String key, Color defaultColor) {
        log.trace("Getting color from preferences for key: {}", key);
        String colorString = preferences.get(key, colorToString(defaultColor));
        Color color = stringToColor(colorString, defaultColor);
        log.debug("Color retrieved from preferences for key {}: {}", key, color);
        return color;
    }

    private void setColorInPreferences(String key, Color color) {
        log.trace("Setting color in preferences for key: {}", key);
        preferences.put(key, colorToString(color));
        log.debug("Color set in preferences for key {}: {}", key, color);
    }

    private void notifyPreferencesChanged() {
        log.trace("Notifying preferences change listeners");
        for (PreferencesChangeListener listener : listeners) {
            listener.onPreferencesChanged();
        }
        log.debug("Preferences change listeners notified");
    }
}
