package com.hotel.ui.theme;

/**
 * UI Constants for the Hotel Management application.
 * Contains text strings, icons references, and other UI-related constants.
 * 
 * @author OOP Project Team
 * @version 2.0
 */
public final class UIConstants {

    // Prevent instantiation
    private UIConstants() {
    }

    // ==================== APPLICATION INFO ====================

    public static final String APP_NAME = "Hotel Management System";
    public static final String APP_NAME_VN = "Hệ thống Quản lý Khách sạn";
    public static final String APP_VERSION = "2.0.0";
    public static final String APP_TITLE = APP_NAME_VN + " v" + APP_VERSION;

    // ==================== WINDOW TITLES ====================

    public static final class Titles {
        public static final String LOGIN = "Đăng nhập - " + APP_NAME_VN;
        public static final String MAIN = APP_NAME_VN;
        public static final String ROOM_DIALOG_ADD = "Thêm phòng mới";
        public static final String ROOM_DIALOG_EDIT = "Sửa thông tin phòng";
        public static final String BOOKING_DIALOG_ADD = "Tạo đặt phòng mới";
        public static final String BOOKING_DIALOG_EDIT = "Chỉnh sửa đặt phòng";
        public static final String CUSTOMER_DIALOG_ADD = "Thêm khách hàng mới";
        public static final String CUSTOMER_DIALOG_EDIT = "Sửa thông tin khách hàng";
        public static final String ABOUT = "Giới thiệu";
        public static final String STATISTICS = "Thống kê";
    }

    // ==================== MENU LABELS ====================

    public static final class Menu {
        // Main menus
        public static final String FILE = "Tệp";
        public static final String ROOMS = "Quản lý Phòng";
        public static final String BOOKINGS = "Đặt phòng";
        public static final String CUSTOMERS = "Khách hàng";
        public static final String REPORTS = "Báo cáo";
        public static final String HELP = "Trợ giúp";

        // Menu items
        public static final String SAVE = "Lưu dữ liệu";
        public static final String EXIT = "Thoát";
        public static final String ADD_ROOM = "Thêm phòng mới";
        public static final String LIST_ROOMS = "Danh sách phòng";
        public static final String NEW_BOOKING = "Đặt phòng mới";
        public static final String LIST_BOOKINGS = "Danh sách đặt phòng";
        public static final String ROOM_STATS = "Thống kê phòng";
        public static final String REVENUE_REPORT = "Báo cáo doanh thu";
        public static final String ABOUT = "Giới thiệu";
    }

    // ==================== TAB LABELS ====================

    public static final class Tabs {
        public static final String DASHBOARD = "Tổng quan";
        public static final String ROOMS = "Quản lý Phòng";
        public static final String BOOKINGS = "Đặt phòng";
        public static final String CUSTOMERS = "Khách hàng";
        public static final String REPORTS = "Báo cáo";
    }

    // ==================== BUTTON LABELS ====================

    public static final class Buttons {
        public static final String ADD = "Thêm mới";
        public static final String EDIT = "Sửa";
        public static final String DELETE = "Xóa";
        public static final String SAVE = "Lưu";
        public static final String CANCEL = "Hủy";
        public static final String REFRESH = "Làm mới";
        public static final String SEARCH = "Tìm kiếm";
        public static final String FILTER = "Lọc";
        public static final String LOGIN = "Đăng nhập";
        public static final String LOGOUT = "Đăng xuất";
        public static final String CONFIRM = "Xác nhận";
        public static final String CLOSE = "Đóng";
        public static final String YES = "Có";
        public static final String NO = "Không";
        public static final String OK = "OK";
    }

    // ==================== FORM LABELS ====================

    public static final class Labels {
        // Login form
        public static final String USERNAME = "Tên đăng nhập:";
        public static final String PASSWORD = "Mật khẩu:";

        // Room form
        public static final String ROOM_ID = "Mã phòng:";
        public static final String ROOM_TYPE = "Loại phòng:";
        public static final String FLOOR = "Tầng:";
        public static final String STATUS = "Trạng thái:";
        public static final String PRICE = "Giá (VND/đêm):";
        public static final String BED_COUNT = "Số giường:";
        public static final String AREA = "Diện tích (m²):";
        public static final String DESCRIPTION = "Mô tả:";

