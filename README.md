# 🏨 Hệ thống Quản lý Khách sạn (Hotel Management System)

## 📋 Mô tả
Đồ án môn học **Lập trình Hướng đối tượng** - Hệ thống quản lý khách sạn với đầy đủ các chức năng quản lý phòng, đặt phòng, khách hàng và báo cáo.

## 👥 Thành viên nhóm
| Thành viên | Nhiệm vụ | Trạng thái |
|------------|----------|------------|
| Thành viên 1 | Quản lý Phòng, Menu, Storage, Login | ✅ Hoàn thành |
| Thành viên 2 | Quản lý Đặt phòng, Khách hàng, Hóa đơn | ✅ Hoàn thành |

## 🛠️ Công nghệ sử dụng
- **Ngôn ngữ**: Java 21
- **UI Framework**: Java Swing + FlatLaf
- **Storage**: JSON (Gson)
- **Build Tool**: Maven
- **Testing**: JUnit 5

## 📐 Cấu trúc dự án
```
hotel-management/
├── src/
│   └── com/hotel/
│       ├── Main.java              # Entry point
│       ├── model/                 # Data models
│       │   ├── room/              # Room classes (Thành viên 1)
│       │   │   ├── Room.java
│       │   │   ├── StandardRoom.java
│       │   │   ├── VIPRoom.java
│       │   │   ├── DeluxeRoom.java
│       │   │   └── RoomFactory.java
│       │   ├── customer/          # Customer classes (Thành viên 2)
│       │   │   └── Customer.java
│       │   ├── booking/           # Booking classes (Thành viên 2)
│       │   │   └── Booking.java
│       │   ├── invoice/           # Invoice classes (Thành viên 2)
│       │   │   └── Invoice.java
│       │   └── enums/             # Enums
│       │       ├── RoomType.java
│       │       ├── RoomStatus.java
│       │       └── BookingStatus.java
│       ├── service/               # Business logic
│       │   ├── RoomManager.java
│       │   ├── CustomerManager.java
│       │   ├── BookingManager.java
│       │   ├── InvoiceManager.java
│       │   └── interfaces/
│       │       ├── IManageable.java
│       │       ├── ISearchable.java
│       │       └── IStorable.java
│       ├── storage/               # Data persistence
│       │   ├── RoomStorage.java
│       │   └── DataStorage.java
│       └── ui/                    # User interface
│           ├── LoginFrame.java
│           ├── MainFrame.java
│           ├── RoomPanel.java
│           ├── RoomDialog.java
│           ├── BookingPanel.java
│           ├── CustomerPanel.java
│           └── InvoicePanel.java
├── test/                          # Unit tests
│   └── SimpleRoomTest.java
├── data/                          # Data files
│   ├── rooms.json
│   ├── users.json
│   ├── customers.json
│   ├── bookings.json
│   └── invoices.json
├── docs/                          # Documentation
│   ├── 01_TECHNICAL_DESIGN.md
│   ├── 02_BACKLOG_MEMBER1.md
│   ├── 03_BACKLOG_MEMBER2.md
│   ├── 04_MEMBER2_SUMMARY.md
│   ├── 05_CLASS_DIAGRAM_MEMBER2.md
│   └── 06_USER_GUIDE_MEMBER2.md
└── pom.xml                        # Maven config
```

## 📄 Tài liệu dự án

> **📘 Tài liệu đầy đủ:** [BaoCao_HotelManagement.md](docs/BaoCao_HotelManagement.md)
>
> Báo cáo tổng hợp bao gồm: Use Case Diagram, Đặc tả Use Case, Sequence Diagram, Class Diagram, và hướng dẫn chi tiết.


## 🚀 Hướng dẫn chạy

### Yêu cầu
- Java JDK 21+
- Maven 3.6+

### Cài đặt dependencies
```bash
mvn clean install
```

### Chạy ứng dụng
```bash
mvn exec:java -Dexec.mainClass="com.hotel.Main"
```

Hoặc chạy trực tiếp từ IDE.

### Chạy tests
```bash
mvn test
```

### Đăng nhập
- **Username**: `admin`
- **Password**: `admin123`

## ✅ Tính năng đã hoàn thành (Thành viên 1)

