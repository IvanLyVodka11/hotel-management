# Tóm tắt Tính năng Thành viên 2 - Quản lý Đặt phòng, Khách hàng, Hóa đơn

## 📅 Ngày cập nhật: 15/12/2025

---

## 🎯 Mục tiêu chính
Phát triển hệ thống quản lý đặt phòng, khách hàng và hóa đơn với các chức năng:
- Quản lý thông tin khách hàng
- Quản lý đặt phòng + kiểm tra phòng trống
- Quản lý hóa đơn + tính toán doanh thu
- Lưu trữ dữ liệu dạng JSON

---

## ✅ Các tính năng đã hoàn thành

### 1️⃣ Model Layer

#### Customer.java
- Quản lý thông tin khách hàng
- Attributes: customerId, fullName, email, phoneNumber, idCard, address, registrationDate, isVIP, loyaltyPoints
- Methods:
  - `getters/setters` cho tất cả fields
  - `addLoyaltyPoints(double points)` - cộng điểm loyalty
  - `equals()`, `hashCode()`, `toString()`

#### Booking.java
- Quản lý thông tin đặt phòng
- Attributes: bookingId, customer, room, checkInDate, checkOutDate, status, totalPrice, notes
- Methods:
  - `calculateTotalPrice()` - tính giá dựa trên số ngày
  - `getNumberOfDays()` - lấy số ngày đặt
  - `isValid()` - kiểm tra tính hợp lệ
  - `getters/setters` cho tất cả fields

#### Invoice.java
- Quản lý thông tin hóa đơn
- Attributes: invoiceId, booking, invoiceDate, subtotal, taxRate, taxAmount, totalAmount, status, notes
- Enum InvoiceStatus: DRAFT, ISSUED, PAID, CANCELLED
- Methods:
  - `calculateAmounts()` - tính toán các khoản tiền
  - `markAsPaid()` - đánh dấu đã thanh toán
  - `markAsIssued()` - đánh dấu đã phát hành
  - `cancel()` - hủy hóa đơn

### 2️⃣ Service Layer (Business Logic)

#### CustomerManager.java
Implements: `IManageable<Customer>`, `ISearchable<Customer>`

**CRUD Operations:**
- `add(Customer)` - thêm khách hàng mới
- `update(Customer)` - cập nhật thông tin
- `delete(String customerId)` - xóa khách hàng
- `getById(String customerId)` - lấy khách hàng theo ID
- `getAll()` - lấy danh sách tất cả

**Search & Filter:**
- `search(String keyword)` - tìm theo tên, email, phone, ID
- `filter(String criteria, Object value)` - lọc theo VIP status, loyalty points

**Reporting:**
- `getTotalCustomers()` - tổng số khách hàng
- `getVIPCustomers()` - số khách hàng VIP

#### BookingManager.java
Implements: `IManageable<Booking>`, `ISearchable<Booking>`

**CRUD Operations:**
- `add(Booking)` - thêm đặt phòng mới
- `update(Booking)` - cập nhật thông tin
- `delete(String bookingId)` - xóa đặt phòng
- `getById(String bookingId)` - lấy theo ID
- `getAll()` - lấy danh sách tất cả

**Search & Filter:**
- `search(String keyword)` - tìm theo booking ID, customer name, room ID
- `filter(String criteria, Object value)` - lọc theo status, customer, room

**Availability Check:**
- `isRoomAvailable(Room, LocalDate, LocalDate)` - kiểm tra phòng trống
- `getAvailableRooms(LocalDate, LocalDate)` - lấy danh sách phòng còn trống

**Booking Information:**
- `getBookingsByStatus(BookingStatus)` - lấy booking theo trạng thái
- `getCustomerBookings(String customerId)` - lấy booking của khách hàng

**Revenue Tracking:**
- `getTotalRevenue()` - tính tổng doanh thu
- `getMonthlyRevenue(int month, int year)` - tính doanh thu theo tháng

#### InvoiceManager.java
Implements: `IManageable<Invoice>`

**CRUD Operations:**
- `add(Invoice)` - thêm hóa đơn
- `update(Invoice)` - cập nhật hóa đơn
- `delete(String invoiceId)` - xóa hóa đơn
- `getById(String invoiceId)` - lấy theo ID
- `getAll()` - lấy danh sách tất cả

**Invoice Management:**
- `createInvoiceFromBooking(Booking, String invoiceId)` - tạo hóa đơn từ booking
- `getInvoiceByBooking(String bookingId)` - lấy hóa đơn theo booking
- `getInvoicesByCustomer(String customerId)` - lấy hóa đơn của khách
- `getInvoicesByStatus(InvoiceStatus)` - lấy theo trạng thái
- `getInvoicesByDateRange(LocalDate, LocalDate)` - lấy theo khoảng thời gian

**Payment Management:**
- `markInvoiceAsPaid(String invoiceId)` - đánh dấu đã thanh toán
- `cancelInvoice(String invoiceId)` - hủy hóa đơn

