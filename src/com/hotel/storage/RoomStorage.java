package com.hotel.storage;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.hotel.model.enums.RoomStatus;
import com.hotel.model.enums.RoomType;
import com.hotel.model.room.*;
import com.hotel.service.interfaces.IStorable;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp xử lý lưu trữ dữ liệu phòng vào file JSON
 * Implements IStorable interface
 * Sử dụng thư viện Gson để serialize/deserialize
 * 
 * @author Member1
 * @version 2.0
 */
public class RoomStorage implements IStorable {
    
    // ==================== CONSTANTS ====================
    
    private static final String DEFAULT_DATA_DIR = "data";
    private static final String ROOMS_FILE = "rooms.json";
    
    // ==================== ATTRIBUTES ====================
    
    private final String dataDirectory;
    private final Gson gson;
    
    // ==================== CONSTRUCTOR ====================
    
    public RoomStorage() {
        this(DEFAULT_DATA_DIR);
    }
    
    public RoomStorage(String dataDirectory) {
        this.dataDirectory = dataDirectory;
        this.gson = createGson();
        ensureDataDirectoryExists();
    }
    
    /**
     * Tạo Gson instance với custom adapter cho Room polymorphism
     */
    private Gson createGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Room.class, new RoomTypeAdapter())
                .create();
    }
    
    private void ensureDataDirectoryExists() {
        Path path = Paths.get(dataDirectory);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                System.err.println("Không thể tạo thư mục dữ liệu: " + e.getMessage());
            }
        }
    }
    
    // ==================== IStorable IMPLEMENTATION ====================
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> boolean save(List<T> data) {
        if (data == null) return false;
        try {
            List<Room> rooms = (List<Room>) data;
            return saveRooms(rooms);
        } catch (ClassCastException e) {
            System.err.println("Dữ liệu không đúng định dạng Room: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> load() {
        return (List<T>) loadRooms();
    }
    
    @Override
    public String getFilePath() {
        return Paths.get(dataDirectory, ROOMS_FILE).toString();
    }
    
    @Override
    public boolean exists() {
        return Files.exists(Paths.get(getFilePath()));
    }
    
    @Override
    public boolean delete() {
        try {
            return Files.deleteIfExists(Paths.get(getFilePath()));
        } catch (IOException e) {
            System.err.println("Không thể xóa file: " + e.getMessage());
            return false;
        }
    }
    
    // ==================== SPECIFIC METHODS ====================
    
    /**
     * Lưu danh sách phòng vào file JSON
     */
    public boolean saveRooms(List<Room> rooms) {
        if (rooms == null) return false;
        
        try {
            RoomDataWrapper wrapper = new RoomDataWrapper(rooms);
            String json = gson.toJson(wrapper);
            Files.writeString(Paths.get(getFilePath()), json, StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            System.err.println("Lỗi khi lưu file: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Đọc danh sách phòng từ file JSON
     */
    public List<Room> loadRooms() {
        Path filePath = Paths.get(getFilePath());
        
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        
        try {
            String json = Files.readString(filePath, StandardCharsets.UTF_8);
            RoomDataWrapper wrapper = gson.fromJson(json, RoomDataWrapper.class);
            return wrapper != null && wrapper.rooms != null ? wrapper.rooms : new ArrayList<>();
        } catch (IOException | JsonSyntaxException e) {
            System.err.println("Lỗi khi đọc file: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Backup file dữ liệu
     */
    public boolean backup() {
        Path source = Paths.get(getFilePath());
        if (!Files.exists(source)) return false;
        
        String backupFileName = "rooms_backup_" + System.currentTimeMillis() + ".json";
        Path backup = Paths.get(dataDirectory, backupFileName);
        
        try {
            Files.copy(source, backup, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            System.err.println("Lỗi khi backup: " + e.getMessage());
            return false;
        }
    }
    
    // ==================== INNER CLASSES ====================
    
    /**
     * Wrapper class cho JSON structure
     */
    private static class RoomDataWrapper {
        List<Room> rooms;
        String version = "2.0";
        long lastModified = System.currentTimeMillis();
        
        RoomDataWrapper(List<Room> rooms) {
            this.rooms = rooms;
        }
    }
    
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
