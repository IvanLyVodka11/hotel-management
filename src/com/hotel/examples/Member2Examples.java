package com.hotel.examples;

import com.hotel.model.booking.Booking;
import com.hotel.model.customer.Customer;
import com.hotel.model.enums.BookingStatus;
import com.hotel.model.invoice.Invoice;
import com.hotel.model.invoice.Invoice.InvoiceStatus;
import com.hotel.model.room.Room;
import com.hotel.service.BookingManager;
import com.hotel.service.CustomerManager;
import com.hotel.service.InvoiceManager;
import com.hotel.service.RoomManager;
import com.hotel.storage.DataStorage;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ví dụ sử dụng các tính năng của Thành viên 2
 * (Booking, Customer, Invoice Management)
 */
public class Member2Examples {

    public static void main(String[] args) {
        System.out.println("=== HOTEL MANAGEMENT SYSTEM - MEMBER 2 EXAMPLES ===\n");

        // Initialize managers
        CustomerManager customerManager = new CustomerManager();
        BookingManager bookingManager = new BookingManager();
        InvoiceManager invoiceManager = new InvoiceManager();
        RoomManager roomManager = null; // Will be initialized separately

        // Set manager relationships
        bookingManager.setRoomManager(roomManager);

        // Initialize storage
        DataStorage storage = new DataStorage(customerManager, bookingManager, 
                                             invoiceManager, roomManager);

        // Load existing data
        storage.loadAllData();

        // Run examples
        exampleCustomerManagement(customerManager, storage);
        exampleBookingManagement(customerManager, bookingManager, roomManager, storage);
        exampleInvoiceManagement(bookingManager, invoiceManager, storage);
        exampleAvailabilityCheck(bookingManager, roomManager);
        exampleReporting(bookingManager, invoiceManager);

        // Save all changes
        storage.saveAllData();
    }

    /**
     * Ví dụ 1: Quản lý khách hàng
     */
    public static void exampleCustomerManagement(CustomerManager customerManager, DataStorage storage) {
        System.out.println("--- EXAMPLE 1: Customer Management ---\n");

        // Create new customers
        Customer customer1 = new Customer(
            "CUST001",
            "Nguyễn Văn A",
            "nguyenvana@email.com",
            "0123456789",
            "012345678901",
            "123 Đường ABC, TP HCM",
            LocalDate.of(2025, 12, 1),
            false
        );

        Customer customer2 = new Customer(
            "CUST002",
            "Trần Thị B",
            "tranthb@email.com",
            "0987654321",
            "123456789012",
            "456 Đường XYZ, Hà Nội",
            LocalDate.of(2025, 11, 15),
            true  // VIP customer
        );

        // Add customers
        customerManager.add(customer1);
        customerManager.add(customer2);
        System.out.println("✓ Đã thêm 2 khách hàng\n");

        // Add loyalty points
        customer1.addLoyaltyPoints(500);
        customerManager.update(customer1);
        System.out.println("✓ Thêm loyalty points cho " + customer1.getFullName() + ": " + 
                          customer1.getLoyaltyPoints() + " points\n");

        // Search customers
        List<Customer> searchResults = customerManager.search("Nguyễn");
        System.out.println("✓ Tìm kiếm 'Nguyễn': " + searchResults.size() + " kết quả\n");

        // Filter VIP customers
        Map<String, Object> vipFilter = new HashMap<>();
        vipFilter.put("vip", true);
        List<Customer> vipCustomers = customerManager.filter(vipFilter);
        System.out.println("✓ Khách hàng VIP: " + vipCustomers.size());
        for (Customer customer : vipCustomers) {
            System.out.println("  - " + customer.getFullName());
        }
        System.out.println();

        // Statistics
        System.out.println("✓ Thống kê khách hàng:");
        System.out.println("  Tổng: " + customerManager.getTotalCustomers());
        System.out.println("  VIP: " + customerManager.getVIPCustomers());
        System.out.println();

        // Save customers
        storage.saveCustomers();
    }

