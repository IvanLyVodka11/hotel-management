package com.hotel.storage;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
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
import com.hotel.util.AppLogger;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Lớp quản lý lưu trữ dữ liệu cho Booking, Customer, và Invoice
 * Handles JSON serialization/deserialization for all managers
 * Sử dụng thư viện Gson để serialize/deserialize
 * 
 * @author Member2
 * @version 2.0
 */
public class DataStorage {
    
    // ==================== CONSTANTS ====================
    
    private static final String DEFAULT_DATA_DIR = getDefaultDataDir();
    private static final String CUSTOMERS_FILE = "customers.json";
    private static final String BOOKINGS_FILE = "bookings.json";
    private static final String INVOICES_FILE = "invoices.json";
    
    // ==================== ATTRIBUTES ====================
    
    private final String dataDirectory;
    private final Gson gson;

    private CustomerManager customerManager;
    private BookingManager bookingManager;
    private InvoiceManager invoiceManager;
    private RoomManager roomManager;

    // ==================== CONSTRUCTOR ====================

    public DataStorage(CustomerManager customerManager, BookingManager bookingManager, 
                       InvoiceManager invoiceManager, RoomManager roomManager) {
        this(DEFAULT_DATA_DIR, customerManager, bookingManager, invoiceManager, roomManager);
    }

    public DataStorage(String dataDirectory, CustomerManager customerManager, BookingManager bookingManager, 
                       InvoiceManager invoiceManager, RoomManager roomManager) {
        this.dataDirectory = dataDirectory;
        this.gson = createGson();
        this.customerManager = customerManager;
        this.bookingManager = bookingManager;
        this.invoiceManager = invoiceManager;
        this.roomManager = roomManager;
        ensureDataDirectoryExists();
    }

