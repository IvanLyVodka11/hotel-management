# TÀI LIỆU THIẾT KẾ KỸ THUẬT
## Dự án: Hệ thống Quản lý Khách sạn (Hotel Management System)
### Môn học: Lập trình Hướng đối tượng

---

## 📋 THÔNG TIN CHUNG

| Mục | Thông tin |
|-----|-----------|
| **Tên dự án** | Hotel Management System |
| **Ngôn ngữ** | Java |
| **UI Framework** | Java Swing + FlatLaf |
| **Lưu trữ** | JSON File (Gson) |
| **Build Tool** | Maven |
| **Số thành viên** | 2 |
| **Phiên bản** | 1.0 |
| **Ngày cập nhật** | 18/01/2026 |

---

## 1. MỤC TIÊU DỰ ÁN

### 1.1 Business Goals
- Xây dựng hệ thống quản lý khách sạn đầy đủ chức năng
- Áp dụng các nguyên tắc Lập trình Hướng đối tượng (OOP)
- Đáp ứng yêu cầu đồ án môn học

### 1.2 Technical Goals
- Áp dụng đầy đủ 4 tính chất OOP: Encapsulation, Inheritance, Polymorphism, Abstraction
- Sử dụng Design Pattern phù hợp
- Code clean, maintainable, testable

---

## 2. YÊU CẦU CHỨC NĂNG (FUNCTIONAL REQUIREMENTS)

### 2.1 Nhóm tác vụ 1: Quản lý Phòng (Thành viên 1)
| ID | Chức năng | Mô tả | Priority |
|----|-----------|-------|----------|
| FR-R01 | Thêm phòng | Thêm phòng mới với các loại: Standard, VIP, Deluxe | High |
| FR-R02 | Xem danh sách phòng | Hiển thị tất cả phòng trong hệ thống | High |
| FR-R03 | Tìm kiếm phòng | Tìm theo mã phòng, loại phòng, tầng, trạng thái | High |
| FR-R04 | Cập nhật phòng | Sửa thông tin phòng (giá, trạng thái) | Medium |
| FR-R05 | Xóa phòng | Xóa phòng khỏi hệ thống | Medium |
| FR-R06 | Sắp xếp phòng | Sắp xếp theo giá, mã phòng, tầng | Low |
| FR-R07 | Báo cáo phòng | Thống kê phòng trống/đang sử dụng | Medium |

### 2.2 Nhóm tác vụ 2: Quản lý Đặt phòng/Khách hàng (Thành viên 2)
| ID | Chức năng | Mô tả | Priority |
|----|-----------|-------|----------|
| FR-B01 | Thêm khách hàng | Đăng ký thông tin khách hàng | High |
| FR-B02 | Đặt phòng | Tạo booking mới | High |
| FR-B03 | Check-in | Nhận phòng | High |
| FR-B04 | Check-out | Trả phòng và tính tiền | High |
| FR-B05 | Tìm kiếm booking | Tìm theo khách, ngày, phòng | Medium |
| FR-B06 | Hủy đặt phòng | Cancel booking | Medium |
| FR-B07 | Báo cáo doanh thu | Thống kê doanh thu theo ngày/tháng | Medium |

### 2.3 Chức năng chung
| ID | Chức năng | Mô tả | Priority |
|----|-----------|-------|----------|
| FR-C01 | Đăng nhập | Xác thực người dùng | High |
| FR-C02 | Menu chính | Hệ thống menu điều hướng | High |
| FR-C03 | Lưu trữ dữ liệu | Lưu/Đọc từ file JSON | High |

---

## 3. THIẾT KẾ HƯỚNG ĐỐI TƯỢNG (OOP DESIGN)

### 3.1 Nguyên tắc OOP áp dụng

#### 🔒 ENCAPSULATION (Đóng gói)
```
- Tất cả attributes đều là private
- Truy cập qua getter/setter
- Validation trong setter methods
```

#### 🔗 INHERITANCE (Kế thừa)
```
Person (abstract)
  ├── Customer
  └── User

Room (abstract)
  ├── StandardRoom
  ├── VIPRoom
  └── DeluxeRoom
```

