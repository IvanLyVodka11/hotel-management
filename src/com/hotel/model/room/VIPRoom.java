package com.hotel.model.room;

import com.hotel.model.enums.RoomType;

/**
 * Phòng VIP - loại phòng cao cấp với tiện nghi nâng cao
 * Kế thừa từ Room (INHERITANCE)
 * Override các abstract methods (POLYMORPHISM)
 * 
 * @author Member1
 * @version 1.0
 */
public class VIPRoom extends Room {
    
    // ==================== CONSTANTS ====================
    
    /** Giá cơ bản mặc định cho phòng VIP (VND/đêm) */
    public static final double DEFAULT_BASE_PRICE = 1000000;
    
    /** Số giường mặc định */
    public static final int DEFAULT_BED_COUNT = 2;
    
    /** Diện tích mặc định (m2) */
    public static final double DEFAULT_AREA = 35.0;
    
    /** Sức chứa tối đa */
    public static final int MAX_OCCUPANCY = 3;
    
    /** Hệ số nhân giá cho phòng VIP */
    public static final double PRICE_MULTIPLIER = 1.2;
    
    // ==================== ADDITIONAL ATTRIBUTES ====================
    
    /** Có view đẹp không */
    private boolean hasView;
    
    /** Có phòng tắm riêng cao cấp không */
    private boolean hasPrivateBathroom;
    
    // ==================== CONSTRUCTORS ====================
    
    /**
     * Constructor mặc định
     */
    public VIPRoom() {
        super();
        setBasePrice(DEFAULT_BASE_PRICE);
        setBedCount(DEFAULT_BED_COUNT);
        setArea(DEFAULT_AREA);
        this.hasView = true;
        this.hasPrivateBathroom = true;
    }
    
    /**
     * Constructor với mã phòng và tầng
     * @param roomId Mã phòng
     * @param floor Số tầng
     */
    public VIPRoom(String roomId, int floor) {
        super(roomId, floor, DEFAULT_BASE_PRICE);
        setBedCount(DEFAULT_BED_COUNT);
        setArea(DEFAULT_AREA);
        this.hasView = true;
        this.hasPrivateBathroom = true;
    }
    
    /**
     * Constructor với giá tùy chỉnh
     * @param roomId Mã phòng
     * @param floor Số tầng
     * @param basePrice Giá cơ bản
     */
    public VIPRoom(String roomId, int floor, double basePrice) {
        super(roomId, floor, basePrice);
        setBedCount(DEFAULT_BED_COUNT);
        setArea(DEFAULT_AREA);
        this.hasView = true;
        this.hasPrivateBathroom = true;
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
    public VIPRoom(String roomId, int floor, double basePrice,
                   String description, int bedCount, double area) {
        super(roomId, floor, basePrice, description, bedCount, area);
        this.hasView = true;
        this.hasPrivateBathroom = true;
    }
    
    // ==================== OVERRIDE ABSTRACT METHODS (Polymorphism) ====================
    
    /**
     * Tính giá phòng VIP
     * Công thức: basePrice × days × 1.2 (20% premium)
     * @param days Số ngày ở
     * @return Tổng tiền
     */
    @Override
    public double calculatePrice(int days) {
        if (days < 1) {
            throw new IllegalArgumentException("Số ngày phải lớn hơn 0");
        }
        return getBasePrice() * days * PRICE_MULTIPLIER;
    }
    
    /**
     * Lấy loại phòng
     * @return RoomType.VIP
     */
    @Override
    public RoomType getRoomType() {
        return RoomType.VIP;
    }
    
    /**
     * Lấy mô tả loại phòng VIP
     * @return Mô tả chi tiết
     */
    @Override
    public String getRoomTypeDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("Phòng VIP - Không gian sang trọng với tiện nghi cao cấp. ");
        if (hasView) {
            sb.append("View đẹp. ");
        }
        if (hasPrivateBathroom) {
            sb.append("Phòng tắm riêng cao cấp. ");
        }
        sb.append("Sức chứa tối đa: ").append(MAX_OCCUPANCY).append(" người.");
        return sb.toString();
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
    
    /**
     * Kiểm tra phòng có view không
     * @return true nếu có view
     */
    public boolean hasView() {
        return hasView;
    }
    
    /**
     * Đặt trạng thái có view
     * @param hasView Có view hay không
     */
    public void setHasView(boolean hasView) {
        this.hasView = hasView;
    }
    
    /**
     * Kiểm tra phòng có phòng tắm riêng cao cấp không
     * @return true nếu có
     */
    public boolean hasPrivateBathroom() {
        return hasPrivateBathroom;
    }
    
    /**
     * Đặt trạng thái có phòng tắm riêng
     * @param hasPrivateBathroom Có phòng tắm riêng hay không
     */
    public void setHasPrivateBathroom(boolean hasPrivateBathroom) {
        this.hasPrivateBathroom = hasPrivateBathroom;
    }
    
    // ==================== OVERRIDE UTILITY METHODS ====================
    
    @Override
    public String getFullInfo() {
        StringBuilder sb = new StringBuilder(super.getFullInfo());
        sb.append(String.format("Có view đẹp: %s\n", hasView ? "Có" : "Không"));
        sb.append(String.format("Phòng tắm cao cấp: %s\n", hasPrivateBathroom ? "Có" : "Không"));
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return String.format("VIPRoom{id='%s', floor=%d, status=%s, price=%s/đêm, view=%s}",
                getRoomId(), getFloor(), getStatus().getDisplayName(), 
                formatPrice(getBasePrice()), hasView ? "Yes" : "No");
    }
}
