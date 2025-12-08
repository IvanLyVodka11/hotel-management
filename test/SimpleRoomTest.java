package test;

import com.hotel.model.room.*;
import com.hotel.model.enums.*;
import com.hotel.service.RoomManager;

/**
 * Lớp kiểm thử đơn giản cho các chức năng Room
 * Không cần JUnit - chạy trực tiếp bằng main method
 * 
 * @author Member1
 * @version 1.0
 */
public class SimpleRoomTest {
    
    private static int passed = 0;
    private static int failed = 0;
    
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║           KIỂM THỬ HỆ THỐNG QUẢN LÝ PHÒNG                ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝\n");
        
        // Test StandardRoom
        testStandardRoom();
        
        // Test VIPRoom
        testVIPRoom();
        
        // Test DeluxeRoom
        testDeluxeRoom();
        
        // Test RoomFactory
        testRoomFactory();
        
        // Test RoomManager
        testRoomManager();
        
        // Kết quả
        System.out.println("\n══════════════════════════════════════════════════════════");
        System.out.println("KẾT QUẢ KIỂM THỬ:");
        System.out.println("  ✓ Passed: " + passed);
        System.out.println("  ✗ Failed: " + failed);
        System.out.println("  Total: " + (passed + failed));
        System.out.println("══════════════════════════════════════════════════════════");
        
