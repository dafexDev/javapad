package com.dfortch.javapad.ui;

import com.dfortch.javapad.i18n.MessageProvider;
import com.dfortch.javapad.prefs.JavaPadUserPreferences;
import com.dfortch.javapad.prefs.PreferencesChangeListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Objects;

public class EditorPreferencesPanel extends JPanel implements PreferencesChangeListener {

    private static final Logger log = LogManager.getLogger(EditorPreferencesPanel.class);

    private final transient MessageProvider messageProvider;

    private final transient JavaPadUserPreferences preferences;

    private static final Integer[] fontSizes = {8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40};
    private static final String[] fontStyles = {"Plain", "Bold", "Italic", "Bold Italic"};

    private JComboBox<String> fontFamilyComboBox;
    private  JComboBox<String> fontStyleComboBox;
    private JComboBox<Integer> fontSizeComboBox;
    private JButton foregroundColorButton;
    private JButton backgroundColorButton;
    private JTextArea previewArea;

    private Color selectedForegroundColor;
    private Color selectedBackgroundColor;

    public EditorPreferencesPanel(MessageProvider messageProvider, JavaPadUserPreferences preferences) {
        this.messageProvider = messageProvider;
        this.preferences = preferences;
        initialize();
    }

    private void initialize() {
        log.trace("Initializing EditorPreferencesPanel");

        try {
            setLayout(new BorderLayout());

            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            GridBagConstraints gbc = new GridBagConstraints();

            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.anchor = GridBagConstraints.NORTHWEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            gbc.gridx = 0;
            gbc.gridy = 0;

            JLabel fontLabel = new JLabel(messageProvider.getMessage("preferences.editor.font.font-family")+":");
            formPanel.add(fontLabel, gbc);

            gbc.weightx = 0;
            gbc.gridx = 1;
            gbc.gridwidth = 2;
            fontFamilyComboBox = new JComboBox<>(getAllSystemFonts());
            fontFamilyComboBox.addActionListener(e -> {
                log.info("Font family changed to {}", fontFamilyComboBox.getSelectedItem());
                updatePreview();
            });

            formPanel.add(fontFamilyComboBox, gbc);

            gbc.weightx = 1.0;
            gbc.gridx = 0;
            gbc.gridy = 1;
            JLabel styleLabel = new JLabel(messageProvider.getMessage("preferences.editor.font.font-style")+":");
            formPanel.add(styleLabel, gbc);

            gbc.weightx = 0;
            gbc.gridx = 1;
            gbc.gridwidth = 2;
            fontStyleComboBox = new JComboBox<>(fontStyles);
            fontStyleComboBox.addActionListener(e -> {
                log.info("Font style changed to {}", fontStyleComboBox.getSelectedItem());
                updatePreview();
            });

            formPanel.add(fontStyleComboBox, gbc);

            gbc.weightx = 1.0;
            gbc.gridx = 0;
            gbc.gridy = 2;
            JLabel sizeLabel = new JLabel(messageProvider.getMessage("preferences.editor.font.font-size")+":");
            formPanel.add(sizeLabel, gbc);

            gbc.weightx = 0;
            gbc.gridx = 1;
            fontSizeComboBox = new JComboBox<>(fontSizes);
            fontSizeComboBox.addActionListener(e -> {
                log.info("Font size changed to {}", fontSizeComboBox.getSelectedItem());
                updatePreview();
            });
            gbc.gridwidth = 2;
            formPanel.add(fontSizeComboBox, gbc);

            gbc.weightx = 1.0;
            gbc.gridx = 0;
            gbc.gridy = 3;
            JLabel fontColorLabel = new JLabel(messageProvider.getMessage("preferences.editor.font.foreground")+":");
            formPanel.add(fontColorLabel, gbc);

            gbc.weightx = 0;
            gbc.gridx = 1;
            gbc.gridwidth = 1;
            foregroundColorButton = new JButton(messageProvider.getMessage("preferences.editor.font.choose-color"));
            foregroundColorButton.addActionListener(e -> {
                Color color = JColorChooser.showDialog(EditorPreferencesPanel.this, messageProvider.getMessage("preferences.dialogs.choose-foreground-color"), Color.BLACK);
                if (color != null) {
                    selectedForegroundColor = color;
                    foregroundColorButton.setBackground(color);
                    log.info("Foreground color changed to {}", color);
                    updatePreview();
                }
            });
            formPanel.add(foregroundColorButton, gbc);

            gbc.weightx = 0;
            gbc.gridx = 2;
            gbc.gridwidth = 1;
            JButton resetForegroundColorButton = new JButton(messageProvider.getMessage("preferences.editor.font.reset-color"));
            resetForegroundColorButton.addActionListener(e -> {
                selectedForegroundColor = null;
                foregroundColorButton.setBackground(null);
                log.info("Foreground color reset");
                updatePreview();
            });
            formPanel.add(resetForegroundColorButton, gbc);

            gbc.weightx = 1.0;
            gbc.gridx = 0;
            gbc.gridy = 4;
            JLabel backgroundColorLabel = new JLabel(messageProvider.getMessage("preferences.editor.font.background")+":");
            formPanel.add(backgroundColorLabel, gbc);

            gbc.weightx = 0;
            gbc.gridx = 1;
            gbc.gridwidth = 1;
            backgroundColorButton = new JButton(messageProvider.getMessage("preferences.editor.font.choose-color"));
            backgroundColorButton.addActionListener(e -> {
                Color color = JColorChooser.showDialog(EditorPreferencesPanel.this, messageProvider.getMessage("preferences.dialogs.choose-background-color"), Color.WHITE);
                if (color != null) {
                    selectedBackgroundColor = color;
                    backgroundColorButton.setBackground(color);
                    log.info("Background color changed to {}", color);
                    updatePreview();
                }
            });
            formPanel.add(backgroundColorButton, gbc);

            gbc.weightx = 0;
            gbc.gridx = 2;
            JButton resetBackgroundColorButton = new JButton(messageProvider.getMessage("preferences.editor.font.reset-color"));
            resetBackgroundColorButton.addActionListener(e -> {
                selectedBackgroundColor = null;
                backgroundColorButton.setBackground(null);
                log.info("Background color reset");
                updatePreview();
            });
            formPanel.add(resetBackgroundColorButton, gbc);

            JPanel previewPanel = new JPanel(new BorderLayout());
            previewPanel.setBorder(BorderFactory.createTitledBorder(messageProvider.getMessage("preferences.editor.preview")));

            previewArea = new JTextArea(messageProvider.getMessage("preferences.editor.preview-text"));
            previewArea.setEditable(false);

            JScrollPane scrollPane = new JScrollPane(previewArea);
            scrollPane.setPreferredSize(new Dimension(0, 200));
            scrollPane.setMinimumSize(new Dimension(0, 200));
            previewPanel.add(scrollPane, BorderLayout.CENTER);

            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.gridwidth = 3;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            formPanel.add(previewPanel, gbc);

            add(formPanel);

            updateForm();

            updatePreview();

            preferences.addPreferencesChangeListener(this);

            log.trace("EditorPreferencesPanel initialized successfully");
        } catch (Exception e) {
            log.error("Error initializing EditorPreferencesPanel", e);
        }
    }

