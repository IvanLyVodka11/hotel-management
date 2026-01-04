package com.hotel.model.room;

import com.hotel.model.enums.RoomStatus;
import com.hotel.model.enums.RoomType;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Room classes (StandardRoom, VIPRoom, DeluxeRoom)
 * Tests inheritance, polymorphism, and encapsulation
 */
class RoomTest {

    // ==================== StandardRoom Tests ====================

    @Test
    @DisplayName("StandardRoom - Default constructor should set default values")
    void standardRoomDefaultConstructor() {
        StandardRoom room = new StandardRoom();

        assertNotNull(room);
        assertEquals(RoomType.STANDARD, room.getRoomType());
        assertEquals(StandardRoom.DEFAULT_BASE_PRICE, room.getBasePrice());
        assertEquals(StandardRoom.DEFAULT_BED_COUNT, room.getBedCount());
    }

    @Test
    @DisplayName("StandardRoom - Constructor with roomId and floor")
    void standardRoomConstructorWithIdAndFloor() {
        StandardRoom room = new StandardRoom("R101", 1);

        assertEquals("R101", room.getRoomId());
        assertEquals(1, room.getFloor());
        assertEquals(RoomStatus.AVAILABLE, room.getStatus());
    }

    @Test
    @DisplayName("StandardRoom - calculatePrice should return basePrice * days")
    void standardRoomCalculatePrice() {
        StandardRoom room = new StandardRoom("R101", 1);
        room.setBasePrice(500000);

        double price = room.calculatePrice(3);

        assertEquals(1500000, price, 0.01);
    }

    @Test
    @DisplayName("StandardRoom - calculatePrice with 0 or negative days should throw exception")
    void standardRoomCalculatePriceInvalidDays() {
        StandardRoom room = new StandardRoom("R101", 1);

        assertThrows(IllegalArgumentException.class, () -> room.calculatePrice(0));
        assertThrows(IllegalArgumentException.class, () -> room.calculatePrice(-1));
    }

    // ==================== VIPRoom Tests ====================

    @Test
    @DisplayName("VIPRoom - Should have 1.2x price multiplier")
    void vipRoomPriceMultiplier() {
        VIPRoom room = new VIPRoom("V201", 2);
        room.setBasePrice(1000000);

        double price = room.calculatePrice(2);

        // 1000000 * 2 * 1.2 = 2400000
        assertEquals(2400000, price, 0.01);
    }

    @Test
    @DisplayName("VIPRoom - getRoomType should return VIP")
    void vipRoomType() {
        VIPRoom room = new VIPRoom();
        assertEquals(RoomType.VIP, room.getRoomType());
    }

    @Test
    @DisplayName("VIPRoom - Default has view and private bathroom")
    void vipRoomDefaultAmenities() {
        VIPRoom room = new VIPRoom();

        assertTrue(room.hasView());
        assertTrue(room.hasPrivateBathroom());
    }

    @Test
    @DisplayName("VIPRoom - canAccommodate should check max occupancy")
    void vipRoomCanAccommodate() {
        VIPRoom room = new VIPRoom();

        assertTrue(room.canAccommodate(1));
        assertTrue(room.canAccommodate(3));
        assertFalse(room.canAccommodate(4));
        assertFalse(room.canAccommodate(0));
    }

    // ==================== DeluxeRoom Tests ====================

    @Test
    @DisplayName("DeluxeRoom - Should have 1.5x price multiplier")
    void deluxeRoomPriceMultiplier() {
        DeluxeRoom room = new DeluxeRoom("D301", 3);
        room.setBasePrice(1500000);

        double price = room.calculatePrice(2);

        // 1500000 * 2 * 1.5 = 4500000
        assertEquals(4500000, price, 0.01);
    }

    @Test
    @DisplayName("DeluxeRoom - getRoomType should return DELUXE")
    void deluxeRoomType() {
        DeluxeRoom room = new DeluxeRoom();
        assertEquals(RoomType.DELUXE, room.getRoomType());
    }

    // ==================== Room Status Tests ====================

    @Test
    @DisplayName("Room - Status changes should work correctly")
    void roomStatusChanges() {
        StandardRoom room = new StandardRoom("R101", 1);

        // Initial status
        assertTrue(room.isAvailable());
        assertEquals(RoomStatus.AVAILABLE, room.getStatus());

        // Occupy
        room.occupy();
        assertFalse(room.isAvailable());
        assertEquals(RoomStatus.OCCUPIED, room.getStatus());

        // Release (to cleaning)
        room.release();
        assertEquals(RoomStatus.CLEANING, room.getStatus());

        // Mark available
        room.markAvailable();
        assertTrue(room.isAvailable());

        // Mark maintenance
        room.markMaintenance();
        assertEquals(RoomStatus.MAINTENANCE, room.getStatus());
        assertFalse(room.isAvailable());
    }

    // ==================== Validation Tests ====================

    @Test
    @DisplayName("Room - setRoomId with null or empty should throw exception")
    void roomIdValidation() {
        StandardRoom room = new StandardRoom();

        assertThrows(IllegalArgumentException.class, () -> room.setRoomId(null));
        assertThrows(IllegalArgumentException.class, () -> room.setRoomId(""));
        assertThrows(IllegalArgumentException.class, () -> room.setRoomId("   "));
    }

    @Test
    @DisplayName("Room - setFloor with invalid value should throw exception")
    void roomFloorValidation() {
        StandardRoom room = new StandardRoom();

        assertThrows(IllegalArgumentException.class, () -> room.setFloor(0));
        assertThrows(IllegalArgumentException.class, () -> room.setFloor(-1));
    }

    @Test
    @DisplayName("Room - setBasePrice with negative should throw exception")
    void roomBasePriceValidation() {
        StandardRoom room = new StandardRoom();

        assertThrows(IllegalArgumentException.class, () -> room.setBasePrice(-100));
    }

    @Test
    @DisplayName("Room - setBedCount with zero or negative should throw exception")
    void roomBedCountValidation() {
        StandardRoom room = new StandardRoom();

        assertThrows(IllegalArgumentException.class, () -> room.setBedCount(0));
        assertThrows(IllegalArgumentException.class, () -> room.setBedCount(-1));
    }

    // ==================== Equals and HashCode Tests ====================

    @Test
    @DisplayName("Room - Rooms with same ID should be equal")
    void roomEquality() {
        StandardRoom room1 = new StandardRoom("R101", 1);
        StandardRoom room2 = new StandardRoom("R101", 2); // Different floor, same ID
        StandardRoom room3 = new StandardRoom("R102", 1);

        assertEquals(room1, room2);
        assertNotEquals(room1, room3);
        assertEquals(room1.hashCode(), room2.hashCode());
    }

    // ==================== Polymorphism Tests ====================

    @Test
    @DisplayName("Polymorphism - Different room types should calculate prices differently")
    void polymorphismPriceCalculation() {
        Room standard = new StandardRoom("R101", 1);
        Room vip = new VIPRoom("V201", 2);
        Room deluxe = new DeluxeRoom("D301", 3);

        // Set same base price
        standard.setBasePrice(1000000);
        vip.setBasePrice(1000000);
        deluxe.setBasePrice(1000000);

        int days = 1;

        // Standard: 1x, VIP: 1.2x, Deluxe: 1.5x
        assertEquals(1000000, standard.calculatePrice(days), 0.01);
        assertEquals(1200000, vip.calculatePrice(days), 0.01);
        assertEquals(1500000, deluxe.calculatePrice(days), 0.01);
    }
}
