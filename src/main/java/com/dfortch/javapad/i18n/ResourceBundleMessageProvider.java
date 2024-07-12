package com.dfortch.javapad.i18n;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceBundleMessageProvider implements MessageProvider {

    private static final Logger log = LogManager.getLogger(ResourceBundleMessageProvider.class);

    private final ResourceBundle resourceBundle;

    public ResourceBundleMessageProvider(String baseName, Locale locale) {
        log.info("Initializing ResourceBundleMessageProvider with baseName: {} and locale: {}", baseName, locale);
        this.resourceBundle = ResourceBundle.getBundle(baseName, locale);
        log.info("ResourceBundle loaded successfully");
    }

    @Override
    public String getMessage(String key) {
        log.trace("Fetching message with key: {}", key);
        String message;
        try {
            message = resourceBundle.getString(key);
            log.debug("Message found for key {}: {}", key, message);
        } catch (java.util.MissingResourceException e) {
            log.error("Message key not found: {}", key, e);
            message = "Key not found: " + key;
        }
        return message;
    }

    @Override
    public String getMessage(String key, Object... params) {
        log.trace("Fetching message with key: {} and params: {}", key, params);
        String message;
        try {
            String pattern = resourceBundle.getString(key);
            message = MessageFormat.format(pattern, params);
            log.debug("Formatted message for key {}: {}", key, message);
        } catch (java.util.MissingResourceException e) {
            log.error("Message key not found: {}", key, e);
            message = "Key not found: " + key;
        }
        return message;
    }
}
