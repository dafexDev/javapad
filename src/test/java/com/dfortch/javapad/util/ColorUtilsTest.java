package com.dfortch.javapad.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.Color;

class ColorUtilsTest {

    @Test
    void testColorToString() {
        Color color = new Color(123, 45, 67);
        String result = ColorUtils.colorToString(color);
        Assertions.assertThat(result).isEqualTo("123,45,67");
    }

    @Test
    void testColorToString_NullColor() {
        String result = ColorUtils.colorToString(null);
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void testStringToColor_ValidString() {
        String colorString = "123,45,67";
        Color defaultColor = Color.BLACK;
        Color result = getColorFromString(colorString, defaultColor);
        Assertions.assertThat(result).isEqualTo(new Color(123, 45, 67));
    }

    @Test
    void testStringToColor_EmptyString() {
        String colorString = "";
        Color defaultColor = Color.BLACK;
        Color result = getColorFromString(colorString, defaultColor);
        Assertions.assertThat(result).isEqualTo(defaultColor);
    }

    @Test
    void testStringToColor_InvalidFormat() {
        String colorString = "123,45";
        Color defaultColor = Color.BLACK;
        Color result = getColorFromString(colorString, defaultColor);
        Assertions.assertThat(result).isEqualTo(defaultColor);
    }

    @Test
    void testStringToColor_NullString() {
        Color defaultColor = Color.BLACK;
        Color result = ColorUtils.stringToColor(null, defaultColor);
        Assertions.assertThat(result).isEqualTo(defaultColor);
    }

    private Color getColorFromString(String colorString, Color defaultColor) {
        return ColorUtils.stringToColor(colorString, defaultColor);
    }
}
