package com.hotel.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.hotel.auth.PermissionManager.Permission;
import com.hotel.auth.UserSession;
import com.hotel.service.BookingManager;
import com.hotel.service.CustomerManager;
import com.hotel.service.InvoiceManager;
import com.hotel.service.RoomManager;
import com.hotel.storage.DataStorage;
import com.hotel.ui.theme.AppTheme;
import com.hotel.ui.theme.UIConstants;

/**
 * Main application frame with themed UI
 * 
 * @author OOP Project Team
 * @version 2.0
 */
public class MainFrame extends JFrame {

    // ==================== COMPONENTS ====================

    private JTabbedPane tabbedPane;
    private RoomPanel roomPanel;
    private BookingPanel bookingPanel;
    private CustomerPanel customerPanel;
    private InvoicePanel invoicePanel;
    private JLabel statusBar;
    private JPanel statusPanel;

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
        applyPermissions(); // Áp dụng phân quyền

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onExit();
            }
        });
    }

    // ==================== UI INITIALIZATION ====================

    private void initializeFrame() {
        setTitle(UIConstants.Titles.MAIN);
        setSize(AppTheme.Size.MAIN_WINDOW);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set background
        getContentPane().setBackground(AppTheme.Background.PRIMARY);
    }

    private void initializeMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(AppTheme.Background.PAPER);
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.Borders.LIGHT));

        // ===== File Menu =====
        JMenu fileMenu = new JMenu(UIConstants.Menu.FILE);
        fileMenu.setFont(AppTheme.Fonts.BODY);

        JMenuItem saveItem = new JMenuItem(UIConstants.Menu.SAVE);
        saveItem.setFont(AppTheme.Fonts.BODY);
        saveItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        saveItem.addActionListener(e -> saveAllData());
        fileMenu.add(saveItem);

        fileMenu.addSeparator();

        // Đăng xuất
        JMenuItem logoutItem = new JMenuItem("Đăng xuất");
        logoutItem.setFont(AppTheme.Fonts.BODY);
        logoutItem.setAccelerator(KeyStroke.getKeyStroke("ctrl L"));
        logoutItem.addActionListener(e -> onLogout());
        fileMenu.add(logoutItem);

        fileMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem(UIConstants.Menu.EXIT);
        exitItem.setFont(AppTheme.Fonts.BODY);
        exitItem.setAccelerator(KeyStroke.getKeyStroke("alt F4"));
        exitItem.addActionListener(e -> onExit());
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);

        // ===== Room Menu =====
        JMenu roomMenu = new JMenu(UIConstants.Menu.ROOMS);
        roomMenu.setFont(AppTheme.Fonts.BODY);

        JMenuItem addRoomItem = new JMenuItem(UIConstants.Menu.ADD_ROOM);
        addRoomItem.setFont(AppTheme.Fonts.BODY);
        addRoomItem.addActionListener(e -> {
            tabbedPane.setSelectedIndex(1); // Rooms tab
        });
        roomMenu.add(addRoomItem);

        JMenuItem listRoomsItem = new JMenuItem(UIConstants.Menu.LIST_ROOMS);
        listRoomsItem.setFont(AppTheme.Fonts.BODY);
        listRoomsItem.addActionListener(e -> {
            tabbedPane.setSelectedIndex(1); // Rooms tab
            roomPanel.refreshTable();
        });
        roomMenu.add(listRoomsItem);

        menuBar.add(roomMenu);

        // ===== Booking Menu =====
        JMenu bookingMenu = new JMenu(UIConstants.Menu.BOOKINGS);
        bookingMenu.setFont(AppTheme.Fonts.BODY);

        JMenuItem newBookingItem = new JMenuItem(UIConstants.Menu.NEW_BOOKING);
        newBookingItem.setFont(AppTheme.Fonts.BODY);
        newBookingItem.addActionListener(e -> {
            tabbedPane.setSelectedIndex(2); // Bookings tab
            if (bookingPanel != null) {
                bookingPanel.openAddDialogFromMenu();
            }
        });
        bookingMenu.add(newBookingItem);

        JMenuItem listBookingsItem = new JMenuItem(UIConstants.Menu.LIST_BOOKINGS);
        listBookingsItem.setFont(AppTheme.Fonts.BODY);
        listBookingsItem.addActionListener(e -> {
            tabbedPane.setSelectedIndex(2); // Bookings tab
            if (bookingPanel != null) {
                bookingPanel.refreshData();
            }
        });
        bookingMenu.add(listBookingsItem);

        menuBar.add(bookingMenu);

        // ===== Report Menu =====
        JMenu reportMenu = new JMenu(UIConstants.Menu.REPORTS);
        reportMenu.setFont(AppTheme.Fonts.BODY);

        JMenuItem roomReportItem = new JMenuItem(UIConstants.Menu.ROOM_STATS);
        roomReportItem.setFont(AppTheme.Fonts.BODY);
        roomReportItem.addActionListener(e -> showRoomStatistics());
        reportMenu.add(roomReportItem);

        JMenuItem revenueReportItem = new JMenuItem(UIConstants.Menu.REVENUE_REPORT);
        revenueReportItem.setFont(AppTheme.Fonts.BODY);
        revenueReportItem.addActionListener(e -> {
            tabbedPane.setSelectedIndex(4); // Reports tab
            if (invoicePanel != null) {
                invoicePanel.showReportDialogFromMenu();
            }
        });
        reportMenu.add(revenueReportItem);

        menuBar.add(reportMenu);

        // ===== Help Menu =====
        JMenu helpMenu = new JMenu(UIConstants.Menu.HELP);
        helpMenu.setFont(AppTheme.Fonts.BODY);

        JMenuItem aboutItem = new JMenuItem(UIConstants.Menu.ABOUT);
        aboutItem.setFont(AppTheme.Fonts.BODY);
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void initializeContent() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(AppTheme.BorderStyles.EMPTY_SM);
        contentPanel.setBackground(AppTheme.Background.PRIMARY);

        // Styled Tabbed Pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(AppTheme.Fonts.BODY_BOLD);
        tabbedPane.setBackground(AppTheme.Background.PAPER);

        // Initialize services first (needed for Dashboard)
        RoomManager roomManager = RoomManager.getInstance();
        customerManager = new CustomerManager();
        bookingManager = new BookingManager(roomManager);
        invoiceManager = new InvoiceManager(bookingManager);
        dataStorage = new DataStorage(customerManager, bookingManager, invoiceManager, roomManager);
        dataStorage.loadAllData();

        // Tab 0: Dashboard (Overview)
        DashboardPanel dashboardPanel = new DashboardPanel(customerManager, bookingManager, invoiceManager);
        tabbedPane.addTab("  " + UIConstants.Tabs.DASHBOARD + "  ", dashboardPanel);

        // Tab 1: Room Management
        roomPanel = new RoomPanel();
        tabbedPane.addTab("  " + UIConstants.Tabs.ROOMS + "  ", roomPanel);

        // Tab 2: Booking Management
        bookingPanel = new BookingPanel(customerManager, bookingManager, roomManager);
        tabbedPane.addTab("  " + UIConstants.Tabs.BOOKINGS + "  ", bookingPanel);

        // Tab 3: Customer Management
        customerPanel = new CustomerPanel(customerManager);
        tabbedPane.addTab("  " + UIConstants.Tabs.CUSTOMERS + "  ", customerPanel);

        // Tab 4: Reports / Invoices
        invoicePanel = new InvoicePanel(bookingManager, invoiceManager);
        tabbedPane.addTab("  " + UIConstants.Tabs.REPORTS + "  ", invoicePanel);

        // Refresh dashboard when tab changes
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 0) {
                dashboardPanel.refreshData();
            }
        });

        contentPanel.add(tabbedPane, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
    }

    private void initializeStatusBar() {
        statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(AppTheme.Background.SECONDARY);
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, AppTheme.Borders.LIGHT),
                BorderFactory.createEmptyBorder(
                        AppTheme.Spacing.SM,
                        AppTheme.Spacing.MD,
                        AppTheme.Spacing.SM,
                        AppTheme.Spacing.MD)));

        // Status indicator
        JPanel statusIndicator = new JPanel(new FlowLayout(FlowLayout.LEFT, AppTheme.Spacing.SM, 0));
        statusIndicator.setOpaque(false);

        JLabel statusDot = new JLabel("●");
        statusDot.setForeground(AppTheme.Status.SUCCESS);
        statusDot.setFont(new Font(AppTheme.FONT_FAMILY, Font.PLAIN, 10));
        statusIndicator.add(statusDot);

        statusBar = new JLabel(UIConstants.Messages.STATUS_READY);
        statusBar.setFont(AppTheme.Fonts.SMALL);
        statusBar.setForeground(AppTheme.Text.SECONDARY);
        statusIndicator.add(statusBar);

        statusPanel.add(statusIndicator, BorderLayout.WEST);

        // User info and version on right
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, AppTheme.Spacing.MD, 0));
        rightPanel.setOpaque(false);

        // Hiển thị thông tin người dùng đang đăng nhập
        UserSession session = UserSession.getInstance();
        if (session.isLoggedIn()) {
            JLabel userLabel = new JLabel("★ " + session.getDisplayName() + " (" + session.getRoleDisplayName() + ")");
            userLabel.setFont(AppTheme.Fonts.SMALL);
            userLabel.setForeground(AppTheme.Primary.MAIN);
            rightPanel.add(userLabel);

            rightPanel.add(new JLabel("  |  "));
        }

        JLabel versionLabel = new JLabel("v" + UIConstants.APP_VERSION);
        versionLabel.setFont(AppTheme.Fonts.SMALL);
        versionLabel.setForeground(AppTheme.Text.DISABLED);
        rightPanel.add(versionLabel);

        statusPanel.add(rightPanel, BorderLayout.EAST);

        add(statusPanel, BorderLayout.SOUTH);
    }

    /**
     * Áp dụng phân quyền - ẩn/hiện các tab và menu theo vai trò
     */
    private void applyPermissions() {
        UserSession session = UserSession.getInstance();

        // Nếu không đăng nhập, chạy với quyền mặc định (cho dev mode)
        if (!session.isLoggedIn()) {
            return;
        }

        // Kiểm tra quyền và vô hiệu hóa các tab không có quyền
        // Tab 0: Dashboard - tất cả đều xem được
        // Tab 1: Rooms
        if (!session.hasPermission(Permission.VIEW_ROOMS)) {
            tabbedPane.setEnabledAt(1, false);
            tabbedPane.setToolTipTextAt(1, "Bạn không có quyền xem quản lý phòng");
        }
        // Tab 2: Bookings
        if (!session.hasPermission(Permission.VIEW_BOOKINGS)) {
            tabbedPane.setEnabledAt(2, false);
            tabbedPane.setToolTipTextAt(2, "Bạn không có quyền xem đặt phòng");
        }
        // Tab 3: Customers
        if (!session.hasPermission(Permission.VIEW_CUSTOMERS)) {
            tabbedPane.setEnabledAt(3, false);
            tabbedPane.setToolTipTextAt(3, "Bạn không có quyền xem khách hàng");
        }
        // Tab 4: Reports/Invoices
        if (!session.hasPermission(Permission.VIEW_INVOICES) && !session.hasPermission(Permission.VIEW_REPORTS)) {
            tabbedPane.setEnabledAt(4, false);
            tabbedPane.setToolTipTextAt(4, "Bạn không có quyền xem báo cáo");
        }

        // Cập nhật title với tên người dùng
        setTitle(UIConstants.Titles.MAIN + " - " + session.getDisplayName());
    }

    // ==================== ACTIONS ====================

    private void saveAllData() {
        setStatus(UIConstants.Messages.STATUS_SAVING, AppTheme.Status.INFO);
        try {
            if (roomPanel != null) {
                roomPanel.saveRoomsData();
            }
            if (dataStorage != null) {
                dataStorage.saveAllData();
            }
            setStatus(UIConstants.Messages.SAVE_SUCCESS, AppTheme.Status.SUCCESS);
        } catch (Exception ex) {
            setStatus(UIConstants.Messages.SAVE_ERROR, AppTheme.Status.ERROR);
            JOptionPane.showMessageDialog(this,
                    UIConstants.Messages.SAVE_ERROR + ": " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onExit() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                UIConstants.Messages.CONFIRM_EXIT,
                "Xác nhận thoát",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            saveAllData();
            dispose();
            System.exit(0);
        } else if (confirm == JOptionPane.NO_OPTION) {
            dispose();
            System.exit(0);
        }
    }

    /**
     * Đăng xuất và quay về màn hình đăng nhập
     */
    private void onLogout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có muốn lưu dữ liệu trước khi đăng xuất?",
                "Đăng xuất",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.CANCEL_OPTION) {
            return; // Hủy đăng xuất
        }

        if (confirm == JOptionPane.YES_OPTION) {
            saveAllData();
        }

        // Xóa session
        UserSession.getInstance().logout();

        // Quay về màn hình đăng nhập
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            dispose();
        });
    }

    private void showRoomStatistics() {
        RoomManager manager = RoomManager.getInstance();

        StringBuilder stats = new StringBuilder();
        stats.append("╔══════════════════════════════════════╗\n");
        stats.append("║        THỐNG KÊ PHÒNG KHÁCH SẠN      ║\n");
        stats.append("╠══════════════════════════════════════╣\n");
        stats.append(String.format("║  Tổng số phòng: %-20d ║\n", manager.count()));
        stats.append("╠══════════════════════════════════════╣\n");
        stats.append("║  THEO LOẠI PHÒNG:                    ║\n");
        manager.countByType().forEach((type, count) -> stats
                .append(String.format("║    • %-15s: %5d phòng   ║\n", type.getDisplayName(), count)));
        stats.append("╠══════════════════════════════════════╣\n");
        stats.append("║  THEO TRẠNG THÁI:                    ║\n");
        manager.countByStatus().forEach((status, count) -> stats
                .append(String.format("║    • %-15s: %5d phòng   ║\n", status.getDisplayName(), count)));
        stats.append("╠══════════════════════════════════════╣\n");
        stats.append(String.format("║  Doanh thu tiềm năng/đêm:            ║\n"));
        stats.append(String.format("║  %,32.0f VND ║\n", manager.calculateTotalPotentialRevenue()));
        stats.append("╚══════════════════════════════════════╝\n");

        JTextArea textArea = new JTextArea(stats.toString());
        textArea.setEditable(false);
        textArea.setFont(AppTheme.Fonts.MONOSPACE);
        textArea.setBackground(AppTheme.Background.PAPER);
        textArea.setForeground(AppTheme.Text.PRIMARY);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450, 350));

        JOptionPane.showMessageDialog(this, scrollPane,
                UIConstants.Titles.STATISTICS, JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAboutDialog() {
        JPanel aboutPanel = new JPanel(new BorderLayout(AppTheme.Spacing.MD, AppTheme.Spacing.MD));
        aboutPanel.setBackground(AppTheme.Background.PAPER);
        aboutPanel.setBorder(AppTheme.BorderStyles.EMPTY_LG);

        // Icon
        JLabel iconLabel = new JLabel("★", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 64));
        aboutPanel.add(iconLabel, BorderLayout.NORTH);

        // Info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(UIConstants.APP_NAME_VN);
        titleLabel.setFont(AppTheme.Fonts.SUBHEADER);
        titleLabel.setForeground(AppTheme.Primary.DARK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(titleLabel);

        infoPanel.add(Box.createVerticalStrut(AppTheme.Spacing.SM));

        JLabel versionLabel = new JLabel("Version " + UIConstants.APP_VERSION);
        versionLabel.setFont(AppTheme.Fonts.BODY);
        versionLabel.setForeground(AppTheme.Text.SECONDARY);
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(versionLabel);

        infoPanel.add(Box.createVerticalStrut(AppTheme.Spacing.LG));

        JLabel descLabel = new JLabel("<html><center>Đồ án môn: Lập trình Hướng đối tượng<br><br>" +
                "Công nghệ: Java Swing + FlatLaf + JSON</center></html>");
        descLabel.setFont(AppTheme.Fonts.SMALL);
        descLabel.setForeground(AppTheme.Text.SECONDARY);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(descLabel);

        infoPanel.add(Box.createVerticalStrut(AppTheme.Spacing.LG));

        JLabel copyrightLabel = new JLabel("© 2024-2025 OOP Project Team");
        copyrightLabel.setFont(AppTheme.Fonts.SMALL);
        copyrightLabel.setForeground(AppTheme.Text.DISABLED);
        copyrightLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(copyrightLabel);

        aboutPanel.add(infoPanel, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(this, aboutPanel,
                UIConstants.Titles.ABOUT, JOptionPane.PLAIN_MESSAGE);
    }

    public void setStatus(String message) {
        setStatus(message, AppTheme.Status.SUCCESS);
    }

    public void setStatus(String message, Color dotColor) {
        statusBar.setText(message);
        // Update status dot color
        if (statusPanel != null && statusPanel.getComponent(0) instanceof JPanel indicator) {
            if (indicator.getComponent(0) instanceof JLabel dot) {
                dot.setForeground(dotColor);
            }
        }
    }
}
