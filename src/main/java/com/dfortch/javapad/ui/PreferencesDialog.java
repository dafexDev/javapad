package com.dfortch.javapad.ui;

import com.dfortch.javapad.i18n.MessageProvider;
import com.dfortch.javapad.prefs.JavaPadUserPreferences;
import com.dfortch.javapad.prefs.JavapadTheme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Locale;

public class PreferencesDialog extends JDialog implements PreferencesActionsPanelListener, WindowListener {

    private static final Logger log = LogManager.getLogger(PreferencesDialog.class);
    private final transient MessageProvider messageProvider;

    private final transient JavaPadUserPreferences preferences;

    private AppearancePreferencesPanel appearancePreferencesPanel;
    private EditorPreferencesPanel editorPreferencesPanel;
    private LocalePreferencesPanel localePreferencesPanel;
    private JPanel contentPanel;
    private DefaultListModel<String> sectionListModel;

    private PreferencesActionsPanel actionsPanel;

    public PreferencesDialog(Frame owner, MessageProvider messageProvider, JavaPadUserPreferences preferences) {
        super(owner, true);
        this.messageProvider = messageProvider;
        this.preferences = preferences;
        initialize();
    }

    private void initialize() {
        log.trace("Initializing Preferences Dialog");
        try {
            setTitle(messageProvider.getMessage("preferences.title"));
            setSize(600, 400);
            setLocationRelativeTo(getOwner());
            addWindowListener(this);
            setResizable(false);

            contentPanel = new JPanel(new CardLayout());

            ActionListener listener = e -> updateSectionList();

            appearancePreferencesPanel = new AppearancePreferencesPanel(messageProvider, preferences);
            appearancePreferencesPanel.addThemeChangeListener(listener);
            editorPreferencesPanel = new EditorPreferencesPanel(messageProvider, preferences);
            editorPreferencesPanel.addFontFamilyChangeListener(listener);
            editorPreferencesPanel.addFontStyleChangeListener(listener);
            editorPreferencesPanel.addFontSizeChangeListener(listener);
            editorPreferencesPanel.addForegroundChangeListener(listener);
            editorPreferencesPanel.addBackgroundChangeListener(listener);
            localePreferencesPanel = new LocalePreferencesPanel(messageProvider, preferences);
            localePreferencesPanel.addLocaleChangeListener(listener);

            contentPanel.add(appearancePreferencesPanel, PreferencesSection.APPEARANCE.name());
            contentPanel.add(editorPreferencesPanel, PreferencesSection.EDITOR.name());
            contentPanel.add(localePreferencesPanel, PreferencesSection.LOCALE.name());

            sectionListModel = new DefaultListModel<>();
            for (PreferencesSection section : PreferencesSection.values()) {
                sectionListModel.addElement(section.getSectionName(messageProvider));
            }

            JList<String> sectionList = new JList<>(sectionListModel);
            sectionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            sectionList.setPreferredSize(new Dimension(100, 0));

            sectionList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    int selectedIndex = sectionList.getSelectedIndex();
                    changeSection(PreferencesSection.values()[selectedIndex]);
                }
            });

            actionsPanel = new PreferencesActionsPanel(messageProvider);

            actionsPanel.addPreferencesActionsPanelListener(this);

            add(new JScrollPane(sectionList), BorderLayout.WEST);
            add(contentPanel, BorderLayout.CENTER);
            add(actionsPanel, BorderLayout.SOUTH);

            log.trace("PreferencesDialog initialized successfully");
        } catch (Exception e) {
            log.error("Error initializing PreferencesDialog", e);
        }

    }

    private void changeSection(PreferencesSection section) {
        log.debug("Changing section to: {}", section.name());
        CardLayout cl = (CardLayout) (contentPanel.getLayout());
        cl.show(contentPanel, section.name());
    }

    private void updateSectionList() {
        updateSectionList(false);
    }

    private void updateSectionList(boolean saveChanges) {
        log.trace("Updating section list, saveChanges: {}", saveChanges);
        for (int i = 0; i < PreferencesSection.values().length; i++) {
            PreferencesSection section = PreferencesSection.values()[i];
            String sectionName = section.getSectionName(messageProvider);

            boolean isChanged = false;
            if (section == PreferencesSection.APPEARANCE) {
                isChanged = appearancePreferencesPanel.isThemeChanged();
            } else if (section == PreferencesSection.EDITOR) {
                isChanged = editorPreferencesPanel.isChanged();
            } else if (section == PreferencesSection.LOCALE) {
                isChanged = localePreferencesPanel.isLocaleChanged();
            }

            if (isChanged && !saveChanges) {
                sectionName += " *";
            } else if (saveChanges) {
                sectionName = section.getSectionName(messageProvider);
            }

            sectionListModel.set(i, sectionName);
        }
    }

    @Override
    public void onResetToDefault(ActionEvent e) {
        log.info("Reset to Default action triggered");
        int result = JOptionPane.showConfirmDialog(this, messageProvider.getMessage("preferences.dialogs.reset"),
                messageProvider.getMessage("preferences.dialogs.reset.title"), JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            log.debug("User confirmed reset to default preferences");
            resetPreferences();
        } else {
            log.debug("User canceled reset to default preferences");
        }
    }

    @Override
    public void onApply(ActionEvent e) {
        log.info("Apply action triggered");
        savePreferences();
    }

    @Override
    public void onAccept(ActionEvent e) {
        log.info("Accept action triggered");
        savePreferences();
        dispose();
    }

    @Override
    public void onCancel(ActionEvent e) {
        log.info("Cancel action triggered");
        dispose();
    }

    private void savePreferences() {
        log.debug("Saving preferences");
        try {
            JavapadTheme theme = appearancePreferencesPanel.getSelectedTheme();
            Font font = editorPreferencesPanel.getSelectedFont();
            Color foregroundColor = editorPreferencesPanel.getSelectedForegroundColor();
            Color backgroundColor = editorPreferencesPanel.getSelectedBackgroundColor();
            Locale locale = localePreferencesPanel.getSelectedLocale();

            foregroundColor = foregroundColor == null ? preferences.getDefaultEditorForegroundColor() : foregroundColor;
            backgroundColor = backgroundColor == null ? preferences.getDefaultEditorBackgroundColor() : backgroundColor;

            preferences.setTheme(theme);
            preferences.setEditorFont(font);
            preferences.setEditorForegroundColor(foregroundColor);
            preferences.setEditorBackgroundColor(backgroundColor);
            preferences.setLocale(locale);

            updateSectionList(true);

            log.info("Preferences saved successfully");
        } catch (Exception e) {
            log.error("Error saving preferences", e);
        }
    }

    private void resetPreferences() {
        log.info("Resetting preferences to default");
        preferences.resetAll();
        updateSectionList(true);
        log.info("Preferences reset to default successfully");
    }

    @Override
    public void windowOpened(WindowEvent e) {
        // Don't need to handle this event
    }

    @Override
    public void windowClosing(WindowEvent e) {
        actionsPanel.removePreferencesActionsPanelListener(this);
    }

    @Override
    public void windowClosed(WindowEvent e) {
        // Don't need to handle this event
    }

    @Override
    public void windowIconified(WindowEvent e) {
        // Don't need to handle this event
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        // Don't need to handle this event
    }

    @Override
    public void windowActivated(WindowEvent e) {
        // Don't need to handle this event
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        // Don't need to handle this event
    }
}
