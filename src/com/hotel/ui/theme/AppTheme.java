package com.hotel.ui.theme;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * Centralized theme configuration for the Hotel Management application.
 * Contains all colors, fonts, and styling constants.
 * 
 * @author OOP Project Team
 * @version 2.0
 */
public final class AppTheme {

    // Prevent instantiation
    private AppTheme() {
    }

    // ==================== COLOR PALETTE ====================

    /**
     * Primary Colors - Blue gradient for main actions and highlights
     */
    public static final class Primary {
        public static final Color LIGHT = new Color(187, 222, 251); // #BBDEFB
        public static final Color MAIN = new Color(33, 150, 243); // #2196F3
        public static final Color DARK = new Color(25, 118, 210); // #1976D2
        public static final Color DARKER = new Color(13, 71, 161); // #0D47A1
    }

    /**
     * Secondary Colors - Teal for accent elements
     */
    public static final class Secondary {
        public static final Color LIGHT = new Color(178, 223, 219); // #B2DFDB
        public static final Color MAIN = new Color(0, 150, 136); // #009688
        public static final Color DARK = new Color(0, 121, 107); // #00796B
    }

    /**
     * Semantic Colors - For status indicators
     */
    public static final class Status {
        public static final Color SUCCESS = new Color(76, 175, 80); // Green - Available, Completed
        public static final Color WARNING = new Color(255, 193, 7); // Amber - Maintenance, Pending
        public static final Color ERROR = new Color(244, 67, 54); // Red - Occupied, Cancelled
        public static final Color INFO = new Color(33, 150, 243); // Blue - Info, Cleaning
    }

    /**
     * Background Colors
     */
    public static final class Background {
        public static final Color PRIMARY = new Color(250, 250, 250); // Main background
        public static final Color SECONDARY = new Color(245, 245, 245); // Cards, panels
        public static final Color PAPER = Color.WHITE; // Paper surfaces
        public static final Color HEADER = new Color(236, 239, 241); // Header areas
        public static final Color SIDEBAR = new Color(55, 71, 79); // Sidebar dark
    }

    /**
     * Text Colors
     */
    public static final class Text {
        public static final Color PRIMARY = new Color(33, 33, 33); // Main text
        public static final Color SECONDARY = new Color(117, 117, 117); // Secondary text
        public static final Color DISABLED = new Color(189, 189, 189); // Disabled text
        public static final Color ON_PRIMARY = Color.WHITE; // Text on primary color
        public static final Color ON_DARK = new Color(236, 239, 241); // Text on dark bg
    }

    /**
     * Border Colors
     */
    public static final class Borders {
        public static final Color LIGHT = new Color(224, 224, 224); // Light border
        public static final Color NORMAL = new Color(189, 189, 189); // Normal border
        public static final Color FOCUSED = Primary.MAIN; // Focused input
    }

    // ==================== TYPOGRAPHY ====================

    /**
     * Font family - Uses system font or falls back to Arial
     */
    public static final String FONT_FAMILY = getSystemFont();

