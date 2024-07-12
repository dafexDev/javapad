package com.dfortch.javapad.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class UIUtilsTest {

    private JFrame testFrame;

    @BeforeEach
    public void setUp() {
        testFrame = new JFrame("Test Frame");
        testFrame.setSize(300, 200);
        testFrame.setVisible(true);
    }

    @AfterEach
    public void tearDown() {
        if (testFrame != null) {
            testFrame.dispose();
        }
    }

    @Test
    public void testConfigureLookAndFeel_Success() throws Exception {
        String lookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();

        UIUtils.configureLookAndFeel(lookAndFeelClassName);

        assertThat(UIManager.getLookAndFeel().getClass().getName()).isEqualTo(lookAndFeelClassName);
    }

    @Test
    public void testConfigureLookAndFeel_Failure() {
        String invalidLookAndFeelClassName = "InvalidLookAndFeel";

        try {
            UIUtils.configureLookAndFeel(invalidLookAndFeelClassName);
            fail("Expected exception");
        } catch (Exception e) {
            assertThat(e).isInstanceOfAny(ClassNotFoundException.class,
                    UnsupportedLookAndFeelException.class, InstantiationError.class, IllegalAccessException.class);
        }
    }

    @Test
    public void testUpdateUI() {
        UIUtils.updateUI();

        assertThat(testFrame.isVisible()).isTrue();
        assertThat(testFrame.getTitle()).isEqualTo("Test Frame");
    }
}
