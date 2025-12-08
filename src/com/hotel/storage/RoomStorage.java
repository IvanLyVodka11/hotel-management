package com.hotel.storage;

import com.hotel.model.enums.RoomStatus;
import com.hotel.model.enums.RoomType;
import com.hotel.model.room.*;
import com.hotel.service.interfaces.IStorable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lớp xử lý lưu trữ dữ liệu phòng vào file JSON
 * Implements IStorable interface
 * Sử dụng phương pháp đọc/ghi JSON thủ công (không cần thư viện ngoài)
 * 
 * @author Member1
 * @version 1.0
 */
public class RoomStorage implements IStorable {
    
    // ==================== CONSTANTS ====================
    
    /** Đường dẫn mặc định cho file dữ liệu */
    private static final String DEFAULT_DATA_DIR = "data";
    private static final String ROOMS_FILE = "rooms.json";
    
    // ==================== ATTRIBUTES ====================
    
    /** Đường dẫn đến thư mục dữ liệu */
    private final String dataDirectory;
    
    // ==================== CONSTRUCTOR ====================
    
    /**
     * Constructor mặc định
     */
    public RoomStorage() {
        this(DEFAULT_DATA_DIR);
    }
    
    /**
     * Constructor với đường dẫn tùy chỉnh
     * @param dataDirectory Đường dẫn thư mục dữ liệu
     */
    public RoomStorage(String dataDirectory) {
        this.dataDirectory = dataDirectory;
        ensureDataDirectoryExists();
    }
    
    /**
     * Đảm bảo thư mục dữ liệu tồn tại
     */
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
    
    /**
     * Lưu danh sách phòng vào file JSON
     * @param data Danh sách phòng cần lưu
     * @return true nếu lưu thành công
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> boolean save(List<T> data) {
        if (data == null) {
            return false;
        }
        
        try {
            List<Room> rooms = (List<Room>) data;
            return saveRooms(rooms);
        } catch (ClassCastException e) {
            System.err.println("Dữ liệu không đúng định dạng Room: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Đọc danh sách phòng từ file JSON
     * @return Danh sách phòng hoặc danh sách rỗng nếu lỗi
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> load() {
        return (List<T>) loadRooms();
    }
    
    /**
     * Lấy đường dẫn file dữ liệu
     * @return Đường dẫn file
     */
    @Override
    public String getFilePath() {
        return Paths.get(dataDirectory, ROOMS_FILE).toString();
    }
    
    /**
     * Kiểm tra file dữ liệu có tồn tại không
     * @return true nếu file tồn tại
     */
    @Override
    public boolean exists() {
        return Files.exists(Paths.get(getFilePath()));
    }
    