    private static String getSystemFont() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return "Segoe UI";
        } else if (os.contains("mac")) {
            return "SF Pro Display";
        } else {
            return "Arial";
        }
    }

    /**
     * Font Sizes
     */
    public static final class FontSize {
        public static final int XS = 10;
        public static final int SM = 12;
        public static final int MD = 14;
        public static final int LG = 16;
        public static final int XL = 18;
        public static final int XXL = 24;
        public static final int XXXL = 32;
    }

    /**
     * Pre-configured Fonts
     */
    public static final class Fonts {
        public static final Font HEADER = new Font(FONT_FAMILY, Font.BOLD, FontSize.XXL);
        public static final Font SUBHEADER = new Font(FONT_FAMILY, Font.BOLD, FontSize.XL);
        public static final Font TITLE = new Font(FONT_FAMILY, Font.BOLD, FontSize.LG);
        public static final Font BODY = new Font(FONT_FAMILY, Font.PLAIN, FontSize.MD);
        public static final Font BODY_BOLD = new Font(FONT_FAMILY, Font.BOLD, FontSize.MD);
        public static final Font SMALL = new Font(FONT_FAMILY, Font.PLAIN, FontSize.SM);
        public static final Font SMALL_ITALIC = new Font(FONT_FAMILY, Font.ITALIC, FontSize.SM);
        public static final Font BUTTON = new Font(FONT_FAMILY, Font.BOLD, FontSize.MD);
        public static final Font TABLE_HEADER = new Font(FONT_FAMILY, Font.BOLD, FontSize.SM);
        public static final Font MONOSPACE = new Font("Consolas", Font.PLAIN, FontSize.MD);
    }

    // ==================== SPACING ====================

    /**
     * Spacing values for consistent margins and padding
     */
    public static final class Spacing {
        public static final int XS = 4;
        public static final int SM = 8;
        public static final int MD = 12;
        public static final int LG = 16;
        public static final int XL = 24;
        public static final int XXL = 32;
    }

    // ==================== COMPONENT DIMENSIONS ====================

    /**
     * Standard component sizes
     */
    public static final class Size {
        // Buttons
        public static final Dimension BUTTON_SMALL = new Dimension(80, 28);
        public static final Dimension BUTTON_MEDIUM = new Dimension(120, 36);
        public static final Dimension BUTTON_LARGE = new Dimension(160, 42);

        // Input fields
        public static final int INPUT_HEIGHT = 32;
        public static final int INPUT_WIDTH = 200;

        // Table
        public static final int TABLE_ROW_HEIGHT = 32;

        // Windows
        public static final Dimension LOGIN_WINDOW = new Dimension(500, 420);
        public static final Dimension MAIN_WINDOW = new Dimension(1280, 720);
        public static final Dimension DIALOG = new Dimension(500, 400);
    }

    // ==================== COMPONENT BORDERS ====================

    /**
     * Pre-configured borders
     */
    public static final class BorderStyles {
        public static final Border EMPTY_SM = new EmptyBorder(Spacing.SM, Spacing.SM, Spacing.SM, Spacing.SM);
        public static final Border EMPTY_MD = new EmptyBorder(Spacing.MD, Spacing.MD, Spacing.MD, Spacing.MD);
        public static final Border EMPTY_LG = new EmptyBorder(Spacing.LG, Spacing.LG, Spacing.LG, Spacing.LG);
        public static final Border EMPTY_XL = new EmptyBorder(Spacing.XL, Spacing.XL, Spacing.XL, Spacing.XL);
    }

    // ==================== BUTTON STYLING ====================

    /**
     * Apply primary button style
     */
    public static void applyPrimaryButtonStyle(JButton button) {
        button.setFont(Fonts.BUTTON);
        button.setBackground(Primary.MAIN);
        button.setForeground(Text.ON_PRIMARY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(Size.BUTTON_MEDIUM);
    }

    /**
     * Apply secondary button style
     */
    public static void applySecondaryButtonStyle(JButton button) {
        button.setFont(Fonts.BUTTON);
        button.setBackground(Background.SECONDARY);
        button.setForeground(Text.PRIMARY);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(Size.BUTTON_MEDIUM);
    }

    /**
     * Apply success button style
     */
    public static void applySuccessButtonStyle(JButton button) {
        button.setFont(Fonts.BUTTON);
        button.setBackground(Status.SUCCESS);
        button.setForeground(Text.ON_PRIMARY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /**
     * Apply danger button style
     */
    public static void applyDangerButtonStyle(JButton button) {
        button.setFont(Fonts.BUTTON);
        button.setBackground(Status.ERROR);
        button.setForeground(Text.ON_PRIMARY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    // ==================== TABLE STYLING ====================

    /**
     * Apply default table styling
     */
    public static void applyTableStyle(JTable table) {
        table.setFont(Fonts.BODY);
        table.setRowHeight(Size.TABLE_ROW_HEIGHT);
        table.setSelectionBackground(Primary.LIGHT);
        table.setSelectionForeground(Text.PRIMARY);
        table.setGridColor(Borders.LIGHT);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));

        // Header styling
        table.getTableHeader().setFont(Fonts.TABLE_HEADER);
        table.getTableHeader().setBackground(Background.HEADER);
        table.getTableHeader().setForeground(Text.PRIMARY);
        table.getTableHeader().setPreferredSize(
                new Dimension(0, Size.TABLE_ROW_HEIGHT + 4));
    }

    // ==================== INPUT STYLING ====================

    /**
     * Apply default input field styling
     */
    public static void applyInputStyle(JTextField field) {
        field.setFont(Fonts.BODY);
        field.setPreferredSize(new Dimension(Size.INPUT_WIDTH, Size.INPUT_HEIGHT));
    }

    /**
     * Apply label styling
     */
    public static void applyLabelStyle(JLabel label) {
        label.setFont(Fonts.BODY);
        label.setForeground(Text.PRIMARY);
    }

    /**
     * Apply header label styling
     */
    public static void applyHeaderStyle(JLabel label) {
        label.setFont(Fonts.HEADER);
        label.setForeground(Primary.DARK);
    }

    // ==================== PANEL STYLING ====================

    /**
     * Apply card panel styling
     */
    public static void applyCardStyle(JPanel panel) {
        panel.setBackground(Background.PAPER);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Borders.LIGHT, 1),
                BorderStyles.EMPTY_MD));
    }

    // ==================== STATUS COLORS ====================

    /**
     * Get color for room status
     */
    public static Color getRoomStatusColor(String status) {
        return switch (status.toLowerCase()) {
            case "trống", "available" -> Status.SUCCESS;
            case "đang sử dụng", "occupied" -> Status.ERROR;
            case "bảo trì", "maintenance" -> Status.WARNING;
            case "đang dọn", "cleaning" -> Status.INFO;
            default -> Text.SECONDARY;
        };
    }

    /**
     * Get color for booking status
     */
    public static Color getBookingStatusColor(String status) {
        return switch (status.toLowerCase()) {
            case "pending", "chờ xác nhận" -> Status.WARNING;
            case "confirmed", "đã xác nhận" -> Status.INFO;
            case "checked_in", "đã nhận phòng" -> Primary.MAIN;
            case "checked_out", "đã trả phòng" -> Status.SUCCESS;
            case "cancelled", "đã hủy" -> Status.ERROR;
            default -> Text.SECONDARY;
        };
    }
}
