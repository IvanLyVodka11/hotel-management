package com.hotel.model.room;

import com.hotel.model.enums.RoomType;

/**
 * Phòng Deluxe - loại phòng cao cấp nhất với đầy đủ tiện nghi sang trọng
 * Kế thừa từ Room (INHERITANCE)
 * Override các abstract methods (POLYMORPHISM)
 * 
 * @author Member1
 * @version 1.0
 */
public class DeluxeRoom extends Room {
    
    // ==================== CONSTANTS ====================
    
    /** Giá cơ bản mặc định cho phòng Deluxe (VND/đêm) */
    public static final double DEFAULT_BASE_PRICE = 1500000;
    
    /** Số giường mặc định */
    public static final int DEFAULT_BED_COUNT = 2;
    
    /** Diện tích mặc định (m2) */
    public static final double DEFAULT_AREA = 50.0;
    
    /** Sức chứa tối đa */
    public static final int MAX_OCCUPANCY = 4;
    
    /** Hệ số nhân giá cho phòng Deluxe */
    public static final double PRICE_MULTIPLIER = 1.5;
    
    // ==================== ADDITIONAL ATTRIBUTES ====================
    
    /** Có view đẹp không */
    private boolean hasView;
    
    /** Có phòng tắm riêng cao cấp không */
    private boolean hasPrivateBathroom;
    
    /** Có bồn tắm jacuzzi không */
    private boolean hasJacuzzi;
    
    /** Có minibar không */
    private boolean hasMinibar;
    
    /** Có phòng khách riêng không */
    private boolean hasLivingRoom;
    
    // ==================== CONSTRUCTORS ====================
    
    /**
     * Constructor mặc định
     */
    public DeluxeRoom() {
        super();
        setBasePrice(DEFAULT_BASE_PRICE);
        setBedCount(DEFAULT_BED_COUNT);
        setArea(DEFAULT_AREA);
        initializeAmenities();
    }
    
    /**
     * Constructor với mã phòng và tầng
     * @param roomId Mã phòng
     * @param floor Số tầng
     */
    public DeluxeRoom(String roomId, int floor) {
        super(roomId, floor, DEFAULT_BASE_PRICE);
        setBedCount(DEFAULT_BED_COUNT);
        setArea(DEFAULT_AREA);
        initializeAmenities();
    }
    
    /**
     * Constructor với giá tùy chỉnh
     * @param roomId Mã phòng
     * @param floor Số tầng
     * @param basePrice Giá cơ bản
     */
    public DeluxeRoom(String roomId, int floor, double basePrice) {
        super(roomId, floor, basePrice);
        setBedCount(DEFAULT_BED_COUNT);
        setArea(DEFAULT_AREA);
        initializeAmenities();
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
    public DeluxeRoom(String roomId, int floor, double basePrice,
                      String description, int bedCount, double area) {
        super(roomId, floor, basePrice, description, bedCount, area);
        initializeAmenities();
    }
    
    /**
     * Khởi tạo các tiện nghi mặc định cho phòng Deluxe
     */
    private void initializeAmenities() {
        this.hasView = true;
        this.hasPrivateBathroom = true;
        this.hasJacuzzi = true;
        this.hasMinibar = true;
        this.hasLivingRoom = true;
    }
    
    // ==================== OVERRIDE ABSTRACT METHODS (Polymorphism) ====================
    
    /**
     * Tính giá phòng Deluxe
     * Công thức: basePrice × days × 1.5 (50% premium)
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
     * @return RoomType.DELUXE
     */
    @Override
    public RoomType getRoomType() {
        return RoomType.DELUXE;
    }
    
    /**
     * Lấy mô tả loại phòng Deluxe
     * @return Mô tả chi tiết
     */
    @Override
    public String getRoomTypeDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("Phòng Deluxe - Trải nghiệm đẳng cấp 5 sao với không gian rộng rãi và tiện nghi sang trọng. ");
        sb.append("Tiện nghi: ");
        if (hasView) sb.append("View đẹp, ");
        if (hasPrivateBathroom) sb.append("Phòng tắm riêng, ");
        if (hasJacuzzi) sb.append("Bồn tắm Jacuzzi, ");
        if (hasMinibar) sb.append("Minibar, ");
        if (hasLivingRoom) sb.append("Phòng khách riêng, ");
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
     * Đếm số tiện nghi cao cấp
     * @return Số tiện nghi
     */
    public int countPremiumAmenities() {
        int count = 0;
        if (hasView) count++;
        if (hasPrivateBathroom) count++;
        if (hasJacuzzi) count++;
        if (hasMinibar) count++;
        if (hasLivingRoom) count++;
        return count;
    }
    
    // ==================== GETTERS & SETTERS FOR AMENITIES ====================
    
    public boolean hasView() {
        return hasView;
    }
    
    public void setHasView(boolean hasView) {
        this.hasView = hasView;
    }
    
    public boolean hasPrivateBathroom() {
        return hasPrivateBathroom;
    }
    
    public void setHasPrivateBathroom(boolean hasPrivateBathroom) {
        this.hasPrivateBathroom = hasPrivateBathroom;
    }
    
    public boolean hasJacuzzi() {
        return hasJacuzzi;
    }
    
    public void setHasJacuzzi(boolean hasJacuzzi) {
        this.hasJacuzzi = hasJacuzzi;
    }
    
    public boolean hasMinibar() {
        return hasMinibar;
    }
    
    public void setHasMinibar(boolean hasMinibar) {
        this.hasMinibar = hasMinibar;
    }
    
    public boolean hasLivingRoom() {
        return hasLivingRoom;
    }
    
    public void setHasLivingRoom(boolean hasLivingRoom) {
        this.hasLivingRoom = hasLivingRoom;
    }
    
    // ==================== OVERRIDE UTILITY METHODS ====================
    
    @Override
    public String getFullInfo() {
        StringBuilder sb = new StringBuilder(super.getFullInfo());
        sb.append("--- Tiện nghi cao cấp ---\n");
        sb.append(String.format("View đẹp: %s\n", hasView ? "Có" : "Không"));
        sb.append(String.format("Phòng tắm riêng: %s\n", hasPrivateBathroom ? "Có" : "Không"));
        sb.append(String.format("Bồn tắm Jacuzzi: %s\n", hasJacuzzi ? "Có" : "Không"));
        sb.append(String.format("Minibar: %s\n", hasMinibar ? "Có" : "Không"));
        sb.append(String.format("Phòng khách riêng: %s\n", hasLivingRoom ? "Có" : "Không"));
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return String.format("DeluxeRoom{id='%s', floor=%d, status=%s, price=%s/đêm, amenities=%d}",
                getRoomId(), getFloor(), getStatus().getDisplayName(), 
                formatPrice(getBasePrice()), countPremiumAmenities());
    }
}
