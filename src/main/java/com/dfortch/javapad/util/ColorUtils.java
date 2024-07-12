package com.dfortch.javapad.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;

public class ColorUtils {

    private static final Logger log = LogManager.getLogger(ColorUtils.class);

    private ColorUtils() {
    }

    public static String colorToString(Color color) {
        log.trace("Converting color to string: {}", color);
        return color == null ? "" : color.getRed() + "," + color.getGreen() + "," + color.getBlue();
    }

    public static Color stringToColor(String colorString, Color defaultColor) {
        log.trace("Converting string to color: {}", colorString);
        if (colorString == null || colorString.isEmpty()) {
            log.warn("Color string is null or empty, using default color: {}", defaultColor);
            return defaultColor;
        }

        String[] rgb = colorString.split(",");

        if (rgb.length != 3) {
            log.error("Invalid color format: {}. Expected format is 'R,G,B'. Using default color: {}", colorString, defaultColor);
            return defaultColor;
        }

        try {
            int red = Integer.parseInt(rgb[0].trim());
            int green = Integer.parseInt(rgb[1].trim());
            int blue = Integer.parseInt(rgb[2].trim());
            Color color = new Color(red, green, blue);
            log.debug("Color created from string: {}", color);
            return color;
        } catch (NumberFormatException e) {
            log.error("Invalid color format: {}", colorString, e);
            return defaultColor;
        }
    }
}
