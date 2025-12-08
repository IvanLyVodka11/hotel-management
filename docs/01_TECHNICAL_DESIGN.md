# TÀI LIỆU THIẾT KẾ KỸ THUẬT
## Dự án: Hệ thống Quản lý Khách sạn (Hotel Management System)

---

## 1. TỔNG QUAN DỰ ÁN

### 1.1 Mục tiêu
Xây dựng hệ thống quản lý khách sạn với các chức năng:
- Quản lý phòng (Room Management)
- Quản lý đặt phòng và khách hàng (Booking & Customer Management)
- Đăng nhập hệ thống
- Báo cáo thống kê

### 1.2 Công nghệ sử dụng
| Thành phần | Công nghệ |
|------------|-----------|
| Ngôn ngữ | Java 17+ |
| UI Framework | Java Swing |
| Data Storage | JSON File |
| Build Tool | Maven |
| Testing | JUnit 5 |

### 1.3 Phân công nhóm
| Thành viên | Nhiệm vụ |
|------------|----------|
| Thành viên 1 | Quản lý Phòng, Menu chính, Storage, Login |
| Thành viên 2 | Quản lý Đặt phòng, Khách hàng, Hóa đơn, Báo cáo |

---

## 2. KIẾN TRÚC HỆ THỐNG

### 2.1 Architecture Pattern: MVC (Model-View-Controller)

```
┌─────────────────────────────────────────────────────────────┐
│                      PRESENTATION LAYER                      │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │ LoginFrame  │  │ MainFrame   │  │ RoomManagementPanel │  │
│  └─────────────┘  └─────────────┘  └─────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      BUSINESS LAYER                          │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │ RoomManager │  │BookingManager│ │   UserManager       │  │
│  └─────────────┘  └─────────────┘  └─────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      DATA LAYER                              │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │    Room     │  │   Booking   │  │   JsonStorage       │  │
│  │  Customer   │  │   Invoice   │  │                     │  │
│  └─────────────┘  └─────────────┘  └─────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

### 2.2 Package Structure

```
src/
├── main/
│   └── java/
│       └── com/
│           └── hotel/
│               ├── model/           # Data models
│               │   ├── Room.java (abstract)
│               │   ├── StandardRoom.java
│               │   ├── VIPRoom.java
│               │   ├── DeluxeRoom.java
│               │   ├── Person.java (abstract)
│               │   ├── Customer.java
│               │   ├── User.java
│               │   ├── Booking.java
│               │   └── Invoice.java
│               │
│               ├── manager/         # Business logic
│               │   ├── IManageable.java (interface)
│               │   ├── RoomManager.java
│               │   ├── BookingManager.java
│               │   └── UserManager.java
│               │
│               ├── storage/         # Data persistence
│               │   ├── IStorage.java (interface)
│               │   └── JsonStorage.java
│               │
│               ├── ui/              # User interface
│               │   ├── LoginFrame.java
│               │   ├── MainFrame.java
│               │   ├── RoomPanel.java
│               │   ├── BookingPanel.java
│               │   └── ReportPanel.java
│               │
│               ├── util/            # Utilities
│               │   └── Constants.java
│               │
│               └── Main.java        # Entry point
│
└── test/
    └── java/
        └── com/
            └── hotel/
                ├── model/
                │   └── RoomTest.java
                └── manager/
                    └── RoomManagerTest.java
```

---

## 3. THIẾT KẾ HƯỚNG ĐỐI TƯỢNG (OOP DESIGN)

### 3.1 Class Diagram

```
                    ┌─────────────────┐
                    │   <<interface>> │
                    │   IManageable   │
                    ├─────────────────┤
                    │ +add(T)         │
                    │ +update(T)      │
                    │ +delete(id)     │
                    │ +findById(id)   │
                    │ +getAll()       │
                    └────────┬────────┘
                             │ implements
            ┌────────────────┼────────────────┐
            ▼                ▼                ▼
   ┌─────────────┐  ┌──────────────┐  ┌─────────────┐
   │ RoomManager │  │BookingManager│  │ UserManager │
   └─────────────┘  └──────────────┘  └─────────────┘


        ┌──────────────────┐
        │  <<abstract>>    │
        │     Person       │
        ├──────────────────┤
        │ #id: String      │
        │ #name: String    │
        │ #phone: String   │
        ├──────────────────┤
        │ +getInfo(): String│
        └────────┬─────────┘
                 │ extends
        ┌────────┴────────┐
        ▼                 ▼
  ┌──────────┐      ┌──────────┐
  │ Customer │      │   User   │
  ├──────────┤      ├──────────┤
  │ -cccd    │      │ -username│
  │ -email   │      │ -password│
  └──────────┘      │ -role    │
                    └──────────┘


        ┌──────────────────────┐
        │    <<abstract>>      │
        │        Room          │
        ├──────────────────────┤
        │ #roomId: String      │
        │ #floor: int          │
        │ #basePrice: double   │
        │ #isAvailable: boolean│
        ├──────────────────────┤
        │ +calculatePrice(days)│
        │ +getRoomType()       │
        └──────────┬───────────┘
                   │ extends
     ┌─────────────┼─────────────┐
     ▼             ▼             ▼
