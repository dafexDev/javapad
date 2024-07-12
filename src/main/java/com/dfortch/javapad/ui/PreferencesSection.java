package com.dfortch.javapad.ui;

import com.dfortch.javapad.i18n.MessageProvider;

public enum PreferencesSection {
    APPEARANCE("preferences.appearance"),
    EDITOR("preferences.editor"),
    LOCALE("preferences.locale");

    private final String sectionName;

    PreferencesSection(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSectionName(MessageProvider messageProvider) {
        return messageProvider.getMessage(sectionName);
    }
}
