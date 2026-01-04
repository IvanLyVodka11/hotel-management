package com.hotel.service;

import com.hotel.model.enums.RoomStatus;
import com.hotel.model.enums.RoomType;
import com.hotel.model.room.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

/**
 * Unit tests for RoomManager service
 * Tests Singleton pattern, CRUD operations, search, and filtering
 */
class RoomManagerTest {

    private RoomManager manager;

    @BeforeEach
    void setUp() {
        // Reset singleton for clean tests
        RoomManager.resetInstance();
        manager = RoomManager.getInstance();
    }

    @AfterEach
    void tearDown() {
        manager.clear();
    }

    // ==================== Singleton Tests ====================

    @Test
    @DisplayName("RoomManager - getInstance should return same instance")
    void singletonPattern() {
        RoomManager instance1 = RoomManager.getInstance();
        RoomManager instance2 = RoomManager.getInstance();

        assertSame(instance1, instance2);
    }

    // ==================== CRUD Tests ====================

    @Test
    @DisplayName("RoomManager - add() should add new room")
    void addRoom() {
        StandardRoom room = new StandardRoom("R101", 1);

        boolean result = manager.add(room);

        assertTrue(result);
        assertEquals(1, manager.count());
        assertNotNull(manager.getById("R101"));
    }

    @Test
    @DisplayName("RoomManager - add() null room should fail")
    void addNullRoom() {
        boolean result = manager.add(null);

        assertFalse(result);
        assertEquals(0, manager.count());
    }

    @Test
    @DisplayName("RoomManager - add() duplicate ID should fail")
    void addDuplicateRoom() {
        StandardRoom room1 = new StandardRoom("R101", 1);
        StandardRoom room2 = new StandardRoom("R101", 2);

        assertTrue(manager.add(room1));
        assertFalse(manager.add(room2));
        assertEquals(1, manager.count());
    }

    @Test
    @DisplayName("RoomManager - update() should modify existing room")
    void updateRoom() {
        StandardRoom room = new StandardRoom("R101", 1);
        room.setBasePrice(500000);
        manager.add(room);

        room.setBasePrice(600000);
        room.setDescription("Updated description");
        boolean result = manager.update(room);

        assertTrue(result);
        Room updated = manager.getById("R101");
        assertEquals(600000, updated.getBasePrice());
        assertEquals("Updated description", updated.getDescription());
    }

    @Test
    @DisplayName("RoomManager - update() non-existing room should fail")
    void updateNonExistingRoom() {
        StandardRoom room = new StandardRoom("R101", 1);

        boolean result = manager.update(room);

        assertFalse(result);
    }

    @Test
    @DisplayName("RoomManager - delete() should remove room")
    void deleteRoom() {
        StandardRoom room = new StandardRoom("R101", 1);
        manager.add(room);

        boolean result = manager.delete("R101");

        assertTrue(result);
        assertEquals(0, manager.count());
        assertNull(manager.getById("R101"));
    }

    @Test
    @DisplayName("RoomManager - delete() removes room regardless of status")
    void deleteOccupiedRoom() {
        StandardRoom room = new StandardRoom("R101", 1);
        room.occupy();
        manager.add(room);

        // Note: Current implementation allows deleting any room
        boolean result = manager.delete("R101");

        assertTrue(result);
        assertEquals(0, manager.count());
    }

    @Test
    @DisplayName("RoomManager - getById() should return correct room")
    void getById() {
        manager.add(new StandardRoom("R101", 1));
        manager.add(new VIPRoom("V201", 2));
        manager.add(new DeluxeRoom("D301", 3));

        Room room = manager.getById("V201");

        assertNotNull(room);
        assertEquals("V201", room.getRoomId());
        assertEquals(RoomType.VIP, room.getRoomType());
    }

    @Test
    @DisplayName("RoomManager - getAll() should return all rooms")
    void getAll() {
        manager.add(new StandardRoom("R101", 1));
        manager.add(new VIPRoom("V201", 2));
        manager.add(new DeluxeRoom("D301", 3));

        List<Room> rooms = manager.getAll();

        assertEquals(3, rooms.size());
    }

    // ==================== Search Tests ====================

    @Test
    @DisplayName("RoomManager - search() by roomId")
    void searchByRoomId() {
        manager.add(new StandardRoom("R101", 1));
        manager.add(new StandardRoom("R102", 1));
        manager.add(new VIPRoom("V201", 2));

        List<Room> results = manager.search("R10");

        assertEquals(2, results.size());
    }

    @Test
    @DisplayName("RoomManager - search() empty keyword returns all")
    void searchEmptyKeyword() {
        manager.add(new StandardRoom("R101", 1));
        manager.add(new VIPRoom("V201", 2));

        List<Room> results = manager.search("");

        assertEquals(2, results.size());
    }

    // ==================== Filter Tests ====================

    @Test
    @DisplayName("RoomManager - findByType() should filter by room type")
    void findByType() {
        manager.add(new StandardRoom("R101", 1));
        manager.add(new StandardRoom("R102", 1));
        manager.add(new VIPRoom("V201", 2));
        manager.add(new DeluxeRoom("D301", 3));

        List<Room> standardRooms = manager.findByType(RoomType.STANDARD);
        List<Room> vipRooms = manager.findByType(RoomType.VIP);

        assertEquals(2, standardRooms.size());
        assertEquals(1, vipRooms.size());
    }

