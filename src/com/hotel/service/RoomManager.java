package com.hotel.service;

import com.hotel.model.enums.RoomStatus;
import com.hotel.model.enums.RoomType;
import com.hotel.model.room.Room;
import com.hotel.service.interfaces.IManageable;
import com.hotel.service.interfaces.ISearchable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Quản lý các thao tác CRUD và business logic cho Room
 * Implements IManageable và ISearchable interfaces
 * 
 * @author Member1
 * @version 1.0
 */
public class RoomManager implements IManageable<Room>, ISearchable<Room> {
    
    // ==================== ATTRIBUTES ====================
    
    /** Danh sách phòng (sử dụng HashMap để tìm kiếm nhanh theo ID) */
    private final Map<String, Room> roomMap;
    
    /** Instance duy nhất (Singleton Pattern) */
    private static RoomManager instance;
    
    // ==================== CONSTRUCTOR ====================
    
    /**
     * Private constructor (Singleton Pattern)
     */
    private RoomManager() {
        this.roomMap = new HashMap<>();
    }
    
    /**
     * Lấy instance duy nhất của RoomManager
     * @return RoomManager instance
     */
    public static synchronized RoomManager getInstance() {
        if (instance == null) {
            instance = new RoomManager();
        }
        return instance;
    }
    
    /**
     * Reset instance (chỉ dùng cho testing)
     */
    public static void resetInstance() {
        instance = null;
    }
    
    // ==================== IManageable IMPLEMENTATION ====================
    
    /**
     * Thêm phòng mới vào hệ thống
     * @param room Phòng cần thêm
     * @return true nếu thêm thành công
     */
    @Override
    public boolean add(Room room) {
        if (room == null) {
            return false;
        }
        
        String roomId = room.getRoomId();
        if (roomId == null || roomMap.containsKey(roomId)) {
            return false; // ID null hoặc đã tồn tại
        }
        
        roomMap.put(roomId, room);
        return true;
    }
    
    /**
     * Cập nhật thông tin phòng
     * @param room Phòng với thông tin mới
     * @return true nếu cập nhật thành công
     */
    @Override
    public boolean update(Room room) {
        if (room == null) {
            return false;
        }
        
        String roomId = room.getRoomId();
        if (roomId == null || !roomMap.containsKey(roomId)) {
            return false; // Không tìm thấy phòng
        }
        
        roomMap.put(roomId, room);
        return true;
    }
    
    /**
     * Xóa phòng theo ID
     * @param roomId ID của phòng cần xóa
     * @return true nếu xóa thành công
     */
    @Override
    public boolean delete(String roomId) {
        if (roomId == null || !roomMap.containsKey(roomId)) {
            return false;
        }
        
        roomMap.remove(roomId);
        return true;
    }
    
    /**
     * Tìm phòng theo ID
     * @param roomId ID của phòng
     * @return Phòng tìm được hoặc null
     */
    @Override
    public Room getById(String roomId) {
        if (roomId == null) {
            return null;
        }
        return roomMap.get(roomId.toUpperCase());
    }
    
    /**
     * Lấy tất cả phòng
     * @return Danh sách tất cả phòng
     */
    @Override
    public List<Room> getAll() {
        return new ArrayList<>(roomMap.values());
    }
    
    /**
     * Đếm số phòng
     * @return Số lượng phòng
     */
    @Override
    public int count() {
        return roomMap.size();
    }
    
    /**
     * Kiểm tra hệ thống có rỗng không
     * @return true nếu không có phòng nào
     */
    @Override
    public boolean isEmpty() {
        return roomMap.isEmpty();
    }
    
    /**
     * Xóa tất cả phòng
     */
    @Override
    public void clear() {
        roomMap.clear();
    }
    
    // ==================== ISearchable IMPLEMENTATION ====================
    
