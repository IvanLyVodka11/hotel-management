# BACKLOG & SPRINT PLANNING - THÀNH VIÊN 1
## Quản lý Phòng (Room Management)

---

## 📋 PRODUCT BACKLOG

### Epic 1: Room Model Layer
| ID | User Story | Acceptance Criteria | Points |
|----|------------|---------------------|--------|
| US-1.1 | Tạo abstract class Room | - Private fields, public getters/setters<br>- Abstract methods: calculatePrice(), getRoomType() | 3 |
| US-1.2 | Tạo StandardRoom extends Room | - Override calculatePrice(): basePrice * days<br>- Override getRoomType() | 2 |
| US-1.3 | Tạo VIPRoom extends Room | - Override calculatePrice(): basePrice * days * 1.2<br>- Override getRoomType() | 2 |
| US-1.4 | Tạo DeluxeRoom extends Room | - Override calculatePrice(): basePrice * days * 1.5<br>- Override getRoomType() | 2 |
| US-1.5 | Tạo RoomType enum | - STANDARD, VIP, DELUXE | 1 |
| US-1.6 | Tạo RoomStatus enum | - AVAILABLE, OCCUPIED, MAINTENANCE, CLEANING | 1 |

### Epic 2: Interfaces
| ID | User Story | Acceptance Criteria | Points |
|----|------------|---------------------|--------|
| US-2.1 | Tạo IManageable<T> interface | - add(), update(), delete(), getById(), getAll() | 2 |
| US-2.2 | Tạo IStorable interface | - save(), load() | 1 |
| US-2.3 | Tạo ISearchable<T> interface | - search(), filter() | 1 |

### Epic 3: Room Business Logic
| ID | User Story | Acceptance Criteria | Points |
|----|------------|---------------------|--------|
| US-3.1 | RoomManager implements interfaces | - CRUD operations cho Room<br>- List<Room> storage | 5 |
| US-3.2 | RoomFactory tạo Room objects | - createRoom(type, id, floor, price) | 3 |
| US-3.3 | Search Room by criteria | - By ID, Type, Floor, Status | 3 |
| US-3.4 | Sort Room | - By price, ID, floor | 2 |

### Epic 4: Room Storage
| ID | User Story | Acceptance Criteria | Points |
|----|------------|---------------------|--------|
| US-4.1 | JsonStorage cho Room | - Save List<Room> to JSON<br>- Load from JSON | 5 |
| US-4.2 | FileHandler utility | - Read/Write file operations | 2 |

### Epic 5: Room UI
| ID | User Story | Acceptance Criteria | Points |
|----|------------|---------------------|--------|
| US-5.1 | RoomPanel main view | - JTable hiển thị rooms<br>- Toolbar với buttons | 8 |
| US-5.2 | AddRoomDialog | - Form thêm phòng mới<br>- Validation | 5 |
| US-5.3 | EditRoomDialog | - Form sửa thông tin phòng | 5 |
| US-5.4 | DeleteRoom confirmation | - Confirm dialog trước khi xóa | 2 |
| US-5.5 | SearchRoomDialog | - Search form với multiple criteria | 4 |
| US-5.6 | RoomReportPanel | - Thống kê số phòng theo loại/trạng thái | 5 |

### Epic 6: Testing
| ID | User Story | Acceptance Criteria | Points |
|----|------------|---------------------|--------|
| US-6.1 | Unit test Room classes | - Test constructors, methods | 3 |
| US-6.2 | Unit test RoomManager | - Test CRUD operations | 3 |
| US-6.3 | Unit test RoomFactory | - Test factory method | 2 |

---

## 🏃 SPRINT BREAKDOWN

### SPRINT 1: Foundation (Tuần 1-2)
**Goal**: Hoàn thiện tất cả Model classes và Interfaces

