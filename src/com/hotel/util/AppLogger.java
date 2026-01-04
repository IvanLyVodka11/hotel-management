package com.hotel.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Lớp tiện ích ghi log cho ứng dụng Quản lý Khách sạn.
 * Hỗ trợ các mức độ log và xuất ra file.
 * 
 * @author OOP Project Team
 * @version 2.0
 */
public final class AppLogger {

    /**
     * Các mức độ log
     */
    public enum Level {
        DEBUG, // Thông tin chi tiết để debug
        INFO, // Thông tin thông thường
        WARN, // Cảnh báo
        ERROR // Lỗi
    }

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String LOG_DIR = "logs";
    private static final String LOG_FILE = "hotel.log";

    private static Level currentLevel = Level.INFO;
    private static boolean consoleLogging = true;
    private static boolean fileLogging = true;

    // Không cho phép tạo instance
    private AppLogger() {
    }

    // ==================== CẤU HÌNH ====================

    /**
     * Thiết lập mức độ log tối thiểu
     * 
     * @param level Mức độ log tối thiểu sẽ được ghi
     */
    public static void setLevel(Level level) {
        currentLevel = level;
    }

    /**
     * Bật/tắt ghi log ra console
     * 
     * @param enabled true để bật, false để tắt
     */
    public static void setConsoleLogging(boolean enabled) {
        consoleLogging = enabled;
    }

    /**
     * Bật/tắt ghi log ra file
     * 
     * @param enabled true để bật, false để tắt
     */
    public static void setFileLogging(boolean enabled) {
        fileLogging = enabled;
    }

    // ==================== CÁC PHƯƠNG THỨC GHI LOG ====================

    /**
     * Ghi log mức DEBUG
     * 
     * @param message Nội dung log
     */
    public static void debug(String message) {
        log(Level.DEBUG, message, null);
    }

    /**
     * Ghi log mức DEBUG với định dạng
     * 
     * @param message Nội dung log (có thể chứa %s, %d...)
     * @param args    Các tham số thay thế
     */
    public static void debug(String message, Object... args) {
        log(Level.DEBUG, String.format(message, args), null);
    }

    /**
     * Ghi log mức INFO
     * 
     * @param message Nội dung log
     */
    public static void info(String message) {
        log(Level.INFO, message, null);
    }

    /**
     * Ghi log mức INFO với định dạng
     * 
     * @param message Nội dung log (có thể chứa %s, %d...)
     * @param args    Các tham số thay thế
     */
    public static void info(String message, Object... args) {
        log(Level.INFO, String.format(message, args), null);
    }

    /**
     * Ghi log mức WARN (cảnh báo)
     * 
     * @param message Nội dung cảnh báo
     */
    public static void warn(String message) {
        log(Level.WARN, message, null);
    }

    /**
     * Ghi log mức WARN với định dạng
     * 
     * @param message Nội dung cảnh báo (có thể chứa %s, %d...)
     * @param args    Các tham số thay thế
     */
    public static void warn(String message, Object... args) {
        log(Level.WARN, String.format(message, args), null);
    }

    /**
     * Ghi log mức WARN kèm exception
     * 
     * @param message   Nội dung cảnh báo
     * @param throwable Exception cần log
     */
    public static void warn(String message, Throwable throwable) {
        log(Level.WARN, message, throwable);
    }

    /**
     * Ghi log mức ERROR
     * 
     * @param message Nội dung lỗi
     */
    public static void error(String message) {
        log(Level.ERROR, message, null);
    }

    /**
     * Ghi log mức ERROR với định dạng
     * 
     * @param message Nội dung lỗi (có thể chứa %s, %d...)
     * @param args    Các tham số thay thế
     */
    public static void error(String message, Object... args) {
        log(Level.ERROR, String.format(message, args), null);
    }

    /**
     * Ghi log mức ERROR kèm exception
     * 
     * @param message   Nội dung lỗi
     * @param throwable Exception cần log
     */
    public static void error(String message, Throwable throwable) {
        log(Level.ERROR, message, throwable);
    }

    // ==================== XỬ LÝ LOG CHÍNH ====================

    /**
     * Xử lý ghi log
     */
    private static void log(Level level, String message, Throwable throwable) {
        if (level.ordinal() < currentLevel.ordinal()) {
            return;
        }

        String timestamp = LocalDateTime.now().format(TIME_FORMAT);
        String levelStr = String.format("%-5s", level.name());
        String logLine = String.format("[%s] [%s] %s", timestamp, levelStr, message);

        // Xuất ra console
        if (consoleLogging) {
            if (level == Level.ERROR) {
                System.err.println(logLine);
            } else {
                System.out.println(logLine);
            }
            if (throwable != null) {
                throwable.printStackTrace(level == Level.ERROR ? System.err : System.out);
            }
        }

        // Xuất ra file
        if (fileLogging) {
            writeToFile(logLine, throwable);
        }
    }

    /**
     * Ghi log vào file
     */
    private static void writeToFile(String logLine, Throwable throwable) {
        try {
            Path logDir = Paths.get(LOG_DIR);
            if (!Files.exists(logDir)) {
                Files.createDirectories(logDir);
            }

            Path logFile = logDir.resolve(LOG_FILE);
            String content = logLine + System.lineSeparator();

            if (throwable != null) {
                StringBuilder sb = new StringBuilder(content);
                sb.append("  Exception: ").append(throwable.getClass().getName())
                        .append(": ").append(throwable.getMessage()).append(System.lineSeparator());
                for (StackTraceElement element : throwable.getStackTrace()) {
                    sb.append("    at ").append(element.toString()).append(System.lineSeparator());
                }
                content = sb.toString();
            }

            Files.writeString(logFile, content,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            // Fallback ra console nếu ghi file thất bại
            System.err.println("Không thể ghi vào file log: " + e.getMessage());
        }
    }

    // ==================== CÁC PHƯƠNG THỨC TIỆN ÍCH ====================

    /**
     * Log khi vào một method (dùng để debug)
     * 
     * @param className  Tên class
     * @param methodName Tên method
     */
    public static void entering(String className, String methodName) {
        debug("VÀO: %s.%s()", className, methodName);
    }

    /**
     * Log khi thoát method (dùng để debug)
     * 
     * @param className  Tên class
     * @param methodName Tên method
     */
    public static void exiting(String className, String methodName) {
        debug("THOÁT: %s.%s()", className, methodName);
    }

    /**
     * Log một thao tác thành công
     * 
     * @param operation Tên thao tác
     */
    public static void success(String operation) {
        info("THÀNH CÔNG: %s", operation);
    }

    /**
     * Log một thao tác thất bại
     * 
     * @param operation Tên thao tác
     * @param cause     Nguyên nhân thất bại
     */
    public static void failure(String operation, Throwable cause) {
        error("THẤT BẠI: %s", operation);
        if (cause != null) {
            error("  Nguyên nhân: %s", cause.getMessage());
        }
    }
}