    /**
     * Ví dụ 2: Quản lý đặt phòng
     */
    public static void exampleBookingManagement(CustomerManager customerManager, 
                                               BookingManager bookingManager,
                                               RoomManager roomManager,
                                               DataStorage storage) {
        System.out.println("--- EXAMPLE 2: Booking Management ---\n");

        // Get existing customer and room
        Customer customer = customerManager.getById("CUST001");
        Room room = roomManager.getById("R101");

        if (customer == null || room == null) {
            System.out.println("✗ Customer hoặc Room không tồn tại\n");
            return;
        }

        // Create booking
        LocalDate checkIn = LocalDate.of(2025, 12, 20);
        LocalDate checkOut = LocalDate.of(2025, 12, 23);

        Booking booking = new Booking(
            "BK001",
            customer,
            room,
            checkIn,
            checkOut,
            BookingStatus.PENDING
        );
        booking.setNotes("Yêu cầu phòng view biển, không hút thuốc");

        bookingManager.add(booking);
        System.out.println("✓ Đã tạo booking:");
        System.out.println("  ID: " + booking.getBookingId());
        System.out.println("  Khách: " + customer.getFullName());
        System.out.println("  Phòng: " + room.getRoomId());
        System.out.println("  Check-in: " + checkIn);
        System.out.println("  Check-out: " + checkOut);
        System.out.println("  Số đêm: " + booking.getNumberOfDays());
        System.out.println("  Giá: " + booking.getTotalPrice() + " VND\n");

        // Update booking status
        booking.setStatus(BookingStatus.CONFIRMED);
        bookingManager.update(booking);
        System.out.println("✓ Cập nhật trạng thái thành CONFIRMED\n");

        // Get bookings by customer
        List<Booking> customerBookings = bookingManager.getCustomerBookings("CUST001");
        System.out.println("✓ Booking của " + customer.getFullName() + ": " + 
                          customerBookings.size() + " bookings\n");

        // Save bookings
        storage.saveBookings();
    }

    /**
     * Ví dụ 3: Quản lý hóa đơn
     */
    public static void exampleInvoiceManagement(BookingManager bookingManager,
                                                InvoiceManager invoiceManager,
                                                DataStorage storage) {
        System.out.println("--- EXAMPLE 3: Invoice Management ---\n");

        // Get existing booking
        Booking booking = bookingManager.getById("BK001");
        if (booking == null) {
            System.out.println("✗ Booking không tồn tại\n");
            return;
        }

        // Create invoice from booking
        Invoice invoice = invoiceManager.createInvoiceFromBooking(booking, "INV001");
        System.out.println("✓ Đã tạo hóa đơn từ booking:");
        System.out.println("  ID: " + invoice.getInvoiceId());
        System.out.println("  Booking: " + booking.getBookingId());
        System.out.println("  Subtotal: " + invoice.getSubtotal() + " VND");
        System.out.println("  Thuế (10%): " + invoice.getTaxAmount() + " VND");
        System.out.println("  Tổng cộng: " + invoice.getTotalAmount() + " VND");
        System.out.println("  Trạng thái: " + invoice.getStatus().getDisplayName() + "\n");

        // Mark invoice as issued
        invoiceManager.update(invoice);
        invoice.markAsIssued();
        invoiceManager.update(invoice);
        System.out.println("✓ Đánh dấu hóa đơn là 'Đã phát hành'\n");

        // Mark invoice as paid
        invoiceManager.markInvoiceAsPaid("INV001");
        System.out.println("✓ Đánh dấu hóa đơn là 'Đã thanh toán'\n");

        // Get invoices by customer
        List<Invoice> customerInvoices = invoiceManager.getInvoicesByCustomer("CUST001");
        System.out.println("✓ Hóa đơn của khách hàng: " + customerInvoices.size() + " hóa đơn\n");

        // Get invoices by status
        List<Invoice> paidInvoices = invoiceManager.getInvoicesByStatus(InvoiceStatus.PAID);
        System.out.println("✓ Hóa đơn đã thanh toán: " + paidInvoices.size() + " hóa đơn\n");

        // Save invoices
        storage.saveInvoices();
    }

