package com.hotel.model.booking;

import com.hotel.model.customer.Customer;
import com.hotel.model.enums.BookingStatus;
import com.hotel.model.room.Room;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Lớp đại diện cho đặt phòng
 * Contains booking information including customer, room, dates, and status
 */
public class Booking {
    private String bookingId;
    private Customer customer;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private BookingStatus status;
    private double totalPrice;
    private String notes;

    /**
     * Constructor đầy đủ thông tin
     */
    public Booking(String bookingId, Customer customer, Room room,
                   LocalDate checkInDate, LocalDate checkOutDate, BookingStatus status) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = status;
        this.totalPrice = calculateTotalPrice();
        this.notes = "";
    }

    /**
     * Tính tổng giá tiền dựa trên số ngày và giá phòng
     */
    public double calculateTotalPrice() {
        if (checkInDate != null && checkOutDate != null && room != null) {
            long days = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
            if (days <= 0) days = 1;
            return room.calculatePrice((int) days);
        }
        return 0;
    }

    /**
     * Lấy số ngày đặt phòng
     */
    public long getNumberOfDays() {
        if (checkInDate != null && checkOutDate != null) {
            long days = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
            return days > 0 ? days : 1;
        }
        return 0;
    }

    /**
     * Kiểm tra xem booking có hợp lệ không
     */
    public boolean isValid() {
        return customerId() != null && room != null &&
               checkInDate != null && checkOutDate != null &&
               checkOutDate.isAfter(checkInDate);
    }

    // Getters and Setters
    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        this.totalPrice = calculateTotalPrice();
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
        this.totalPrice = calculateTotalPrice();
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
        this.totalPrice = calculateTotalPrice();
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
        this.totalPrice = calculateTotalPrice();
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    private String customerId() {
        return customer != null ? customer.getCustomerId() : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(bookingId, booking.bookingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId='" + bookingId + '\'' +
                ", customer=" + customer.getFullName() +
                ", room=" + room.getRoomId() +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", status=" + status +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
