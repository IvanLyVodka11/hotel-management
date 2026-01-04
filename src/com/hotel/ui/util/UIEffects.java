package com.hotel.ui.util;

import com.hotel.ui.theme.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Các phương thức tiện ích cho hiệu ứng UI, animations và validation form.
 * Bao gồm: hover effects, focus effects, toast notifications, loading states.
 * 
 * @author OOP Project Team
 * @version 2.0
 */
public final class UIEffects {

    // Constructor private - không cho phép tạo instance
    private UIEffects() {
    }

    // ==================== HIỆU ỨNG HOVER ====================

    /**
     * Thêm hiệu ứng hover cho nút (làm tối khi di chuột vào)
     * 
     * @param button Nút cần thêm hiệu ứng
     */
    public static void addHoverEffect(JButton button) {
        Color originalBg = button.getBackground();

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(darkenColor(originalBg, 0.1f));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalBg);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(darkenColor(originalBg, 0.2f));
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(darkenColor(originalBg, 0.1f));
                }
            }
        });
    }

    /**
     * Thêm hiệu ứng hover cho dòng trong bảng
     * 
     * @param table Bảng cần thêm hiệu ứng
     */
    public static void addTableHoverEffect(JTable table) {
        table.addMouseMotionListener(new MouseAdapter() {
            int lastRow = -1;

            @Override
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row != lastRow) {
                    lastRow = row;
                    table.repaint();
                }
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                table.repaint();
            }
        });
    }

    // ==================== HIỆU ỨNG FOCUS ====================

    /**
     * Thêm hiệu ứng highlight khi focus vào ô nhập liệu
     * 
     * @param field Ô nhập liệu cần thêm hiệu ứng
     */
    public static void addFocusEffect(JTextField field) {
        Color originalBorder = AppTheme.Borders.NORMAL;
        Color focusBorder = AppTheme.Primary.MAIN;

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(focusBorder, 2),
                        BorderFactory.createEmptyBorder(2, 6, 2, 6)));
            }

            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(originalBorder, 1),
                        BorderFactory.createEmptyBorder(3, 7, 3, 7)));
            }
        });
    }

    // ==================== PHẢN HỒI VALIDATION ====================

    /**
     * Hiển thị trạng thái lỗi trên field
     * 
     * @param field Component cần hiển thị lỗi
     */
    public static void showError(JComponent field) {
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.Status.ERROR, 2),
                BorderFactory.createEmptyBorder(2, 6, 2, 6)));
    }

    /**
     * Hiển thị trạng thái thành công trên field
     * 
     * @param field Component cần hiển thị thành công
     */
    public static void showSuccess(JComponent field) {
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.Status.SUCCESS, 2),
                BorderFactory.createEmptyBorder(2, 6, 2, 6)));
    }

    /**
     * Xóa trạng thái validation
     * 
     * @param field Component cần xóa trạng thái
     */
    public static void clearValidation(JComponent field) {
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.Borders.NORMAL, 1),
                BorderFactory.createEmptyBorder(3, 7, 3, 7)));
    }

    /**
     * Tạo label hiển thị thông báo lỗi
     * 
     * @param message Nội dung thông báo lỗi
     * @return JLabel đã được định dạng
     */
    public static JLabel createErrorLabel(String message) {
        JLabel label = new JLabel("[!] " + message);
        label.setFont(AppTheme.Fonts.SMALL);
        label.setForeground(AppTheme.Status.ERROR);
        return label;
    }

    // ==================== CHỈ BÁO ĐANG TẢI ====================

    /**
     * Hiển thị trạng thái đang tải trên nút
     * 
     * @param button      Nút cần hiển thị loading
     * @param loadingText Text hiển thị khi loading
     */
    public static void showLoading(JButton button, String loadingText) {
        button.setEnabled(false);
        button.setText(loadingText + "...");
    }

    /**
     * Khôi phục nút từ trạng thái loading
     * 
     * @param button       Nút cần khôi phục
     * @param originalText Text gốc của nút
     */
    public static void hideLoading(JButton button, String originalText) {
        button.setEnabled(true);
        button.setText(originalText);
    }

    // ==================== THÔNG BÁO TOAST ====================

    /**
     * Hiển thị thông báo toast (mặc định 3 giây)
     * 
     * @param parent  Component cha
     * @param message Nội dung thông báo
     * @param type    Loại thông báo (SUCCESS/ERROR/WARNING/INFO)
     */
    public static void showToast(Component parent, String message, ToastType type) {
        showToast(parent, message, type, 3000);
    }

    /**
     * Hiển thị thông báo toast với thời gian tùy chỉnh
     * 
     * @param parent     Component cha
     * @param message    Nội dung thông báo
     * @param type       Loại thông báo
     * @param durationMs Thời gian hiển thị (ms)
     */
    public static void showToast(Component parent, String message, ToastType type, int durationMs) {
        SwingUtilities.invokeLater(() -> {
            JWindow toast = new JWindow();
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(type.borderColor, 1),
                    BorderFactory.createEmptyBorder(12, 16, 12, 16)));
            panel.setBackground(type.bgColor);

            JLabel iconLabel = new JLabel(type.icon);
            iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
            iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
            panel.add(iconLabel, BorderLayout.WEST);

            JLabel msgLabel = new JLabel(message);
            msgLabel.setFont(AppTheme.Fonts.BODY);
            msgLabel.setForeground(type.textColor);
            panel.add(msgLabel, BorderLayout.CENTER);

            toast.add(panel);
            toast.pack();

            // Đặt vị trí ở giữa phía dưới cửa sổ cha
            Window window = SwingUtilities.getWindowAncestor(parent);
            if (window != null) {
                int x = window.getX() + (window.getWidth() - toast.getWidth()) / 2;
                int y = window.getY() + window.getHeight() - toast.getHeight() - 50;
                toast.setLocation(x, y);
            }

            toast.setVisible(true);

            // Tự động ẩn sau thời gian quy định
            Timer timer = new Timer(durationMs, e -> {
                toast.setVisible(false);
                toast.dispose();
            });
            timer.setRepeats(false);
            timer.start();
        });
    }

    /**
     * Enum định nghĩa các loại toast notification
     */
    public enum ToastType {
        SUCCESS("[OK]", AppTheme.Status.SUCCESS, new Color(232, 245, 233), new Color(27, 94, 32)),
        ERROR("[X]", AppTheme.Status.ERROR, new Color(255, 235, 238), new Color(183, 28, 28)),
        WARNING("[!]", AppTheme.Status.WARNING, new Color(255, 248, 225), new Color(255, 111, 0)),
        INFO("[i]", AppTheme.Primary.MAIN, new Color(227, 242, 253), new Color(13, 71, 161));

        final String icon; // Icon hiển thị
        final Color borderColor; // Màu viền
        final Color bgColor; // Màu nền
        final Color textColor; // Màu chữ

        ToastType(String icon, Color borderColor, Color bgColor, Color textColor) {
            this.icon = icon;
            this.borderColor = borderColor;
            this.bgColor = bgColor;
            this.textColor = textColor;
        }
    }

    // ==================== CÁC PHƯƠNG THỨC HỖ TRỢ ====================

    /**
     * Làm tối màu theo tỷ lệ
     * 
     * @param color  Màu gốc
     * @param factor Tỷ lệ làm tối (0-1)
     * @return Màu đã được làm tối
     */
    private static Color darkenColor(Color color, float factor) {
        int r = Math.max(0, (int) (color.getRed() * (1 - factor)));
        int g = Math.max(0, (int) (color.getGreen() * (1 - factor)));
        int b = Math.max(0, (int) (color.getBlue() * (1 - factor)));
        return new Color(r, g, b, color.getAlpha());
    }

    /**
     * Làm sáng màu theo tỷ lệ
     * 
     * @param color  Màu gốc
     * @param factor Tỷ lệ làm sáng (0-1)
     * @return Màu đã được làm sáng
     */
    private static Color lightenColor(Color color, float factor) {
        int r = Math.min(255, (int) (color.getRed() + (255 - color.getRed()) * factor));
        int g = Math.min(255, (int) (color.getGreen() + (255 - color.getGreen()) * factor));
        int b = Math.min(255, (int) (color.getBlue() + (255 - color.getBlue()) * factor));
        return new Color(r, g, b, color.getAlpha());
    }
}