#### Tasks:
- [x] Task 1.1: Tạo project structure
- [ ] Task 1.2: Implement RoomType enum
- [ ] Task 1.3: Implement RoomStatus enum  
- [ ] Task 1.4: Implement abstract Room class
- [ ] Task 1.5: Implement StandardRoom
- [ ] Task 1.6: Implement VIPRoom
- [ ] Task 1.7: Implement DeluxeRoom
- [ ] Task 1.8: Implement IManageable interface
- [ ] Task 1.9: Implement IStorable interface
- [ ] Task 1.10: Implement ISearchable interface
- [ ] Task 1.11: Write unit tests cho Room classes

**Definition of Done**:
- Tất cả classes compile không lỗi
- Unit tests pass 100%
- Code review completed

---

### SPRINT 2: Business Logic (Tuần 3-4)
**Goal**: Hoàn thiện Manager và Storage layer

#### Tasks:
- [ ] Task 2.1: Implement RoomFactory
- [ ] Task 2.2: Implement RoomManager
- [ ] Task 2.3: Implement JsonStorage (phần Room)
- [ ] Task 2.4: Implement FileHandler utility
- [ ] Task 2.5: Implement Constants class
- [ ] Task 2.6: Implement Validator utility
- [ ] Task 2.7: Write unit tests cho Manager
- [ ] Task 2.8: Integration test với JSON storage

**Definition of Done**:
- CRUD operations hoạt động qua console
- Data persist sau khi restart
- Unit tests pass 100%

---

### SPRINT 3: UI Development (Tuần 5-6)
**Goal**: Hoàn thiện UI cho Room Management

#### Tasks:
- [ ] Task 3.1: Implement RoomPanel
- [ ] Task 3.2: Implement RoomTable component
- [ ] Task 3.3: Implement AddRoomDialog
- [ ] Task 3.4: Implement EditRoomDialog
- [ ] Task 3.5: Connect UI với RoomManager
- [ ] Task 3.6: Implement delete confirmation
- [ ] Task 3.7: UI testing

**Definition of Done**:
- UI hiển thị đúng data
- CRUD từ UI hoạt động
- Data persist khi thao tác từ UI

---

### SPRINT 4: Polish & Reports (Tuần 7-8)
**Goal**: Search, Report và hoàn thiện

#### Tasks:
- [ ] Task 4.1: Implement SearchRoomDialog
- [ ] Task 4.2: Implement Room filter functionality
- [ ] Task 4.3: Implement Sort functionality
- [ ] Task 4.4: Implement RoomReportPanel
- [ ] Task 4.5: Final testing
- [ ] Task 4.6: Bug fixes
- [ ] Task 4.7: Documentation

**Definition of Done**:
- Tất cả features hoạt động
- No critical bugs
- Documentation hoàn thiện

---

## 📊 VELOCITY TRACKING

| Sprint | Planned | Completed | Notes |
|--------|---------|-----------|-------|
| Sprint 1 | 14 pts | - | |
| Sprint 2 | 15 pts | - | |
| Sprint 3 | 20 pts | - | |
| Sprint 4 | 14 pts | - | |
| **Total** | **63 pts** | - | |

---

## ✅ DAILY CHECKLIST

### Trước khi code:
- [ ] Đọc User Story và Acceptance Criteria
- [ ] Viết test cases trước (TDD)
- [ ] Design class/method signature

### Trong khi code:
- [ ] Follow naming conventions
- [ ] Private fields, public methods
- [ ] Add comments cho complex logic
- [ ] Handle exceptions properly

### Sau khi code:
- [ ] Run unit tests
- [ ] Self code review
- [ ] Commit với meaningful message
- [ ] Update task status

---

## 🔗 INTEGRATION POINTS VỚI THÀNH VIÊN 2

| My Component | Their Component | Integration |
|--------------|-----------------|-------------|
| Room | Booking | Booking chứa Room reference |
| RoomManager | BookingManager | Check room availability |
| RoomPanel | MainFrame | Navigation từ menu |
| JsonStorage | JsonStorage | Shared file handling |

---

**Last Updated**: 08/12/2024
**Status**: Ready for Sprint 1
