package com.hotel.ui;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Frame đăng nhập hệ thống
 * 
 * @author Member1
 * @version 1.0
 */
public class LoginFrame extends JFrame {
    
    // ==================== CONSTANTS ====================
    
    private static final String TITLE = "Đăng nhập - Hệ thống Quản lý Khách sạn";
    private static final int WIDTH = 480;
    private static final int HEIGHT = 300;
    
    // Default credentials (for demo)
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin123";

    private static final String DEFAULT_DATA_DIR = getDefaultDataDir();
    private static final String USERS_FILE = Paths.get(DEFAULT_DATA_DIR, "users.json").toString();

    private final Map<String, String> credentials = new HashMap<>();
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
        setTitle(TITLE);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // When the window gains focus (first time or after Alt-Tab), focus the username field.
        addWindowFocusListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowGainedFocus(java.awt.event.WindowEvent e) {
                focusUsernameField();
            }
        });
    }

    private static String getDefaultDataDir() {
        String userDir = System.getProperty("user.dir");
        Path projectDataDir = Paths.get(userDir, "data");
        if (Files.exists(projectDataDir)) {
            return projectDataDir.toString();
        }
        return "data";
    }

    private void loadCredentialsIfNeeded() {
        if (credentialsLoaded) return;
        credentialsLoaded = true;

        // Always keep the demo admin account as a fallback.
        credentials.put(DEFAULT_USERNAME, DEFAULT_PASSWORD);

        try {
            Path usersPath = Paths.get(USERS_FILE);
            if (!Files.exists(usersPath)) return;

            String content = Files.readString(usersPath);
            if (content == null || content.trim().isEmpty()) return;

            JsonObject root = JsonParser.parseString(content).getAsJsonObject();
            if (!root.has("users")) return;

            JsonArray users = root.getAsJsonArray("users");
            for (JsonElement el : users) {
                if (!el.isJsonObject()) continue;
                JsonObject u = el.getAsJsonObject();
                if (!u.has("username") || !u.has("password")) continue;

                String username = u.get("username").getAsString();
                String password = u.get("password").getAsString();
                if (username != null && !username.isBlank()) {
                    credentials.put(username.trim(), password != null ? password : "");
                }
            }
        } catch (Exception ignored) {
            // If users.json is invalid/unreadable, fall back to demo admin.
        }
    }

    private void focusUsernameField() {
        SwingUtilities.invokeLater(() -> {
            try {
                toFront();
                requestFocus();
            } catch (Exception ignored) {
                // Best-effort: some platforms may block focus stealing
            }

            if (usernameField != null) {
                usernameField.requestFocusInWindow();
                usernameField.selectAll();
            }
        });
    }
    
    private void initializeUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(new Color(240, 248, 255));
        
        // Header
        JLabel headerLabel = new JLabel("ĐĂNG NHẬP HỆ THỐNG", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setForeground(new Color(0, 51, 102));
        headerLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0;
        JLabel userLabel = new JLabel("Tên đăng nhập:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(userLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        usernameField = new JTextField(22);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    passwordField.requestFocus();
                }
            }
        });
        formPanel.add(usernameField, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel passLabel = new JLabel("Mật khẩu:");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(passLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        passwordField = new JPasswordField(22);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        });
        formPanel.add(passwordField, gbc);
        
        // Message Label
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        messageLabel.setForeground(Color.RED);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(messageLabel, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setOpaque(false);
        
        loginButton = new JButton("Đăng nhập");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setPreferredSize(new Dimension(150, 35));
        loginButton.setBackground(new Color(0, 102, 204));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(e -> performLogin());
        buttonPanel.add(loginButton);

        getRootPane().setDefaultButton(loginButton);
        
        JButton exitButton = new JButton("Thoát");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 14));
        exitButton.setPreferredSize(new Dimension(100, 35));
        exitButton.addActionListener(e -> System.exit(0));
        buttonPanel.add(exitButton);

        // Hint label
        JLabel hintLabel = new JLabel("Demo: admin / admin123", SwingConstants.CENTER);
        hintLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        hintLabel.setForeground(Color.GRAY);
        hintLabel.setBorder(new EmptyBorder(5, 0, 5, 0));
        
        // Rebuild button panel with hint
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setOpaque(false);
        southPanel.add(buttonPanel, BorderLayout.CENTER);
        southPanel.add(hintLabel, BorderLayout.SOUTH);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
    
    // ==================== LOGIN LOGIC ====================
    
    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        // Validation
        if (username.isEmpty()) {
            showMessage("Vui lòng nhập tên đăng nhập!", Color.ORANGE);
            usernameField.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            showMessage("Vui lòng nhập mật khẩu!", Color.ORANGE);
            passwordField.requestFocus();
            return;
        }
        
        // Check credentials (simple demo authentication)
        if (authenticate(username, password)) {
            showMessage("Đăng nhập thành công!", new Color(0, 128, 0));
            
            // Open Main Frame
            SwingUtilities.invokeLater(() -> {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
                dispose(); // Close login frame
            });
        } else {
            showMessage("Sai tên đăng nhập hoặc mật khẩu!", Color.RED);
            passwordField.setText("");
            passwordField.requestFocus();
        }
    }
    
    /**
     * Xác thực đăng nhập (demo version)
     * Trong thực tế sẽ kiểm tra từ database/file
     */
    private boolean authenticate(String username, String password) {
        loadCredentialsIfNeeded();
        String expected = credentials.get(username);
        return expected != null && expected.equals(password);
    }
    
    private void showMessage(String message, Color color) {
        messageLabel.setText(message);
        messageLabel.setForeground(color);
    }
}
