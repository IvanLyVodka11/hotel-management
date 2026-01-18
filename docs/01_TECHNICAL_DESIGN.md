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
└── com/
    └── hotel/
        ├── Main.java                    # Entry point
        ├── auth/                        # Authentication & Authorization
        │   ├── PermissionManager.java
        │   └── UserSession.java
        ├── model/                       # Data models
        │   ├── room/
        │   │   ├── Room.java (abstract)
        │   │   ├── StandardRoom.java
        │   │   ├── VIPRoom.java
        │   │   ├── DeluxeRoom.java
        │   │   └── RoomFactory.java
        │   ├── customer/
        │   │   └── Customer.java
        │   ├── booking/
        │   │   └── Booking.java
        │   ├── invoice/
        │   │   └── Invoice.java
        │   └── enums/
        │       ├── RoomType.java
        │       ├── RoomStatus.java
        │       └── BookingStatus.java
        │
        ├── service/                     # Business logic
        │   ├── RoomManager.java
        │   ├── BookingManager.java
        │   ├── CustomerManager.java
        │   ├── InvoiceManager.java
        │   └── interfaces/
        │       ├── IManageable.java
        │       ├── ISearchable.java
        │       └── IStorable.java
        │
        ├── storage/                     # Data persistence
        │   └── DataStorage.java
        │
        ├── ui/                          # User interface
        │   ├── LoginFrame.java
        │   ├── MainFrame.java
        │   ├── DashboardPanel.java
        │   ├── RoomPanel.java
        │   ├── BookingPanel.java
        │   ├── CustomerPanel.java
        │   ├── InvoicePanel.java
        │   ├── base/
        │   ├── theme/
        │   └── util/
        │
        └── util/                        # Utilities
            ├── AppLogger.java
            └── Result.java

test/
└── com/
    └── hotel/
        ├── model/
        └── service/
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
                    │ +getById(id)    │
                    │ +getAll()       │
                    └────────┬────────┘
                             │ implements
       ┌─────────────────────┼─────────────────────┐
       ▼                     ▼                     ▼
┌─────────────┐     ┌──────────────┐     ┌────────────────┐
│ RoomManager │     │BookingManager│     │CustomerManager │
└─────────────┘     └──────────────┘     └────────────────┘


        ┌──────────────────────┐
        │    <<abstract>>      │
        │        Room          │
        ├──────────────────────┤
        │ #roomId: String      │
        │ #floor: int          │
        │ #basePrice: double   │
        │ #status: RoomStatus  │
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


        ┌──────────────────┐
        │     Customer     │
        ├──────────────────┤
        │ -customerId      │
        │ -fullName        │
        │ -email           │
        │ -phoneNumber     │
        │ -idCard          │
        │ -isVIP           │
        │ -loyaltyPoints   │
        └──────────────────┘
```

### 3.2 Các nguyên lý OOP được áp dụng

| Nguyên lý | Áp dụng |
|-----------|---------|
| **Encapsulation** | Các thuộc tính private với getter/setter |
| **Inheritance** | Room → StandardRoom, VIPRoom, DeluxeRoom |
| **Polymorphism** | calculatePrice() trả về giá khác nhau cho từng loại phòng |
| **Abstraction** | Abstract class Room; Interface IManageable, ISearchable, IStorable |

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

#### DataStorage.java
```
Methods:
- loadAllData(): void
- saveAllData(): void
- loadRooms() / saveRooms()
- loadCustomers() / saveCustomers()
- loadBookings() / saveBookings()
- loadInvoices() / saveInvoices()
- loadUsers() / saveUsers()
```

### 4.4 UI Components

#### LoginFrame.java
- Username field
- Password field
- Login button
- Validation

#### MainFrame.java
- Menu bar (File, Room, Booking, Customer, Invoice, Help)
- Tab panels (Dashboard, Room, Booking, Customer, Invoice)
- Status bar
- Role-based permissions

#### RoomPanel.java
- Table hiển thị danh sách phòng
- Dialog thêm/sửa phòng (RoomDialog)
- Buttons: Add, Edit, Delete, Refresh
- Filter by type, status

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
[
  {
    "roomId": "R101",
    "type": "STANDARD",
    "floor": 1,
    "basePrice": 500000,
    "status": "AVAILABLE",
    "maxOccupancy": 2
  }
]
```

### 6.2 users.json
```json
[
  {
    "username": "admin",
    "password": "admin123",
    "role": "MANAGER"
  }
]
```

---

## 7. SPRINT PLANNING - THÀNH VIÊN 1

### Sprint 1: Core Models (2 ngày)
- [x] Room.java (abstract)
- [x] StandardRoom.java
- [x] VIPRoom.java
- [x] DeluxeRoom.java
- [x] RoomFactory.java
- [x] Unit tests cho Room

### Sprint 2: Manager & Storage (2 ngày)
- [x] IManageable.java interface
- [x] ISearchable.java interface
- [x] IStorable.java interface
- [x] RoomManager.java
- [x] DataStorage.java
- [x] Unit tests

### Sprint 3: UI - Login & Main (2 ngày)
- [x] LoginFrame.java
- [x] MainFrame.java
- [x] UserSession.java
- [x] PermissionManager.java

### Sprint 4: UI - Room Management (2 ngày)
- [x] RoomPanel.java
- [x] RoomDialog.java (form thêm/sửa)
- [x] DashboardPanel.java
- [x] Integration testing

### Sprint 5: Integration & Polish (2 ngày)
- [x] Tích hợp với Thành viên 2
- [x] Bug fixes
- [x] Documentation

---

## 8. REVIEW CHECKLIST

### Design Review
- [x] Có sử dụng Abstract Class?
- [x] Có sử dụng Interface?
- [x] Có sử dụng Inheritance?
- [x] Có sử dụng Polymorphism?
- [x] Có sử dụng Encapsulation?

### Code Review
- [x] Code conventions
- [x] Error handling
- [x] Input validation
- [x] Unit tests pass
- [x] No code duplication

---

**Tài liệu version:** 1.0
**Ngày cập nhật:** 18/01/2026
**Trạng thái:** ✅ Hoàn thành
