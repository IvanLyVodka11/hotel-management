package com.hotel.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.hotel.service.BookingManager;
import com.hotel.service.CustomerManager;
import com.hotel.service.InvoiceManager;
import com.hotel.service.RoomManager;
import com.hotel.storage.DataStorage;

/**
 * Frame chính của ứng dụng Quản lý Khách sạn
 * 
 * @author Member1
 * @version 1.0
 */
public class MainFrame extends JFrame {
    
    // ==================== CONSTANTS ====================
    
    private static final String APP_TITLE = "🏨 Hệ thống Quản lý Khách sạn";
    private static final int DEFAULT_WIDTH = 1200;
    private static final int DEFAULT_HEIGHT = 700;
    
    // ==================== COMPONENTS ====================
    
    private JTabbedPane tabbedPane;
    private RoomPanel roomPanel;
    private BookingPanel bookingPanel;
    private CustomerPanel customerPanel;
    private InvoicePanel invoicePanel;
    private JLabel statusBar;

    // ==================== SERVICES ====================

    private CustomerManager customerManager;
    private BookingManager bookingManager;
    private InvoiceManager invoiceManager;
    private DataStorage dataStorage;
    
    // ==================== CONSTRUCTOR ====================
    
    public MainFrame() {
        initializeFrame();
        initializeMenuBar();
        initializeContent();
        initializeStatusBar();
        
        // Window closing handler
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onExit();
            }
        });
    }
    
    // ==================== UI INITIALIZATION ====================
    
    private void initializeFrame() {
        setTitle(APP_TITLE);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setMinimumSize(new Dimension(800, 500));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Set icon (if available)
        try {
            // setIconImage(new ImageIcon("resources/icon.png").getImage());
        } catch (Exception e) {
            // Ignore if icon not found
        }
    }
    
    private void initializeMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // ===== File Menu =====
        JMenu fileMenu = new JMenu("📁 File");
        
        JMenuItem saveItem = new JMenuItem("💾 Lưu dữ liệu");
        saveItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        saveItem.addActionListener(e -> saveAllData());
        fileMenu.add(saveItem);
        
        fileMenu.addSeparator();
        
        JMenuItem exitItem = new JMenuItem("🚪 Thoát");
        exitItem.setAccelerator(KeyStroke.getKeyStroke("alt F4"));
        exitItem.addActionListener(e -> onExit());
        fileMenu.add(exitItem);
        
        menuBar.add(fileMenu);
        
        // ===== Room Menu =====
        JMenu roomMenu = new JMenu("🛏️ Quản lý Phòng");
        
        JMenuItem addRoomItem = new JMenuItem("➕ Thêm phòng mới");
        addRoomItem.addActionListener(e -> {
            tabbedPane.setSelectedIndex(0);
            // Trigger add dialog from RoomPanel
        });
        roomMenu.add(addRoomItem);
        
        JMenuItem listRoomsItem = new JMenuItem("📋 Danh sách phòng");
        listRoomsItem.addActionListener(e -> {
            tabbedPane.setSelectedIndex(0);
            roomPanel.refreshTable();
        });
        roomMenu.add(listRoomsItem);
        
        menuBar.add(roomMenu);
        
        // ===== Booking Menu (Placeholder for Member 2) =====
        JMenu bookingMenu = new JMenu("📅 Đặt phòng");
        
        JMenuItem newBookingItem = new JMenuItem("➕ Đặt phòng mới");
        newBookingItem.addActionListener(e -> {
            tabbedPane.setSelectedIndex(1);
            if (bookingPanel != null) {
                bookingPanel.openAddDialogFromMenu();
            }
        });
        bookingMenu.add(newBookingItem);
        
        JMenuItem listBookingsItem = new JMenuItem("📋 Danh sách đặt phòng");
        listBookingsItem.addActionListener(e -> {
            tabbedPane.setSelectedIndex(1);
            if (bookingPanel != null) {
                bookingPanel.refreshData();
            }
        });
        bookingMenu.add(listBookingsItem);
        
        menuBar.add(bookingMenu);
        
        // ===== Report Menu =====
        JMenu reportMenu = new JMenu("📊 Báo cáo");
        
        JMenuItem roomReportItem = new JMenuItem("🛏️ Thống kê phòng");
        roomReportItem.addActionListener(e -> showRoomStatistics());
        reportMenu.add(roomReportItem);
        
        JMenuItem revenueReportItem = new JMenuItem("💰 Báo cáo doanh thu");
        revenueReportItem.addActionListener(e -> {
            tabbedPane.setSelectedIndex(3);
            if (invoicePanel != null) {
                invoicePanel.showReportDialogFromMenu();
            }
        });
        reportMenu.add(revenueReportItem);
        
        menuBar.add(reportMenu);
        
        // ===== Help Menu =====
        JMenu helpMenu = new JMenu("❓ Trợ giúp");
        
        JMenuItem aboutItem = new JMenuItem("ℹ️ Giới thiệu");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    private void initializeContent() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        // Tabbed Pane
        tabbedPane = new JTabbedPane();
        
        // Tab 1: Room Management (Member 1)
        roomPanel = new RoomPanel();
        tabbedPane.addTab("🛏️ Quản lý Phòng", roomPanel);

        // Shared services (Member 2)
        RoomManager roomManager = RoomManager.getInstance();
        customerManager = new CustomerManager();
        bookingManager = new BookingManager(roomManager);
        invoiceManager = new InvoiceManager(bookingManager);
        dataStorage = new DataStorage(customerManager, bookingManager, invoiceManager, roomManager);
        dataStorage.loadAllData();
        
        // Tab 2: Booking Management (Member 2)
        bookingPanel = new BookingPanel(customerManager, bookingManager, roomManager);
        tabbedPane.addTab("📅 Đặt phòng", bookingPanel);
        
        // Tab 3: Customer Management (Member 2)
        customerPanel = new CustomerPanel(customerManager);
        tabbedPane.addTab("👥 Khách hàng", customerPanel);
        
        // Tab 4: Reports / Invoices (Member 2)
        invoicePanel = new InvoicePanel(bookingManager, invoiceManager);
        tabbedPane.addTab("📊 Báo cáo", invoicePanel);
        
        contentPanel.add(tabbedPane, BorderLayout.CENTER);
        
        add(contentPanel);
    }
    
    private JPanel createPlaceholderPanel(String title, String message) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(50, 50, 50, 50));
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        messageLabel.setForeground(Color.GRAY);
        panel.add(messageLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void initializeStatusBar() {
        statusBar = new JLabel("  ✅ Sẵn sàng");
        statusBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
                new EmptyBorder(5, 10, 5, 10)
        ));
        add(statusBar, BorderLayout.SOUTH);
    }
    
    // ==================== ACTIONS ====================
    
    private void saveAllData() {
        setStatus("💾 Đang lưu dữ liệu...");
        try {
            if (roomPanel != null) {
                roomPanel.saveRoomsData();
            }
            if (dataStorage != null) {
                dataStorage.saveAllData();
            }
            setStatus("✅ Đã lưu dữ liệu");
        } catch (Exception ex) {
            setStatus("❌ Lỗi khi lưu dữ liệu");
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void onExit() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có muốn lưu dữ liệu trước khi thoát?",
                "Xác nhận thoát",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            saveAllData();
            dispose();
            System.exit(0);
        } else if (confirm == JOptionPane.NO_OPTION) {
            dispose();
            System.exit(0);
        }
        // Cancel: do nothing
    }
    
    private void showRoomStatistics() {
        // Simple statistics dialog
        com.hotel.service.RoomManager manager = com.hotel.service.RoomManager.getInstance();
        
        StringBuilder stats = new StringBuilder();
        stats.append("📊 THỐNG KÊ PHÒNG\n\n");
        stats.append("Tổng số phòng: ").append(manager.count()).append("\n\n");
        
        stats.append("📌 Theo loại phòng:\n");
        manager.countByType().forEach((type, count) -> 
                stats.append("  • ").append(type.getDisplayName()).append(": ").append(count).append("\n"));
        
        stats.append("\n📌 Theo trạng thái:\n");
        manager.countByStatus().forEach((status, count) -> 
                stats.append("  • ").append(status.getDisplayName()).append(": ").append(count).append("\n"));
        
        stats.append("\n💰 Tổng doanh thu tiềm năng/đêm: ");
        stats.append(String.format("%,.0f VND", manager.calculateTotalPotentialRevenue()));
        
        JTextArea textArea = new JTextArea(stats.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        
        JOptionPane.showMessageDialog(this, new JScrollPane(textArea), 
                "Thống kê Phòng", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showAboutDialog() {
        String about = "🏨 HỆ THỐNG QUẢN LÝ KHÁCH SẠN\n\n" +
                "Version: 1.0.0\n\n" +
                "📚 Đồ án môn: Lập trình Hướng đối tượng\n\n" +
                "👥 Nhóm phát triển:\n" +
                "   • Thành viên 1: Quản lý Phòng\n" +
                "   • Thành viên 2: Quản lý Đặt phòng & Khách hàng\n\n" +
                "🛠️ Công nghệ: Java Swing + JSON Storage\n\n" +
                "© 2024 - OOP Project";
        
        JOptionPane.showMessageDialog(this, about, "Giới thiệu", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void setStatus(String message) {
        statusBar.setText("  " + message);
    }
}