    protected void updateForm() {
        log.trace("Updating form with current preferences");

        try {
            Font defaultFont = preferences.getEditorFont();
            fontFamilyComboBox.setSelectedItem(defaultFont.getFamily());
            fontStyleComboBox.setSelectedIndex(getFontStyleIndex(defaultFont.getStyle()));
            fontSizeComboBox.setSelectedItem(defaultFont.getSize());

            selectedForegroundColor = preferences.getEditorForegroundColor();
            selectedBackgroundColor = preferences.getEditorBackgroundColor();

            foregroundColorButton.setBackground(selectedForegroundColor);
            backgroundColorButton.setBackground(selectedBackgroundColor);

            updatePreview();
            log.debug("Form updated with current preferences");
        } catch (Exception e) {
            log.error("Error updating form with current preferences", e);
        }
    }

    public Font getSelectedFont() {
        String fontFamily = (String) fontFamilyComboBox.getSelectedItem();
        int fontStyle = getFontStyleFromComboBox();
        int fontSize = (Integer) fontSizeComboBox.getSelectedItem();

        Font selectedFont = new Font(fontFamily, fontStyle, fontSize);
        log.trace("Selected font retrieved: {}", selectedFont);
        return selectedFont;
    }

    public Color getSelectedForegroundColor() {
        log.trace("Selected foreground color retrieved: {}", selectedForegroundColor);
        return selectedForegroundColor;
    }

    public Color getSelectedBackgroundColor() {
        log.trace("Selected background color retrieved: {}", selectedBackgroundColor);
        return selectedBackgroundColor;
    }

    public boolean isFontChanged() {
        Font prefFont = preferences.getEditorFont();
        boolean fontChanged = !Objects.equals(prefFont, getSelectedFont());
        if (fontChanged) {
            log.info("Font change detected: {}", getSelectedFont());
        } else {
            log.debug("No change in font detected");
        }
        return fontChanged;
    }