### 1. Quản lý Phòng
- [x] Thêm phòng mới
- [x] Sửa thông tin phòng
- [x] Xóa phòng
- [x] Xem danh sách phòng
- [x] Tìm kiếm phòng (theo ID, loại, tầng)
- [x] Lọc phòng (theo loại, trạng thái)
- [x] Sắp xếp phòng
- [x] Thống kê phòng

### 2. Hệ thống
- [x] Đăng nhập
- [x] Menu chính
- [x] Lưu/Load dữ liệu JSON

### 3. OOP Concepts
- [x] **Abstraction**: Abstract class `Room`, `Person`
- [x] **Encapsulation**: Private fields, public getters/setters
- [x] **Inheritance**: StandardRoom, VIPRoom, DeluxeRoom extends Room
- [x] **Polymorphism**: calculatePrice(), getRoomType()
- [x] **Interfaces**: IManageable, ISearchable, IStorable
- [x] **Design Patterns**: Singleton (RoomManager), Factory (RoomFactory)

## ✅ Tính năng đã hoàn thành (Thành viên 2)

Ghi chú: Đã tích hợp UI vào MainFrame (tab Đặt phòng/Khách hàng/Báo cáo) và hoàn thiện luồng tạo/sửa đặt phòng.

### 1. Quản lý Khách hàng
- [x] Model Customer class
  - [x] Attributes: customerId, fullName, email, phoneNumber, idCard, address, registrationDate, isVIP, loyaltyPoints
  - [x] Getters/Setters
  - [x] Loyalty points system
- [x] CustomerManager (CRUD + Search)
  - [x] add(), update(), delete(), getById(), getAll()
  - [x] search() - tìm theo tên, email, phone
  - [x] filter() - lọc theo VIP status, loyalty points
  - [x] getTotalCustomers(), getVIPCustomers()

### 2. Quản lý Đặt phòng
- [x] Model Booking class
  - [x] References to Customer và Room
  - [x] Attributes: bookingId, checkInDate, checkOutDate, status, totalPrice, notes
  - [x] calculateTotalPrice() - tính dựa trên số ngày
  - [x] getNumberOfDays() method
  - [x] isValid() - kiểm tra tính hợp lệ
- [x] BookingManager (CRUD + Search)
  - [x] add(), update(), delete(), getById(), getAll()
  - [x] search() - tìm theo booking ID, customer name, room ID
  - [x] filter() - lọc theo status, customer, room
  - [x] isRoomAvailable() - kiểm tra phòng trống
  - [x] getAvailableRooms() - lấy danh sách phòng còn trống
  - [x] getBookingsByStatus(), getCustomerBookings()
  - [x] getTotalRevenue(), getMonthlyRevenue()

### 3. Quản lý Hóa đơn
- [x] Model Invoice class
  - [x] References to Booking
  - [x] Attributes: invoiceId, invoiceDate, subtotal, taxRate, taxAmount, totalAmount, status, notes
  - [x] Automatic amount calculation
  - [x] markAsPaid(), markAsIssued(), cancel() methods
- [x] InvoiceManager (CRUD)
  - [x] add(), update(), delete(), getById(), getAll()
  - [x] createInvoiceFromBooking()
  - [x] getInvoiceByBooking(), getInvoicesByCustomer()
  - [x] getInvoicesByStatus(), getInvoicesByDateRange()
  - [x] getTotalRevenue(), getTotalTax(), getUnpaidRevenue()
  - [x] markInvoiceAsPaid(), cancelInvoice()
  - [x] getMonthlyRevenue()
  - [x] getPaidInvoices(), getUnpaidInvoices()

### 4. Storage & Persistence
- [x] DataStorage class
  - [x] Load/Save Customers to JSON
  - [x] Load/Save Bookings to JSON
  - [x] Load/Save Invoices to JSON
  - [x] Proper serialization/deserialization
  - [x] Handle relationships between entities

### 5. Design Patterns & OOP Concepts (Thành viên 2)
- [x] **Encapsulation**: Private fields với public getters/setters
- [x] **Inheritance**: Proper class hierarchy
- [x] **Interfaces**: IManageable, ISearchable implementation
- [x] **Composition**: Booking contains Customer và Room, Invoice contains Booking
- [x] **JSON Serialization**: Custom JSON parsing and conversion

