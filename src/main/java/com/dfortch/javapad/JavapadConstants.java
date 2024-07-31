package com.dfortch.javapad;

import com.dfortch.javapad.prefs.JavapadTheme;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

public class JavapadConstants {

    protected static final Locale[] AVAILABLE_LOCALES = {Locale.ENGLISH, Locale.FRENCH, Locale.forLanguageTag("es")};

    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    public static final JavapadTheme DEFAULT_THEME = JavapadTheme.FLATLAF_LIGHT;

    public static final String MESSAGE_RESOURCE_BUNDLE_BASE_NAME = "com.dfortch.javapad.messages";

    public static final Path USER_DATA_PATH = Paths.get(System.getProperty("user.home"), ".javapad");

    public static final Path CACHE_PATH = USER_DATA_PATH.resolve("cache");

    public static final String RECENT_FILES_CACHE_FILE = "recent_files";

    public static final int MAX_RECENT_FILES = 10;

    private JavapadConstants() {
    }
}