    public boolean isForegroundColorChanged() {
        Color prefForegroundColor = preferences.getEditorForegroundColor();
        boolean foregroundColorChanged = !Objects.equals(prefForegroundColor, getSelectedForegroundColor());
        if (foregroundColorChanged) {
            log.info("Foreground color change detected: {}", getSelectedForegroundColor());
        } else {
            log.debug("No change in foreground color detected");
        }
        return foregroundColorChanged;
    }

    public boolean isBackgroundColorChanged() {
        Color prefBackgroundColor = preferences.getEditorBackgroundColor();
        boolean backgroundColorChanged = !Objects.equals(prefBackgroundColor, getSelectedBackgroundColor());
        if (backgroundColorChanged) {
            log.info("Background color change detected: {}", getSelectedBackgroundColor());
        } else {
            log.debug("No change in background color detected");
        }
        return backgroundColorChanged;
    }

    public boolean isChanged() {
        boolean changed = isFontChanged() || isForegroundColorChanged() || isBackgroundColorChanged();
        log.trace("Checking if any preferences have changed: {}", changed);
        return changed;
    }

    private int getFontStyleFromComboBox() {
        int fontStyle;
        switch ((String) Objects.requireNonNull(fontStyleComboBox.getSelectedItem())) {
            case "Plain" -> fontStyle = Font.PLAIN;
            case "Bold" -> fontStyle = Font.BOLD;
            case "Italic" -> fontStyle = Font.ITALIC;
            case "Bold Italic" -> fontStyle = Font.BOLD | Font.ITALIC;
            default -> {
                log.error("Unknown font style selected: {}", fontStyleComboBox.getSelectedItem());
                throw new IllegalArgumentException("Unknown font style: " + fontStyleComboBox.getSelectedItem());
            }
        }
        log.trace("Font style from combo box: {}", fontStyle);
        return fontStyle;
    }

    private int getFontStyleIndex(int fontStyle) {
        int index = switch (fontStyle) {
            case Font.PLAIN -> 0;
            case Font.BOLD -> 1;
            case Font.ITALIC -> 2;
            case Font.BOLD | Font.ITALIC -> 3;
            default -> -1;
        };
        log.trace("Font style index for style {}: {}", fontStyle, index);
        return index;
    }

    protected void updatePreview() {
        log.trace("Updating preview with selected font and colors");
        try {
            String selectedFont = (String) fontFamilyComboBox.getSelectedItem();
            int selectedStyle = getFontStyleFromComboBox();
            int selectedSize = (Integer) fontSizeComboBox.getSelectedItem();

            if (selectedFont != null && selectedSize > 0) {
                Font font = new Font(selectedFont, selectedStyle, selectedSize);
                previewArea.setFont(font);
                previewArea.setForeground(selectedForegroundColor);
                previewArea.setBackground(selectedBackgroundColor);
                log.debug("Preview updated with font: {}, style: {}, size: {}", selectedFont, selectedStyle, selectedSize);
            } else {
                log.warn("Invalid font settings for preview: font = {}, size = {}", selectedFont, selectedSize);
            }
        } catch (Exception e) {
            log.error("Error updating preview", e);
        }
    }

    private String[] getAllSystemFonts() {
        log.trace("Retrieving all system fonts");
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            String[] fonts = ge.getAvailableFontFamilyNames();
            log.debug("System fonts retrieved: {}", (Object) fonts);
            return fonts;
        } catch (Exception e) {
            log.error("Error retrieving system fonts", e);
            return new String[0];
        }
    }

    public void addFontFamilyChangeListener(ActionListener listener) {
        fontStyleComboBox.addActionListener(listener);
        log.trace("Font style change listener added");
    }

    public void addFontStyleChangeListener(ActionListener listener) {
        fontSizeComboBox.addActionListener(listener);
        log.trace("Font size change listener added");
    }

    public void addFontSizeChangeListener(ActionListener listener) {
        foregroundColorButton.addActionListener(listener);
        log.trace("Foreground color change listener added");
    }

    public void addForegroundChangeListener(ActionListener listener) {
        backgroundColorButton.addActionListener(listener);
        log.trace("Background color change listener added");
    }

    public void addBackgroundChangeListener(ActionListener listener) {
        backgroundColorButton.addActionListener(listener);
    }

    @Override
    public void onPreferencesChanged() {
        log.trace("Preferences have changed, updating form");
        updateForm();
    }
}