## ✅ Tích hợp UI (Thành viên 2)
- [x] UI Integration: gắn BookingPanel/CustomerPanel/InvoicePanel vào MainFrame
- [x] Menu Integration: bật menu Đặt phòng, Danh sách đặt phòng, Báo cáo doanh thu
- [x] AddBookingDialog: nạp danh sách phòng trống theo ngày + tạo Booking thật
- [x] EditBookingDialog: hiển thị ngày hiện tại + cập nhật ngày/trạng thái

## 📋 Báo cáo Compilation (Thành viên 2)
- [COMPILATION_REPORT.md](COMPILATION_REPORT.md) - **Báo cáo đầy đủ về việc fix tất cả 40+ lỗi compilation**
  - ✅ 0 errors, 5 warnings (unused imports)
  - ✅ Tất cả interface contracts đã tuân thủ
  - ✅ Type mismatches đã được khắc phục
  - ✅ 8 files compiled successfully

## 📚 Tài liệu chi tiết

📘 **[Báo cáo đầy đủ - BaoCao_HotelManagement.md](docs/BaoCao_HotelManagement.md)** - Tài liệu tổng hợp bao gồm:
- Chương 1: Giới thiệu bối cảnh và bài toán
- Chương 2: Phân tích yêu cầu (Use Case, Sequence Diagram, Đặc tả chi tiết)
- Chương 3: Thiết kế hệ thống (Kiến trúc MVC, Class Diagram)
- Chương 4: Thiết kế chi tiết các lớp
- Chương 5: Tổ chức mã nguồn
- Chương 6: Hướng dẫn sử dụng

<details>
<summary>📁 Các file tài liệu gốc (đã tổng hợp)</summary>

| File | Mô tả |
|------|-------|
| [01_TECHNICAL_DESIGN.md](docs/01_TECHNICAL_DESIGN.md) | Thiết kế kỹ thuật |
| [02_BACKLOG_MEMBER1.md](docs/02_BACKLOG_MEMBER1.md) | Backlog thành viên 1 |
| [03_BACKLOG_MEMBER2.md](docs/03_BACKLOG_MEMBER2.md) | Backlog thành viên 2 |
| [04_MEMBER2_SUMMARY.md](docs/04_MEMBER2_SUMMARY.md) | Tổng kết thành viên 2 |
| [05_CLASS_DIAGRAM_MEMBER2.md](docs/05_CLASS_DIAGRAM_MEMBER2.md) | Class diagram |
| [06_USER_GUIDE_MEMBER2.md](docs/06_USER_GUIDE_MEMBER2.md) | Hướng dẫn sử dụng API |

</details>

## 📊 Loại phòng

| Loại | Giá cơ bản | Hệ số | Sức chứa |
|------|-----------|-------|----------|
| Standard | 500,000 VND | x1.0 | 2 người |
| VIP | 1,000,000 VND | x1.2 | 3 người |
| Deluxe | 1,500,000 VND | x1.5 | 4 người |

## 🔗 Quick Links

### APIs (Thành viên 2)
```java
// Customer Management
CustomerManager.add(Customer)
CustomerManager.search(keyword)
CustomerManager.filter(criteria, value)

// Booking Management
BookingManager.add(Booking)
BookingManager.isRoomAvailable(room, checkIn, checkOut)
BookingManager.getAvailableRooms(checkIn, checkOut)
BookingManager.getMonthlyRevenue(month, year)

// Invoice Management
InvoiceManager.createInvoiceFromBooking(booking, invoiceId)
InvoiceManager.markInvoiceAsPaid(invoiceId)
InvoiceManager.getTotalRevenue()

// Data Persistence
DataStorage.loadAllData()
DataStorage.saveAllData()
```

## 📞 Hỗ trợ & Liên hệ

- **Thành viên 1** (Room Management): [Backlog](docs/02_BACKLOG_MEMBER1.md)
- **Thành viên 2** (Booking, Customer, Invoice): [Summary](docs/04_MEMBER2_SUMMARY.md)

## 📝 License
MIT License - OOP Project 2025
