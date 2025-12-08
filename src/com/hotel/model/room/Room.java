package com.hotel.model.room;

import com.hotel.model.enums.RoomStatus;
import com.hotel.model.enums.RoomType;

/**
 * Abstract class định nghĩa cấu trúc chung cho tất cả loại phòng
 * Áp dụng nguyên tắc ABSTRACTION và ENCAPSULATION của OOP
 * 
 * @author Member1
 * @version 1.0
 */
public abstract class Room {
    
    // ==================== ATTRIBUTES (Private - Encapsulation) ====================
    
    /** Mã phòng (unique identifier) */
    private String roomId;
    
    /** Số tầng */
    private int floor;
    
    /** Trạng thái phòng */
    private RoomStatus status;
    
    /** Giá cơ bản (VND/đêm) */
    private double basePrice;
    
    /** Mô tả phòng */
    private String description;
    
    /** Số giường */
    private int bedCount;
    
    /** Diện tích (m2) */
    private double area;
    
    // ==================== CONSTRUCTORS ====================
    
    /**
     * Constructor mặc định
     */
    protected Room() {
        this.status = RoomStatus.AVAILABLE;
        this.description = "";
        this.bedCount = 1;
        this.area = 20.0;
    }
    
    /**
     * Constructor với các tham số cơ bản
     * @param roomId Mã phòng
     * @param floor Số tầng
     * @param basePrice Giá cơ bản
     */
    protected Room(String roomId, int floor, double basePrice) {
        this();
        setRoomId(roomId);
        setFloor(floor);
        setBasePrice(basePrice);
    }
    
    /**
     * Constructor đầy đủ tham số
     * @param roomId Mã phòng
     * @param floor Số tầng
     * @param basePrice Giá cơ bản
     * @param description Mô tả
     * @param bedCount Số giường
     * @param area Diện tích
     */
    protected Room(String roomId, int floor, double basePrice, 
                   String description, int bedCount, double area) {
        this(roomId, floor, basePrice);
        setDescription(description);
        setBedCount(bedCount);
        setArea(area);
    }
    
    // ==================== ABSTRACT METHODS (Subclass phải override) ====================
    
    /**
     * Tính giá phòng cho số ngày ở
     * Mỗi loại phòng có cách tính giá khác nhau (Polymorphism)
     * @param days Số ngày ở
     * @return Tổng tiền
     */
    public abstract double calculatePrice(int days);
    
    /**
     * Lấy loại phòng
     * @return RoomType enum
     */
    public abstract RoomType getRoomType();
    
    /**
     * Lấy mô tả chi tiết loại phòng
     * @return Mô tả
     */
    public abstract String getRoomTypeDescription();
    
    // ==================== GETTERS & SETTERS (Encapsulation) ====================
    
    /**
     * Lấy mã phòng
     * @return Mã phòng
     */
    public String getRoomId() {
        return roomId;
    }
    
