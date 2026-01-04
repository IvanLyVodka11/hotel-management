package com.hotel.service;

import com.hotel.model.booking.Booking;
import com.hotel.model.customer.Customer;
import com.hotel.model.enums.BookingStatus;
import com.hotel.model.room.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Unit tests for BookingManager service
 * Tests booking operations and room availability
 */
class BookingManagerTest {

    private BookingManager bookingManager;
    private RoomManager roomManager;
    private Customer customer;
    private Room room;

    @BeforeEach
    void setUp() {
        RoomManager.resetInstance();
        roomManager = RoomManager.getInstance();
        bookingManager = new BookingManager(roomManager);

        // Create test room
        room = new StandardRoom("R101", 1);
        roomManager.add(room);

        // Create test customer with full constructor
        customer = new Customer(
                "C001", // customerId
                "Test Customer", // fullName
                "test@email.com", // email
                "0123456789", // phoneNumber
                "123456789012", // idCard
                "123 Test Street", // address
                LocalDate.now(), // registrationDate
                false // isVIP
        );
    }

    @AfterEach
    void tearDown() {
        bookingManager.clear();
        roomManager.clear();
    }

    // ==================== CRUD Tests ====================

    @Test
    @DisplayName("BookingManager - add() should add valid booking")
    void addBooking() {
        Booking booking = createTestBooking("B001", 1, 3);

        boolean result = bookingManager.add(booking);

        assertTrue(result);
        assertEquals(1, bookingManager.count());
    }

    @Test
    @DisplayName("BookingManager - add() duplicate ID should fail")
    void addDuplicateBooking() {
        Booking booking1 = createTestBooking("B001", 1, 3);
        Booking booking2 = createTestBooking("B001", 5, 7);

        assertTrue(bookingManager.add(booking1));
        assertFalse(bookingManager.add(booking2));
    }

    @Test
    @DisplayName("BookingManager - getById() should return correct booking")
    void getById() {
        Booking booking = createTestBooking("B001", 1, 3);
        bookingManager.add(booking);

        Booking found = bookingManager.getById("B001");

        assertNotNull(found);
        assertEquals("B001", found.getBookingId());
    }

    @Test
    @DisplayName("BookingManager - delete() should remove booking")
    void deleteBooking() {
        Booking booking = createTestBooking("B001", 1, 3);
        bookingManager.add(booking);

        boolean result = bookingManager.delete("B001");

        assertTrue(result);
        assertEquals(0, bookingManager.count());
    }

    // ==================== Room Availability Tests ====================

    @Test
    @DisplayName("BookingManager - isRoomAvailable() should return true for free dates")
    void isRoomAvailableForFreeDates() {
        Booking booking = createTestBooking("B001", 1, 3);
        bookingManager.add(booking);

        // Check dates after the booking
        boolean available = bookingManager.isRoomAvailable(
                room,
                LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(7));

        assertTrue(available);
    }

    @Test
    @DisplayName("BookingManager - isRoomAvailable() should return false for occupied dates")
    void isRoomAvailableForOccupiedDates() {
        Booking booking = createTestBooking("B001", 1, 5);
        bookingManager.add(booking);

        // Check overlapping dates
        boolean available = bookingManager.isRoomAvailable(
                room,
                LocalDate.now().plusDays(2),
                LocalDate.now().plusDays(4));

        assertFalse(available);
    }

    @Test
    @DisplayName("BookingManager - getAvailableRooms() should return rooms not booked")
    void getAvailableRooms() {
        Room room2 = new VIPRoom("V201", 2);
        roomManager.add(room2);

        // Book the first room
        Booking booking = createTestBooking("B001", 1, 5);
        bookingManager.add(booking);

        List<Room> available = bookingManager.getAvailableRooms(
                LocalDate.now().plusDays(2),
                LocalDate.now().plusDays(4));

        assertEquals(1, available.size());
        assertEquals("V201", available.get(0).getRoomId());
    }

    // ==================== Filter Tests ====================

    @Test
    @DisplayName("BookingManager - getBookingsByStatus() should filter correctly")
    void getBookingsByStatus() {
        Booking booking1 = createTestBooking("B001", 1, 3);
        Booking booking2 = createTestBooking("B002", 5, 7);
        booking2.setStatus(BookingStatus.CONFIRMED);

        bookingManager.add(booking1);
        bookingManager.add(booking2);

        List<Booking> pending = bookingManager.getBookingsByStatus(BookingStatus.PENDING);
        List<Booking> confirmed = bookingManager.getBookingsByStatus(BookingStatus.CONFIRMED);

        assertEquals(1, pending.size());
        assertEquals(1, confirmed.size());
    }

    @Test
    @DisplayName("BookingManager - getCustomerBookings() should filter by customer")
    void getCustomerBookings() {
        Customer customer2 = new Customer(
                "C002", "Another Customer", "other@email.com", "0987654321",
                "987654321012", "456 Other Street", LocalDate.now(), false);

        Booking booking1 = createTestBooking("B001", 1, 3);
        Booking booking2 = new Booking("B002", customer2, room,
                LocalDate.now().plusDays(5), LocalDate.now().plusDays(7), BookingStatus.PENDING);

        bookingManager.add(booking1);
        bookingManager.add(booking2);

        List<Booking> customerBookings = bookingManager.getCustomerBookings("C001");

        assertEquals(1, customerBookings.size());
        assertEquals("B001", customerBookings.get(0).getBookingId());
    }

    // ==================== Revenue Tests ====================

    @Test
    @DisplayName("BookingManager - getTotalRevenue() should sum completed bookings")
    void getTotalRevenue() {
        Booking booking1 = createTestBooking("B001", 1, 3);
        booking1.setStatus(BookingStatus.CHECKED_OUT);
        booking1.setTotalPrice(1500000);

        Booking booking2 = createTestBooking("B002", 5, 7);
        booking2.setStatus(BookingStatus.CHECKED_OUT);
        booking2.setTotalPrice(1000000);

        Booking booking3 = createTestBooking("B003", 10, 12);
        booking3.setStatus(BookingStatus.PENDING); // Not completed
        booking3.setTotalPrice(500000);

        bookingManager.add(booking1);
        bookingManager.add(booking2);
        bookingManager.add(booking3);

        double revenue = bookingManager.getTotalRevenue();

        assertEquals(2500000, revenue, 0.01);
    }

    // ==================== Search Tests ====================

    @Test
    @DisplayName("BookingManager - search() should find by customer name")
    void searchByCustomerName() {
        Booking booking = createTestBooking("B001", 1, 3);
        bookingManager.add(booking);

        List<Booking> results = bookingManager.search("Test");

        assertEquals(1, results.size());
    }

    @Test
    @DisplayName("BookingManager - search() should find by room ID")
    void searchByRoomId() {
        Booking booking = createTestBooking("B001", 1, 3);
        bookingManager.add(booking);

        List<Booking> results = bookingManager.search("R101");

        assertEquals(1, results.size());
    }

    // ==================== Helper Methods ====================

    private Booking createTestBooking(String bookingId, int checkInDays, int checkOutDays) {
        return new Booking(
                bookingId,
                customer,
                room,
                LocalDate.now().plusDays(checkInDays),
                LocalDate.now().plusDays(checkOutDays),
                BookingStatus.PENDING);
    }
}
