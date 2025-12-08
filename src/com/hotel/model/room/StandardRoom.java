package com.hotel.model.room;

import com.hotel.model.enums.RoomType;

/**
 * Phòng tiêu chuẩn - loại phòng cơ bản nhất
 * Kế thừa từ Room (INHERITANCE)
 * Override các abstract methods (POLYMORPHISM)
 * 
 * @author Member1
 * @version 1.0
 */
public class StandardRoom extends Room {
    
    // ==================== CONSTANTS ====================
    
    /** Giá cơ bản mặc định cho phòng Standard (VND/đêm) */
    public static final double DEFAULT_BASE_PRICE = 500000;
    
    /** Số giường mặc định */
    public static final int DEFAULT_BED_COUNT = 1;
    
    /** Diện tích mặc định (m2) */
    public static final double DEFAULT_AREA = 20.0;
    
    /** Sức chứa tối đa */
    public static final int MAX_OCCUPANCY = 2;
    
    // ==================== CONSTRUCTORS ====================
    
    /**
     * Constructor mặc định
     */
    public StandardRoom() {
        super();
        setBasePrice(DEFAULT_BASE_PRICE);
        setBedCount(DEFAULT_BED_COUNT);
        setArea(DEFAULT_AREA);
    }
    
    /**
     * Constructor với mã phòng và tầng
     * @param roomId Mã phòng
     * @param floor Số tầng
     */
    public StandardRoom(String roomId, int floor) {
        super(roomId, floor, DEFAULT_BASE_PRICE);
        setBedCount(DEFAULT_BED_COUNT);
        setArea(DEFAULT_AREA);
    }
    
    /**
     * Constructor với giá tùy chỉnh
     * @param roomId Mã phòng
     * @param floor Số tầng
     * @param basePrice Giá cơ bản
     */
    public StandardRoom(String roomId, int floor, double basePrice) {
        super(roomId, floor, basePrice);
        setBedCount(DEFAULT_BED_COUNT);
        setArea(DEFAULT_AREA);
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
    public StandardRoom(String roomId, int floor, double basePrice,
                        String description, int bedCount, double area) {
        super(roomId, floor, basePrice, description, bedCount, area);
    }
    
    // ==================== OVERRIDE ABSTRACT METHODS (Polymorphism) ====================
    
    /**
     * Tính giá phòng Standard
     * Công thức: basePrice × days (không có hệ số nhân)
     * @param days Số ngày ở
     * @return Tổng tiền
     */
    @Override
    public double calculatePrice(int days) {
        if (days < 1) {
            throw new IllegalArgumentException("Số ngày phải lớn hơn 0");
        }
        return getBasePrice() * days;
    }
    
    /**
     * Lấy loại phòng
     * @return RoomType.STANDARD
     */
    @Override
    public RoomType getRoomType() {
        return RoomType.STANDARD;
    }
    
    /**
     * Lấy mô tả loại phòng Standard
     * @return Mô tả chi tiết
     */
    @Override
    public String getRoomTypeDescription() {
        return "Phòng Tiêu Chuẩn - Đầy đủ tiện nghi cơ bản, phù hợp cho khách công tác hoặc du lịch ngắn ngày. " +
               "Sức chứa tối đa: " + MAX_OCCUPANCY + " người.";
    }
    
    // ==================== SPECIFIC METHODS ====================
    
    /**
     * Lấy sức chứa tối đa của phòng
     * @return Số người tối đa
     */
    public int getMaxOccupancy() {
        return MAX_OCCUPANCY;
    }
    
    /**
     * Kiểm tra số khách có vượt quá sức chứa không
     * @param guestCount Số khách
     * @return true nếu hợp lệ
     */
    public boolean canAccommodate(int guestCount) {
        return guestCount > 0 && guestCount <= MAX_OCCUPANCY;
    }
    
    // ==================== OVERRIDE UTILITY METHODS ====================
    
    @Override
    public String toString() {
        return String.format("StandardRoom{id='%s', floor=%d, status=%s, price=%s/đêm}",
                getRoomId(), getFloor(), getStatus().getDisplayName(), formatPrice(getBasePrice()));
    }
}
