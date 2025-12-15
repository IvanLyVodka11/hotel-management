package com.hotel.storage;

import com.google.gson.*;
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

import java.io.*;
import java.time.LocalDate;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Lớp quản lý lưu trữ dữ liệu cho Booking, Customer, và Invoice
 * Handles JSON serialization/deserialization for all managers
 */
public class DataStorage {
    private static final String DEFAULT_DATA_DIR = getDefaultDataDir();
    private static final String CUSTOMERS_FILE = Paths.get(DEFAULT_DATA_DIR, "customers.json").toString();
    private static final String BOOKINGS_FILE = Paths.get(DEFAULT_DATA_DIR, "bookings.json").toString();
    private static final String INVOICES_FILE = Paths.get(DEFAULT_DATA_DIR, "invoices.json").toString();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private CustomerManager customerManager;
    private BookingManager bookingManager;
    private InvoiceManager invoiceManager;
    private RoomManager roomManager;

    public DataStorage(CustomerManager customerManager, BookingManager bookingManager, 
                       InvoiceManager invoiceManager, RoomManager roomManager) {
        this.customerManager = customerManager;
        this.bookingManager = bookingManager;
        this.invoiceManager = invoiceManager;
        this.roomManager = roomManager;
    }

    private static String getDefaultDataDir() {
        String userDir = System.getProperty("user.dir");
        Path projectDataDir = Paths.get(userDir, "data");
        if (Files.exists(projectDataDir)) {
            return projectDataDir.toString();
        }
        return "data";
    }

    /**
     * Tải tất cả dữ liệu từ file JSON
     */
    public void loadAllData() {
        loadCustomers();
        loadBookings();
        loadInvoices();
    }

    /**
     * Lưu tất cả dữ liệu vào file JSON
     */
    public void saveAllData() {
        saveCustomers();
        saveBookings();
        saveInvoices();
    }

    // ==================== CUSTOMERS ====================

