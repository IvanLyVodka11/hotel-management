package com.hotel.ui;

import com.hotel.auth.PermissionManager.Permission;
import com.hotel.auth.UserSession;
import com.hotel.model.booking.Booking;
import com.hotel.model.enums.BookingStatus;
import com.hotel.model.enums.RoomStatus;
import com.hotel.model.room.Room;
import com.hotel.service.BookingManager;
import com.hotel.service.CustomerManager;
import com.hotel.service.RoomManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Panel quản lý đặt phòng với CRUD operations
 * 
 * @author Member2
 * @version 1.0
 */
public class BookingPanel extends JPanel {

    // ==================== CONSTANTS ====================

    private static final String[] COLUMN_NAMES = {
            "ID", "Khách hàng", "Phòng", "Check-in", "Check-out", "Trạng thái", "Tổng (VND)"
    };

    // ==================== COMPONENTS ====================

    private JTable bookingTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> statusFilter;
    private JLabel statusLabel;

    // ==================== SERVICES ====================

    private final BookingManager bookingManager;
    private final CustomerManager customerManager;
    private final RoomManager roomManager;

    // ==================== CONSTRUCTOR ====================

    public BookingPanel() {
        this(new CustomerManager(), new BookingManager(RoomManager.getInstance()), RoomManager.getInstance());
    }

    public BookingPanel(CustomerManager customerManager, BookingManager bookingManager, RoomManager roomManager) {
        this.customerManager = customerManager;
        this.bookingManager = bookingManager;
        this.roomManager = roomManager;
        this.bookingManager.setRoomManager(roomManager);

        initializeUI();
        loadData();
    }

    // ==================== UI INITIALIZATION ====================

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Top panel - Search & Filter
        add(createTopPanel(), BorderLayout.NORTH);

        // Center - Table
        add(createTablePanel(), BorderLayout.CENTER);

