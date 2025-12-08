package com.hotel.model.room;

import com.hotel.model.enums.RoomType;

/**
 * Factory class để tạo các đối tượng Room
 * Áp dụng Factory Design Pattern
 * 
 * @author Member1
 * @version 1.0
 */
public class RoomFactory {
    
    /**
     * Private constructor để ngăn việc tạo instance
     * (Factory Pattern - chỉ dùng static methods)
     */
    private RoomFactory() {
        // Utility class
    }
    
    /**
     * Tạo phòng mới theo loại
     * @param type Loại phòng (STANDARD, VIP, DELUXE)
     * @param roomId Mã phòng
     * @param floor Số tầng
     * @return Room instance
     * @throws IllegalArgumentException nếu loại phòng không hợp lệ
     */
    public static Room createRoom(RoomType type, String roomId, int floor) {
        if (type == null) {
            throw new IllegalArgumentException("Loại phòng không được null");
        }
        
        switch (type) {
            case STANDARD:
                return new StandardRoom(roomId, floor);
            case VIP:
                return new VIPRoom(roomId, floor);
            case DELUXE:
                return new DeluxeRoom(roomId, floor);
            default:
                throw new IllegalArgumentException("Loại phòng không được hỗ trợ: " + type);
        }
    }
    
    /**
     * Tạo phòng mới với giá tùy chỉnh
     * @param type Loại phòng
     * @param roomId Mã phòng
     * @param floor Số tầng
     * @param basePrice Giá cơ bản
     * @return Room instance
     */
    public static Room createRoom(RoomType type, String roomId, int floor, double basePrice) {
        if (type == null) {
            throw new IllegalArgumentException("Loại phòng không được null");
        }
        
        switch (type) {
            case STANDARD:
                return new StandardRoom(roomId, floor, basePrice);
            case VIP:
                return new VIPRoom(roomId, floor, basePrice);
            case DELUXE:
                return new DeluxeRoom(roomId, floor, basePrice);
            default:
                throw new IllegalArgumentException("Loại phòng không được hỗ trợ: " + type);
        }
    }
    
    /**
     * Tạo phòng từ String type
     * @param typeString Tên loại phòng (string)
     * @param roomId Mã phòng
     * @param floor Số tầng
     * @return Room instance
     */
    public static Room createRoom(String typeString, String roomId, int floor) {
        RoomType type = RoomType.fromString(typeString);
        if (type == null) {
            throw new IllegalArgumentException("Loại phòng không hợp lệ: " + typeString);
        }
        return createRoom(type, roomId, floor);
    }
    
    /**
     * Tạo phòng Standard
     * @param roomId Mã phòng
     * @param floor Số tầng
     * @return StandardRoom instance
     */
    public static StandardRoom createStandardRoom(String roomId, int floor) {
        return new StandardRoom(roomId, floor);
    }
    
    /**
     * Tạo phòng VIP
     * @param roomId Mã phòng
     * @param floor Số tầng
     * @return VIPRoom instance
     */
    public static VIPRoom createVIPRoom(String roomId, int floor) {
        return new VIPRoom(roomId, floor);
    }
    
    /**
     * Tạo phòng Deluxe
     * @param roomId Mã phòng
     * @param floor Số tầng
     * @return DeluxeRoom instance
     */
    public static DeluxeRoom createDeluxeRoom(String roomId, int floor) {
        return new DeluxeRoom(roomId, floor);
    }
    
    /**
     * Clone một phòng với ID mới
     * @param source Phòng nguồn
     * @param newRoomId ID mới
     * @return Room mới với cùng thuộc tính
     */
    public static Room cloneRoom(Room source, String newRoomId) {
        if (source == null) {
            throw new IllegalArgumentException("Phòng nguồn không được null");
        }
        
        Room newRoom = createRoom(source.getRoomType(), newRoomId, source.getFloor(), source.getBasePrice());
        newRoom.setDescription(source.getDescription());
        newRoom.setBedCount(source.getBedCount());
        newRoom.setArea(source.getArea());
        newRoom.setStatus(source.getStatus());
        
        return newRoom;
    }
}