    /**
     * Xóa file dữ liệu
     * @return true nếu xóa thành công
     */
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
     * Lưu danh sách phòng vào file
     * @param rooms Danh sách phòng
     * @return true nếu lưu thành công
     */
    public boolean saveRooms(List<Room> rooms) {
        if (rooms == null) {
            return false;
        }
        
        try {
            StringBuilder json = new StringBuilder();
            json.append("{\n");
            json.append("  \"version\": \"1.0\",\n");
            json.append("  \"lastModified\": ").append(System.currentTimeMillis()).append(",\n");
            json.append("  \"rooms\": [\n");
            
            for (int i = 0; i < rooms.size(); i++) {
                Room room = rooms.get(i);
                json.append(roomToJson(room));
                if (i < rooms.size() - 1) {
                    json.append(",");
                }
                json.append("\n");
            }
            
            json.append("  ]\n");
            json.append("}");
            
            Path filePath = Paths.get(getFilePath());
            Files.writeString(filePath, json.toString(), StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            System.err.println("Lỗi khi lưu file: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Chuyển đối tượng Room thành JSON string
     */
    private String roomToJson(Room room) {
        StringBuilder sb = new StringBuilder();
        sb.append("    {\n");
        sb.append("      \"roomType\": \"").append(room.getRoomType().name()).append("\",\n");
        sb.append("      \"roomId\": \"").append(escapeJson(room.getRoomId())).append("\",\n");
        sb.append("      \"floor\": ").append(room.getFloor()).append(",\n");
        sb.append("      \"basePrice\": ").append(room.getBasePrice()).append(",\n");
        sb.append("      \"status\": \"").append(room.getStatus().name()).append("\",\n");
        sb.append("      \"description\": \"").append(escapeJson(room.getDescription())).append("\",\n");
        sb.append("      \"bedCount\": ").append(room.getBedCount()).append(",\n");
        sb.append("      \"area\": ").append(room.getArea());
        
        // Thêm thuộc tính riêng của từng loại phòng
        if (room instanceof VIPRoom) {
            VIPRoom vipRoom = (VIPRoom) room;
            sb.append(",\n      \"hasView\": ").append(vipRoom.hasView());
            sb.append(",\n      \"hasPrivateBathroom\": ").append(vipRoom.hasPrivateBathroom());
        } else if (room instanceof DeluxeRoom) {
            DeluxeRoom deluxeRoom = (DeluxeRoom) room;
            sb.append(",\n      \"hasView\": ").append(deluxeRoom.hasView());
            sb.append(",\n      \"hasPrivateBathroom\": ").append(deluxeRoom.hasPrivateBathroom());
            sb.append(",\n      \"hasJacuzzi\": ").append(deluxeRoom.hasJacuzzi());
            sb.append(",\n      \"hasMinibar\": ").append(deluxeRoom.hasMinibar());
            sb.append(",\n      \"hasLivingRoom\": ").append(deluxeRoom.hasLivingRoom());
        }
        
        sb.append("\n    }");
        return sb.toString();
    }
    
    /**
     * Escape các ký tự đặc biệt trong JSON string
     */
    private String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
    
    /**
     * Đọc danh sách phòng từ file
     * @return Danh sách phòng hoặc danh sách rỗng
     */
    public List<Room> loadRooms() {
        Path filePath = Paths.get(getFilePath());
        
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        
        try {
            String json = Files.readString(filePath, StandardCharsets.UTF_8);
            return parseRoomsFromJson(json);
        } catch (IOException e) {
            System.err.println("Lỗi khi đọc file: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Parse JSON string thành danh sách Room
     */
    private List<Room> parseRoomsFromJson(String json) {
        List<Room> rooms = new ArrayList<>();
        
        // Tìm array rooms trong JSON
        int roomsStart = json.indexOf("\"rooms\"");
        if (roomsStart == -1) return rooms;
        
        int arrayStart = json.indexOf("[", roomsStart);
        int arrayEnd = json.lastIndexOf("]");
        if (arrayStart == -1 || arrayEnd == -1) return rooms;
        
        String roomsArray = json.substring(arrayStart + 1, arrayEnd);
        
        // Parse từng object room
        int braceCount = 0;
        int objectStart = -1;
        
        for (int i = 0; i < roomsArray.length(); i++) {
            char c = roomsArray.charAt(i);
            if (c == '{') {
                if (braceCount == 0) {
                    objectStart = i;
                }
                braceCount++;
            } else if (c == '}') {
                braceCount--;
                if (braceCount == 0 && objectStart != -1) {
                    String roomJson = roomsArray.substring(objectStart, i + 1);
                    Room room = parseRoomFromJson(roomJson);
                    if (room != null) {
                        rooms.add(room);
                    }
                    objectStart = -1;
                }
            }
        }
        
        return rooms;
    }
    
    /**
     * Parse một JSON object thành Room
     */
    private Room parseRoomFromJson(String json) {
        try {
            String roomType = extractStringValue(json, "roomType");
            String roomId = extractStringValue(json, "roomId");
            int floor = extractIntValue(json, "floor");
            double basePrice = extractDoubleValue(json, "basePrice");
            String status = extractStringValue(json, "status");
            String description = extractStringValue(json, "description");
            int bedCount = extractIntValue(json, "bedCount");
            double area = extractDoubleValue(json, "area");
            
            Room room;
            RoomType type = RoomType.valueOf(roomType);
            
            switch (type) {
                case STANDARD:
                    room = new StandardRoom(roomId, floor, basePrice, description, bedCount, area);
                    break;
                case VIP:
                    VIPRoom vipRoom = new VIPRoom(roomId, floor, basePrice, description, bedCount, area);
                    vipRoom.setHasView(extractBooleanValue(json, "hasView"));
                    vipRoom.setHasPrivateBathroom(extractBooleanValue(json, "hasPrivateBathroom"));
                    room = vipRoom;
                    break;
                case DELUXE:
                    DeluxeRoom deluxeRoom = new DeluxeRoom(roomId, floor, basePrice, description, bedCount, area);
                    deluxeRoom.setHasView(extractBooleanValue(json, "hasView"));
                    deluxeRoom.setHasPrivateBathroom(extractBooleanValue(json, "hasPrivateBathroom"));
                    deluxeRoom.setHasJacuzzi(extractBooleanValue(json, "hasJacuzzi"));
                    deluxeRoom.setHasMinibar(extractBooleanValue(json, "hasMinibar"));
                    deluxeRoom.setHasLivingRoom(extractBooleanValue(json, "hasLivingRoom"));
                    room = deluxeRoom;
                    break;
                default:
                    return null;
            }
            
            room.setStatus(RoomStatus.valueOf(status));
            return room;
        } catch (Exception e) {
            System.err.println("Lỗi parse room: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Trích xuất giá trị String từ JSON
     */
    private String extractStringValue(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return unescapeJson(matcher.group(1));
        }
        return "";
    }
    
    /**
     * Trích xuất giá trị int từ JSON
     */
    private int extractIntValue(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*(-?\\d+)");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }
    
    /**
     * Trích xuất giá trị double từ JSON
     */
    private double extractDoubleValue(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*(-?\\d+\\.?\\d*)");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1));
        }
        return 0.0;
    }
    
    /**
     * Trích xuất giá trị boolean từ JSON
     */
    private boolean extractBooleanValue(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*(true|false)");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return Boolean.parseBoolean(matcher.group(1));
        }
        return false;
    }
    
    /**
     * Unescape các ký tự đặc biệt trong JSON string
     */
    private String unescapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\\\", "\\")
                   .replace("\\\"", "\"")
                   .replace("\\n", "\n")
                   .replace("\\r", "\r")
                   .replace("\\t", "\t");
    }
    
    /**
     * Backup file dữ liệu
     * @return true nếu backup thành công
     */
    public boolean backup() {
        Path source = Paths.get(getFilePath());
        if (!Files.exists(source)) {
            return false;
        }
        
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
}
