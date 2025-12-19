package com.hotel.service;

import com.hotel.model.booking.Booking;

import com.hotel.model.enums.BookingStatus;
import com.hotel.model.room.Room;
import com.hotel.service.interfaces.IManageable;
import com.hotel.service.interfaces.ISearchable;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.ArrayList;

/**
 * Lớp quản lý đặt phòng
 * Handles CRUD operations and availability checking for bookings
 */
public class BookingManager implements IManageable<Booking>, ISearchable<Booking> {
    private List<Booking> bookings;
    private RoomManager roomManager;

    public BookingManager() {
        this.bookings = new ArrayList<>();
        this.roomManager = null; // Sẽ được set từ ngoài
    }

    public BookingManager(RoomManager roomManager) {
        this.bookings = new ArrayList<>();
        this.roomManager = roomManager;
    }

    public void setRoomManager(RoomManager roomManager) {
        this.roomManager = roomManager;
    }

    @Override
    public boolean add(Booking booking) {
        if (booking != null && booking.isValid() && !exists(booking.getBookingId())) {
            return bookings.add(booking);
        }
        return false;
    }

    @Override
    public boolean update(Booking booking) {
        if (booking != null) {
            for (int i = 0; i < bookings.size(); i++) {
                if (bookings.get(i).getBookingId().equals(booking.getBookingId())) {
                    bookings.set(i, booking);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean delete(String bookingId) {
        return bookings.removeIf(b -> b.getBookingId().equals(bookingId));
    }

    @Override
    public Booking getById(String bookingId) {
        return bookings.stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Booking> getAll() {
        return new ArrayList<>(bookings);
    }

    @Override
    public List<Booking> search(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return bookings.stream()
                .filter(b -> b.getBookingId().contains(keyword) ||
                        b.getCustomer().getFullName().toLowerCase().contains(lowerKeyword) ||
                        b.getRoom().getRoomId().contains(keyword))
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> filter(Map<String, Object> criteria) {
        List<Booking> result = new ArrayList<>(bookings);
        
        if (criteria.containsKey("status")) {
            BookingStatus status = (BookingStatus) criteria.get("status");
            result = result.stream()
                    .filter(b -> b.getStatus() == status)
                    .collect(Collectors.toList());
        }
        
        if (criteria.containsKey("customerId")) {
            String customerId = (String) criteria.get("customerId");
            result = result.stream()
                    .filter(b -> b.getCustomer().getCustomerId().equals(customerId))
                    .collect(Collectors.toList());
        }
        
        if (criteria.containsKey("roomId")) {
            String roomId = (String) criteria.get("roomId");
            result = result.stream()
                    .filter(b -> b.getRoom().getRoomId().equals(roomId))
                    .collect(Collectors.toList());
        }
        
        return result;
    }
    
    @Override
    public int count() {
        return bookings.size();
    }
    
    @Override
    public boolean isEmpty() {
        return bookings.isEmpty();
    }
    
    @Override
    public void clear() {
        bookings.clear();
    }

    /**
     * Kiểm tra xem phòng có sẵn cho khoảng thời gian không
     */
    public boolean isRoomAvailable(Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        return bookings.stream()
                .filter(b -> b.getRoom().getRoomId().equals(room.getRoomId()))
                .filter(b -> b.getStatus() != BookingStatus.CANCELLED)
                .noneMatch(b -> {
                    LocalDate bCheckIn = b.getCheckInDate();
                    LocalDate bCheckOut = b.getCheckOutDate();
                    return !(checkOutDate.isBefore(bCheckIn) || checkInDate.isAfter(bCheckOut));
                });
    }

    /**
     * Lấy danh sách các phòng còn trống trong khoảng thời gian
     */
    public List<Room> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate) {
        if (roomManager == null) return new ArrayList<>();
        
        return roomManager.getAll().stream()
                .filter(room -> isRoomAvailable(room, checkInDate, checkOutDate))
                .collect(Collectors.toList());
    }

    /**
     * Lấy danh sách booking theo trạng thái
     */
    public List<Booking> getBookingsByStatus(BookingStatus status) {
        return bookings.stream()
                .filter(b -> b.getStatus() == status)
                .collect(Collectors.toList());
    }

    /**
     * Lấy danh sách booking của khách hàng
     */
    public List<Booking> getCustomerBookings(String customerId) {
        return bookings.stream()
                .filter(b -> b.getCustomer().getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }

    /**
     * Tính tổng doanh thu từ các booking
     */
    public double getTotalRevenue() {
        return bookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.CHECKED_OUT)
                .mapToDouble(Booking::getTotalPrice)
                .sum();
    }

    /**
     * Tính tổng doanh thu trong tháng
     */
    public double getMonthlyRevenue(int month, int year) {
        return bookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.CHECKED_OUT &&
                        b.getCheckOutDate().getMonth().getValue() == month &&
                        b.getCheckOutDate().getYear() == year)
                .mapToDouble(Booking::getTotalPrice)
                .sum();
    }

    private boolean exists(String bookingId) {
        return bookings.stream().anyMatch(b -> b.getBookingId().equals(bookingId));
    }

    public int getTotalBookings() {
        return bookings.size();
    }

    public int getCompletedBookings() {
        return (int) bookings.stream().filter(b -> b.getStatus() == BookingStatus.CHECKED_OUT).count();
    }
}