    /**
     * Tìm kiếm phòng theo keyword
     * @param keyword Từ khóa tìm kiếm
     * @return Danh sách phòng tìm được
     */
    @Override
    public List<Room> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }
        
        String searchKey = keyword.toLowerCase().trim();
        return roomMap.values().stream()
                .filter(room -> matchesKeyword(room, searchKey))
                .collect(Collectors.toList());
    }
    
    /**
     * Kiểm tra phòng có khớp từ khóa không
     */
    private boolean matchesKeyword(Room room, String keyword) {
        return room.getRoomId().toLowerCase().contains(keyword) ||
               room.getRoomType().getDisplayName().toLowerCase().contains(keyword) ||
               room.getStatus().getDisplayName().toLowerCase().contains(keyword) ||
               room.getDescription().toLowerCase().contains(keyword);
    }
    
    /**
     * Lọc phòng theo điều kiện
     * @param criteria Điều kiện lọc (Map key-value)
     * @return Danh sách phòng thỏa mãn
     */
    @Override
    public List<Room> filter(Map<String, Object> criteria) {
        if (criteria == null || criteria.isEmpty()) {
            return getAll();
        }
        
        return roomMap.values().stream()
                .filter(room -> matchesCriteria(room, criteria))
                .collect(Collectors.toList());
    }
    
    /**
     * Kiểm tra phòng có thỏa mãn các điều kiện không
     */
    private boolean matchesCriteria(Room room, Map<String, Object> criteria) {
        for (Map.Entry<String, Object> entry : criteria.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            
            switch (key.toLowerCase()) {
                case "type":
                    if (value instanceof RoomType && room.getRoomType() != value) {
                        return false;
                    }
                    break;
                case "status":
                    if (value instanceof RoomStatus && room.getStatus() != value) {
                        return false;
                    }
                    break;
                case "floor":
                    if (value instanceof Integer && room.getFloor() != (Integer) value) {
                        return false;
                    }
                    break;
                case "available":
                    if (value instanceof Boolean && room.isAvailable() != (Boolean) value) {
                        return false;
                    }
                    break;
                case "minprice":
                    if (value instanceof Double && room.getBasePrice() < (Double) value) {
                        return false;
                    }
                    break;
                case "maxprice":
                    if (value instanceof Double && room.getBasePrice() > (Double) value) {
                        return false;
                    }
                    break;
            }
        }
        return true;
    }
    
    // ==================== SPECIFIC SEARCH METHODS ====================
    
    /**
     * Tìm phòng theo loại
     * @param type Loại phòng
     * @return Danh sách phòng theo loại
     */
    public List<Room> findByType(RoomType type) {
        if (type == null) {
            return new ArrayList<>();
        }
        return roomMap.values().stream()
                .filter(room -> room.getRoomType() == type)
                .collect(Collectors.toList());
    }
    
    /**
     * Tìm phòng theo trạng thái
     * @param status Trạng thái phòng
     * @return Danh sách phòng theo trạng thái
     */
    public List<Room> findByStatus(RoomStatus status) {
        if (status == null) {
            return new ArrayList<>();
        }
        return roomMap.values().stream()
                .filter(room -> room.getStatus() == status)
                .collect(Collectors.toList());
    }
    
    /**
     * Tìm phòng theo tầng
     * @param floor Số tầng
     * @return Danh sách phòng trên tầng
     */
    public List<Room> findByFloor(int floor) {
        return roomMap.values().stream()
                .filter(room -> room.getFloor() == floor)
                .collect(Collectors.toList());
    }
    
    /**
     * Tìm tất cả phòng trống
     * @return Danh sách phòng trống
     */
    public List<Room> findAvailableRooms() {
        return findByStatus(RoomStatus.AVAILABLE);
    }
    
    /**
     * Tìm phòng trống theo loại
     * @param type Loại phòng
     * @return Danh sách phòng trống theo loại
     */
    public List<Room> findAvailableRoomsByType(RoomType type) {
        return roomMap.values().stream()
                .filter(room -> room.getRoomType() == type && room.isAvailable())
                .collect(Collectors.toList());
    }
    
    /**
     * Tìm phòng trong khoảng giá
     * @param minPrice Giá thấp nhất
     * @param maxPrice Giá cao nhất
     * @return Danh sách phòng trong khoảng giá
     */
    public List<Room> findByPriceRange(double minPrice, double maxPrice) {
        return roomMap.values().stream()
                .filter(room -> room.getBasePrice() >= minPrice && room.getBasePrice() <= maxPrice)
                .collect(Collectors.toList());
    }
    
    // ==================== SORTING METHODS ====================
    
    /**
     * Sắp xếp theo giá (tăng dần)
     * @return Danh sách phòng đã sắp xếp
     */
    public List<Room> sortByPriceAscending() {
        return roomMap.values().stream()
                .sorted(Comparator.comparingDouble(Room::getBasePrice))
                .collect(Collectors.toList());
    }
    
    /**
     * Sắp xếp theo giá (giảm dần)
     * @return Danh sách phòng đã sắp xếp
     */
    public List<Room> sortByPriceDescending() {
        return roomMap.values().stream()
                .sorted(Comparator.comparingDouble(Room::getBasePrice).reversed())
                .collect(Collectors.toList());
    }
    
    /**
     * Sắp xếp theo ID
     * @return Danh sách phòng đã sắp xếp
     */
    public List<Room> sortById() {
        return roomMap.values().stream()
                .sorted(Comparator.comparing(Room::getRoomId))
                .collect(Collectors.toList());
    }
    
    /**
     * Sắp xếp theo tầng
     * @return Danh sách phòng đã sắp xếp
     */
    public List<Room> sortByFloor() {
        return roomMap.values().stream()
                .sorted(Comparator.comparingInt(Room::getFloor))
                .collect(Collectors.toList());
    }
    
    /**
     * Sắp xếp theo loại phòng
     * @return Danh sách phòng đã sắp xếp
     */
    public List<Room> sortByType() {
        return roomMap.values().stream()
                .sorted(Comparator.comparing(Room::getRoomType))
                .collect(Collectors.toList());
    }
    
    // ==================== STATISTICS METHODS ====================
    
    /**
     * Đếm số phòng theo loại
     * @return Map loại phòng -> số lượng
     */
    public Map<RoomType, Long> countByType() {
        return roomMap.values().stream()
                .collect(Collectors.groupingBy(Room::getRoomType, Collectors.counting()));
    }
    
    /**
     * Đếm số phòng theo trạng thái
     * @return Map trạng thái -> số lượng
     */
    public Map<RoomStatus, Long> countByStatus() {
        return roomMap.values().stream()
                .collect(Collectors.groupingBy(Room::getStatus, Collectors.counting()));
    }
    
    /**
     * Đếm số phòng theo tầng
     * @return Map tầng -> số lượng
     */
    public Map<Integer, Long> countByFloor() {
        return roomMap.values().stream()
                .collect(Collectors.groupingBy(Room::getFloor, Collectors.counting()));
    }
    
    /**
     * Tính tổng doanh thu tiềm năng (nếu tất cả phòng được đặt 1 đêm)
     * @return Tổng doanh thu tiềm năng
     */
    public double calculateTotalPotentialRevenue() {
        return roomMap.values().stream()
                .mapToDouble(room -> room.calculatePrice(1))
                .sum();
    }
    
    /**
     * Lấy phòng có giá cao nhất
     * @return Phòng đắt nhất hoặc null
     */
    public Room getMostExpensiveRoom() {
        return roomMap.values().stream()
                .max(Comparator.comparingDouble(Room::getBasePrice))
                .orElse(null);
    }
    
    /**
     * Lấy phòng có giá thấp nhất
     * @return Phòng rẻ nhất hoặc null
     */
    public Room getCheapestRoom() {
        return roomMap.values().stream()
                .min(Comparator.comparingDouble(Room::getBasePrice))
                .orElse(null);
    }
    
    // ==================== BUSINESS METHODS ====================
    
    /**
     * Kiểm tra ID phòng đã tồn tại chưa
     * @param roomId ID cần kiểm tra
     * @return true nếu đã tồn tại
     */
    public boolean exists(String roomId) {
        return roomId != null && roomMap.containsKey(roomId.toUpperCase());
    }
    
    /**
     * Đặt phòng (chuyển trạng thái sang OCCUPIED)
     * @param roomId ID phòng
     * @return true nếu đặt thành công
     */
    public boolean occupyRoom(String roomId) {
        Room room = getById(roomId);
        if (room == null || !room.isAvailable()) {
            return false;
        }
        room.occupy();
        return true;
    }
    
    /**
     * Trả phòng (chuyển trạng thái sang CLEANING)
     * @param roomId ID phòng
     * @return true nếu trả thành công
     */
    public boolean releaseRoom(String roomId) {
        Room room = getById(roomId);
        if (room == null || room.getStatus() != RoomStatus.OCCUPIED) {
            return false;
        }
        room.release();
        return true;
    }
    
    /**
     * Đánh dấu phòng sẵn sàng
     * @param roomId ID phòng
     * @return true nếu thành công
     */
    public boolean markRoomAvailable(String roomId) {
        Room room = getById(roomId);
        if (room == null) {
            return false;
        }
        room.markAvailable();
        return true;
    }
    
    /**
     * Đánh dấu phòng bảo trì
     * @param roomId ID phòng
     * @return true nếu thành công
     */
    public boolean markRoomMaintenance(String roomId) {
        Room room = getById(roomId);
        if (room == null) {
            return false;
        }
        room.markMaintenance();
        return true;
    }
    
    // ==================== DATA LOADING ====================
    
    /**
     * Load danh sách phòng từ bên ngoài
     * @param rooms Danh sách phòng cần load
     */
    public void loadRooms(List<Room> rooms) {
        if (rooms != null) {
            roomMap.clear();
            for (Room room : rooms) {
                if (room != null && room.getRoomId() != null) {
                    roomMap.put(room.getRoomId(), room);
                }
            }
        }
    }
}
