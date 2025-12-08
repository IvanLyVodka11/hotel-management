package com.hotel.service.interfaces;

import java.util.List;
import java.util.Map;

/**
 * Interface định nghĩa khả năng tìm kiếm
 * 
 * @param <T> Kiểu đối tượng được tìm kiếm
 * @author Member1
 * @version 1.0
 */
public interface ISearchable<T> {
    
    /**
     * Tìm kiếm theo từ khóa (search chung)
     * @param keyword Từ khóa tìm kiếm
     * @return Danh sách kết quả
     */
    List<T> search(String keyword);
    
    /**
     * Lọc theo nhiều tiêu chí
     * @param criteria Map chứa các tiêu chí lọc (key: tên field, value: giá trị)
     * @return Danh sách kết quả
     */
    List<T> filter(Map<String, Object> criteria);
}
