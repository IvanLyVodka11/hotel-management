package com.hotel.ui;

import com.hotel.service.BookingManager;
import com.hotel.service.CustomerManager;
import com.hotel.service.InvoiceManager;
import com.hotel.service.RoomManager;
import com.hotel.model.enums.RoomStatus;
import com.hotel.model.enums.BookingStatus;
import com.hotel.ui.theme.AppTheme;
import com.hotel.ui.theme.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Dashboard panel with overview statistics and quick actions
 * 
 * @author OOP Project Team
 * @version 2.0
 */
public class DashboardPanel extends JPanel {

    private final RoomManager roomManager;
    private BookingManager bookingManager;
    private CustomerManager customerManager;
    private InvoiceManager invoiceManager;

    // Stats labels to update
    private JLabel totalRoomsLabel;
    private JLabel availableRoomsLabel;
    private JLabel occupiedRoomsLabel;
    private JLabel maintenanceRoomsLabel;
    private JLabel totalBookingsLabel;
    private JLabel pendingBookingsLabel;
    private JLabel totalCustomersLabel;
    private JLabel todayRevenueLabel;

    public DashboardPanel() {
        this.roomManager = RoomManager.getInstance();
        initializeUI();
    }

    public DashboardPanel(CustomerManager customerManager, BookingManager bookingManager,
            InvoiceManager invoiceManager) {
        this.roomManager = RoomManager.getInstance();
        this.customerManager = customerManager;
        this.bookingManager = bookingManager;
        this.invoiceManager = invoiceManager;
        initializeUI();
    }

    public void setManagers(CustomerManager customerManager, BookingManager bookingManager,
            InvoiceManager invoiceManager) {
        this.customerManager = customerManager;
        this.bookingManager = bookingManager;
        this.invoiceManager = invoiceManager;
        refreshData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(AppTheme.Spacing.LG, AppTheme.Spacing.LG));
        setBorder(AppTheme.BorderStyles.EMPTY_LG);
        setBackground(AppTheme.Background.PRIMARY);

        // Header
        add(createHeaderPanel(), BorderLayout.NORTH);

