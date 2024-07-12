package com.dfortch.javapad.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class UIUtils {

    private static final Logger log = LogManager.getLogger(UIUtils.class);

    private UIUtils() {
    }

    public static void configureLookAndFeel(String lookAndFeelClassName) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        log.trace("Configuring Look and Feel with class name {}", lookAndFeelClassName);
        try {
            UIManager.setLookAndFeel(lookAndFeelClassName);
            log.debug("Successfully set Look and Feel to {}", lookAndFeelClassName);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            log.warn("Unable to set Look and Feel {}", lookAndFeelClassName, e);
            throw e;
        }
    }

    public static void updateUI() {
        log.trace("Updating UI for all open windows");
        try {
            for (Window window : Window.getWindows()) {
                SwingUtilities.updateComponentTreeUI(window);
                log.debug("Updated UI for window: {}", window);
            }
            log.info("UI update completed successfully");
        } catch (Exception e) {
            log.error("Error occurred while updating UI", e);
        }
    }
}
