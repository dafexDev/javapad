package com.dfortch.javapad;

import com.dfortch.javapad.i18n.MessageProvider;
import com.dfortch.javapad.i18n.ResourceBundleMessageProvider;
import com.dfortch.javapad.prefs.JavaPadUserPreferences;
import com.dfortch.javapad.io.FileOperations;
import com.dfortch.javapad.io.FileOperationsImpl;
import com.dfortch.javapad.io.RecentFilesManager;
import com.dfortch.javapad.io.RecentFilesManagerImpl;
import com.dfortch.javapad.prefs.JavaPrefBasedJavapadUserPreferences;
import com.dfortch.javapad.ui.MainFrame;
import com.dfortch.javapad.util.UIUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

import java.io.IOException;
import java.util.Locale;

import static com.dfortch.javapad.util.FileUtils.createDirectoryIfNotExists;

public class Main {
    private static final Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        log.trace("Starting main method");

        createDirectoryIfNotExists(JavapadConstants.USER_DATA_PATH);
        createDirectoryIfNotExists(JavapadConstants.CACHE_PATH);

        DependencyContainer.register(JavaPadUserPreferences.class, new JavaPrefBasedJavapadUserPreferences(new DefaultJavapadUserPreferencesProperties()));
        DependencyContainer.register(FileOperations.class, new FileOperationsImpl());
        DependencyContainer.register(RecentFilesManager.class, new RecentFilesManagerImpl(JavapadConstants.CACHE_PATH, JavapadConstants.RECENT_FILES_CACHE_FILE, JavapadConstants.MAX_RECENT_FILES));

        JavaPadUserPreferences preferences = DependencyContainer.get(JavaPadUserPreferences.class);
        log.info("Retrieved user preferences");

        Locale.setDefault(preferences.getLocale());

        DependencyContainer.register(MessageProvider.class, new ResourceBundleMessageProvider(JavapadConstants.MESSAGE_RESOURCE_BUNDLE_BASE_NAME, preferences.getLocale()));

        MessageProvider messageProvider = DependencyContainer.get(MessageProvider.class);
        FileOperations fileOperations = DependencyContainer.get(FileOperations.class);
        RecentFilesManager recentFilesManager = DependencyContainer.get(RecentFilesManager.class);

        try {
            UIUtils.configureLookAndFeel(preferences.getTheme().getLookAndFeelClassName());
            log.info("Look and Feel set to {}", preferences.getTheme().getLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException e) {
            log.error("Failed to set Look and Feel from theme {}", preferences.getTheme(), e);
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame mainUI = new MainFrame(messageProvider, fileOperations, recentFilesManager, preferences);

            mainUI.setVisible(true);
        });
    }

}