#### 🔄 POLYMORPHISM (Đa hình)
```
- Room.calculatePrice() - mỗi loại phòng tính giá khác nhau
- Person.getInfo() - hiển thị thông tin khác nhau
- Override toString() cho tất cả class
```

#### 📐 ABSTRACTION (Trừu tượng)
```
- Abstract class: Person, Room
- Interface: IManageable, IStorable, ISearchable
```

### 3.2 Design Patterns sử dụng

| Pattern | Áp dụng | Lý do |
|---------|---------|-------|
| **Singleton** | HotelManagementSystem, StorageManager | Chỉ cần 1 instance duy nhất |
| **Factory** | RoomFactory | Tạo các loại phòng khác nhau |
| **Strategy** | SearchStrategy | Các chiến lược tìm kiếm khác nhau |
| **Observer** | RoomStatusObserver | Cập nhật UI khi trạng thái phòng thay đổi |

---

## 4. KIẾN TRÚC HỆ THỐNG (ARCHITECTURE)

### 4.1 Layer Architecture
```
┌─────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                    │
│              (Java Swing - UI Components)                │
│   MainFrame, LoginPanel, RoomPanel, BookingPanel, etc.  │
├─────────────────────────────────────────────────────────┤
│                    BUSINESS LAYER                        │
│                 (Manager Classes)                        │
│     RoomManager, BookingManager, UserManager, etc.      │
├─────────────────────────────────────────────────────────┤
│                     MODEL LAYER                          │
│                  (Entity Classes)                        │
│   Room, Customer, Booking, Invoice, etc.                │
├─────────────────────────────────────────────────────────┤
│                    DATA LAYER                            │
│              (Storage & Persistence)                     │
│               DataStorage (Gson + JSON)                 │
└─────────────────────────────────────────────────────────┘
```

### 4.2 Package Structure
```
src/
└── com/hotel/
    ├── Main.java                      # Entry point
    ├── auth/                          # Authentication & Authorization
    │   ├── PermissionManager.java
    │   └── UserSession.java
    ├── model/                         # Entity classes
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
    ├── service/                       # Business logic + Interfaces
    │   ├── RoomManager.java
    │   ├── BookingManager.java
    │   ├── CustomerManager.java
    │   ├── InvoiceManager.java
    │   └── interfaces/
    │       ├── IManageable.java
    │       ├── IStorable.java
    │       └── ISearchable.java
    │
    ├── storage/                       # Data persistence
    │   └── DataStorage.java
    │
    ├── ui/                            # Swing UI
    │   ├── LoginFrame.java
    │   ├── MainFrame.java
    │   ├── DashboardPanel.java
    │   ├── RoomPanel.java
    │   ├── BookingPanel.java
    │   ├── CustomerPanel.java
    │   ├── InvoicePanel.java
    │   ├── RoomDialog.java
    │   ├── AddBookingDialog.java
    │   ├── EditBookingDialog.java
    │   ├── AddCustomerDialog.java
    │   ├── EditCustomerDialog.java
    │   ├── CustomerDetailDialog.java
    │   ├── base/
    │   ├── theme/
    │   └── util/
    │
    └── util/                          # Utilities
        ├── AppLogger.java
        └── Result.java

data/                                  # JSON data files
├── rooms.json
├── customers.json
├── bookings.json
├── invoices.json
└── users.json

test/                                  # Unit tests
└── com/hotel/
    ├── model/
    └── service/
```

---

## 5. CLASS DIAGRAM CHI TIẾT

### 5.1 Model Classes

