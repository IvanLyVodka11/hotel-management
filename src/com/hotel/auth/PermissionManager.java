package com.hotel.auth;

import java.util.EnumSet;
import java.util.Set;

/**
 * Hệ thống phân quyền người dùng.
 * Định nghĩa các vai trò và quyền hạn tương ứng.
 * 
 * @author OOP Project Team
 * @version 2.0
 */
public class PermissionManager {

    /**
     * Các quyền trong hệ thống
     */
    public enum Permission {
        // Quản lý phòng
        VIEW_ROOMS, // Xem danh sách phòng
        MANAGE_ROOMS, // Thêm/sửa/xóa phòng

        // Quản lý khách hàng
        VIEW_CUSTOMERS, // Xem danh sách khách hàng
        MANAGE_CUSTOMERS, // Thêm/sửa/xóa khách hàng
        VERIFY_CUSTOMERS, // Xác thực khách hàng

        // Quản lý đặt phòng
        VIEW_BOOKINGS, // Xem danh sách đặt phòng
        CREATE_BOOKING, // Đăng ký phòng
        MANAGE_BOOKINGS, // Sửa/xóa đặt phòng

        // Quản lý hóa đơn
        VIEW_INVOICES, // Xem hóa đơn
        CREATE_INVOICE, // Lập hóa đơn
        MANAGE_INVOICES, // Sửa/xóa hóa đơn

        // Báo cáo
        VIEW_REPORTS, // Xem báo cáo
        EXPORT_REPORTS, // Xuất báo cáo

        // Quản lý nhân viên & tài khoản (chỉ Admin)
        MANAGE_STAFF, // Quản lý nhân viên
        MANAGE_ACCOUNTS, // Quản lý tài khoản

        // Quản lý dịch vụ
        VIEW_SERVICES, // Xem dịch vụ
        MANAGE_SERVICES, // Quản lý dịch vụ
        PROVIDE_SERVICES, // Cung cấp dịch vụ

        // Dashboard
        VIEW_DASHBOARD // Xem tổng quan
    }

    /**
     * Các vai trò trong hệ thống
     */
    public enum Role {
        ADMIN, // Quản trị viên - Full quyền
        MANAGER, // Quản lý - Gần như full quyền
        STAFF, // Nhân viên lễ tân
        SERVICE // Bộ phận dịch vụ
    }

    /**
     * Lấy danh sách quyền theo vai trò
     */
    public static Set<Permission> getPermissionsByRole(Role role) {
        return switch (role) {
            case ADMIN -> EnumSet.allOf(Permission.class); // Tất cả quyền

            case MANAGER -> EnumSet.of(
                    // Quản lý phòng
                    Permission.VIEW_ROOMS,
                    Permission.MANAGE_ROOMS,
                    // Quản lý khách hàng
                    Permission.VIEW_CUSTOMERS,
                    Permission.MANAGE_CUSTOMERS,
                    Permission.VERIFY_CUSTOMERS,
                    // Quản lý đặt phòng
                    Permission.VIEW_BOOKINGS,
                    Permission.CREATE_BOOKING,
                    Permission.MANAGE_BOOKINGS,
                    // Hóa đơn
                    Permission.VIEW_INVOICES,
                    Permission.CREATE_INVOICE,
                    Permission.MANAGE_INVOICES,
                    // Báo cáo
                    Permission.VIEW_REPORTS,
                    Permission.EXPORT_REPORTS,
                    // Dịch vụ
                    Permission.VIEW_SERVICES,
                    Permission.MANAGE_SERVICES,
                    // Dashboard
                    Permission.VIEW_DASHBOARD);

            case STAFF -> EnumSet.of(
                    // Lễ tân - theo Use Case
                    Permission.VIEW_ROOMS, // Xem phòng
                    Permission.VIEW_CUSTOMERS, // Xem khách hàng
                    Permission.VERIFY_CUSTOMERS, // Xác thực khách hàng
                    Permission.VIEW_BOOKINGS, // Xem đặt phòng
                    Permission.CREATE_BOOKING, // Đăng ký phòng
                    Permission.VIEW_INVOICES, // Xem hóa đơn
                    Permission.CREATE_INVOICE, // Lập hóa đơn
                    Permission.VIEW_DASHBOARD // Xem tổng quan
                );

            case SERVICE -> EnumSet.of(
                    // Bộ phận dịch vụ
                    Permission.VIEW_ROOMS, // Xem phòng
                    Permission.VIEW_SERVICES, // Xem dịch vụ
                    Permission.PROVIDE_SERVICES, // Cung cấp dịch vụ
                    Permission.VIEW_DASHBOARD // Xem tổng quan
                );
        };
    }

    /**
     * Chuyển đổi string role sang enum
     */
    public static Role parseRole(String roleStr) {
        if (roleStr == null)
            return Role.STAFF;
        return switch (roleStr.toUpperCase()) {
            case "ADMIN" -> Role.ADMIN;
            case "MANAGER" -> Role.MANAGER;
            case "SERVICE" -> Role.SERVICE;
            default -> Role.STAFF;
        };
    }

    /**
     * Lấy tên hiển thị của vai trò
     */
    public static String getRoleDisplayName(Role role) {
        return switch (role) {
            case ADMIN -> "Quản trị viên";
            case MANAGER -> "Quản lý";
            case STAFF -> "Nhân viên Lễ tân";
            case SERVICE -> "Bộ phận Dịch vụ";
        };
    }
}