    /**
     * Tải danh sách khách hàng từ file JSON
     */
    public void loadCustomers() {
        try {
            File file = new File(CUSTOMERS_FILE);
            if (!file.exists()) {
                return;
            }

            String content = readFile(CUSTOMERS_FILE);
            if (content.isEmpty()) {
                return;
            }

            JsonArray jsonArray = JsonParser.parseString(content).getAsJsonArray();
            for (JsonElement element : jsonArray) {
                Customer customer = parseCustomer(element.getAsJsonObject());
                if (customer != null) {
                    customerManager.add(customer);
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tải khách hàng: " + e.getMessage());
        }
    }

    /**
     * Lưu danh sách khách hàng vào file JSON
     */
    public void saveCustomers() {
        try {
            JsonArray jsonArray = new JsonArray();
            for (Customer customer : customerManager.getAll()) {
                jsonArray.add(customerToJson(customer));
            }
            writeFile(CUSTOMERS_FILE, gson.toJson(jsonArray));
        } catch (Exception e) {
            System.err.println("Lỗi khi lưu khách hàng: " + e.getMessage());
        }
    }

    // ==================== BOOKINGS ====================

    /**
     * Tải danh sách đặt phòng từ file JSON
     */
    public void loadBookings() {
        try {
            File file = new File(BOOKINGS_FILE);
            if (!file.exists()) {
                return;
            }

            String content = readFile(BOOKINGS_FILE);
            if (content.isEmpty()) {
                return;
            }

            JsonArray jsonArray = JsonParser.parseString(content).getAsJsonArray();
            for (JsonElement element : jsonArray) {
                Booking booking = parseBooking(element.getAsJsonObject());
                if (booking != null) {
                    bookingManager.add(booking);
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tải đặt phòng: " + e.getMessage());
        }
    }

    /**
     * Lưu danh sách đặt phòng vào file JSON
     */
    public void saveBookings() {
        try {
            JsonArray jsonArray = new JsonArray();
            for (Booking booking : bookingManager.getAll()) {
                jsonArray.add(bookingToJson(booking));
            }
            writeFile(BOOKINGS_FILE, gson.toJson(jsonArray));
        } catch (Exception e) {
            System.err.println("Lỗi khi lưu đặt phòng: " + e.getMessage());
        }
    }

    // ==================== INVOICES ====================

    /**
     * Tải danh sách hóa đơn từ file JSON
     */
    public void loadInvoices() {
        try {
            File file = new File(INVOICES_FILE);
            if (!file.exists()) {
                return;
            }

            String content = readFile(INVOICES_FILE);
            if (content.isEmpty()) {
                return;
            }

            JsonArray jsonArray = JsonParser.parseString(content).getAsJsonArray();
            for (JsonElement element : jsonArray) {
                Invoice invoice = parseInvoice(element.getAsJsonObject());
                if (invoice != null) {
                    invoiceManager.add(invoice);
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tải hóa đơn: " + e.getMessage());
        }
    }

    /**
     * Lưu danh sách hóa đơn vào file JSON
     */
    public void saveInvoices() {
        try {
            JsonArray jsonArray = new JsonArray();
            for (Invoice invoice : invoiceManager.getAll()) {
                jsonArray.add(invoiceToJson(invoice));
            }
            writeFile(INVOICES_FILE, gson.toJson(jsonArray));
        } catch (Exception e) {
            System.err.println("Lỗi khi lưu hóa đơn: " + e.getMessage());
        }
    }

    // ==================== JSON PARSERS ====================

    private Customer parseCustomer(JsonObject json) {
        try {
            String customerId = json.get("customerId").getAsString();
            String fullName = json.get("fullName").getAsString();
            String email = json.get("email").getAsString();
            String phoneNumber = json.get("phoneNumber").getAsString();
            String idCard = json.get("idCard").getAsString();
            String address = json.get("address").getAsString();
            LocalDate registrationDate = LocalDate.parse(json.get("registrationDate").getAsString());
            boolean isVIP = json.get("isVIP").getAsBoolean();

            Customer customer = new Customer(customerId, fullName, email, phoneNumber, 
                                           idCard, address, registrationDate, isVIP);
            if (json.has("loyaltyPoints")) {
                customer.setLoyaltyPoints(json.get("loyaltyPoints").getAsDouble());
            }
            return customer;
        } catch (Exception e) {
            System.err.println("Lỗi parse Customer: " + e.getMessage());
            return null;
        }
    }

    private Booking parseBooking(JsonObject json) {
        try {
            String bookingId = json.get("bookingId").getAsString();
            String customerId = json.get("customerId").getAsString();
            String roomId = json.get("roomId").getAsString();
            LocalDate checkInDate = LocalDate.parse(json.get("checkInDate").getAsString());
            LocalDate checkOutDate = LocalDate.parse(json.get("checkOutDate").getAsString());
            BookingStatus status = BookingStatus.valueOf(json.get("status").getAsString());

            Customer customer = customerManager.getById(customerId);
            Room room = roomManager.getById(roomId);

            if (customer != null && room != null) {
                Booking booking = new Booking(bookingId, customer, room, checkInDate, checkOutDate, status);
                if (json.has("notes")) {
                    booking.setNotes(json.get("notes").getAsString());
                }
                return booking;
            }
        } catch (Exception e) {
            System.err.println("Lỗi parse Booking: " + e.getMessage());
        }
        return null;
    }

    private Invoice parseInvoice(JsonObject json) {
        try {
            String invoiceId = json.get("invoiceId").getAsString();
            String bookingId = json.get("bookingId").getAsString();
            LocalDate invoiceDate = LocalDate.parse(json.get("invoiceDate").getAsString());
            double taxRate = json.get("taxRate").getAsDouble();

            Booking booking = bookingManager.getById(bookingId);
            if (booking != null) {
                Invoice invoice = new Invoice(invoiceId, booking, invoiceDate, taxRate);
                if (json.has("status")) {
                    invoice.setStatus(InvoiceStatus.valueOf(json.get("status").getAsString()));
                }
                if (json.has("notes")) {
                    invoice.setNotes(json.get("notes").getAsString());
                }
                return invoice;
            }
        } catch (Exception e) {
            System.err.println("Lỗi parse Invoice: " + e.getMessage());
        }
        return null;
    }

    // ==================== JSON CONVERTERS ====================

    private JsonObject customerToJson(Customer customer) {
        JsonObject json = new JsonObject();
        json.addProperty("customerId", customer.getCustomerId());
        json.addProperty("fullName", customer.getFullName());
        json.addProperty("email", customer.getEmail());
        json.addProperty("phoneNumber", customer.getPhoneNumber());
        json.addProperty("idCard", customer.getIdCard());
        json.addProperty("address", customer.getAddress());
        json.addProperty("registrationDate", customer.getRegistrationDate().toString());
        json.addProperty("isVIP", customer.isVIP());
        json.addProperty("loyaltyPoints", customer.getLoyaltyPoints());
        return json;
    }

    private JsonObject bookingToJson(Booking booking) {
        JsonObject json = new JsonObject();
        json.addProperty("bookingId", booking.getBookingId());
        json.addProperty("customerId", booking.getCustomer().getCustomerId());
        json.addProperty("roomId", booking.getRoom().getRoomId());
        json.addProperty("checkInDate", booking.getCheckInDate().toString());
        json.addProperty("checkOutDate", booking.getCheckOutDate().toString());
        json.addProperty("status", booking.getStatus().name());
        json.addProperty("totalPrice", booking.getTotalPrice());
        json.addProperty("notes", booking.getNotes());
        return json;
    }

    private JsonObject invoiceToJson(Invoice invoice) {
        JsonObject json = new JsonObject();
        json.addProperty("invoiceId", invoice.getInvoiceId());
        json.addProperty("bookingId", invoice.getBooking().getBookingId());
        json.addProperty("invoiceDate", invoice.getInvoiceDate().toString());
        json.addProperty("subtotal", invoice.getSubtotal());
        json.addProperty("taxRate", invoice.getTaxRate());
        json.addProperty("taxAmount", invoice.getTaxAmount());
        json.addProperty("totalAmount", invoice.getTotalAmount());
        json.addProperty("status", invoice.getStatus().name());
        json.addProperty("notes", invoice.getNotes());
        return json;
    }

    // ==================== FILE I/O ====================

    private String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    private void writeFile(String filePath, String content) throws IOException {
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        }
    }
}
