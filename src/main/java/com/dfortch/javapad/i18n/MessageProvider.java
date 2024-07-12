package com.dfortch.javapad.i18n;

public interface MessageProvider {
    String getMessage(String key);

    String getMessage(String key, Object... params);
}
