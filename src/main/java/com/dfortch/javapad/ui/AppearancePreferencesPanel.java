package com.dfortch.javapad.ui;

import com.dfortch.javapad.i18n.MessageProvider;
import com.dfortch.javapad.prefs.JavaPadUserPreferences;
import com.dfortch.javapad.prefs.JavapadTheme;
import com.dfortch.javapad.prefs.PreferencesChangeListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class AppearancePreferencesPanel extends JPanel implements PreferencesChangeListener {

    private static final Logger log = LogManager.getLogger(AppearancePreferencesPanel.class);

    private final transient MessageProvider messageProvider;

    private final transient JavaPadUserPreferences preferences;

    private JComboBox<JavapadTheme> themeComboBox;

    public AppearancePreferencesPanel(MessageProvider messageProvider, JavaPadUserPreferences preferences) {
        this.messageProvider = messageProvider;
        this.preferences = preferences;
        initialize();
    }

    private void initialize() {
        log.trace("Initializing AppearancePreferencesPanel");

        try {
            setLayout(new BorderLayout());
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setLayout(new GridBagLayout());
            formPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            GridBagConstraints gbc = new GridBagConstraints();

            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.anchor = GridBagConstraints.NORTHWEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            gbc.gridx = 0;
            gbc.gridy = 0;

            JLabel themeLabel = new JLabel(messageProvider.getMessage("preferences.appearance.theme")+":");
            formPanel.add(themeLabel, gbc);

            gbc.weightx = 0;
            gbc.gridx = 1;
            themeComboBox = new JComboBox<>(JavapadTheme.values());
            updateThemeComboBox();

            formPanel.add(themeComboBox, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            gbc.weighty = 1.0;
            formPanel.add(Box.createVerticalGlue(), gbc);

            add(formPanel);

            updateForm();

            preferences.addPreferencesChangeListener(this);

            log.trace("AppearancePreferencesPanel initialized successfully");
        } catch (Exception e) {
            log.error("Error initializing AppearancePreferencesPanel", e);
        }
        log.trace("AppearancePreferencesPanel initialized");
    }

    private void updateThemeComboBox() {
        DefaultComboBoxModel<JavapadTheme> model = (DefaultComboBoxModel<JavapadTheme>) themeComboBox.getModel();
        model.removeAllElements();
        for (JavapadTheme theme : JavapadTheme.values()) {
            model.addElement(theme);
        }
        themeComboBox.setRenderer(new ThemeRenderer(messageProvider));
    }

    public JavapadTheme getSelectedTheme() {
        JavapadTheme selectedTheme = (JavapadTheme) themeComboBox.getSelectedItem();
        log.trace("Selected theme retrieved: {}", selectedTheme.getThemeName(messageProvider));
        return selectedTheme;
    }

    public boolean isThemeChanged() {
        boolean themeChanged = !preferences.getTheme().equals(themeComboBox.getSelectedItem());
        if (themeChanged) {
            log.info("Theme change detected: {}", getSelectedTheme().getThemeName(messageProvider));
        } else {
            log.debug("No change in theme detected");
        }
        return themeChanged;
    }

    public void addThemeChangeListener(ActionListener listener) {
        themeComboBox.addActionListener(listener);
        log.debug("Theme change listener added");
    }

    private void updateForm() {
        log.trace("Updating form with current preferences");
        JavapadTheme preferencesTheme = preferences.getTheme();
        boolean themeFound = false;

        try {
            for (JavapadTheme theme : JavapadTheme.values()) {
                if (preferencesTheme.equals(theme)) {
                    themeComboBox.setSelectedItem(theme);
                    log.debug("Theme set to: {}", theme.getThemeName(messageProvider));
                    themeFound = true;
                    break;
                }
            }
            if (!themeFound) {
                log.warn("No matching theme found for current preferences");
            }
        } catch (Exception e) {
            log.error("Error updating form with current preferences", e);
        }
    }

    @Override
    public void onPreferencesChanged() {
        log.trace("Preferences have been changed, updating form");
        updateForm();
    }
}
