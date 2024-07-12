package com.dfortch.javapad.ui;

import java.awt.event.ActionEvent;

public interface PreferencesActionsPanelListener {
    void onResetToDefault(ActionEvent e);

    void onApply(ActionEvent e);

    void onAccept(ActionEvent e);

    void onCancel(ActionEvent e);
}
