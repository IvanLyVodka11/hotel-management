package com.hotel.model.enums;

/**
 * Enum định nghĩa trạng thái phòng
 * 
 * @author Member1
 * @version 1.0
 */
public enum RoomStatus {
    AVAILABLE("Trống", true),
    OCCUPIED("Đang sử dụng", false),
    MAINTENANCE("Đang bảo trì", false),
    CLEANING("Đang dọn dẹp", false),
    RESERVED("Đã đặt trước", false);

    private final String displayName;
    private final boolean canBook;

    /**
     * Constructor cho RoomStatus enum
     * @param displayName Tên hiển thị
     * @param canBook Có thể đặt phòng không
     */
    RoomStatus(String displayName, boolean canBook) {
        this.displayName = displayName;
        this.canBook = canBook;
    }

    /**
     * Lấy tên hiển thị của trạng thái
     * @return Tên hiển thị
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Kiểm tra phòng có thể đặt được không
     * @return true nếu có thể đặt
     */
    public boolean canBook() {
        return canBook;
    }

    /**
     * Chuyển đổi string thành RoomStatus
     * @param text String cần chuyển đổi
     * @return RoomStatus tương ứng hoặc null nếu không tìm thấy
     */
    public static RoomStatus fromString(String text) {
        for (RoomStatus status : RoomStatus.values()) {
            if (status.name().equalsIgnoreCase(text) || 
                status.displayName.equalsIgnoreCase(text)) {
                return status;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
