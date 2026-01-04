package com.hotel.ui;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hotel.auth.UserSession;
import com.hotel.ui.theme.AppTheme;
import com.hotel.ui.theme.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Modern login frame with themed UI
 * 
 * @author OOP Project Team
 * @version 2.0
 */
public class LoginFrame extends JFrame {

    // ==================== DATA ====================

    private static final String DEFAULT_DATA_DIR = getDefaultDataDir();
    private static final String USERS_FILE = Paths.get(DEFAULT_DATA_DIR, UIConstants.Paths.USERS_FILE).toString();

    private final Map<String, String> credentials = new HashMap<>();
    private final Map<String, String[]> userInfo = new HashMap<>(); // username -> [id, name, role]
    private boolean credentialsLoaded = false;

    // ==================== COMPONENTS ====================

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;

    // ==================== CONSTRUCTOR ====================

    public LoginFrame() {
        initializeFrame();
        initializeUI();
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            focusUsernameField();
        }
    }

    // ==================== UI INITIALIZATION ====================

    private void initializeFrame() {
        setTitle(UIConstants.Titles.LOGIN);
        setSize(AppTheme.Size.LOGIN_WINDOW);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        addWindowFocusListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowGainedFocus(java.awt.event.WindowEvent e) {
                focusUsernameField();
            }
        });
    }

    private static String getDefaultDataDir() {
        String userDir = System.getProperty("user.dir");
        Path projectDataDir = Paths.get(userDir, UIConstants.Paths.DATA_DIR);
        if (Files.exists(projectDataDir)) {
            return projectDataDir.toString();
        }
        return UIConstants.Paths.DATA_DIR;
    }

    private void loadCredentialsIfNeeded() {
        if (credentialsLoaded)
            return;
        credentialsLoaded = true;

        credentials.put(UIConstants.Demo.USERNAME, UIConstants.Demo.PASSWORD);
        userInfo.put(UIConstants.Demo.USERNAME, new String[] { "U000", "Quản trị viên", "ADMIN" });

        try {
            Path usersPath = Paths.get(USERS_FILE);
            if (!Files.exists(usersPath))
                return;

            String content = Files.readString(usersPath);
            if (content == null || content.trim().isEmpty())
                return;

            JsonObject root = JsonParser.parseString(content).getAsJsonObject();
            if (!root.has("users"))
                return;

            JsonArray users = root.getAsJsonArray("users");
            for (JsonElement el : users) {
                if (!el.isJsonObject())
                    continue;
                JsonObject u = el.getAsJsonObject();
                if (!u.has("username") || !u.has("password"))
                    continue;

                String username = u.get("username").getAsString();
                String password = u.get("password").getAsString();
                String id = u.has("id") ? u.get("id").getAsString() : "U000";
                String name = u.has("name") ? u.get("name").getAsString() : username;
                String role = u.has("role") ? u.get("role").getAsString() : "STAFF";

                if (username != null && !username.isBlank()) {
                    credentials.put(username.trim(), password != null ? password : "");
                    userInfo.put(username.trim(), new String[] { id, name, role });
                }
            }
        } catch (Exception ignored) {
            // Fall back to demo admin
        }
    }

    private void focusUsernameField() {
        SwingUtilities.invokeLater(() -> {
            try {
                toFront();
                requestFocus();
            } catch (Exception ignored) {
            }

            if (usernameField != null) {
                usernameField.requestFocusInWindow();
                usernameField.selectAll();
            }
        });
    }

    private void initializeUI() {
        // Main container with gradient-like background
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(AppTheme.Background.PRIMARY);

        // Left decoration panel (colored sidebar)
        JPanel sidePanel = new JPanel();
        sidePanel.setBackground(AppTheme.Primary.MAIN);
        sidePanel.setPreferredSize(new Dimension(8, 0));
        mainPanel.add(sidePanel, BorderLayout.WEST);

        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout(0, AppTheme.Spacing.LG));
        contentPanel.setBorder(AppTheme.BorderStyles.EMPTY_XL);
        contentPanel.setBackground(AppTheme.Background.PRIMARY);

        // Header section
        JPanel headerPanel = createHeaderPanel();
        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Form section
        JPanel formPanel = createFormPanel();
        contentPanel.add(formPanel, BorderLayout.CENTER);

        // Bottom section with buttons and hint
        JPanel bottomPanel = createBottomPanel();
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, AppTheme.Spacing.MD, 0));

        // Title (no icon - cleaner look)

        // Title
        JLabel titleLabel = new JLabel("ĐĂNG NHẬP HỆ THỐNG", SwingConstants.CENTER);
        titleLabel.setFont(AppTheme.Fonts.HEADER);
        titleLabel.setForeground(AppTheme.Primary.DARK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(AppTheme.Spacing.SM, 0, 0, 0));
        panel.add(titleLabel, BorderLayout.CENTER);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Hệ thống Quản lý Khách sạn", SwingConstants.CENTER);
        subtitleLabel.setFont(AppTheme.Fonts.SMALL);
        subtitleLabel.setForeground(AppTheme.Text.SECONDARY);
        panel.add(subtitleLabel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(AppTheme.Spacing.SM, AppTheme.Spacing.SM, AppTheme.Spacing.SM, AppTheme.Spacing.SM);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        JLabel userLabel = new JLabel(UIConstants.Labels.USERNAME);
        userLabel.setFont(AppTheme.Fonts.BODY);
        userLabel.setForeground(AppTheme.Text.PRIMARY);
        panel.add(userLabel, gbc);

        // Username field
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        usernameField = new JTextField(20);
        usernameField.setFont(AppTheme.Fonts.BODY);
        usernameField.setPreferredSize(new Dimension(200, AppTheme.Size.INPUT_HEIGHT));
        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    passwordField.requestFocus();
                }
            }
        });
        panel.add(usernameField, gbc);

        // Password label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel passLabel = new JLabel(UIConstants.Labels.PASSWORD);
        passLabel.setFont(AppTheme.Fonts.BODY);
        passLabel.setForeground(AppTheme.Text.PRIMARY);
        panel.add(passLabel, gbc);

        // Password field
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        passwordField = new JPasswordField(20);
        passwordField.setFont(AppTheme.Fonts.BODY);
        passwordField.setPreferredSize(new Dimension(200, AppTheme.Size.INPUT_HEIGHT));
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        });
        panel.add(passwordField, gbc);

        // Message label
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(AppTheme.Spacing.MD, AppTheme.Spacing.SM, 0, AppTheme.Spacing.SM);
        messageLabel = new JLabel(" ");
        messageLabel.setFont(AppTheme.Fonts.SMALL);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(messageLabel, gbc);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, AppTheme.Spacing.SM));
        panel.setOpaque(false);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, AppTheme.Spacing.MD, 0));
        buttonPanel.setOpaque(false);

        // Login button with primary style
        loginButton = new JButton(UIConstants.Buttons.LOGIN);
        loginButton.setPreferredSize(new Dimension(140, 40));
        AppTheme.applyPrimaryButtonStyle(loginButton);
        loginButton.addActionListener(e -> performLogin());
        buttonPanel.add(loginButton);

        getRootPane().setDefaultButton(loginButton);

        // Exit button with secondary style
        JButton exitButton = new JButton(UIConstants.Buttons.CANCEL);
        exitButton.setPreferredSize(new Dimension(100, 40));
        AppTheme.applySecondaryButtonStyle(exitButton);
        exitButton.addActionListener(e -> System.exit(0));
        buttonPanel.add(exitButton);

        panel.add(buttonPanel, BorderLayout.CENTER);

        // Hint label
        JLabel hintLabel = new JLabel(UIConstants.Demo.HINT, SwingConstants.CENTER);
        hintLabel.setFont(AppTheme.Fonts.SMALL_ITALIC);
        hintLabel.setForeground(AppTheme.Text.DISABLED);
        hintLabel.setBorder(BorderFactory.createEmptyBorder(AppTheme.Spacing.SM, 0, 0, 0));
        panel.add(hintLabel, BorderLayout.SOUTH);

        return panel;
    }

    // ==================== LOGIN LOGIC ====================

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        // Validation
        if (username.isEmpty()) {
            showMessage("Vui lòng nhập tên đăng nhập!", AppTheme.Status.WARNING);
            usernameField.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            showMessage("Vui lòng nhập mật khẩu!", AppTheme.Status.WARNING);
            passwordField.requestFocus();
            return;
        }

        // Check credentials
        if (authenticate(username, password)) {
            showMessage(UIConstants.Messages.LOGIN_SUCCESS, AppTheme.Status.SUCCESS);

            SwingUtilities.invokeLater(() -> {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
                dispose();
            });
        } else {
            showMessage(UIConstants.Messages.LOGIN_ERROR, AppTheme.Status.ERROR);
            passwordField.setText("");
            passwordField.requestFocus();
        }
    }

    private boolean authenticate(String username, String password) {
        loadCredentialsIfNeeded();
        String expected = credentials.get(username);
        if (expected != null && expected.equals(password)) {
            // Lưu thông tin user vào session
            String[] info = userInfo.getOrDefault(username, new String[] { "U000", username, "STAFF" });
            UserSession.getInstance().login(info[0], username, info[1], info[2]);
            return true;
        }
        return false;
    }

    private void showMessage(String message, Color color) {
        messageLabel.setText(message);
        messageLabel.setForeground(color);
    }
}