        // Bottom - Buttons
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(10, 5));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Tim kiem:"));
        searchField = new JTextField(15);
        searchField.addActionListener(e -> performSearch());
        searchPanel.add(searchField);

        JButton searchBtn = new JButton("Tìm");
        searchBtn.addActionListener(e -> performSearch());
        searchPanel.add(searchBtn);

        // Status filter
        searchPanel.add(new JLabel("| Trạng thái:"));
        statusFilter = new JComboBox<>(
                new String[] { "Tất cả", "Chờ xác nhận", "Đã xác nhận", "Đã nhận phòng", "Đã trả phòng", "Đã hủy" });
        statusFilter.addActionListener(e -> filterByStatus());
        searchPanel.add(statusFilter);

        topPanel.add(searchPanel, BorderLayout.CENTER);

        return topPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());

        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bookingTable = new JTable(tableModel);
        bookingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookingTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(bookingTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Kiểm tra quyền
        UserSession session = UserSession.getInstance();
        boolean canCreate = session.hasPermission(Permission.CREATE_BOOKING);
        boolean canManage = session.hasPermission(Permission.MANAGE_BOOKINGS);

        JButton addBtn = new JButton("[+] Thêm");
        addBtn.addActionListener(e -> openAddDialog());
        addBtn.setEnabled(canCreate);
        if (!canCreate)
            addBtn.setToolTipText("Bạn không có quyền đặt phòng");
        buttonPanel.add(addBtn);

        JButton editBtn = new JButton("[S] Sửa");
        editBtn.addActionListener(e -> openEditDialog());
        editBtn.setEnabled(canManage);
        if (!canManage)
            editBtn.setToolTipText("Bạn không có quyền sửa đặt phòng");
        buttonPanel.add(editBtn);

        JButton deleteBtn = new JButton("[X] Xóa");
        deleteBtn.addActionListener(e -> deleteBooking());
        deleteBtn.setEnabled(canManage);
        if (!canManage)
            deleteBtn.setToolTipText("Bạn không có quyền xóa đặt phòng");
        buttonPanel.add(deleteBtn);

        JButton confirmBtn = new JButton("[OK] Xác nhận");
        confirmBtn.addActionListener(e -> confirmBooking());
        confirmBtn.setEnabled(canCreate); // Staff có thể xác nhận
        buttonPanel.add(confirmBtn);

        JButton reportBtn = new JButton("Báo cáo");
        reportBtn.addActionListener(e -> showReports());
        reportBtn.setEnabled(session.hasPermission(Permission.VIEW_REPORTS));
        buttonPanel.add(reportBtn);

        JButton refreshBtn = new JButton("[R] Làm mới");
        refreshBtn.addActionListener(e -> loadData());
        buttonPanel.add(refreshBtn);

        // Status label
        statusLabel = new JLabel("Tổng: 0 đặt phòng");

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(buttonPanel, BorderLayout.CENTER);
        southPanel.add(statusLabel, BorderLayout.SOUTH);

        return southPanel;
    }

    // ==================== DATA OPERATIONS ====================

    private void loadData() {
        tableModel.setRowCount(0);
        List<Booking> bookings = bookingManager.getAll();

        for (Booking booking : bookings) {
            Object[] row = {
                    booking.getBookingId(),
                    booking.getCustomer().getFullName(),
                    booking.getRoom().getRoomId(),
                    booking.getCheckInDate(),
                    booking.getCheckOutDate(),
                    getStatusText(booking.getStatus()),
                    String.format("%,d", (long) booking.getTotalPrice())
            };
            tableModel.addRow(row);
        }

        updateStatus();
    }

    private void performSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadData();
            return;
        }

        tableModel.setRowCount(0);
        List<Booking> results = bookingManager.search(keyword);

        for (Booking booking : results) {
            Object[] row = {
                    booking.getBookingId(),
                    booking.getCustomer().getFullName(),
                    booking.getRoom().getRoomId(),
                    booking.getCheckInDate(),
                    booking.getCheckOutDate(),
                    getStatusText(booking.getStatus()),
                    String.format("%,d", (long) booking.getTotalPrice())
            };
            tableModel.addRow(row);
        }

        statusLabel.setText("Tìm được: " + results.size() + " đặt phòng");
    }

    private void filterByStatus() {
        int selectedIndex = statusFilter.getSelectedIndex();
        if (selectedIndex == 0) {
            loadData();
            return;
        }

        BookingStatus status = BookingStatus.values()[selectedIndex - 1];
        tableModel.setRowCount(0);
        Map<String, Object> filter = new HashMap<>();
        filter.put("status", status);

        List<Booking> filtered = bookingManager.filter(filter);

        for (Booking booking : filtered) {
            Object[] row = {
                    booking.getBookingId(),
                    booking.getCustomer().getFullName(),
                    booking.getRoom().getRoomId(),
                    booking.getCheckInDate(),
                    booking.getCheckOutDate(),
                    getStatusText(booking.getStatus()),
                    String.format("%,d", (long) booking.getTotalPrice())
            };
            tableModel.addRow(row);
        }

        statusLabel.setText("Trạng thái: " + filtered.size() + " đặt phòng");
    }

    private void updateStatus() {
        int total = bookingManager.getTotalBookings();
        statusLabel.setText("Tổng: " + total + " đặt phòng");
    }

    private String getStatusText(BookingStatus status) {
        return switch (status) {
            case PENDING -> "Chờ xác nhận";
            case CONFIRMED -> "Đã xác nhận";
            case CHECKED_IN -> "Đã nhận phòng";
            case CHECKED_OUT -> "Đã trả phòng";
            case CANCELLED -> "Hủy bỏ";
        };
    }

    // ==================== DIALOG OPERATIONS ====================

    private void openAddDialog() {
        new AddBookingDialog(this, bookingManager, customerManager, roomManager).setVisible(true);
    }

    public void openAddDialogFromMenu() {
        openAddDialog();
    }

    private void openEditDialog() {
        int selectedRow = bookingTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Chọn đặt phòng cần sửa!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String bookingId = (String) tableModel.getValueAt(selectedRow, 0);
        Booking booking = bookingManager.getById(bookingId);

        if (booking != null) {
            new EditBookingDialog(this, bookingManager, roomManager, booking).setVisible(true);
        }
    }

    private void deleteBooking() {
        int selectedRow = bookingTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Chọn đặt phòng cần xóa!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String bookingId = (String) tableModel.getValueAt(selectedRow, 0);
        Booking booking = bookingManager.getById(bookingId);

        int option = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa đặt phòng này?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            if (bookingManager.delete(bookingId)) {
                syncRoomStatusAfterDelete(booking);
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void confirmBooking() {
        int selectedRow = bookingTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Chọn đặt phòng cần xác nhận!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String bookingId = (String) tableModel.getValueAt(selectedRow, 0);
        Booking booking = bookingManager.getById(bookingId);

        if (booking != null && booking.getStatus() == BookingStatus.PENDING) {
            booking.setStatus(BookingStatus.CONFIRMED);
            syncRoomStatusForBooking(booking);
            if (bookingManager.update(booking)) {
                JOptionPane.showMessageDialog(this, "Xác nhận thành công!");
                loadData();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Chỉ có thể xác nhận đặt phòng chờ!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showReports() {
        double totalRevenue = bookingManager.getTotalRevenue();
        int totalBookings = bookingManager.getTotalBookings();
        int completedBookings = bookingManager.getCompletedBookings();

        int pending = bookingManager.getBookingsByStatus(BookingStatus.PENDING).size();
        int confirmed = bookingManager.getBookingsByStatus(BookingStatus.CONFIRMED).size();
        int checkedIn = bookingManager.getBookingsByStatus(BookingStatus.CHECKED_IN).size();
        int checkedOut = bookingManager.getBookingsByStatus(BookingStatus.CHECKED_OUT).size();
        int cancelled = bookingManager.getBookingsByStatus(BookingStatus.CANCELLED).size();

        java.time.LocalDate now = java.time.LocalDate.now();
        int month = now.getMonthValue();
        int year = now.getYear();
        double monthlyRevenue = bookingManager.getMonthlyRevenue(month, year);

        String report = String.format(
                "=== BAO CAO DAT PHONG ===\n\n" +
                        "Tổng booking: %d\n" +
                        "Hoàn thành (đã trả phòng): %d\n" +
                        "Tổng doanh thu (booking hoàn thành): %,.0f VND\n" +
                        "Doanh thu bình quân/booking hoàn thành: %,.0f VND\n" +
                        "Doanh thu tháng %02d/%d: %,.0f VND\n\n" +
                        "Trạng thái booking:\n" +
                        "- %s: %d\n" +
                        "- %s: %d\n" +
                        "- %s: %d\n" +
                        "- %s: %d\n" +
                        "- %s: %d\n",
                totalBookings,
                completedBookings,
                totalRevenue,
                completedBookings > 0 ? totalRevenue / completedBookings : 0,
                month,
                year,
                monthlyRevenue,
                BookingStatus.PENDING.getDisplayName(), pending,
                BookingStatus.CONFIRMED.getDisplayName(), confirmed,
                BookingStatus.CHECKED_IN.getDisplayName(), checkedIn,
                BookingStatus.CHECKED_OUT.getDisplayName(), checkedOut,
                BookingStatus.CANCELLED.getDisplayName(), cancelled);

        JOptionPane.showMessageDialog(this, report, "Báo cáo", JOptionPane.INFORMATION_MESSAGE);
    }

    // ==================== PUBLIC METHODS ====================

    public void refreshData() {
        loadData();
    }

    private void syncRoomStatusForBooking(Booking booking) {
        if (booking == null || booking.getRoom() == null)
            return;
        Room room = roomManager.getById(booking.getRoom().getRoomId());
        if (room == null)
            return;

        BookingStatus status = booking.getStatus();
        if (status == BookingStatus.CANCELLED || status == BookingStatus.CHECKED_OUT) {
            room.setStatus(RoomStatus.AVAILABLE);
        } else if (status == BookingStatus.CHECKED_IN) {
            room.setStatus(RoomStatus.OCCUPIED);
        } else {
            room.setStatus(RoomStatus.RESERVED);
        }
        roomManager.update(room);
    }

    private void syncRoomStatusAfterDelete(Booking deletedBooking) {
        if (deletedBooking == null || deletedBooking.getRoom() == null)
            return;
        String roomId = deletedBooking.getRoom().getRoomId();

        boolean hasActiveBooking = bookingManager.getAll().stream()
                .filter(b -> b.getRoom() != null && roomId.equals(b.getRoom().getRoomId()))
                .anyMatch(b -> b.getStatus() != BookingStatus.CANCELLED && b.getStatus() != BookingStatus.CHECKED_OUT);

        Room room = roomManager.getById(roomId);
        if (room == null)
            return;

        if (!hasActiveBooking) {
            room.setStatus(RoomStatus.AVAILABLE);
            roomManager.update(room);
        }
    }
}
