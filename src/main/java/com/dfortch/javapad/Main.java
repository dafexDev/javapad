package com.dfortch.javapad;

import com.dfortch.javapad.i18n.MessageProvider;
import com.dfortch.javapad.prefs.JavaPadUserPreferences;
import com.dfortch.javapad.io.FileOperations;
import com.dfortch.javapad.io.RecentFilesManager;
import com.dfortch.javapad.ui.MainFrame;
import com.dfortch.javapad.util.UIUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

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

        Weld weld = new Weld();

        try (WeldContainer container = weld.initialize()) {
            JavaPadUserPreferences preferences = container.select(JavaPadUserPreferences.class).get();
            MessageProvider messageProvider = container.select(MessageProvider.class).get();
            FileOperations fileOperations = container.select(FileOperations.class).get();
            RecentFilesManager recentFilesManager = container.select(RecentFilesManager.class).get();

            Locale.setDefault(preferences.getLocale());

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

}