**Revenue Reporting:**
- `getTotalRevenue()` - tổng doanh thu đã thanh toán
- `getTotalTax()` - tổng thuế đã thu
- `getUnpaidRevenue()` - doanh thu chưa thanh toán
- `getMonthlyRevenue(int month, int year)` - doanh thu theo tháng
- `getPaidInvoices()` - số hóa đơn đã thanh toán
- `getUnpaidInvoices()` - số hóa đơn chưa thanh toán

### 3️⃣ Storage Layer

#### DataStorage.java
Quản lý lưu trữ dữ liệu JSON cho Customers, Bookings, và Invoices

**Features:**
- `loadAllData()` - tải tất cả dữ liệu từ JSON
- `saveAllData()` - lưu tất cả dữ liệu vào JSON
- `loadCustomers()` / `saveCustomers()` - load/save customers
- `loadBookings()` / `saveBookings()` - load/save bookings
- `loadInvoices()` / `saveInvoices()` - load/save invoices
- Proper serialization/deserialization
- Handle relationships giữa các entities

**Files được tạo:**
- `data/customers.json` - lưu khách hàng
- `data/bookings.json` - lưu đặt phòng
- `data/invoices.json` - lưu hóa đơn

---

## 📊 Thống kê công việc

### Model Classes: 3 classes
- ✅ Customer.java
- ✅ Booking.java
- ✅ Invoice.java

### Service Classes: 3 managers
- ✅ CustomerManager.java (~200 LOC)
- ✅ BookingManager.java (~300 LOC)
- ✅ InvoiceManager.java (~280 LOC)

### Storage Classes: 1 class
- ✅ DataStorage.java (~400 LOC)

### Total Code: ~1400 LOC

---

## 🔗 Integration Points

### Với Thành viên 1 (Room Management)
```
Booking
├── contains Customer (thành viên 2)
└── contains Room (thành viên 1)

BookingManager
└── uses RoomManager để kiểm tra room availability

DataStorage
├── saves/loads Customers, Bookings, Invoices (thành viên 2)
└── integrates với RoomStorage (thành viên 1)
```

### Entity Relationships
```
Customer (1) ----> (n) Booking
Booking (1) ----> (1) Room
Booking (1) ----> (1) Invoice
```

---

## 📝 Các file được tạo

### Models
- `src/com/hotel/model/customer/Customer.java`
- `src/com/hotel/model/booking/Booking.java`
- `src/com/hotel/model/invoice/Invoice.java`

### Services
- `src/com/hotel/service/CustomerManager.java`
- `src/com/hotel/service/BookingManager.java`
- `src/com/hotel/service/InvoiceManager.java`

### Storage
- `src/com/hotel/storage/DataStorage.java`

### Documentation
- `docs/03_BACKLOG_MEMBER2.md` - Chi tiết Sprint planning

---

## 🎨 Design Patterns & OOP Concepts

✅ **Encapsulation**: Private fields + public getters/setters
✅ **Composition**: Booking contains Customer + Room, Invoice contains Booking
✅ **Inheritance**: Managers implement IManageable + ISearchable
✅ **Polymorphism**: Different implementations của CRUD operations
✅ **Single Responsibility**: Mỗi manager chỉ quản lý một loại entity
✅ **Open/Closed Principle**: Easy to extend với new managers
✅ **Dependency Injection**: Managers receive dependencies via constructor

---

## 📋 Công việc sắp tới (Sprint 4-5)

### UI Development
- [ ] BookingPanel - JTable + toolbar
- [ ] CustomerPanel - JTable + toolbar
- [ ] InvoicePanel - JTable + toolbar
- [ ] AddBookingDialog - Form + validation
- [ ] AddCustomerDialog - Form + validation
- [ ] CreateInvoiceDialog - Form + calculations

### Advanced Features
- [ ] SearchBookingDialog - Advanced search
- [ ] BookingReportPanel - Statistics
- [ ] InvoiceReportPanel - Revenue reports
- [ ] Integration tests
- [ ] Unit tests

### Optional
- [ ] Export invoice to PDF
- [ ] Email invoice
- [ ] Booking confirmation
- [ ] Customer review/rating

---

## 💡 Key Features Implemented

### Booking Availability Check
```
isRoomAvailable(room, checkInDate, checkOutDate)
- Kiểm tra các booking hiện tại
- Loại trừ những booking đã cancelled
- Kiểm tra overlapping dates
```

### Price Calculation
```
Booking.calculateTotalPrice()
= Room.calculatePrice(numberOfDays)
= basePrice * numberOfDays * multiplier
```

### Revenue Tracking
```
getTotalRevenue() - tổng doanh thu
getMonthlyRevenue() - doanh thu tháng
getTotalTax() - tổng thuế
getUnpaidRevenue() - doanh thu chưa thanh toán
```

---

## 🔍 Testing Strategy

### Unit Tests Cần:
- ✅ Customer CRUD operations
- ✅ Booking calculations
- ✅ Room availability logic
- ✅ Invoice calculations
- ✅ JSON serialization

### Integration Tests Cần:
- ✅ End-to-end booking flow
- ✅ DataStorage load/save
- ✅ Manager interactions

---

**Trạng thái**: ✅ Sprint 1-3 HOÀN THÀNH, Sprint 4-5 ĐANG TIẾN HÀNH
**Người phụ trách**: Thành viên 2
**Ngày cập nhật cuối**: 15/12/2025
