package com.hotel.ui;

import com.hotel.model.booking.Booking;
import com.hotel.model.enums.BookingStatus;
import com.hotel.model.enums.RoomStatus;
import com.hotel.model.room.Room;
import com.hotel.service.BookingManager;
import com.hotel.service.RoomManager;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Dialog để sửa thông tin đặt phòng
 */
public class EditBookingDialog extends JDialog {
    private JSpinner checkInSpinner, checkOutSpinner;
    private JComboBox<String> statusCombo;
    private BookingManager bookingManager;
    private RoomManager roomManager;
    private BookingPanel parentPanel;
    private Booking booking;
    
    public EditBookingDialog(BookingPanel parentPanel, BookingManager bookingManager, RoomManager roomManager, Booking booking) {
        super((Frame) SwingUtilities.getWindowAncestor(parentPanel), "Sửa đặt phòng", true);
        this.parentPanel = parentPanel;
        this.bookingManager = bookingManager;
        this.roomManager = roomManager;
        this.booking = booking;
        
        initializeUI();
        setSize(500, 300);
        setLocationRelativeTo(null);
    }
    
    private void initializeUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Booking ID (read-only)
        mainPanel.add(new JLabel("ID:"));
        JTextField idField = new JTextField(booking.getBookingId());
        idField.setEditable(false);
        mainPanel.add(idField);
        
        // Check-in
        mainPanel.add(new JLabel("Check-in:"));
        SpinnerDateModel checkInModel = new SpinnerDateModel();
        checkInModel.setValue(toDate(booking.getCheckInDate()));
        checkInSpinner = new JSpinner(checkInModel);
        checkInSpinner.setEditor(new JSpinner.DateEditor(checkInSpinner, "dd/MM/yyyy"));
        mainPanel.add(checkInSpinner);
        
        // Check-out
        mainPanel.add(new JLabel("Check-out:"));
        SpinnerDateModel checkOutModel = new SpinnerDateModel();
        checkOutModel.setValue(toDate(booking.getCheckOutDate()));
        checkOutSpinner = new JSpinner(checkOutModel);
        checkOutSpinner.setEditor(new JSpinner.DateEditor(checkOutSpinner, "dd/MM/yyyy"));
        mainPanel.add(checkOutSpinner);
        
        // Status
        mainPanel.add(new JLabel("Trạng thái:"));
        statusCombo = new JComboBox<>(new String[]{"Chờ xác nhận", "Đã xác nhận", "Đã nhận phòng", "Đã trả phòng", "Hủy bỏ"});
        statusCombo.setSelectedIndex(booking.getStatus().ordinal());
        mainPanel.add(statusCombo);
        
        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton saveBtn = new JButton("Lưu");
        saveBtn.addActionListener(e -> updateBooking());
        buttonPanel.add(saveBtn);
        
        JButton cancelBtn = new JButton("Hủy");
        cancelBtn.addActionListener(e -> dispose());
        buttonPanel.add(cancelBtn);
        
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void updateBooking() {
        LocalDate checkIn = toLocalDate((Date) checkInSpinner.getValue());
        LocalDate checkOut = toLocalDate((Date) checkOutSpinner.getValue());

        if (checkIn == null || checkOut == null || !checkOut.isAfter(checkIn)) {
            JOptionPane.showMessageDialog(this, "Ngày check-out phải sau ngày check-in!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        booking.setCheckInDate(checkIn);
        booking.setCheckOutDate(checkOut);
        booking.setStatus(BookingStatus.values()[statusCombo.getSelectedIndex()]);

        syncRoomStatus(booking);
        
        if (bookingManager.update(booking)) {
            JOptionPane.showMessageDialog(this, "Cập nhật đặt phòng thành công!");
            parentPanel.refreshData();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật đặt phòng thất bại!", "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void syncRoomStatus(Booking booking) {
        if (booking == null || booking.getRoom() == null || roomManager == null) return;
        Room room = roomManager.getById(booking.getRoom().getRoomId());
        if (room == null) return;

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

    private Date toDate(LocalDate date) {
        if (date == null) return new Date();
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private LocalDate toLocalDate(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