        if (failed > 0) {
            System.exit(1);
        }
    }
    
    // ==================== TEST METHODS ====================
    
    private static void testStandardRoom() {
        System.out.println("📋 Testing StandardRoom...");
        
        StandardRoom room = new StandardRoom("R101", 1);
        
        // Test giá trị mặc định
        assertEquals("R101", room.getRoomId(), "Room ID");
        assertEquals(1, room.getFloor(), "Floor");
        assertEquals(500000.0, room.getBasePrice(), "Base Price");
        assertEquals(RoomStatus.AVAILABLE, room.getStatus(), "Status");
        assertEquals(RoomType.STANDARD, room.getRoomType(), "Room Type");
        
        // Test tính giá
        assertEquals(500000.0, room.calculatePrice(1), "Calculate price for 1 day");
        assertEquals(1000000.0, room.calculatePrice(2), "Calculate price for 2 days");
        
        // Test trạng thái
        assertTrue(room.isAvailable(), "Room should be available");
        room.occupy();
        assertFalse(room.isAvailable(), "Room should not be available after occupy");
        assertEquals(RoomStatus.OCCUPIED, room.getStatus(), "Status after occupy");
        
        room.release();
        assertEquals(RoomStatus.CLEANING, room.getStatus(), "Status after release");
        
        room.markAvailable();
        assertTrue(room.isAvailable(), "Room should be available after markAvailable");
        
        // Test capacity
        assertTrue(room.canAccommodate(2), "Can accommodate 2");
        assertFalse(room.canAccommodate(3), "Cannot accommodate 3");
        
        System.out.println();
    }
    
    private static void testVIPRoom() {
        System.out.println("📋 Testing VIPRoom...");
        
        VIPRoom room = new VIPRoom("R201", 2);
        
        // Test giá trị mặc định
        assertEquals("R201", room.getRoomId(), "Room ID");
        assertEquals(1000000.0, room.getBasePrice(), "Base Price");
        assertEquals(RoomType.VIP, room.getRoomType(), "Room Type");
        
        // Test tính giá với hệ số 1.2
        double expected = 1000000 * 1.2;
        assertEquals(expected, room.calculatePrice(1), "Calculate price for 1 day");
        
        // Test thuộc tính riêng
        assertTrue(room.hasView(), "Default has view");
        assertTrue(room.hasPrivateBathroom(), "Default has private bathroom");
        
        room.setHasView(false);
        assertFalse(room.hasView(), "Has view after set false");
        
        // Test capacity
        assertTrue(room.canAccommodate(3), "Can accommodate 3");
        assertFalse(room.canAccommodate(4), "Cannot accommodate 4");
        
        System.out.println();
    }
    
    private static void testDeluxeRoom() {
        System.out.println("📋 Testing DeluxeRoom...");
        
        DeluxeRoom room = new DeluxeRoom("R301", 3);
        
        // Test giá trị mặc định
        assertEquals("R301", room.getRoomId(), "Room ID");
        assertEquals(1500000.0, room.getBasePrice(), "Base Price");
        assertEquals(RoomType.DELUXE, room.getRoomType(), "Room Type");
        
        // Test tính giá với hệ số 1.5
        double expected = 1500000 * 1.5;
        assertEquals(expected, room.calculatePrice(1), "Calculate price for 1 day");
        
        // Test thuộc tính riêng
        assertTrue(room.hasJacuzzi(), "Default has jacuzzi");
        assertTrue(room.hasMinibar(), "Default has minibar");
        assertTrue(room.hasLivingRoom(), "Default has living room");
        
        // Test capacity
        assertTrue(room.canAccommodate(4), "Can accommodate 4");
        assertFalse(room.canAccommodate(5), "Cannot accommodate 5");
        
        System.out.println();
    }
    
    private static void testRoomFactory() {
        System.out.println("📋 Testing RoomFactory...");
        
        // Test tạo từng loại phòng
        Room standard = RoomFactory.createRoom(RoomType.STANDARD, "F101", 1);
        assertTrue(standard instanceof StandardRoom, "Factory creates StandardRoom");
        assertEquals(RoomType.STANDARD, standard.getRoomType(), "StandardRoom type");
        
        Room vip = RoomFactory.createRoom(RoomType.VIP, "F201", 2);
        assertTrue(vip instanceof VIPRoom, "Factory creates VIPRoom");
        assertEquals(RoomType.VIP, vip.getRoomType(), "VIPRoom type");
        
        Room deluxe = RoomFactory.createRoom(RoomType.DELUXE, "F301", 3);
        assertTrue(deluxe instanceof DeluxeRoom, "Factory creates DeluxeRoom");
        assertEquals(RoomType.DELUXE, deluxe.getRoomType(), "DeluxeRoom type");
        
        System.out.println();
    }
    
    private static void testRoomManager() {
        System.out.println("📋 Testing RoomManager...");
        
        RoomManager manager = RoomManager.getInstance();
        manager.clear(); // Xóa dữ liệu cũ
        
        // Test Singleton
        RoomManager manager2 = RoomManager.getInstance();
        assertTrue(manager == manager2, "Singleton pattern");
        
        // Test thêm phòng
        StandardRoom room1 = new StandardRoom("M101", 1);
        assertTrue(manager.add(room1), "Add room successfully");
        assertEquals(1, manager.count(), "Count after add");
        
        // Test tìm phòng
        Room found = manager.getById("M101");
        assertNotNull(found, "Find room by ID");
        assertEquals("M101", found.getRoomId(), "Found room ID matches");
        
        // Test thêm phòng trùng ID
        StandardRoom duplicateRoom = new StandardRoom("M101", 2);
        assertFalse(manager.add(duplicateRoom), "Cannot add duplicate ID");
        
        // Test cập nhật
        room1.setDescription("Updated description");
        assertTrue(manager.update(room1), "Update room");
        
        // Test xóa
        assertTrue(manager.delete("M101"), "Delete room");
        assertEquals(0, manager.count(), "Count after delete");
        assertTrue(manager.isEmpty(), "Manager is empty");
        
        System.out.println();
    }
    
    // ==================== ASSERTION METHODS ====================
    
    private static void assertEquals(Object expected, Object actual, String message) {
        if (expected.equals(actual)) {
            System.out.println("  ✓ " + message);
            passed++;
        } else {
            System.out.println("  ✗ " + message + " - Expected: " + expected + ", Got: " + actual);
            failed++;
        }
    }
    
    private static void assertEquals(double expected, double actual, String message) {
        if (Math.abs(expected - actual) < 0.001) {
            System.out.println("  ✓ " + message);
            passed++;
        } else {
            System.out.println("  ✗ " + message + " - Expected: " + expected + ", Got: " + actual);
            failed++;
        }
    }
    
    private static void assertTrue(boolean condition, String message) {
        if (condition) {
            System.out.println("  ✓ " + message);
            passed++;
        } else {
            System.out.println("  ✗ " + message + " - Expected: true, Got: false");
            failed++;
        }
    }
    
    private static void assertFalse(boolean condition, String message) {
        if (!condition) {
            System.out.println("  ✓ " + message);
            passed++;
        } else {
            System.out.println("  ✗ " + message + " - Expected: false, Got: true");
            failed++;
        }
    }
    
    private static void assertNotNull(Object obj, String message) {
        if (obj != null) {
            System.out.println("  ✓ " + message);
            passed++;
        } else {
            System.out.println("  ✗ " + message + " - Expected: not null, Got: null");
            failed++;
        }
    }
}
