package com.hotel.ui;

import com.hotel.model.booking.Booking;
import com.hotel.model.customer.Customer;
import com.hotel.model.enums.BookingStatus;
import com.hotel.model.enums.RoomStatus;
import com.hotel.model.enums.RoomType;
import com.hotel.model.room.Room;
import com.hotel.service.BookingManager;
import com.hotel.service.CustomerManager;
import com.hotel.service.RoomManager;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Dialog để thêm đặt phòng mới
 */
public class AddBookingDialog extends JDialog {
    private JComboBox<String> customerCombo, roomCombo;
    private JComboBox<RoomType> roomTypeCombo;
    private JSpinner checkInSpinner, checkOutSpinner;
    private final BookingPanel parentPanel;
    private final BookingManager bookingManager;
    private final CustomerManager customerManager;
    private final RoomManager roomManager;
    private JButton saveBtn;

    public AddBookingDialog(BookingPanel parentPanel, BookingManager bookingManager,
            CustomerManager customerManager, RoomManager roomManager) {
        super((Frame) SwingUtilities.getWindowAncestor(parentPanel), "Thêm đặt phòng", true);
        this.parentPanel = parentPanel;
        this.bookingManager = bookingManager;
        this.customerManager = customerManager;
        this.roomManager = roomManager;
        this.bookingManager.setRoomManager(roomManager);

        initializeUI();
        setSize(500, 350);
        setLocationRelativeTo(null);
    }

    private void initializeUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(6, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Customer
        mainPanel.add(new JLabel("Khách hàng:"));
        customerCombo = new JComboBox<>();
        for (Customer customer : customerManager.getAll()) {
            customerCombo.addItem(customer.getCustomerId() + " - " + customer.getFullName());
        }
        mainPanel.add(customerCombo);

        // Check-in (tạo trước Room để dùng cho filter)
        mainPanel.add(new JLabel("Check-in:"));
        SpinnerDateModel checkInModel = new SpinnerDateModel();
        checkInModel.setValue(new java.util.Date());
        checkInSpinner = new JSpinner(checkInModel);
        checkInSpinner.setEditor(new JSpinner.DateEditor(checkInSpinner, "dd/MM/yyyy"));
        mainPanel.add(checkInSpinner);

        // Check-out
        mainPanel.add(new JLabel("Check-out:"));
        SpinnerDateModel checkOutModel = new SpinnerDateModel();
        // Mặc định checkout là ngày mai
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.DAY_OF_MONTH, 1);
        checkOutModel.setValue(cal.getTime());
        checkOutSpinner = new JSpinner(checkOutModel);
        checkOutSpinner.setEditor(new JSpinner.DateEditor(checkOutSpinner, "dd/MM/yyyy"));
        mainPanel.add(checkOutSpinner);

        // Room Type (chọn loại phòng trước)
        mainPanel.add(new JLabel("Loại phòng:"));
        roomTypeCombo = new JComboBox<>(RoomType.values());
        roomTypeCombo.addActionListener(e -> refreshAvailableRooms());
        mainPanel.add(roomTypeCombo);

        // Room (tạo sau spinners)
        mainPanel.add(new JLabel("Phòng:"));
        roomCombo = new JComboBox<>();
        refreshAvailableRooms(); // Bây giờ spinners đã được khởi tạo
        mainPanel.add(roomCombo);

        // Listener để refresh rooms khi thay đổi ngày
        ChangeListener refreshRoomsListener = e -> refreshAvailableRooms();
        checkInSpinner.addChangeListener(refreshRoomsListener);
        checkOutSpinner.addChangeListener(refreshRoomsListener);

        // Buttons
        JPanel buttonPanel = new JPanel();
        saveBtn = new JButton("Lưu");
        saveBtn.addActionListener(e -> saveBooking());
        buttonPanel.add(saveBtn);

        JButton cancelBtn = new JButton("Hủy");
        cancelBtn.addActionListener(e -> dispose());
        buttonPanel.add(cancelBtn);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void refreshAvailableRooms() {
        roomCombo.removeAllItems();
        if (roomManager == null || bookingManager == null) {
            if (saveBtn != null)
                saveBtn.setEnabled(false);
            return;
        }

        LocalDate checkIn = toLocalDate((Date) checkInSpinner.getValue());
        LocalDate checkOut = toLocalDate((Date) checkOutSpinner.getValue());

        boolean datesOk = checkIn != null && checkOut != null && checkOut.isAfter(checkIn);
        if (!datesOk) {
            if (saveBtn != null)
                saveBtn.setEnabled(false);
            return;
        }

        // Lấy loại phòng đã chọn
        RoomType selectedType = (RoomType) roomTypeCombo.getSelectedItem();
        if (selectedType == null) {
            if (saveBtn != null)
                saveBtn.setEnabled(false);
            return;
        }

        // Sử dụng method mới: getAvailableRoomsByType
        // Logic đúng: Khách chọn ngày + loại phòng -> Hệ thống trả về danh sách phòng
        // thỏa mãn
        List<Room> rooms = bookingManager.getAvailableRoomsByType(checkIn, checkOut, selectedType).stream()
                .filter(r -> r.getStatus() != null && r.getStatus().canBook())
                .toList();

        for (Room room : rooms) {
            roomCombo.addItem(room.getRoomId() + " - " + room.getRoomType().getDisplayName() + " - "
                    + room.getStatus().getDisplayName());
        }

        if (saveBtn != null)
            saveBtn.setEnabled(roomCombo.getItemCount() > 0);
    }

    private LocalDate toLocalDate(Date date) {
        if (date == null)
            return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private void saveBooking() {
        if (customerCombo.getSelectedItem() == null || roomCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng và phòng!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String customerId = ((String) customerCombo.getSelectedItem()).split(" - ")[0];
            Customer customer = customerManager.getById(customerId);

            String roomId = ((String) roomCombo.getSelectedItem()).split(" - ")[0];
            Room room = roomManager.getById(roomId);

            LocalDate checkIn = toLocalDate((Date) checkInSpinner.getValue());
            LocalDate checkOut = toLocalDate((Date) checkOutSpinner.getValue());

            if (checkIn == null || checkOut == null || !checkOut.isAfter(checkIn)) {
                throw new Exception("Ngày check-out phải sau ngày check-in");
            }

            if (customer == null) {
                throw new Exception("Khách hàng không tồn tại");
            }

            if (room == null) {
                throw new Exception("Phòng không tồn tại");
            }

            if (!room.getStatus().canBook()) {
                throw new Exception("Phòng hiện không thể đặt");
            }

            if (!bookingManager.isRoomAvailable(room, checkIn, checkOut)) {
                throw new Exception("Phòng đã có đặt phòng trong khoảng thời gian này");
            }

            String bookingId = "BK-" + System.currentTimeMillis();
            Booking booking = new Booking(bookingId, customer, room, checkIn, checkOut, BookingStatus.PENDING);
            boolean added = bookingManager.add(booking);
            if (!added) {
                throw new Exception("Không thể tạo đặt phòng (dữ liệu không hợp lệ hoặc trùng ID)");
            }

            // Mark room as reserved (visual status)
            room.setStatus(RoomStatus.RESERVED);
            roomManager.update(room);

            JOptionPane.showMessageDialog(this, "Tạo đặt phòng thành công!");
            parentPanel.refreshData();
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
