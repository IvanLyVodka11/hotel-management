package com.hotel.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Frame đăng nhập hệ thống
 * 
 * @author Member1
 * @version 1.0
 */
public class LoginFrame extends JFrame {
    
    // ==================== CONSTANTS ====================
    
    private static final String TITLE = "🏨 Đăng nhập - Hệ thống Quản lý Khách sạn";
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;
    
    // Default credentials (for demo)
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin123";
    
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
    
    // ==================== UI INITIALIZATION ====================
    
    private void initializeFrame() {
        setTitle(TITLE);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void initializeUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(new Color(240, 248, 255));
        
        // Header
        JLabel headerLabel = new JLabel("🏨 ĐĂNG NHẬP HỆ THỐNG", SwingConstants.CENTER);
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
        JLabel userLabel = new JLabel("👤 Tên đăng nhập:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(userLabel, gbc);
        
        gbc.gridx = 1;
        usernameField = new JTextField(15);
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
        JLabel passLabel = new JLabel("🔒 Mật khẩu:");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(passLabel, gbc);
        
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
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
        
        loginButton = new JButton("🔑 Đăng nhập");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setPreferredSize(new Dimension(150, 35));
        loginButton.setBackground(new Color(0, 102, 204));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(e -> performLogin());
        buttonPanel.add(loginButton);
        
        JButton exitButton = new JButton("❌ Thoát");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 14));
        exitButton.setPreferredSize(new Dimension(100, 35));
        exitButton.addActionListener(e -> System.exit(0));
        buttonPanel.add(exitButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Hint label
        JLabel hintLabel = new JLabel("💡 Demo: admin / admin123", SwingConstants.CENTER);
        hintLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        hintLabel.setForeground(Color.GRAY);
        hintLabel.setBorder(new EmptyBorder(5, 0, 5, 0));
        mainPanel.add(hintLabel, BorderLayout.SOUTH);
        
        // Rebuild button panel with hint
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setOpaque(false);
        southPanel.add(buttonPanel, BorderLayout.CENTER);
        southPanel.add(hintLabel, BorderLayout.SOUTH);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
    }
    
    // ==================== LOGIN LOGIC ====================
    
    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        // Validation
        if (username.isEmpty()) {
            showMessage("⚠️ Vui lòng nhập tên đăng nhập!", Color.ORANGE);
            usernameField.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            showMessage("⚠️ Vui lòng nhập mật khẩu!", Color.ORANGE);
            passwordField.requestFocus();
            return;
        }
        
        // Check credentials (simple demo authentication)
        if (authenticate(username, password)) {
            showMessage("✅ Đăng nhập thành công!", new Color(0, 128, 0));
            
            // Open Main Frame
            SwingUtilities.invokeLater(() -> {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
                dispose(); // Close login frame
            });
        } else {
            showMessage("❌ Sai tên đăng nhập hoặc mật khẩu!", Color.RED);
            passwordField.setText("");
            passwordField.requestFocus();
        }
    }
    
    /**
     * Xác thực đăng nhập (demo version)
     * Trong thực tế sẽ kiểm tra từ database/file
     */
    private boolean authenticate(String username, String password) {
        // Simple authentication for demo
        return DEFAULT_USERNAME.equals(username) && DEFAULT_PASSWORD.equals(password);
    }
    
    private void showMessage(String message, Color color) {
        messageLabel.setText(message);
        messageLabel.setForeground(color);
    }
}
