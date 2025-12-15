package com.hotel;

import com.hotel.storage.RoomStorage;
import com.hotel.service.RoomManager;
import com.hotel.model.room.Room;
import java.util.List;

/**
 * Console test để kiểm tra việc load rooms.json
 */
public class ConsoleTest {
    public static void main(String[] args) {
        System.out.println("=== CONSOLE TEST ===");
        System.out.println("Current directory: " + System.getProperty("user.dir"));
        System.out.println();
        
        System.out.println("[1] Testing RoomStorage.loadRooms()");
        RoomStorage storage = new RoomStorage();
        List<Room> rooms = storage.loadRooms();
        System.out.println("[1] ✓ Loaded " + rooms.size() + " rooms from storage");
        System.out.println();
        
        System.out.println("[2] Listing all rooms:");
        for (int i = 0; i < Math.min(5, rooms.size()); i++) {
            Room room = rooms.get(i);
            System.out.println("    " + (i+1) + ". " + room.getRoomId() + 
                             " - Type: " + room.getRoomType() + 
                             " - Status: " + room.getStatus());
        }
        if (rooms.size() > 5) {
            System.out.println("    ... và " + (rooms.size() - 5) + " phòng khác");
        }
        System.out.println();
        
        System.out.println("[3] Testing RoomManager.loadRooms()");
        RoomManager manager = RoomManager.getInstance();
        manager.loadRooms(rooms);
        List<Room> managerRooms = manager.getAll();
        System.out.println("[3] ✓ RoomManager has " + managerRooms.size() + " rooms");
        System.out.println();
        
        System.out.println("=== TEST COMPLETE ===");
    }
}
