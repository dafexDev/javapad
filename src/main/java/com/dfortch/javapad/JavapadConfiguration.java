package com.dfortch.javapad;

import com.dfortch.javapad.i18n.MessageProvider;
import com.dfortch.javapad.i18n.ResourceBundleMessageProvider;
import com.dfortch.javapad.io.FileOperations;
import com.dfortch.javapad.io.FileOperationsImpl;
import com.dfortch.javapad.io.RecentFilesManager;
import com.dfortch.javapad.io.RecentFilesManagerImpl;
import com.dfortch.javapad.prefs.JavaPadUserPreferences;
import com.dfortch.javapad.prefs.JavaPrefBasedJavapadUserPreferences;
import com.dfortch.javapad.prefs.JavapadTheme;
import com.dfortch.javapad.prefs.JavapadUserPreferencesProperties;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

@ApplicationScoped
public class JavapadConfiguration {

    @Produces
    public JavapadUserPreferencesProperties javapadUserPreferencesProperties() {
        return new JavapadUserPreferencesProperties() {
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
        };
    }

    @Produces
    public JavaPadUserPreferences javaPadUserPreferences(JavapadUserPreferencesProperties properties) {
        return new JavaPrefBasedJavapadUserPreferences(properties);
    }

    @Produces
    public MessageProvider messageProvider(JavaPadUserPreferences preferences) {
        return new ResourceBundleMessageProvider(JavapadConstants.MESSAGE_RESOURCE_BUNDLE_BASE_NAME, preferences.getLocale());
    }

    @Produces
    public FileOperations fileOperations() {
        return new FileOperationsImpl();
    }

    @Produces
    public RecentFilesManager recentFilesManager() {
        return new RecentFilesManagerImpl(JavapadConstants.CACHE_PATH, JavapadConstants.RECENT_FILES_CACHE_FILE,
                JavapadConstants.MAX_RECENT_FILES);
    }
}
