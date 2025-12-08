package com.hotel.service.interfaces;

import java.util.List;

/**
 * Interface định nghĩa khả năng lưu trữ dữ liệu
 * Áp dụng Generic để tái sử dụng cho nhiều loại đối tượng
 * 
 * @author Member1
 * @version 1.0
 */
public interface IStorable {
    
    /**
     * Lưu danh sách dữ liệu vào storage (file/database)
     * @param data Danh sách dữ liệu cần lưu
     * @param <T> Kiểu dữ liệu
     * @return true nếu lưu thành công
     */
    <T> boolean save(List<T> data);
    
    /**
     * Tải dữ liệu từ storage
     * @param <T> Kiểu dữ liệu
     * @return Danh sách dữ liệu hoặc danh sách rỗng nếu lỗi
     */
    <T> List<T> load();
    
    /**
     * Lấy đường dẫn/tên file lưu trữ
     * @return Đường dẫn storage
     */
    String getFilePath();
    
    /**
     * Kiểm tra file dữ liệu có tồn tại không
     * @return true nếu tồn tại
     */
    boolean exists();
    
    /**
     * Xóa file dữ liệu
     * @return true nếu xóa thành công
     */
    boolean delete();
}