    /**
     * Ví dụ 4: Kiểm tra phòng trống
     */
    public static void exampleAvailabilityCheck(BookingManager bookingManager, 
                                               RoomManager roomManager) {
        System.out.println("--- EXAMPLE 4: Availability Check ---\n");

        Room room = roomManager.getById("R101");
        if (room == null) {
            System.out.println("✗ Room không tồn tại\n");
            return;
        }

        LocalDate checkIn1 = LocalDate.of(2025, 12, 20);
        LocalDate checkOut1 = LocalDate.of(2025, 12, 23);

        boolean available1 = bookingManager.isRoomAvailable(room, checkIn1, checkOut1);
        System.out.println("✓ Kiểm tra phòng " + room.getRoomId() + ":");
        System.out.println("  Từ " + checkIn1 + " đến " + checkOut1);
        System.out.println("  Trạng thái: " + (available1 ? "TRỐNG" : "ĐÃ ĐẶT") + "\n");

        // Check different dates
        LocalDate checkIn2 = LocalDate.of(2025, 12, 24);
        LocalDate checkOut2 = LocalDate.of(2025, 12, 26);

        boolean available2 = bookingManager.isRoomAvailable(room, checkIn2, checkOut2);
        System.out.println("✓ Kiểm tra phòng " + room.getRoomId() + ":");
        System.out.println("  Từ " + checkIn2 + " đến " + checkOut2);
        System.out.println("  Trạng thái: " + (available2 ? "TRỐNG" : "ĐÃ ĐẶT") + "\n");

        // Get available rooms for date range
        List<Room> availableRooms = bookingManager.getAvailableRooms(checkIn2, checkOut2);
        System.out.println("✓ Phòng trống từ " + checkIn2 + " đến " + checkOut2 + ":");
        System.out.println("  Tìm thấy " + availableRooms.size() + " phòng\n");
    }

    /**
     * Ví dụ 5: Báo cáo doanh thu
     */
    public static void exampleReporting(BookingManager bookingManager, 
                                       InvoiceManager invoiceManager) {
        System.out.println("--- EXAMPLE 5: Revenue Reporting ---\n");

        // Booking revenue
        double bookingRevenue = bookingManager.getTotalRevenue();
        System.out.println("✓ Tổng doanh thu (Bookings): " + bookingRevenue + " VND");

        int completedBookings = bookingManager.getCompletedBookings();
        System.out.println("  Booking hoàn thành: " + completedBookings + "\n");

        // Invoice revenue
        double invoiceRevenue = invoiceManager.getTotalRevenue();
        System.out.println("✓ Tổng doanh thu (Invoices): " + invoiceRevenue + " VND");

        double totalTax = invoiceManager.getTotalTax();
        System.out.println("  Tổng thuế: " + totalTax + " VND");

        double unpaidRevenue = invoiceManager.getUnpaidRevenue();
        System.out.println("  Doanh thu chưa thanh toán: " + unpaidRevenue + " VND\n");

        // Invoice statistics
        int paidCount = invoiceManager.getPaidInvoices();
        int unpaidCount = invoiceManager.getUnpaidInvoices();
        System.out.println("✓ Thống kê hóa đơn:");
        System.out.println("  Đã thanh toán: " + paidCount);
        System.out.println("  Chưa thanh toán: " + unpaidCount + "\n");

        // Monthly revenue
        double decemberRevenue = bookingManager.getMonthlyRevenue(12, 2025);
        System.out.println("✓ Doanh thu tháng 12/2025: " + decemberRevenue + " VND\n");
    }
}
