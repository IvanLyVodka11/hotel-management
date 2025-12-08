# 🏨 Hệ thống Quản lý Khách sạn (Hotel Management System)

## 📋 Mô tả
Đồ án môn học **Lập trình Hướng đối tượng** - Hệ thống quản lý khách sạn với đầy đủ các chức năng quản lý phòng, đặt phòng, khách hàng và báo cáo.

## 👥 Thành viên nhóm
| Thành viên | Nhiệm vụ | Trạng thái |
|------------|----------|------------|
| Thành viên 1 | Quản lý Phòng, Menu, Storage, Login | ✅ Hoàn thành |
| Thành viên 2 | Quản lý Đặt phòng, Khách hàng, Hóa đơn | 🔄 Đang phát triển |

## 🛠️ Công nghệ sử dụng
- **Ngôn ngữ**: Java 17
- **UI Framework**: Java Swing + FlatLaf
- **Storage**: JSON (Gson)
- **Build Tool**: Maven
- **Testing**: JUnit 5

## 📐 Cấu trúc dự án
```
OOPproject/
├── src/
│   └── com/hotel/
│       ├── Main.java              # Entry point
│       ├── model/                 # Data models
│       │   ├── room/              # Room classes
│       │   │   ├── Room.java      # Abstract class
│       │   │   ├── StandardRoom.java
│       │   │   ├── VIPRoom.java
│       │   │   ├── DeluxeRoom.java
│       │   │   └── RoomFactory.java
│       │   └── enums/             # Enums
│       │       ├── RoomType.java
│       │       ├── RoomStatus.java
│       │       └── BookingStatus.java
│       ├── service/               # Business logic
│       │   ├── RoomManager.java
│       │   └── interfaces/
│       │       ├── IManageable.java
│       │       ├── ISearchable.java
│       │       └── IStorable.java
│       ├── storage/               # Data persistence
│       │   └── RoomStorage.java
│       └── ui/                    # User interface
│           ├── LoginFrame.java
│           ├── MainFrame.java
│           ├── RoomPanel.java
│           └── RoomDialog.java
├── test/                          # Unit tests
│   └── com/hotel/
│       ├── model/room/RoomTest.java
│       └── service/RoomManagerTest.java
├── data/                          # Data files
│   ├── rooms.json
│   └── users.json
├── docs/                          # Documentation
│   ├── 01_TECHNICAL_DESIGN.md
│   └── 02_BACKLOG_MEMBER1.md
└── pom.xml                        # Maven config
```

## 🚀 Hướng dẫn chạy

### Yêu cầu
- Java JDK 17+
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

## 🔄 Tính năng đang phát triển (Thành viên 2)
- [ ] Quản lý Khách hàng
- [ ] Quản lý Đặt phòng
- [ ] Hóa đơn
- [ ] Báo cáo doanh thu

## 📊 Loại phòng

| Loại | Giá cơ bản | Hệ số | Sức chứa |
|------|-----------|-------|----------|
| Standard | 500,000 VND | x1.0 | 2 người |
| VIP | 1,000,000 VND | x1.2 | 3 người |
| Deluxe | 1,500,000 VND | x1.5 | 4 người |

## 📝 License
MIT License - OOP Project 2024