```
┌────────────────────────────────────────┐
│           <<abstract>>                  │
│               Room                      │
├────────────────────────────────────────┤
│ - roomId: String                       │
│ - floor: int                           │
│ - basePrice: double                    │
│ - status: RoomStatus                   │
│ - maxOccupancy: int                    │
├────────────────────────────────────────┤
│ + Room(id, floor, price, occupancy)    │
│ + getRoomId(): String                  │
│ + getStatus(): RoomStatus              │
│ + setStatus(status): void              │
│ + {abstract} calculatePrice(): double  │
│ + {abstract} getRoomType(): RoomType   │
│ + toString(): String                   │
└────────────────────────────────────────┘
                △
                │ extends
    ┌───────────┼───────────┐
    │           │           │
┌───┴────┐ ┌────┴────┐ ┌────┴────┐
│Standard│ │  VIP    │ │ Deluxe  │
│  Room  │ │  Room   │ │  Room   │
└────────┘ └─────────┘ └─────────┘


┌────────────────────────────────────────┐
│              Customer                   │
├────────────────────────────────────────┤
│ - customerId: String                   │
│ - fullName: String                     │
│ - email: String                        │
│ - phoneNumber: String                  │
│ - idCard: String                       │
│ - address: String                      │
│ - registrationDate: LocalDate         │
│ - isVIP: boolean                       │
│ - loyaltyPoints: double                │
├────────────────────────────────────────┤
│ + getters/setters                      │
│ + addLoyaltyPoints(points): void       │
│ + toString(): String                   │
└────────────────────────────────────────┘
```

### 5.2 Interface Definitions

```
┌────────────────────────────────────────┐
│         <<interface>>                   │
│          IManageable<T>                 │
├────────────────────────────────────────┤
│ + add(item: T): boolean                │
│ + update(item: T): boolean             │
│ + delete(id: String): boolean          │
│ + getById(id: String): T               │
│ + getAll(): List<T>                    │
└────────────────────────────────────────┘

┌────────────────────────────────────────┐
│         <<interface>>                   │
│           IStorable                     │
├────────────────────────────────────────┤
│ + save(): void                         │
│ + load(): void                         │
└────────────────────────────────────────┘

┌────────────────────────────────────────┐
│         <<interface>>                   │
│          ISearchable<T>                 │
├────────────────────────────────────────┤
│ + search(keyword: String): List<T>     │
│ + filter(criteria: Map): List<T>       │
└────────────────────────────────────────┘
```

---

## 6. PHÂN CÔNG CÔNG VIỆC

### 6.1 Thành viên 1 - Quản lý Phòng

