package com.dfortch.javapad.ui;

import com.dfortch.javapad.i18n.MessageProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class StatusBar extends JPanel {

    private static final Logger log = LogManager.getLogger(StatusBar.class);

    private final transient MessageProvider messageProvider;

    private JLabel lineColumnLabel;

    private JLabel characterCountLabel;

    private JLabel fileNameLabel;

    private int currentLine;
    private int currentColumn;
    private int characterCount;

    private String currentFileName;

    public StatusBar(int currentLine, int currentColumn, int characterCount, String currentFileName, MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
        this.currentLine = currentLine;
        this.currentColumn = currentColumn;
        this.characterCount = characterCount;
        this.currentFileName = currentFileName;
        initialize();
    }

    private void initialize() {
        log.trace("Initializing StatusBar");
        try {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();

            Border topBorder = BorderFactory.createMatteBorder(0, 0, 0, 1, UIManager.getColor("Separator.foreground"));
            Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
            Border border = BorderFactory.createCompoundBorder(topBorder, emptyBorder);

            lineColumnLabel = new JLabel();
            lineColumnLabel.setBorder(border);
            characterCountLabel = new JLabel();
            characterCountLabel.setBorder(border);
            fileNameLabel = new JLabel(currentFileName);
            fileNameLabel.setBorder(emptyBorder);

            lineColumnLabel.setHorizontalAlignment(SwingConstants.LEFT);
            characterCountLabel.setHorizontalAlignment(SwingConstants.LEFT);
            fileNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);

            gbc.gridx = 0;
            gbc.weightx = 0.0;
            gbc.anchor = GridBagConstraints.WEST;
            add(lineColumnLabel, gbc);

            gbc.gridx = 2;
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            add(characterCountLabel, gbc);

            gbc.gridx = 4;
            gbc.weightx = 0.0;
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.EAST;
            add(fileNameLabel, gbc);

            setCurrentLineAndColumn(currentLine, currentColumn);
            setCharacterCount(characterCount);
        } catch (Exception e) {
            log.error("Error initializing StatusBar");
        }
        log.trace("StatusBar initialized successfully");
    }

    public void setCurrentLineAndColumn(int line, int column) {
        log.debug("Setting current line: {}, current column: {}", line, column);
        currentLine = line;
        currentColumn = column;
        lineColumnLabel.setText(messageProvider.getMessage("main.statusbar.linecol", currentLine, currentColumn));
        log.info("Updated line and column display: Line {}, Column {}", currentLine, currentColumn);
    }

    public void setCharacterCount(int characterCount) {
        log.debug("Setting character count: {}", characterCount);
        this.characterCount = characterCount;
        characterCountLabel.setText(messageProvider.getMessage("main.statusbar.characters", characterCount));
        log.info("Updated character count display: {}", this.characterCount);
    }

    public void setFileName(String fileName) {
        log.debug("Setting file name: {}", fileName);
        currentFileName = fileName;
        fileNameLabel.setText(currentFileName);
        log.info("Updated file name display: {}", currentFileName);
    }
}
