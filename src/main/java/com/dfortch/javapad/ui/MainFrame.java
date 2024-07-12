package com.dfortch.javapad.ui;

import com.dfortch.javapad.BuildInfo;
import com.dfortch.javapad.i18n.MessageProvider;
import com.dfortch.javapad.io.FileOperations;
import com.dfortch.javapad.io.RecentFilesManager;
import com.dfortch.javapad.prefs.JavaPadUserPreferences;
import com.dfortch.javapad.prefs.JavapadTheme;
import com.dfortch.javapad.prefs.PreferencesChangeListener;
import com.dfortch.javapad.util.UIUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainFrame extends JFrame implements PreferencesChangeListener, WindowListener {

    private static final Logger log = LogManager.getLogger(MainFrame.class);

    private final transient MessageProvider messageProvider;

    private final transient FileOperations fileOperations;

    private final transient RecentFilesManager recentFilesManager;

    private final transient JavaPadUserPreferences preferences;

    private File currentFile;

    private String currentFileContent;

    private boolean fileSaved;

    private JMenu openRecentFilesMenu;

    private JMenu themeMenu;

    private JTextArea contentTextArea;

    private StatusBar statusBar;

    public MainFrame(MessageProvider messageProvider, FileOperations fileOperations, RecentFilesManager recentFilesManager,
                     JavaPadUserPreferences preferences) {
        this.messageProvider = messageProvider;
        this.fileOperations = fileOperations;
        this.recentFilesManager = recentFilesManager;
        this.preferences = preferences;

        fileSaved = true;

        initialize();
    }

    private void initialize() {
        log.info("Initializing MainFrame");

        setTitle(messageProvider.getMessage("main.title.new-file"));
        setSize(new Dimension(1400, 800));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        addWindowListener(this);

        UndoManager undoManager = new UndoManager();

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu(messageProvider.getMessage("main.menu.file"));

        JMenuItem newFileMenuItem = new JMenuItem(messageProvider.getMessage("main.menu.file.new-file"));
        newFileMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        newFileMenuItem.addActionListener(e -> newFile());

        JMenuItem openFileMenuItem = new JMenuItem(messageProvider.getMessage("main.menu.file.open-file"));
        openFileMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        openFileMenuItem.addActionListener(e -> openFile());

        openRecentFilesMenu = new JMenu(messageProvider.getMessage("main.menu.file.open-recent"));
        updateRecentFilesMenu();

        JMenuItem saveFileMenuItem = new JMenuItem(messageProvider.getMessage("main.menu.file.save-file"));
        saveFileMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        saveFileMenuItem.addActionListener(e -> saveFile());

        JMenuItem saveFileAsMenuItem = new JMenuItem(messageProvider.getMessage("main.menu.file.save-file-as"));
        saveFileAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        saveFileAsMenuItem.addActionListener(e -> saveFileAs());

        JMenuItem exitFileMenuItem = new JMenuItem("Exit");
        exitFileMenuItem.addActionListener(e -> {
            log.info("Exiting application");
            System.exit(0);
        });

        fileMenu.add(newFileMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(openFileMenuItem);
        fileMenu.add(openRecentFilesMenu);
        fileMenu.addSeparator();
        fileMenu.add(saveFileMenuItem);
        fileMenu.add(saveFileAsMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitFileMenuItem);

        menuBar.add(fileMenu);

        JMenu editMenu = new JMenu(messageProvider.getMessage("main.menu.edit"));

        JMenuItem undoItem = new JMenuItem(messageProvider.getMessage("main.menu.edit.undo"));
        undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        undoItem.addActionListener(e -> {
            try {
                undoManager.undo();
                log.info("Undo action performed");
            } catch (CannotUndoException ex) {
                log.warn("Undo action failed", ex);
            }
        });

        JMenuItem redoItem = new JMenuItem(messageProvider.getMessage("main.menu.edit.redo"));
        redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
        redoItem.addActionListener(e -> {
            try {
                undoManager.redo();
                log.info("Redo action performed");
            } catch (CannotRedoException ex) {
                log.warn("Redo action failed", ex);
            }
        });

        editMenu.add(undoItem);
        editMenu.add(redoItem);
        editMenu.addSeparator();
        editMenu.add(
                createItemFromAction(new DefaultEditorKit.CutAction(),
                        KeyEvent.VK_X,
                        InputEvent.CTRL_DOWN_MASK,
                        messageProvider.getMessage("main.menu.edit.cut")));
        editMenu.add(
                createItemFromAction(new DefaultEditorKit.CopyAction(),
                        KeyEvent.VK_C,
                        InputEvent.CTRL_DOWN_MASK,
                        messageProvider.getMessage("main.menu.edit.copy")));
        editMenu.add(
                createItemFromAction(new DefaultEditorKit.PasteAction(),
                        KeyEvent.VK_V,
                        InputEvent.CTRL_DOWN_MASK,
                        messageProvider.getMessage("main.menu.edit.paste")));

        menuBar.add(editMenu);

        JMenu preferencesMenu = new JMenu(messageProvider.getMessage("main.menu.preferences"));

        themeMenu = new JMenu(messageProvider.getMessage("main.menu.preferences.theme"));
        addThemesInMenu();

        JMenuItem openPreferencesMenuItem = new JMenuItem(messageProvider.getMessage("main.menu.preferences.open-preferences"));
        openPreferencesMenuItem.addActionListener(e -> openPreferences());

        preferencesMenu.add(themeMenu);
        preferencesMenu.addSeparator();
        preferencesMenu.add(openPreferencesMenuItem);

        menuBar.add(preferencesMenu);

        JMenu helpMenu = new JMenu(messageProvider.getMessage("main.menu.help"));

        JMenuItem aboutMenuItem = new JMenuItem(messageProvider.getMessage("main.menu.help.about"));
        aboutMenuItem.addActionListener(e -> showAbout());

        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        contentTextArea = new JTextArea();
        contentTextArea.getDocument().addUndoableEditListener(undoManager);
        contentTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkContent();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkContent();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkContent();
            }

            private void checkContent() {
                if (currentFile != null) {
                    fileSaved = currentFileContent.equals(contentTextArea.getText());
                    setTitle(generateTitle(currentFile.getName(), fileSaved));
                } else {
                    fileSaved = contentTextArea.getText().isEmpty();
                    setTitle(generateTitle(null, fileSaved));
                }
            }
        });
        contentTextArea.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                updateStatus();
            }

            private void updateStatus() {
                try {
                    int line;
                    int column;
                    int caretpos = contentTextArea.getCaretPosition();

                    line = contentTextArea.getLineOfOffset(caretpos);
                    column = caretpos - contentTextArea.getLineStartOffset(line);

                    line += 1;

                    statusBar.setCurrentLineAndColumn(line, column);
                } catch (BadLocationException e) {
                    log.warn("Caret update failed", e);
                }
                int numChars = contentTextArea.getDocument().getLength();
                statusBar.setCharacterCount(numChars);
            }
        });
        contentTextArea.setAutoscrolls(true);
        styleContentTextArea();

        JScrollPane textAreaScrollPane = new JScrollPane(contentTextArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        statusBar = new StatusBar(1, 0, 0, messageProvider.getMessage("main.statusbar.filename.no-file"), messageProvider);

        add(textAreaScrollPane, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);

        preferences.addPreferencesChangeListener(this);

        log.info("MainFrame Initialized successfully");
    }

    private void showAbout() {
        log.info("Showing About dialog");
        JLabel versionLabel = new JLabel(messageProvider.getMessage("about.label.version") + ": " + BuildInfo.getBuildVersion());
        JLabel urlLabel = new JLabel(messageProvider.getMessage("about.label.url") + ": " + BuildInfo.getBuildUrl());
        JLabel dateLabel = new JLabel(messageProvider.getMessage("about.label.date") + ": " + BuildInfo.getBuildDate());

        JPanel contentPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        contentPanel.add(versionLabel);
        contentPanel.add(urlLabel);
        contentPanel.add(dateLabel);

        JOptionPane.showMessageDialog(this, contentPanel, messageProvider.getMessage("about.title"), JOptionPane.INFORMATION_MESSAGE);
    }

    private void newFile() {
        log.info("Creating new file");
        if (confirmSave()) {
            currentFile = null;
            currentFileContent = "";
            contentTextArea.setText("");
            fileSaved = true;
            setTitle(generateTitle(null, true));
            statusBar.setFileName(messageProvider.getMessage("main.statusbar.filename.no-file"));
        }
    }

    private void openFile() {
        log.info("Opening file through file chooser");
        if (confirmSave()) {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                openFile(selectedFile);
            }
        }
    }

    private void openFile(File file) {
        log.info("Opening file {}", file.getAbsolutePath());
        try {
            String content = fileOperations.readFromFile(file);
            currentFile = file;
            currentFileContent = content;
            contentTextArea.setText(content);
            fileSaved = true;
            setTitle(generateTitle(file.getName(), true));
            recentFilesManager.addRecentFile(file);
            updateRecentFilesMenu();
            statusBar.setFileName(file.getName());
        } catch (IOException e) {
            log.error("Failed to open file {}", file.getAbsolutePath(), e);
            JOptionPane.showMessageDialog(this, messageProvider.getMessage("main.dialogs.open-file-error", e), messageProvider.getMessage("main.dialogs.open-file-error.title"), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveFile() {
        log.info("Saving file");
        if (currentFile == null) {
            saveFileAs();
        } else {
            saveFile(currentFile);
        }
    }

    private void saveFileAs() {
        log.info("Saving file as new file");
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showSaveDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            saveFile(selectedFile);
        }
    }

    private void saveFile(File file) {
        log.info("Saving file to {}", file.getAbsolutePath());
        try {
            String content = contentTextArea.getText();
            fileOperations.saveToFile(file, content, true);
            currentFile = file;
            currentFileContent = content;
            fileSaved = true;
            setTitle(generateTitle(file.getName(), true));
            recentFilesManager.addRecentFile(file);
            updateRecentFilesMenu();
            statusBar.setFileName(file.getName());
        } catch (IOException e) {
            log.error("Failed to save file {}", file.getAbsolutePath(), e);
            JOptionPane.showMessageDialog(this, messageProvider.getMessage("main.dialog.save-file-error"), messageProvider.getMessage("main.dialog.error-title"), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openPreferences() {
        log.info("Opening preferences dialog");
        PreferencesDialog preferencesUI = new PreferencesDialog(this, messageProvider, preferences);
        preferencesUI.setVisible(true);
    }

    private String generateTitle(String fileName, boolean saved) {
        String title;
        if (fileName == null) {
            title = messageProvider.getMessage("main.title.new-file");
        } else {
            title = messageProvider.getMessage("main.title.file", fileName);
        }

        return saved ? title : "*" + title;
    }

    private boolean confirmSave() {
        if (fileSaved) {
            return true;
        }
        int option = JOptionPane.showConfirmDialog(this, messageProvider.getMessage("main.dialogs.confirm-save"), messageProvider.getMessage("main.dialogs.confirm-save.title"), JOptionPane.YES_NO_CANCEL_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            saveFile();
            return true;
        } else {
            return option == JOptionPane.NO_OPTION;
        }
    }

    private void updateRecentFilesMenu() {
        try {
            addRecentFilesInMenu();
        } catch (Exception e) {
            log.error("An error occurred while updating the recent files menu", e);
        }
    }

    private void addRecentFilesInMenu()  {
        openRecentFilesMenu.removeAll();
        try {
            List<File> recentFiles = recentFilesManager.getRecentFiles();

            if (recentFiles.isEmpty()) {
                JMenuItem noRecentFilesMenuItem = new JMenuItem(messageProvider.getMessage("main.menu.file.open-recent.no-files"));
                noRecentFilesMenuItem.setEnabled(false);
                openRecentFilesMenu.add(noRecentFilesMenuItem);
            } else {
                openRecentFilesMenu.setEnabled(true);
                recentFiles.forEach(file -> {
                    JMenuItem recentFileMenuItem = new JMenuItem(file.getPath());
                    recentFileMenuItem.putClientProperty("file", file);
                    recentFileMenuItem.addActionListener(e -> {
                        log.info("Opening recent file {}", file.getName());
                        openFile(file);
                    });
                    openRecentFilesMenu.add(recentFileMenuItem);
                });
                JMenuItem clearRecentFilesMenuItem = new JMenuItem(messageProvider.getMessage("main.menu.file.open-recent.clear-files"));
                clearRecentFilesMenuItem.addActionListener(e -> {
                    try {
                        recentFilesManager.clearRecentFiles();
                        addRecentFilesInMenu();
                    } catch (IOException ex) {
                        log.error("Failed to clear recent files", ex);
                        throw new RuntimeException("Failed to clear recent files", ex);
                    }
                });
                openRecentFilesMenu.addSeparator();
                openRecentFilesMenu.add(clearRecentFilesMenuItem);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to retrieve recent files", e);
        }
    }

    private void addThemesInMenu() {
        themeMenu.removeAll();
        JavapadTheme currentTheme = preferences.getTheme();
        ButtonGroup group = new ButtonGroup();
        for (JavapadTheme theme : JavapadTheme.values()) {
            JRadioButtonMenuItem themeRadioButtonMenuItem = new JRadioButtonMenuItem(theme.getThemeName(messageProvider));
            themeRadioButtonMenuItem.putClientProperty("theme", theme);
            themeRadioButtonMenuItem.addActionListener(e -> changeTheme(theme));
            themeRadioButtonMenuItem.setSelected(theme.equals(currentTheme));
            group.add(themeRadioButtonMenuItem);
            themeMenu.add(themeRadioButtonMenuItem);
        }
    }

    private void changeTheme(JavapadTheme theme) {
        log.info("Changing theme to {}", theme.getThemeName(messageProvider));
        preferences.setTheme(theme);
    }

    private JMenuItem createItemFromAction(Action action, int key, int modifiers, String name) {
        action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(key, modifiers));
        action.putValue(Action.NAME, name);

        return new JMenuItem(action);
    }

    private void styleContentTextArea() {
        Font font = preferences.getEditorFont();
        Color foregroundColor = preferences.getEditorForegroundColor();
        Color backgroundColor = preferences.getEditorBackgroundColor();

        contentTextArea.setFont(font);

        contentTextArea.setForeground(foregroundColor);

        contentTextArea.setBackground(backgroundColor);
    }

    private void changeThemeAndReloadUI() {
        try {
            UIUtils.configureLookAndFeel(preferences.getTheme().getLookAndFeelClassName());
            UIUtils.updateUI();
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException e) {
            log.error("Error while changing theme and UI", e);
        }
    }

    @Override
    public void onPreferencesChanged() {
        log.info("Preferences changed");
        changeThemeAndReloadUI();
        addThemesInMenu();
        updateRecentFilesMenu();
        styleContentTextArea();
    }

    @Override
    public void windowOpened(WindowEvent e) {
        log.info("Window opened");
    }

    @Override
    public void windowClosing(WindowEvent e) {
        log.info("Window closing");
        if (confirmSave()) {
            preferences.removePreferencesChangeListener(this);
            dispose();
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
        log.info("Window closed");
    }

    @Override
    public void windowIconified(WindowEvent e) {
        log.info("Window iconified");
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        log.info("Window deiconified");
    }

    @Override
    public void windowActivated(WindowEvent e) {
        log.info("Window activated");
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        log.info("Window deactivated");
    }
}