    @Test
    @DisplayName("RoomManager - findByStatus() should filter by status")
    void findByStatus() {
        StandardRoom room1 = new StandardRoom("R101", 1);
        StandardRoom room2 = new StandardRoom("R102", 1);
        room2.occupy();
        VIPRoom room3 = new VIPRoom("V201", 2);
        room3.markMaintenance();

        manager.add(room1);
        manager.add(room2);
        manager.add(room3);

        List<Room> available = manager.findByStatus(RoomStatus.AVAILABLE);
        List<Room> occupied = manager.findByStatus(RoomStatus.OCCUPIED);
        List<Room> maintenance = manager.findByStatus(RoomStatus.MAINTENANCE);

        assertEquals(1, available.size());
        assertEquals(1, occupied.size());
        assertEquals(1, maintenance.size());
    }

    @Test
    @DisplayName("RoomManager - findByFloor() should filter by floor")
    void findByFloor() {
        manager.add(new StandardRoom("R101", 1));
        manager.add(new StandardRoom("R102", 1));
        manager.add(new VIPRoom("V201", 2));
        manager.add(new DeluxeRoom("D301", 3));

        List<Room> floor1 = manager.findByFloor(1);
        List<Room> floor2 = manager.findByFloor(2);

        assertEquals(2, floor1.size());
        assertEquals(1, floor2.size());
    }

    @Test
    @DisplayName("RoomManager - findAvailableRooms() should return only available")
    void findAvailableRooms() {
        StandardRoom room1 = new StandardRoom("R101", 1);
        StandardRoom room2 = new StandardRoom("R102", 1);
        room2.occupy();
        manager.add(room1);
        manager.add(room2);

        List<Room> available = manager.findAvailableRooms();

        assertEquals(1, available.size());
        assertEquals("R101", available.get(0).getRoomId());
    }

    @Test
    @DisplayName("RoomManager - findByPriceRange() should filter by price")
    void findByPriceRange() {
        StandardRoom room1 = new StandardRoom("R101", 1);
        room1.setBasePrice(400000);
        VIPRoom room2 = new VIPRoom("V201", 2);
        room2.setBasePrice(800000);
        DeluxeRoom room3 = new DeluxeRoom("D301", 3);
        room3.setBasePrice(1200000);

        manager.add(room1);
        manager.add(room2);
        manager.add(room3);

        List<Room> midRange = manager.findByPriceRange(500000, 1000000);

        assertEquals(1, midRange.size());
        assertEquals("V201", midRange.get(0).getRoomId());
    }

    // ==================== Sorting Tests ====================

    @Test
    @DisplayName("RoomManager - sortByPriceAscending()")
    void sortByPriceAscending() {
        DeluxeRoom room1 = new DeluxeRoom("D301", 3);
        room1.setBasePrice(1500000);
        StandardRoom room2 = new StandardRoom("R101", 1);
        room2.setBasePrice(500000);
        VIPRoom room3 = new VIPRoom("V201", 2);
        room3.setBasePrice(1000000);

        manager.add(room1);
        manager.add(room2);
        manager.add(room3);

        List<Room> sorted = manager.sortByPriceAscending();

        assertEquals("R101", sorted.get(0).getRoomId());
        assertEquals("V201", sorted.get(1).getRoomId());
        assertEquals("D301", sorted.get(2).getRoomId());
    }

    // ==================== Statistics Tests ====================

    @Test
    @DisplayName("RoomManager - countByType() should return correct counts")
    void countByType() {
        manager.add(new StandardRoom("R101", 1));
        manager.add(new StandardRoom("R102", 1));
        manager.add(new VIPRoom("V201", 2));

        Map<RoomType, Long> counts = manager.countByType();

        assertEquals(2, counts.get(RoomType.STANDARD));
        assertEquals(1, counts.get(RoomType.VIP));
    }

    @Test
    @DisplayName("RoomManager - countByStatus() should return correct counts")
    void countByStatus() {
        StandardRoom room1 = new StandardRoom("R101", 1);
        StandardRoom room2 = new StandardRoom("R102", 1);
        room2.occupy();
        manager.add(room1);
        manager.add(room2);

        Map<RoomStatus, Long> counts = manager.countByStatus();

        assertEquals(1, counts.get(RoomStatus.AVAILABLE));
        assertEquals(1, counts.get(RoomStatus.OCCUPIED));
    }

    // ==================== Status Change Tests ====================

    @Test
    @DisplayName("RoomManager - occupyRoom() should change status")
    void occupyRoom() {
        StandardRoom room = new StandardRoom("R101", 1);
        manager.add(room);

        boolean result = manager.occupyRoom("R101");

        assertTrue(result);
        assertEquals(RoomStatus.OCCUPIED, manager.getById("R101").getStatus());
    }

    @Test
    @DisplayName("RoomManager - markRoomAvailable() should change status")
    void markRoomAvailable() {
        StandardRoom room = new StandardRoom("R101", 1);
        room.occupy();
        manager.add(room);

        boolean result = manager.markRoomAvailable("R101");

        assertTrue(result);
        assertEquals(RoomStatus.AVAILABLE, manager.getById("R101").getStatus());
    }
}
