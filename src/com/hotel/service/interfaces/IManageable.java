package com.hotel.service.interfaces;

import java.util.List;

/**
 * Interface định nghĩa các thao tác quản lý cơ bản (CRUD)
 * Áp dụng Generic để tái sử dụng cho nhiều loại đối tượng
 * 
 * @param <T> Kiểu đối tượng được quản lý
 * @author Member1
 * @version 1.0
 */
public interface IManageable<T> {
    
    /**
     * Thêm đối tượng mới vào hệ thống
     * @param item Đối tượng cần thêm
     * @return true nếu thêm thành công, false nếu thất bại
     */
    boolean add(T item);
    
    /**
     * Cập nhật thông tin đối tượng
     * @param item Đối tượng với thông tin mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean update(T item);
    
    /**
     * Xóa đối tượng theo ID
     * @param id ID của đối tượng cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean delete(String id);
    
    /**
     * Tìm đối tượng theo ID
     * @param id ID của đối tượng cần tìm
     * @return Đối tượng tìm được hoặc null nếu không tìm thấy
     */
    T getById(String id);
    
    /**
     * Lấy tất cả đối tượng trong hệ thống
     * @return Danh sách tất cả đối tượng
     */
    List<T> getAll();
    
    /**
     * Đếm số lượng đối tượng
     * @return Số lượng đối tượng
     */
    int count();
    
    /**
     * Kiểm tra danh sách có rỗng không
     * @return true nếu rỗng
     */
    boolean isEmpty();
    
    /**
     * Xóa tất cả đối tượng
     */
    void clear();
}
