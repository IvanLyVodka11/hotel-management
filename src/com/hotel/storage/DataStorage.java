package com.hotel.storage;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.hotel.model.booking.Booking;
import com.hotel.model.customer.Customer;
import com.hotel.model.enums.BookingStatus;
import com.hotel.model.enums.RoomStatus;
import com.hotel.model.enums.RoomType;
import com.hotel.model.invoice.Invoice;
import com.hotel.model.invoice.Invoice.InvoiceStatus;
import com.hotel.model.room.DeluxeRoom;
import com.hotel.model.room.Room;
import com.hotel.model.room.StandardRoom;
import com.hotel.model.room.VIPRoom;
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
import java.util.ArrayList;
import java.util.List;

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
    private static final String ROOMS_FILE = "rooms.json";
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
     * Tạo Gson instance với custom adapters cho LocalDate và Room polymorphism
     */
    private Gson createGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(Room.class, new RoomTypeAdapter())
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

    private String getRoomsFilePath() {
        return Paths.get(dataDirectory, ROOMS_FILE).toString();
    }

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
        loadRooms();  // Load rooms first (needed for bookings)
        loadCustomers();
        loadBookings();
        loadInvoices();
        AppLogger.info("Đã tải tất cả dữ liệu từ thư mục: %s", dataDirectory);
    }

    /**
     * Lưu tất cả dữ liệu vào file JSON
     */
    public void saveAllData() {
        saveRooms();
        saveCustomers();
        saveBookings();
        saveInvoices();
        AppLogger.info("Đã lưu tất cả dữ liệu vào thư mục: %s", dataDirectory);
    }

    // ==================== ROOMS ====================

    /**
     * Wrapper class cho JSON structure của Room
     */
    private static class RoomDataWrapper {
        List<Room> rooms;

        RoomDataWrapper(List<Room> rooms) {
            this.rooms = rooms;
        }
    }

    /**
     * Tải danh sách phòng từ file JSON
     */
    public void loadRooms() {
        Path filePath = Paths.get(getRoomsFilePath());
        if (!Files.exists(filePath)) {
            AppLogger.info("File phòng không tồn tại: %s", filePath);
            return;
        }

        try {
            String json = Files.readString(filePath, StandardCharsets.UTF_8);
            RoomDataWrapper wrapper = gson.fromJson(json, RoomDataWrapper.class);
            if (wrapper != null && wrapper.rooms != null) {
                roomManager.loadRooms(wrapper.rooms);
                AppLogger.info("Đã tải %d phòng từ %s", wrapper.rooms.size(), filePath);
            }
        } catch (IOException | JsonSyntaxException e) {
            AppLogger.error("Lỗi khi tải phòng từ file: " + filePath, e);
        }
    }

    /**
     * Lưu danh sách phòng vào file JSON
     */
    public void saveRooms() {
        try {
            RoomDataWrapper wrapper = new RoomDataWrapper(roomManager.getAll());
            String json = gson.toJson(wrapper);
            Files.writeString(Paths.get(getRoomsFilePath()), json, StandardCharsets.UTF_8);
            AppLogger.info("Đã lưu %d phòng vào %s", roomManager.getAll().size(), getRoomsFilePath());
        } catch (IOException e) {
            AppLogger.error("Lỗi khi lưu phòng vào file: " + getRoomsFilePath(), e);
        }
    }

    /**
     * Lưu danh sách phòng (public method cho RoomPanel)
     */
    public boolean saveRooms(List<Room> rooms) {
        if (rooms == null) return false;
        try {
            RoomDataWrapper wrapper = new RoomDataWrapper(rooms);
            String json = gson.toJson(wrapper);
            Files.writeString(Paths.get(getRoomsFilePath()), json, StandardCharsets.UTF_8);
            AppLogger.info("Đã lưu %d phòng vào %s", rooms.size(), getRoomsFilePath());
            return true;
        } catch (IOException e) {
            AppLogger.error("Lỗi khi lưu phòng vào file: " + getRoomsFilePath(), e);
            return false;
        }
    }

    /**
     * Đọc danh sách phòng từ file JSON (public method cho RoomPanel)
     */
    public List<Room> loadRoomsList() {
        Path filePath = Paths.get(getRoomsFilePath());
        if (!Files.exists(filePath)) {
            AppLogger.debug("File phòng không tồn tại: %s", filePath);
            return new ArrayList<>();
        }

        try {
            String json = Files.readString(filePath, StandardCharsets.UTF_8);
            RoomDataWrapper wrapper = gson.fromJson(json, RoomDataWrapper.class);
            List<Room> result = wrapper != null && wrapper.rooms != null ? wrapper.rooms : new ArrayList<>();
            AppLogger.info("Đã tải %d phòng từ %s", result.size(), filePath);
            return result;
        } catch (IOException | JsonSyntaxException e) {
            AppLogger.error("Lỗi khi tải phòng từ file: " + filePath, e);
            return new ArrayList<>();
        }
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

    // ==================== ROOM TYPE ADAPTER ====================

    /**
     * Custom TypeAdapter để xử lý polymorphism của Room
     * Gson cần biết cách serialize/deserialize các subclass của Room
     */
    private static class RoomTypeAdapter implements JsonSerializer<Room>, JsonDeserializer<Room> {

        @Override
        public JsonElement serialize(Room room, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject json = new JsonObject();

            // Lưu loại phòng để biết deserialize thành class nào
            json.addProperty("roomType", room.getRoomType().name());

            // Thuộc tính chung
            json.addProperty("roomId", room.getRoomId());
            json.addProperty("floor", room.getFloor());
            json.addProperty("basePrice", room.getBasePrice());
            json.addProperty("status", room.getStatus().name());
            json.addProperty("description", room.getDescription());
            json.addProperty("bedCount", room.getBedCount());
            json.addProperty("area", room.getArea());

            // Thuộc tính riêng của từng loại phòng
            if (room instanceof VIPRoom) {
                VIPRoom vip = (VIPRoom) room;
                json.addProperty("hasView", vip.hasView());
                json.addProperty("hasPrivateBathroom", vip.hasPrivateBathroom());
            } else if (room instanceof DeluxeRoom) {
                DeluxeRoom deluxe = (DeluxeRoom) room;
                json.addProperty("hasView", deluxe.hasView());
                json.addProperty("hasPrivateBathroom", deluxe.hasPrivateBathroom());
                json.addProperty("hasJacuzzi", deluxe.hasJacuzzi());
                json.addProperty("hasMinibar", deluxe.hasMinibar());
                json.addProperty("hasLivingRoom", deluxe.hasLivingRoom());
            }

            return json;
        }

        @Override
        public Room deserialize(JsonElement jsonElement, Type typeOfT,
                JsonDeserializationContext context) throws JsonParseException {
            JsonObject json = jsonElement.getAsJsonObject();

            // Đọc loại phòng
            RoomType roomType = RoomType.valueOf(json.get("roomType").getAsString());

            // Đọc thuộc tính chung
            String roomId = json.get("roomId").getAsString();
            int floor = json.get("floor").getAsInt();
            double basePrice = json.get("basePrice").getAsDouble();
            RoomStatus status = RoomStatus.valueOf(json.get("status").getAsString());
            String description = getStringOrDefault(json, "description", "");
            int bedCount = getIntOrDefault(json, "bedCount", 1);
            double area = getDoubleOrDefault(json, "area", 20.0);

            // Tạo đối tượng theo loại phòng
            Room room;
            switch (roomType) {
                case VIP:
                    VIPRoom vip = new VIPRoom(roomId, floor, basePrice, description, bedCount, area);
                    vip.setHasView(getBooleanOrDefault(json, "hasView", true));
                    vip.setHasPrivateBathroom(getBooleanOrDefault(json, "hasPrivateBathroom", true));
                    room = vip;
                    break;
                case DELUXE:
                    DeluxeRoom deluxe = new DeluxeRoom(roomId, floor, basePrice, description, bedCount, area);
                    deluxe.setHasView(getBooleanOrDefault(json, "hasView", true));
                    deluxe.setHasPrivateBathroom(getBooleanOrDefault(json, "hasPrivateBathroom", true));
                    deluxe.setHasJacuzzi(getBooleanOrDefault(json, "hasJacuzzi", true));
                    deluxe.setHasMinibar(getBooleanOrDefault(json, "hasMinibar", true));
                    deluxe.setHasLivingRoom(getBooleanOrDefault(json, "hasLivingRoom", true));
                    room = deluxe;
                    break;
                default:
                    room = new StandardRoom(roomId, floor, basePrice, description, bedCount, area);
                    break;
            }

            room.setStatus(status);
            return room;
        }

        // Helper methods để xử lý null-safe
        private String getStringOrDefault(JsonObject json, String key, String defaultValue) {
            return json.has(key) && !json.get(key).isJsonNull() ? json.get(key).getAsString() : defaultValue;
        }

        private int getIntOrDefault(JsonObject json, String key, int defaultValue) {
            return json.has(key) && !json.get(key).isJsonNull() ? json.get(key).getAsInt() : defaultValue;
        }

        private double getDoubleOrDefault(JsonObject json, String key, double defaultValue) {
            return json.has(key) && !json.get(key).isJsonNull() ? json.get(key).getAsDouble() : defaultValue;
        }

        private boolean getBooleanOrDefault(JsonObject json, String key, boolean defaultValue) {
            return json.has(key) && !json.get(key).isJsonNull() ? json.get(key).getAsBoolean() : defaultValue;
        }
    }
}