        // Booking form
        public static final String CUSTOMER = "Khách hàng:";
        public static final String ROOM = "Phòng:";
        public static final String CHECK_IN = "Ngày nhận phòng:";
        public static final String CHECK_OUT = "Ngày trả phòng:";
        public static final String TOTAL_PRICE = "Tổng tiền:";
        public static final String NOTES = "Ghi chú:";

        // Customer form
        public static final String FULL_NAME = "Họ và tên:";
        public static final String EMAIL = "Email:";
        public static final String PHONE = "Số điện thoại:";
        public static final String ID_CARD = "CMND/CCCD:";
        public static final String ADDRESS = "Địa chỉ:";
    }

    // ==================== TABLE COLUMNS ====================

    public static final class Columns {
        public static final String[] ROOMS = {
                "Mã phòng", "Loại phòng", "Tầng", "Trạng thái",
                "Giá (VND/đêm)", "Số giường", "Diện tích (m²)"
        };

        public static final String[] BOOKINGS = {
                "Mã đặt phòng", "Khách hàng", "Phòng", "Nhận phòng",
                "Trả phòng", "Trạng thái", "Tổng tiền"
        };

        public static final String[] CUSTOMERS = {
                "Mã KH", "Họ tên", "Email", "Điện thoại",
                "CMND/CCCD", "VIP", "Điểm tích lũy"
        };

        public static final String[] INVOICES = {
                "Mã hóa đơn", "Mã đặt phòng", "Ngày lập",
                "Tổng tiền", "Thuế", "Thành tiền", "Trạng thái"
        };
    }

    // ==================== FILTER OPTIONS ====================

    public static final class Filters {
        public static final String ALL = "Tất cả";

        public static final String[] ROOM_TYPES = { ALL, "Standard", "VIP", "Deluxe" };
        public static final String[] ROOM_STATUSES = { ALL, "Trống", "Đang sử dụng", "Bảo trì", "Đang dọn" };
        public static final String[] BOOKING_STATUSES = { ALL, "Chờ xác nhận", "Đã xác nhận", "Đã nhận phòng",
                "Đã trả phòng", "Đã hủy" };
    }

    // ==================== MESSAGES ====================

    public static final class Messages {
        // Success
        public static final String SAVE_SUCCESS = "Đã lưu dữ liệu thành công!";
        public static final String ADD_SUCCESS = "Thêm thành công!";
        public static final String UPDATE_SUCCESS = "Cập nhật thành công!";
        public static final String DELETE_SUCCESS = "Xóa thành công!";
        public static final String LOGIN_SUCCESS = "Đăng nhập thành công!";

        // Error
        public static final String SAVE_ERROR = "Lỗi khi lưu dữ liệu!";
        public static final String LOAD_ERROR = "Lỗi khi tải dữ liệu!";
        public static final String DUPLICATE_ID = "Mã đã tồn tại!";
        public static final String INVALID_INPUT = "Dữ liệu không hợp lệ!";
        public static final String LOGIN_ERROR = "Sai tên đăng nhập hoặc mật khẩu!";

        // Warnings
        public static final String CONFIRM_DELETE = "Bạn có chắc muốn xóa?";
        public static final String CONFIRM_EXIT = "Bạn có muốn lưu dữ liệu trước khi thoát?";
        public static final String NO_SELECTION = "Vui lòng chọn một mục!";

        // Info
        public static final String EMPTY_FIELD = "Vui lòng điền đầy đủ thông tin!";
        public static final String STATUS_READY = "Sẵn sàng";
        public static final String STATUS_SAVING = "Đang lưu...";
        public static final String STATUS_LOADING = "Đang tải...";
    }

    // ==================== CREDENTIALS (Demo) ====================

    public static final class Demo {
        public static final String USERNAME = "admin";
        public static final String PASSWORD = "admin123";
        public static final String HINT = "Demo: admin / admin123";
    }

    // ==================== FILE PATHS ====================

    public static final class Paths {
        public static final String DATA_DIR = "data";
        public static final String ROOMS_FILE = "rooms.json";
        public static final String USERS_FILE = "users.json";
        public static final String CUSTOMERS_FILE = "customers.json";
        public static final String BOOKINGS_FILE = "bookings.json";
        public static final String INVOICES_FILE = "invoices.json";
    }
}