| Sprint | Task | File/Class | Story Points |
|--------|------|------------|--------------|
| 1 | Thiết kế Room classes | Room.java, StandardRoom, VIPRoom, DeluxeRoom | 5 |
| 1 | Enum RoomType, RoomStatus | RoomType.java, RoomStatus.java | 2 |
| 1 | Interface IManageable, IStorable | interfaces/*.java | 3 |
| 2 | RoomManager | RoomManager.java | 5 |
| 2 | RoomFactory | RoomFactory.java | 3 |
| 2 | JsonStorage cho Room | JsonStorage.java (phần Room) | 5 |
| 3 | RoomPanel UI | RoomPanel.java | 8 |
| 3 | AddRoomDialog | AddRoomDialog.java | 5 |
| 3 | EditRoomDialog | EditRoomDialog.java | 5 |
| 4 | Search & Filter Room | SearchDialog.java | 5 |
| 4 | Room Report | ReportPanel.java (phần Room) | 5 |
| 4 | Unit Tests | RoomTest.java, RoomManagerTest.java | 5 |

**Tổng: 56 Story Points**

### 6.2 Thành viên 2 - Quản lý Đặt phòng/Khách hàng

| Sprint | Task | File/Class | Story Points |
|--------|------|------------|--------------|
| 1 | Person, Customer, User classes | Person.java, Customer.java, User.java | 5 |
| 1 | Booking, Invoice classes | Booking.java, Invoice.java | 5 |
| 1 | Enum BookingStatus | BookingStatus.java | 2 |
| 2 | CustomerManager, BookingManager | *Manager.java | 8 |
| 2 | UserManager + Authentication | UserManager.java | 5 |
| 2 | JsonStorage cho Booking/Customer | JsonStorage.java (phần còn lại) | 5 |
| 3 | LoginPanel | LoginPanel.java | 5 |
| 3 | BookingPanel UI | BookingPanel.java | 8 |
| 3 | CustomerPanel UI | CustomerPanel.java | 5 |
| 4 | Check-in/Check-out flow | BookingService.java | 5 |
| 4 | Revenue Report | ReportPanel.java (phần Revenue) | 5 |
| 4 | Unit Tests | BookingTest.java, CustomerTest.java | 5 |

**Tổng: 63 Story Points**

### 6.3 Công việc chung

| Task | Người phụ trách | Story Points |
|------|-----------------|--------------|
| MainFrame, MenuPanel | Cả 2 | 5 |
| HotelManagementSystem (main) | Cả 2 | 3 |
| Integration Testing | Cả 2 | 5 |
| Documentation | Cả 2 | 5 |

---

## 7. SPRINT PLANNING

### Sprint 1 (Tuần 1-2): Foundation
- [ ] Thiết kế và implement tất cả Model classes
- [ ] Định nghĩa Interfaces
- [ ] Định nghĩa Enums
- **Deliverable**: Tất cả entity classes hoàn thiện

### Sprint 2 (Tuần 3-4): Business Logic
- [ ] Implement Manager classes
- [ ] Implement Factory pattern
- [ ] Implement Storage layer
- **Deliverable**: Business logic hoàn thiện, có thể test qua console

### Sprint 3 (Tuần 5-6): UI Development
- [ ] MainFrame và navigation
- [ ] Tất cả Panel UI
- [ ] Dialog components
- **Deliverable**: UI hoàn thiện, có thể demo

### Sprint 4 (Tuần 7-8): Polish & Testing
- [ ] Search & Filter features
- [ ] Reports
- [ ] Unit tests
- [ ] Bug fixes
- **Deliverable**: Sản phẩm hoàn chỉnh

---

## 8. DATA SCHEMA (JSON)

### rooms.json
```json
{
  "rooms": [
    {
      "roomId": "R101",
      "roomType": "STANDARD",
      "floor": 1,
      "basePrice": 500000,
      "status": "AVAILABLE",
      "description": "Standard room with city view"
    }
  ]
}
```

### customers.json
```json
{
  "customers": [
    {
      "id": "C001",
      "name": "Nguyen Van A",
      "phoneNumber": "0901234567",
      "cccd": "079123456789",
      "email": "nguyenvana@email.com",
      "address": "123 ABC Street, HCM"
    }
  ]
}
```

### bookings.json
```json
{
  "bookings": [
    {
      "bookingId": "BK001",
      "customerId": "C001",
      "roomId": "R101",
      "checkInDate": "2024-12-10",
      "checkOutDate": "2024-12-12",
      "status": "CONFIRMED",
      "totalPrice": 1000000,
      "createdAt": "2024-12-08T10:30:00"
    }
  ]
}
```

### users.json
```json
{
  "users": [
    {
      "id": "U001",
      "name": "Admin",
      "phoneNumber": "",
      "username": "admin",
      "password": "admin123",
      "role": "ADMIN"
    }
  ]
}
```

---

## 9. REVIEW CHECKLIST

### Code Review Criteria
- [ ] Tuân thủ naming conventions
- [ ] Proper encapsulation (private fields, public methods)
- [ ] Sử dụng inheritance đúng cách
- [ ] Polymorphism được áp dụng
- [ ] Không có code duplication
- [ ] Exception handling
- [ ] Comments và documentation
- [ ] Unit tests passed

### OOP Checklist
- [ ] Abstract class được sử dụng đúng
- [ ] Interface được định nghĩa và implement
- [ ] Factory pattern hoạt động
- [ ] Singleton pattern đúng cách
- [ ] Override methods có @Override annotation

---

## 10. APPROVAL

| Role | Người duyệt | Ngày | Trạng thái |
|------|-------------|------|------------|
| Technical Lead | | | Pending |
| Team Member 1 | | | Pending |
| Team Member 2 | | | Pending |

---

**Document Version**: 1.0
**Last Updated**: 08/12/2025
**Status**: DRAFT - Chờ Review