┌──────────┐ ┌──────────┐ ┌──────────┐
│ Standard │ │   VIP    │ │  Deluxe  │
│   Room   │ │   Room   │ │   Room   │
└──────────┘ └──────────┘ └──────────┘
```

### 3.2 Các nguyên lý OOP được áp dụng

| Nguyên lý | Áp dụng |
|-----------|---------|
| **Encapsulation** | Các thuộc tính private/protected với getter/setter |
| **Inheritance** | Room → StandardRoom, VIPRoom, DeluxeRoom |
| **Polymorphism** | calculatePrice() trả về giá khác nhau cho từng loại phòng |
| **Abstraction** | Abstract class Room, Person; Interface IManageable, IStorage |

---

## 4. THIẾT KẾ CHI TIẾT - THÀNH VIÊN 1

### 4.1 Model Classes

#### 4.1.1 Room.java (Abstract)
```
Attributes:
- roomId: String (PK)
- floor: int
- basePrice: double
- isAvailable: boolean
- maxOccupancy: int

Methods:
- calculatePrice(int days): double (abstract)
- getRoomType(): String (abstract)
- getters/setters
```

#### 4.1.2 StandardRoom.java
- basePrice = 500,000 VND
- maxOccupancy = 2
- calculatePrice = basePrice × days

#### 4.1.3 VIPRoom.java
- basePrice = 1,000,000 VND
- maxOccupancy = 3
- calculatePrice = basePrice × days × 1.2

#### 4.1.4 DeluxeRoom.java
- basePrice = 1,500,000 VND
- maxOccupancy = 4
- calculatePrice = basePrice × days × 1.5

### 4.2 RoomManager.java

```
Methods:
- addRoom(Room room): boolean
- updateRoom(Room room): boolean
- deleteRoom(String roomId): boolean
- findById(String roomId): Room
- findByFloor(int floor): List<Room>
- findByType(String type): List<Room>
- findAvailableRooms(): List<Room>
- getAll(): List<Room>
- sortByPrice(): List<Room>
- sortByFloor(): List<Room>
```

### 4.3 Storage

#### JsonStorage.java
```
Methods:
- saveRooms(List<Room> rooms): void
- loadRooms(): List<Room>
- saveUsers(List<User> users): void
- loadUsers(): List<User>
```

### 4.4 UI Components

#### LoginFrame.java
- Username field
- Password field
- Login button
- Validation

#### MainFrame.java
- Menu bar (File, Room, Booking, Report, Help)
- Tab panels
- Status bar

#### RoomPanel.java
- Table hiển thị danh sách phòng
- Form thêm/sửa phòng
- Buttons: Add, Edit, Delete, Search, Refresh

---

## 5. DATA FLOW

### 5.1 Thêm phòng mới
```
User → RoomPanel → RoomManager → JsonStorage → rooms.json
         │
         └── Validation
```

### 5.2 Tìm kiếm phòng
```
User → RoomPanel → RoomManager → Filter → Display
```

---

## 6. FILE STORAGE FORMAT

### 6.1 rooms.json
```json
{
  "rooms": [
    {
      "roomId": "R101",
      "type": "STANDARD",
      "floor": 1,
      "basePrice": 500000,
      "isAvailable": true,
      "maxOccupancy": 2
    }
  ]
}
```

### 6.2 users.json
```json
{
  "users": [
    {
      "id": "U001",
      "username": "admin",
      "password": "hashed_password",
      "name": "Administrator",
      "role": "ADMIN"
    }
  ]
}
```

---

## 7. SPRINT PLANNING - THÀNH VIÊN 1

### Sprint 1: Core Models (2 ngày)
- [ ] Room.java (abstract)
- [ ] StandardRoom.java
- [ ] VIPRoom.java
- [ ] DeluxeRoom.java
- [ ] Unit tests cho Room

### Sprint 2: Manager & Storage (2 ngày)
- [ ] IManageable.java interface
- [ ] RoomManager.java
- [ ] IStorage.java interface
- [ ] JsonStorage.java
- [ ] Unit tests

### Sprint 3: UI - Login & Main (2 ngày)
- [ ] LoginFrame.java
- [ ] MainFrame.java
- [ ] User.java model
- [ ] UserManager.java

### Sprint 4: UI - Room Management (2 ngày)
- [ ] RoomPanel.java
- [ ] RoomDialog.java (form thêm/sửa)
- [ ] Integration testing

### Sprint 5: Integration & Polish (2 ngày)
- [ ] Tích hợp với Thành viên 2
- [ ] Bug fixes
- [ ] Documentation

---

## 8. REVIEW CHECKLIST

### Design Review
- [ ] Có sử dụng Abstract Class?
- [ ] Có sử dụng Interface?
- [ ] Có sử dụng Inheritance?
- [ ] Có sử dụng Polymorphism?
- [ ] Có sử dụng Encapsulation?

### Code Review
- [ ] Code conventions
- [ ] Error handling
- [ ] Input validation
- [ ] Unit tests pass
- [ ] No code duplication

---

**Tài liệu version:** 1.0
**Ngày tạo:** 08/12/2024
**Tác giả:** Thành viên 1
