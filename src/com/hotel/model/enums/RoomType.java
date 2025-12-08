package com.hotel.model.enums;

/**
 * Enum định nghĩa các loại phòng trong khách sạn
 * 
 * @author Member1
 * @version 1.0
 */
public enum RoomType {
    STANDARD("Phòng Thường", 1.0),
    VIP("Phòng VIP", 1.2),
    DELUXE("Phòng Deluxe", 1.5);

    private final String displayName;
    private final double priceMultiplier;

    /**
     * Constructor cho RoomType enum
     * @param displayName Tên hiển thị
     * @param priceMultiplier Hệ số nhân giá
     */
    RoomType(String displayName, double priceMultiplier) {
        this.displayName = displayName;
        this.priceMultiplier = priceMultiplier;
    }

    /**
     * Lấy tên hiển thị của loại phòng
     * @return Tên hiển thị
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Lấy hệ số nhân giá của loại phòng
     * @return Hệ số nhân giá
     */
    public double getPriceMultiplier() {
        return priceMultiplier;
    }

    /**
     * Chuyển đổi string thành RoomType
     * @param text String cần chuyển đổi
     * @return RoomType tương ứng hoặc null nếu không tìm thấy
     */
    public static RoomType fromString(String text) {
        for (RoomType type : RoomType.values()) {
            if (type.name().equalsIgnoreCase(text) || 
                type.displayName.equalsIgnoreCase(text)) {
                return type;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
