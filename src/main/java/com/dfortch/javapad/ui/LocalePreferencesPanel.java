package com.dfortch.javapad.ui;

import com.dfortch.javapad.i18n.MessageProvider;
import com.dfortch.javapad.prefs.JavaPadUserPreferences;
import com.dfortch.javapad.prefs.PreferencesChangeListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Locale;

public class LocalePreferencesPanel extends JPanel implements PreferencesChangeListener {

    private static final Logger log = LogManager.getLogger(LocalePreferencesPanel.class);
    private final MessageProvider messageProvider;

    private final JavaPadUserPreferences preferences;

    private JComboBox<Locale> localeComboBox;

    private JLabel warningLabel;

    public LocalePreferencesPanel(MessageProvider messageProvider, JavaPadUserPreferences preferences) {
        this.messageProvider = messageProvider;
        this.preferences = preferences;

        initialize();
    }

    private void initialize() {
        log.trace("Initializing LocalePreferencesPanel");
        try {
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            JPanel formPanel = new JPanel(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();

            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.anchor = GridBagConstraints.NORTHWEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            gbc.gridx = 0;
            gbc.gridy = 0;

            JLabel localeLabel = new JLabel(messageProvider.getMessage("preferences.locale.label")+":");
            formPanel.add(localeLabel, gbc);

            gbc.weightx = 0;
            gbc.gridx = 1;
            localeComboBox = new JComboBox<>(preferences.getAvailableLocales());
            localeComboBox.addActionListener(e -> handleLocaleChange());

            formPanel.add(localeComboBox, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            gbc.weighty = 1.0;
            formPanel.add(Box.createVerticalGlue(), gbc);

            add(formPanel, BorderLayout.CENTER);

            warningLabel = new JLabel(messageProvider.getMessage("preferences.locale.warning"));
            warningLabel.setForeground(Color.RED);
            warningLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            warningLabel.setVisible(false);
            add(warningLabel, BorderLayout.SOUTH);

            add(warningLabel, BorderLayout.SOUTH);

            updateForm();

            preferences.addPreferencesChangeListener(this);

            log.trace("LocalePreferencesPanel initialized successfully");
        } catch (Exception e) {
            log.error("Error initializing LocalePreferencesPanel", e);
        }
    }

    public Locale getSelectedLocale() {
        log.trace("Retrieving selected locale");
        Locale selectedLocale = (Locale) localeComboBox.getSelectedItem();
        log.debug("Selected locale: {}", selectedLocale);
        return selectedLocale;
    }

    public boolean isLocaleChanged() {
        boolean localeChanged = !preferences.getLocale().equals(localeComboBox.getSelectedItem());
        if (localeChanged) {
            log.info("Locale change detected: new locale = {}", localeComboBox.getSelectedItem());
        } else {
            log.debug("No change in locale detected");
        }
        return localeChanged;
    }

    public void addLocaleChangeListener(ActionListener e) {
        localeComboBox.addActionListener(e);
        log.trace("Locale change listener added");
    }

    private void handleLocaleChange() {
        log.trace("Handling locale change");
        warningLabel.setVisible(isLocaleChanged());
    }

    private void updateForm() {
        log.trace("Updating form with current preferences");
        Locale prefLocale = preferences.getLocale();
        try {
            for (Locale locale : preferences.getAvailableLocales()) {
                if (prefLocale.equals(locale)) {
                    localeComboBox.setSelectedItem(locale);
                    log.debug("Locale set in form: {}", locale);
                    break;
                }
            }
        } catch (Exception e) {
            log.error("Error updating form with current preferences", e);
        }
    }

    @Override
    public void onPreferencesChanged() {
        log.trace("Preferences have changed, updating form");
        updateForm();
    }
}