        // Main content
        add(createMainContent(), BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        // Left - Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setOpaque(false);

        JLabel welcomeLabel = new JLabel("👋 Chào mừng trở lại!");
        welcomeLabel.setFont(AppTheme.Fonts.HEADER);
        welcomeLabel.setForeground(AppTheme.Primary.DARK);
        titlePanel.add(welcomeLabel);

        panel.add(titlePanel, BorderLayout.WEST);

        // Right - Date
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        datePanel.setOpaque(false);

        LocalDate today = LocalDate.now();
        String dateStr = today.format(DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy"));
        JLabel dateLabel = new JLabel("📅 " + dateStr);
        dateLabel.setFont(AppTheme.Fonts.BODY);
        dateLabel.setForeground(AppTheme.Text.SECONDARY);
        datePanel.add(dateLabel);

        panel.add(datePanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createMainContent() {
        JPanel panel = new JPanel(new BorderLayout(AppTheme.Spacing.LG, AppTheme.Spacing.LG));
        panel.setOpaque(false);

        // Top - Stats cards
        panel.add(createStatsPanel(), BorderLayout.NORTH);

        // Center - Quick info panels
        panel.add(createQuickInfoPanel(), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, AppTheme.Spacing.MD, 0));
        panel.setOpaque(false);

        // Room stats card
        totalRoomsLabel = new JLabel("0");
        availableRoomsLabel = new JLabel("0 trống");
        panel.add(createStatCard("■", "Tổng số phòng", totalRoomsLabel, availableRoomsLabel, AppTheme.Primary.MAIN));

        // Occupied rooms
        occupiedRoomsLabel = new JLabel("0");
        maintenanceRoomsLabel = new JLabel("0 bảo trì");
        panel.add(createStatCard("●", "Đang sử dụng", occupiedRoomsLabel, maintenanceRoomsLabel,
                AppTheme.Status.ERROR));

        // Bookings
        totalBookingsLabel = new JLabel("0");
        pendingBookingsLabel = new JLabel("0 chờ xác nhận");
        panel.add(createStatCard("▶", "Đặt phòng", totalBookingsLabel, pendingBookingsLabel, AppTheme.Status.INFO));

        // Customers
        totalCustomersLabel = new JLabel("0");
        todayRevenueLabel = new JLabel("0 VND");
        panel.add(createStatCard("👥", "Khách hàng", totalCustomersLabel, todayRevenueLabel, AppTheme.Status.SUCCESS));

        // Load actual data
        refreshData();

        return panel;
    }

    private JPanel createStatCard(String icon, String title, JLabel mainValue, JLabel subValue, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(AppTheme.Spacing.SM, AppTheme.Spacing.SM));
        card.setBackground(AppTheme.Background.PAPER);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.Borders.LIGHT, 1),
                new EmptyBorder(AppTheme.Spacing.LG, AppTheme.Spacing.LG, AppTheme.Spacing.LG, AppTheme.Spacing.LG)));

        // Left accent bar
        JPanel accentBar = new JPanel();
        accentBar.setBackground(accentColor);
        accentBar.setPreferredSize(new Dimension(4, 0));
        card.add(accentBar, BorderLayout.WEST);

        // Content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        // Icon and title row
        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, AppTheme.Spacing.XS, 0));
        titleRow.setOpaque(false);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        titleRow.add(iconLabel);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(AppTheme.Fonts.SMALL);
        titleLabel.setForeground(AppTheme.Text.SECONDARY);
        titleRow.add(titleLabel);

        content.add(titleRow);
        content.add(Box.createVerticalStrut(AppTheme.Spacing.SM));

        // Main value
        mainValue.setFont(new Font(AppTheme.FONT_FAMILY, Font.BOLD, 28));
        mainValue.setForeground(AppTheme.Text.PRIMARY);
        mainValue.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(mainValue);

        // Sub value
        subValue.setFont(AppTheme.Fonts.SMALL);
        subValue.setForeground(accentColor);
        subValue.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(subValue);

        card.add(content, BorderLayout.CENTER);

        return card;
    }

    private JPanel createQuickInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, AppTheme.Spacing.LG, 0));
        panel.setOpaque(false);

        // Left - Room status overview
        panel.add(createRoomStatusPanel());

        // Right - Quick actions
        panel.add(createQuickActionsPanel());

        return panel;
    }

    private JPanel createRoomStatusPanel() {
        JPanel card = new JPanel(new BorderLayout(AppTheme.Spacing.MD, AppTheme.Spacing.MD));
        card.setBackground(AppTheme.Background.PAPER);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.Borders.LIGHT, 1),
                new EmptyBorder(AppTheme.Spacing.LG, AppTheme.Spacing.LG, AppTheme.Spacing.LG, AppTheme.Spacing.LG)));

        // Header
        JLabel headerLabel = new JLabel("★ Trạng thái phòng");
        headerLabel.setFont(AppTheme.Fonts.TITLE);
        headerLabel.setForeground(AppTheme.Text.PRIMARY);
        card.add(headerLabel, BorderLayout.NORTH);

        // Status bars
        JPanel statusBars = new JPanel();
        statusBars.setLayout(new BoxLayout(statusBars, BoxLayout.Y_AXIS));
        statusBars.setOpaque(false);

        int total = Math.max(roomManager.count(), 1);
        int available = roomManager.findAvailableRooms().size();
        int occupied = roomManager.findByStatus(RoomStatus.OCCUPIED).size();
        int maintenance = roomManager.findByStatus(RoomStatus.MAINTENANCE).size();
        int cleaning = roomManager.findByStatus(RoomStatus.CLEANING).size();

        statusBars.add(Box.createVerticalStrut(AppTheme.Spacing.MD));
        statusBars.add(createStatusBar("Trống", available, total, AppTheme.Status.SUCCESS));
        statusBars.add(Box.createVerticalStrut(AppTheme.Spacing.SM));
        statusBars.add(createStatusBar("Đang sử dụng", occupied, total, AppTheme.Status.ERROR));
        statusBars.add(Box.createVerticalStrut(AppTheme.Spacing.SM));
        statusBars.add(createStatusBar("Bảo trì", maintenance, total, AppTheme.Status.WARNING));
        statusBars.add(Box.createVerticalStrut(AppTheme.Spacing.SM));
        statusBars.add(createStatusBar("Đang dọn", cleaning, total, AppTheme.Status.INFO));

        card.add(statusBars, BorderLayout.CENTER);

        return card;
    }

    private JPanel createStatusBar(String label, int count, int total, Color color) {
        JPanel panel = new JPanel(new BorderLayout(AppTheme.Spacing.SM, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // Label
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(AppTheme.Fonts.SMALL);
        labelComponent.setForeground(AppTheme.Text.SECONDARY);
        labelComponent.setPreferredSize(new Dimension(100, 20));
        panel.add(labelComponent, BorderLayout.WEST);

        // Progress bar
        JProgressBar progressBar = new JProgressBar(0, total);
        progressBar.setValue(count);
        progressBar.setStringPainted(false);
        progressBar.setBackground(AppTheme.Background.SECONDARY);
        progressBar.setForeground(color);
        progressBar.setPreferredSize(new Dimension(150, 8));
        progressBar.setBorderPainted(false);

        JPanel barPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 6));
        barPanel.setOpaque(false);
        barPanel.add(progressBar);
        panel.add(barPanel, BorderLayout.CENTER);

        // Count
        JLabel countLabel = new JLabel(String.valueOf(count));
        countLabel.setFont(AppTheme.Fonts.BODY_BOLD);
        countLabel.setForeground(color);
        countLabel.setPreferredSize(new Dimension(30, 20));
        countLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(countLabel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createQuickActionsPanel() {
        JPanel card = new JPanel(new BorderLayout(AppTheme.Spacing.MD, AppTheme.Spacing.MD));
        card.setBackground(AppTheme.Background.PAPER);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.Borders.LIGHT, 1),
                new EmptyBorder(AppTheme.Spacing.LG, AppTheme.Spacing.LG, AppTheme.Spacing.LG, AppTheme.Spacing.LG)));

        // Header
        JLabel headerLabel = new JLabel("⚡ Thao tác nhanh");
        headerLabel.setFont(AppTheme.Fonts.TITLE);
        headerLabel.setForeground(AppTheme.Text.PRIMARY);
        card.add(headerLabel, BorderLayout.NORTH);

        // Action buttons
        JPanel actionsPanel = new JPanel(new GridLayout(2, 2, AppTheme.Spacing.MD, AppTheme.Spacing.MD));
        actionsPanel.setOpaque(false);

        actionsPanel.add(createActionButton("🏢", "Thêm phòng", AppTheme.Primary.MAIN, () -> {
            // Navigate to room panel
            Container parent = getParent();
            if (parent instanceof JTabbedPane tabs) {
                tabs.setSelectedIndex(1); // Room panel tab
            }
        }));

        actionsPanel.add(createActionButton("+", "Đặt phòng mới", AppTheme.Status.SUCCESS, () -> {
            Container parent = getParent();
            if (parent instanceof JTabbedPane tabs) {
                tabs.setSelectedIndex(2); // Booking panel tab
            }
        }));

        actionsPanel.add(createActionButton("★", "Thêm khách hàng", AppTheme.Secondary.MAIN, () -> {
            Container parent = getParent();
            if (parent instanceof JTabbedPane tabs) {
                tabs.setSelectedIndex(3); // Customer panel tab
            }
        }));

        actionsPanel.add(createActionButton("▶", "Xem báo cáo", AppTheme.Status.WARNING, () -> {
            Container parent = getParent();
            if (parent instanceof JTabbedPane tabs) {
                tabs.setSelectedIndex(4); // Reports panel tab
            }
        }));

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.setBorder(new EmptyBorder(AppTheme.Spacing.MD, 0, 0, 0));
        centerWrapper.add(actionsPanel, BorderLayout.NORTH);

        card.add(centerWrapper, BorderLayout.CENTER);

        return card;
    }

    private JButton createActionButton(String icon, String text, Color color, Runnable action) {
        JButton button = new JButton("<html><center>" + icon + "<br>" + text + "</center></html>");
        button.setFont(AppTheme.Fonts.SMALL);
        button.setBackground(AppTheme.Background.PAPER);
        button.setForeground(AppTheme.Text.PRIMARY);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                new EmptyBorder(AppTheme.Spacing.MD, AppTheme.Spacing.SM, AppTheme.Spacing.MD, AppTheme.Spacing.SM)));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 80));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(AppTheme.Background.PAPER);
            }
        });

        button.addActionListener(e -> action.run());

        return button;
    }

    public void refreshData() {
        // Room stats
        int totalRooms = roomManager.count();
        int availableRooms = roomManager.findAvailableRooms().size();
        int occupiedRooms = roomManager.findByStatus(RoomStatus.OCCUPIED).size();
        int maintenanceRooms = roomManager.findByStatus(RoomStatus.MAINTENANCE).size();

        totalRoomsLabel.setText(String.valueOf(totalRooms));
        availableRoomsLabel.setText(availableRooms + " trống");
        occupiedRoomsLabel.setText(String.valueOf(occupiedRooms));
        maintenanceRoomsLabel.setText(maintenanceRooms + " bảo trì");

        // Booking stats
        if (bookingManager != null) {
            int totalBookings = bookingManager.getTotalBookings();
            int pendingBookings = bookingManager.getBookingsByStatus(BookingStatus.PENDING).size();
            totalBookingsLabel.setText(String.valueOf(totalBookings));
            pendingBookingsLabel.setText(pendingBookings + " chờ xác nhận");
        }

        // Customer stats
        if (customerManager != null) {
            int totalCustomers = customerManager.count();
            totalCustomersLabel.setText(String.valueOf(totalCustomers));
        }

        // Revenue
        if (invoiceManager != null) {
            double totalRevenue = invoiceManager.getTotalRevenue();
            todayRevenueLabel.setText(String.format("%,.0f VND", totalRevenue));
        }
    }
}
