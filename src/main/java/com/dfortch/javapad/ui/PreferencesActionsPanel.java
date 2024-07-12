package com.dfortch.javapad.ui;

import com.dfortch.javapad.i18n.MessageProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class PreferencesActionsPanel extends JPanel {

    private static final Logger log = LogManager.getLogger(PreferencesActionsPanel.class);
    private final transient MessageProvider messageProvider;

    private transient List<PreferencesActionsPanelListener> listeners = new ArrayList<>();

    public PreferencesActionsPanel(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
        initialize();
    }

    public void initialize() {
        log.trace("Initializing PreferencesActionsPanel");
        try {
            setLayout(new BorderLayout());

            Border topBorder = BorderFactory.createMatteBorder(1, 0, 0, 0, UIManager.getColor("Separator.foreground"));
            Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
            Border border = BorderFactory.createCompoundBorder(topBorder, emptyBorder);

            setBorder(border);

            JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

            JButton resetButton = new JButton(messageProvider.getMessage("preferences.button.reset"));
            resetButton.addActionListener(this::handleResetToDefault);

            leftPanel.add(resetButton);

            JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

            JButton applyButton = new JButton(messageProvider.getMessage("preferences.button.apply"));
            applyButton.addActionListener(this::handleApply);

            JButton cancelButton = new JButton(messageProvider.getMessage("preferences.button.cancel"));
            cancelButton.addActionListener(this::handleCancel);

            JButton acceptButton = new JButton(messageProvider.getMessage("preferences.button.accept"));
            acceptButton.addActionListener(this::handleAccept);

            rightPanel.add(applyButton);
            rightPanel.add(acceptButton);
            rightPanel.add(cancelButton);

            add(leftPanel, BorderLayout.WEST);
            add(rightPanel, BorderLayout.EAST);
        } catch (Exception e) {
            log.error("Error initializing PreferencesActionsPanel", e);
        }
        log.trace("PreferencesActionsPanel initialized successfully");
    }

    private void handleResetToDefault(ActionEvent e) {
        log.info("Reset to Default button clicked");
        for (PreferencesActionsPanelListener listener : listeners) {
            log.debug("handleResetToDefault: Notifying listener: {}", listener);
            listener.onResetToDefault(e);
        }
    }

    private void handleApply(ActionEvent e) {
        log.info("Apply button clicked");
        for (PreferencesActionsPanelListener listener : listeners) {
            log.debug("handleApply: Notifying listener: {}", listener);
            listener.onApply(e);
        }
    }

    private void handleCancel(ActionEvent e) {
        log.info("Cancel button clicked");
        for (PreferencesActionsPanelListener listener : listeners) {
            log.debug("handleCancel: Notifying listener: {}", listener);
            listener.onCancel(e);
        }
    }

    private void handleAccept(ActionEvent e) {
        log.info("Accept button clicked");
        for (PreferencesActionsPanelListener listener : listeners) {
            log.debug("handleAccept: Notifying listener: {}", listener);
            listener.onAccept(e);
        }
    }

    public void addPreferencesActionsPanelListener(PreferencesActionsPanelListener listener) {
        log.info("Adding PreferencesActionsPanelListener: {}", listener);
        listeners.add(listener);
    }

    public void removePreferencesActionsPanelListener(PreferencesActionsPanelListener listener) {
        log.info("Removing PreferencesActionsPanelListener: {}", listener);
        listeners.remove(listener);
    }
}