    /**
     * Đặt mã phòng (có validation)
     * @param roomId Mã phòng mới
     * @throws IllegalArgumentException nếu mã phòng không hợp lệ
     */
    public void setRoomId(String roomId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phòng không được để trống");
        }
        this.roomId = roomId.trim().toUpperCase();
    }
    
    /**
     * Lấy số tầng
     * @return Số tầng
     */
    public int getFloor() {
        return floor;
    }
    
    /**
     * Đặt số tầng (có validation)
     * @param floor Số tầng mới
     * @throws IllegalArgumentException nếu tầng không hợp lệ
     */
    public void setFloor(int floor) {
        if (floor < 1 || floor > 100) {
            throw new IllegalArgumentException("Số tầng phải từ 1 đến 100");
        }
        this.floor = floor;
    }
    
    /**
     * Lấy trạng thái phòng
     * @return Trạng thái phòng
     */
    public RoomStatus getStatus() {
        return status;
    }
    
    /**
     * Đặt trạng thái phòng
     * @param status Trạng thái mới
     */
    public void setStatus(RoomStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Trạng thái không được null");
        }
        this.status = status;
    }
    
    /**
     * Lấy giá cơ bản
     * @return Giá cơ bản
     */
    public double getBasePrice() {
        return basePrice;
    }
    
    /**
     * Đặt giá cơ bản (có validation)
     * @param basePrice Giá mới
     * @throws IllegalArgumentException nếu giá không hợp lệ
     */
    public void setBasePrice(double basePrice) {
        if (basePrice < 0) {
            throw new IllegalArgumentException("Giá phòng không được âm");
        }
        this.basePrice = basePrice;
    }
    
    /**
     * Lấy mô tả phòng
     * @return Mô tả
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Đặt mô tả phòng
     * @param description Mô tả mới
     */
    public void setDescription(String description) {
        this.description = description != null ? description : "";
    }
    
    /**
     * Lấy số giường
     * @return Số giường
     */
    public int getBedCount() {
        return bedCount;
    }
    
    /**
     * Đặt số giường (có validation)
     * @param bedCount Số giường mới
     */
    public void setBedCount(int bedCount) {
        if (bedCount < 1 || bedCount > 10) {
            throw new IllegalArgumentException("Số giường phải từ 1 đến 10");
        }
        this.bedCount = bedCount;
    }
    
    /**
     * Lấy diện tích
     * @return Diện tích (m2)
     */
    public double getArea() {
        return area;
    }
    
    /**
     * Đặt diện tích (có validation)
     * @param area Diện tích mới
     */
    public void setArea(double area) {
        if (area < 10 || area > 500) {
            throw new IllegalArgumentException("Diện tích phải từ 10 đến 500 m2");
        }
        this.area = area;
    }
    
    // ==================== BUSINESS METHODS ====================
    
    /**
     * Kiểm tra phòng có sẵn để đặt không
     * @return true nếu phòng trống
     */
    public boolean isAvailable() {
        return status == RoomStatus.AVAILABLE;
    }
    
    /**
     * Đặt phòng thành đang sử dụng
     */
    public void occupy() {
        this.status = RoomStatus.OCCUPIED;
    }
    
    /**
     * Giải phóng phòng (chuyển sang cleaning)
     */
    public void release() {
        this.status = RoomStatus.CLEANING;
    }
    
    /**
     * Đánh dấu phòng sẵn sàng
     */
    public void markAvailable() {
        this.status = RoomStatus.AVAILABLE;
    }
    
    /**
     * Đánh dấu phòng bảo trì
     */
    public void markMaintenance() {
        this.status = RoomStatus.MAINTENANCE;
    }
    
    // ==================== UTILITY METHODS ====================
    
    /**
     * Format giá tiền theo VND
     * @param amount Số tiền
     * @return String format
     */
    protected String formatPrice(double amount) {
        return String.format("%,.0f VND", amount);
    }
    
    /**
     * Lấy thông tin phòng dạng ngắn gọn
     * @return Thông tin ngắn
     */
    public String getShortInfo() {
        return String.format("%s - %s - %s", roomId, getRoomType().getDisplayName(), status.getDisplayName());
    }
    
    /**
     * Lấy thông tin phòng đầy đủ
     * @return Thông tin đầy đủ
     */
    public String getFullInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== THÔNG TIN PHÒNG ===\n");
        sb.append(String.format("Mã phòng: %s\n", roomId));
        sb.append(String.format("Loại phòng: %s\n", getRoomType().getDisplayName()));
        sb.append(String.format("Tầng: %d\n", floor));
        sb.append(String.format("Trạng thái: %s\n", status.getDisplayName()));
        sb.append(String.format("Giá cơ bản: %s/đêm\n", formatPrice(basePrice)));
        sb.append(String.format("Số giường: %d\n", bedCount));
        sb.append(String.format("Diện tích: %.1f m²\n", area));
        sb.append(String.format("Mô tả: %s\n", description));
        return sb.toString();
    }
    
    // ==================== OVERRIDE METHODS ====================
    
    @Override
    public String toString() {
        return String.format("Room{id='%s', type=%s, floor=%d, status=%s, price=%s}",
                roomId, getRoomType(), floor, status, formatPrice(basePrice));
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof Room)) return false;
        Room other = (Room) obj;
        return roomId != null && roomId.equals(other.roomId);
    }
    
    @Override
    public int hashCode() {
        return roomId != null ? roomId.hashCode() : 0;
    }
}