    /**
     * Tạo Gson instance với custom adapters cho LocalDate
     */
    private Gson createGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    private void ensureDataDirectoryExists() {
        Path path = Paths.get(dataDirectory);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                AppLogger.info("Đã tạo thư mục dữ liệu: %s", dataDirectory);
            } catch (IOException e) {
                AppLogger.error("Không thể tạo thư mục dữ liệu: " + dataDirectory, e);
            }
        }
    }

    // ==================== HELPER METHODS ====================

    private String getCustomersFilePath() {
        return Paths.get(dataDirectory, CUSTOMERS_FILE).toString();
    }

    private String getBookingsFilePath() {
        return Paths.get(dataDirectory, BOOKINGS_FILE).toString();
    }

    private String getInvoicesFilePath() {
        return Paths.get(dataDirectory, INVOICES_FILE).toString();
    }

    private static String getDefaultDataDir() {
        String userDir = System.getProperty("user.dir");
        Path projectDataDir = Paths.get(userDir, "data");
        if (Files.exists(projectDataDir)) {
            return projectDataDir.toString();
        }
        return "data";
    }

    // ==================== LOAD/SAVE ALL ====================

    /**
     * Tải tất cả dữ liệu từ file JSON
     */
    public void loadAllData() {
        loadCustomers();
        loadBookings();
        loadInvoices();
        AppLogger.info("Đã tải tất cả dữ liệu từ thư mục: %s", dataDirectory);
    }

    /**
     * Lưu tất cả dữ liệu vào file JSON
     */
    public void saveAllData() {
        saveCustomers();
        saveBookings();
        saveInvoices();
        AppLogger.info("Đã lưu tất cả dữ liệu vào thư mục: %s", dataDirectory);
    }

    // ==================== CUSTOMERS ====================

    /**
     * Tải danh sách khách hàng từ file JSON
     */
    public void loadCustomers() {
        Path filePath = Paths.get(getCustomersFilePath());
        if (!Files.exists(filePath)) {
            AppLogger.info("File khách hàng không tồn tại: %s", filePath);
            return;
        }

        try {
            String content = Files.readString(filePath, StandardCharsets.UTF_8);
            if (content.isEmpty() || content.isBlank()) {
                return;
            }

            JsonArray jsonArray = JsonParser.parseString(content).getAsJsonArray();
            int count = 0;
            for (JsonElement element : jsonArray) {
                Customer customer = parseCustomer(element.getAsJsonObject());
                if (customer != null) {
                    customerManager.add(customer);
                    count++;
                }
            }
            AppLogger.info("Đã tải %d khách hàng từ %s", count, filePath);
        } catch (IOException e) {
            AppLogger.error("Lỗi khi tải khách hàng từ file: " + filePath, e);
        } catch (JsonSyntaxException e) {
            AppLogger.error("Lỗi cú pháp JSON khi tải khách hàng: " + filePath, e);
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
            String json = gson.toJson(jsonArray);
            Files.writeString(Paths.get(getCustomersFilePath()), json, StandardCharsets.UTF_8);
            AppLogger.info("Đã lưu %d khách hàng vào %s", customerManager.getAll().size(), getCustomersFilePath());
        } catch (IOException e) {
            AppLogger.error("Lỗi khi lưu khách hàng vào file: " + getCustomersFilePath(), e);
        }
    }

    // ==================== BOOKINGS ====================

    /**
     * Tải danh sách đặt phòng từ file JSON
     */
    public void loadBookings() {
        Path filePath = Paths.get(getBookingsFilePath());
        if (!Files.exists(filePath)) {
            AppLogger.info("File đặt phòng không tồn tại: %s", filePath);
            return;
        }

        try {
            String content = Files.readString(filePath, StandardCharsets.UTF_8);
            if (content.isEmpty() || content.isBlank()) {
                return;
            }

            JsonArray jsonArray = JsonParser.parseString(content).getAsJsonArray();
            int count = 0;
            for (JsonElement element : jsonArray) {
                Booking booking = parseBooking(element.getAsJsonObject());
                if (booking != null) {
                    bookingManager.add(booking);
                    count++;
                }
            }
            AppLogger.info("Đã tải %d đặt phòng từ %s", count, filePath);
        } catch (IOException e) {
            AppLogger.error("Lỗi khi tải đặt phòng từ file: " + filePath, e);
        } catch (JsonSyntaxException e) {
            AppLogger.error("Lỗi cú pháp JSON khi tải đặt phòng: " + filePath, e);
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
            String json = gson.toJson(jsonArray);
            Files.writeString(Paths.get(getBookingsFilePath()), json, StandardCharsets.UTF_8);
            AppLogger.info("Đã lưu %d đặt phòng vào %s", bookingManager.getAll().size(), getBookingsFilePath());
        } catch (IOException e) {
            AppLogger.error("Lỗi khi lưu đặt phòng vào file: " + getBookingsFilePath(), e);
        }
    }

    // ==================== INVOICES ====================

    /**
     * Tải danh sách hóa đơn từ file JSON
     */
    public void loadInvoices() {
        Path filePath = Paths.get(getInvoicesFilePath());
        if (!Files.exists(filePath)) {
            AppLogger.info("File hóa đơn không tồn tại: %s", filePath);
            return;
        }

        try {
            String content = Files.readString(filePath, StandardCharsets.UTF_8);
            if (content.isEmpty() || content.isBlank()) {
                return;
            }

            JsonArray jsonArray = JsonParser.parseString(content).getAsJsonArray();
            int count = 0;
            for (JsonElement element : jsonArray) {
                Invoice invoice = parseInvoice(element.getAsJsonObject());
                if (invoice != null) {
                    invoiceManager.add(invoice);
                    count++;
                }
            }
            AppLogger.info("Đã tải %d hóa đơn từ %s", count, filePath);
        } catch (IOException e) {
            AppLogger.error("Lỗi khi tải hóa đơn từ file: " + filePath, e);
        } catch (JsonSyntaxException e) {
            AppLogger.error("Lỗi cú pháp JSON khi tải hóa đơn: " + filePath, e);
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
            String json = gson.toJson(jsonArray);
            Files.writeString(Paths.get(getInvoicesFilePath()), json, StandardCharsets.UTF_8);
            AppLogger.info("Đã lưu %d hóa đơn vào %s", invoiceManager.getAll().size(), getInvoicesFilePath());
        } catch (IOException e) {
            AppLogger.error("Lỗi khi lưu hóa đơn vào file: " + getInvoicesFilePath(), e);
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
            AppLogger.error("Lỗi parse Customer: " + e.getMessage(), e);
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
            } else {
                AppLogger.warn("Không tìm thấy Customer hoặc Room cho Booking: %s", bookingId);
            }
        } catch (Exception e) {
            AppLogger.error("Lỗi parse Booking: " + e.getMessage(), e);
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
            } else {
                AppLogger.warn("Không tìm thấy Booking cho Invoice: %s", invoiceId);
            }
        } catch (Exception e) {
            AppLogger.error("Lỗi parse Invoice: " + e.getMessage(), e);
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

    // ==================== LOCAL DATE ADAPTER ====================

    /**
     * Custom Gson adapter cho LocalDate serialization/deserialization
     */
    private static class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        @Override
        public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(date.format(formatter));
        }

        @Override
        public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
                throws JsonParseException {
            return LocalDate.parse(json.getAsString(), formatter);
        }
    }
}
