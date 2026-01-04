package com.hotel.auth;

import com.hotel.auth.PermissionManager.Permission;
import com.hotel.auth.PermissionManager.Role;

import java.util.Set;

/**
 * Lưu trữ thông tin phiên đăng nhập hiện tại.
 * Singleton pattern để truy cập từ mọi nơi trong ứng dụng.
 * 
 * @author OOP Project Team
 * @version 2.0
 */
public class UserSession {

    private static UserSession instance;

    private String userId;
    private String username;
    private String displayName;
    private Role role;
    private Set<Permission> permissions;
    private boolean loggedIn;

    private UserSession() {
        this.loggedIn = false;
    }

    /**
     * Lấy instance duy nhất của UserSession
     */
    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    /**
     * Đăng nhập với thông tin người dùng
     */
    public void login(String userId, String username, String displayName, String roleStr) {
        this.userId = userId;
        this.username = username;
        this.displayName = displayName;
        this.role = PermissionManager.parseRole(roleStr);
        this.permissions = PermissionManager.getPermissionsByRole(this.role);
        this.loggedIn = true;
    }

    /**
     * Đăng xuất
     */
    public void logout() {
        this.userId = null;
        this.username = null;
        this.displayName = null;
        this.role = null;
        this.permissions = null;
        this.loggedIn = false;
    }

    /**
     * Kiểm tra đã đăng nhập chưa
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Kiểm tra có quyền cụ thể không
     * Nếu chưa đăng nhập (dev mode với --skip-login), cho phép tất cả
     */
    public boolean hasPermission(Permission permission) {
        // Dev mode: nếu không login, cho phép tất cả
        if (!loggedIn) {
            return true;
        }
        return permissions != null && permissions.contains(permission);
    }

    /**
     * Kiểm tra có bất kỳ quyền nào trong danh sách không
     */
    public boolean hasAnyPermission(Permission... checkPermissions) {
        if (!loggedIn || permissions == null)
            return false;
        for (Permission p : checkPermissions) {
            if (permissions.contains(p))
                return true;
        }
        return false;
    }

    /**
     * Kiểm tra có phải Admin hoặc Manager không
     */
    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    public boolean isManager() {
        return role == Role.MANAGER || role == Role.ADMIN;
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Role getRole() {
        return role;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    /**
     * Lấy tên vai trò hiển thị
     */
    public String getRoleDisplayName() {
        return role != null ? PermissionManager.getRoleDisplayName(role) : "Chưa đăng nhập";
    }
}
