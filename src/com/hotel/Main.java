package com.hotel;

import com.hotel.ui.LoginFrame;
import com.hotel.ui.MainFrame;

import javax.swing.*;

/**
 * Điểm khởi chạy chính của ứng dụng Quản lý Khách sạn
 * 
 * @author Member1
 * @version 1.0
 */
public class Main {
    
    /**
     * Main method - Entry point
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Set Look and Feel
        try {
            // Try to use FlatLaf for modern look
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
        } catch (Exception e) {
            // Fallback to system look and feel
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                // Use default
            }
        }
        
        // Run on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Check if we should skip login (for development)
            boolean skipLogin = args.length > 0 && "--skip-login".equals(args[0]);
            
            if (skipLogin) {
                // Go directly to main frame
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            } else {
                // Show login frame first
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            }
        });
    }

    /**
     * Sets the default font for all Swing components
     * @param f The font resource to set
     */
    private static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }
}
