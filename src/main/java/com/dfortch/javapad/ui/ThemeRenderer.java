package com.dfortch.javapad.ui;

import com.dfortch.javapad.i18n.MessageProvider;
import com.dfortch.javapad.prefs.JavapadTheme;

import javax.swing.*;
import java.awt.*;

public class ThemeRenderer extends DefaultListCellRenderer {

    private final transient MessageProvider messageProvider;

    public ThemeRenderer(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof JavapadTheme theme) {
            label.setText(theme.getThemeName(messageProvider));
        }
        return label;
    }
}
