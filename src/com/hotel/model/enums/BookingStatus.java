package com.hotel.model.enums;

/**
 * Enum định nghĩa trạng thái đặt phòng
 * 
 * @author Member2
 * @version 1.0
 */
public enum BookingStatus {
    PENDING("Chờ xác nhận"),
    CONFIRMED("Đã xác nhận"),
    CHECKED_IN("Đã nhận phòng"),
    CHECKED_OUT("Đã trả phòng"),
    CANCELLED("Đã hủy");

    private final String displayName;

    /**
     * Constructor cho BookingStatus enum
     * @param displayName Tên hiển thị
     */
    BookingStatus(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Lấy tên hiển thị của trạng thái
     * @return Tên hiển thị
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Chuyển đổi string thành BookingStatus
     * @param text String cần chuyển đổi
     * @return BookingStatus tương ứng hoặc null nếu không tìm thấy
     */
    public static BookingStatus fromString(String text) {
        for (BookingStatus status : BookingStatus.values()) {
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
