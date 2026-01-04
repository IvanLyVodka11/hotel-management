package com.hotel.util;

/**
 * Lớp bao bọc kết quả cho các thao tác có thể thất bại.
 * Cung cấp cách trả về thành công/thất bại kèm dữ liệu và thông báo lỗi rõ
 * ràng.
 * 
 * @param <T> Kiểu dữ liệu khi thành công
 * @author OOP Project Team
 * @version 2.0
 */
public class Result<T> {

    private final boolean success; // Trạng thái thành công/thất bại
    private final T data; // Dữ liệu trả về (nếu có)
    private final String message; // Thông báo
    private final Exception exception;// Exception (nếu có)

    /**
     * Constructor private - sử dụng các factory method
     */
    private Result(boolean success, T data, String message, Exception exception) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.exception = exception;
    }

    // ==================== CÁC FACTORY METHOD ====================

    /**
     * Tạo kết quả thành công với dữ liệu
     * 
     * @param data Dữ liệu trả về
     * @return Result thành công
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(true, data, null, null);
    }

    /**
     * Tạo kết quả thành công với dữ liệu và thông báo
     * 
     * @param data    Dữ liệu trả về
     * @param message Thông báo thành công
     * @return Result thành công
     */
    public static <T> Result<T> success(T data, String message) {
        return new Result<>(true, data, message, null);
    }

    /**
     * Tạo kết quả thành công không có dữ liệu
     * 
     * @return Result thành công
     */
    public static <T> Result<T> ok() {
        return new Result<>(true, null, null, null);
    }

    /**
     * Tạo kết quả thành công với thông báo
     * 
     * @param message Thông báo thành công
     * @return Result thành công
     */
    public static <T> Result<T> ok(String message) {
        return new Result<>(true, null, message, null);
    }

    /**
     * Tạo kết quả thất bại với thông báo lỗi
     * 
     * @param message Thông báo lỗi
     * @return Result thất bại
     */
    public static <T> Result<T> failure(String message) {
        return new Result<>(false, null, message, null);
    }

    /**
     * Tạo kết quả thất bại với exception
     * 
     * @param exception Exception xảy ra
     * @return Result thất bại
     */
    public static <T> Result<T> failure(Exception exception) {
        return new Result<>(false, null, exception.getMessage(), exception);
    }

    /**
     * Tạo kết quả thất bại với thông báo và exception
     * 
     * @param message   Thông báo lỗi
     * @param exception Exception xảy ra
     * @return Result thất bại
     */
    public static <T> Result<T> failure(String message, Exception exception) {
        return new Result<>(false, null, message, exception);
    }

    // ==================== CÁC GETTER ====================

    /**
     * Kiểm tra thao tác có thành công không
     * 
     * @return true nếu thành công
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Kiểm tra thao tác có thất bại không
     * 
     * @return true nếu thất bại
     */
    public boolean isFailure() {
        return !success;
    }

    /**
     * Lấy dữ liệu trả về
     * 
     * @return Dữ liệu hoặc null
     */
    public T getData() {
        return data;
    }

    /**
     * Lấy dữ liệu hoặc giá trị mặc định
     * 
     * @param defaultValue Giá trị mặc định nếu data null
     * @return Dữ liệu hoặc giá trị mặc định
     */
    public T getDataOrDefault(T defaultValue) {
        return data != null ? data : defaultValue;
    }

    /**
     * Lấy thông báo
     * 
     * @return Thông báo hoặc null
     */
    public String getMessage() {
        return message;
    }

    /**
     * Lấy exception
     * 
     * @return Exception hoặc null
     */
    public Exception getException() {
        return exception;
    }

    // ==================== CÁC PHƯƠNG THỨC FUNCTIONAL ====================

    /**
     * Thực hiện hành động nếu thành công
     * 
     * @param action Hành động cần thực hiện với dữ liệu
     * @return Result hiện tại (để chain)
     */
    public Result<T> onSuccess(java.util.function.Consumer<T> action) {
        if (success && data != null) {
            action.accept(data);
        }
        return this;
    }

    /**
     * Thực hiện hành động nếu thất bại
     * 
     * @param action Hành động cần thực hiện với thông báo lỗi
     * @return Result hiện tại (để chain)
     */
    public Result<T> onFailure(java.util.function.Consumer<String> action) {
        if (!success) {
            action.accept(message);
        }
        return this;
    }

    /**
     * Chuyển đổi dữ liệu nếu thành công
     * 
     * @param mapper Hàm chuyển đổi
     * @return Result mới với dữ liệu đã chuyển đổi
     */
    public <U> Result<U> map(java.util.function.Function<T, U> mapper) {
        if (success && data != null) {
            return Result.success(mapper.apply(data));
        }
        return Result.failure(message, exception);
    }

    @Override
    public String toString() {
        if (success) {
            return "Result{THÀNH CÔNG" + (data != null ? ", data=" + data : "") + "}";
        } else {
            return "Result{THẤT BẠI, message='" + message + "'}";
        }
    }
}
