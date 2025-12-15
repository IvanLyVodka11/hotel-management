# Hướng dẫn Sử dụng Tính năng Thành viên 2

## 📖 Mục lục
1. Quản lý Khách hàng
2. Quản lý Đặt phòng
3. Quản lý Hóa đơn
4. Kiểm tra Phòng Trống
5. Báo cáo Doanh thu
6. Lưu & Tải Dữ liệu

---

## 1. Quản lý Khách hàng

### Tạo Khách hàng mới

```java
CustomerManager customerManager = new CustomerManager();

// Tạo customer
Customer customer = new Customer(
    "CUST001",                              // customerId
    "Nguyễn Văn A",                         // fullName
    "nguyenvana@email.com",                 // email
    "0123456789",                           // phoneNumber
    "012345678901",                         // idCard
    "123 Đường ABC, TP HCM",               // address
    LocalDate.of(2025, 12, 15),            // registrationDate
    false                                   // isVIP
);

// Thêm vào danh sách
customerManager.add(customer);
```

### Cập nhật thông tin khách hàng

```java
// Lấy customer
Customer existing = customerManager.getById("CUST001");

// Cập nhật thông tin
existing.setEmail("new_email@email.com");
existing.setPhoneNumber("0987654321");
existing.setVIP(true);

// Lưu thay đổi
customerManager.update(existing);
```

### Quản lý Loyalty Points

```java
Customer customer = customerManager.getById("CUST001");

// Cộng điểm loyalty (ví dụ: 1000 điểm mỗi lần book)
customer.addLoyaltyPoints(1000);

// Cập nhật
customerManager.update(customer);

// Kiểm tra điểm
System.out.println("Điểm loyalty: " + customer.getLoyaltyPoints());
```

### Tìm kiếm khách hàng

```java
// Tìm theo keyword (tên, email, phone, ID)
List<Customer> results = customerManager.search("Nguyễn");

// Lọc khách hàng VIP
List<Customer> vipCustomers = customerManager.filter("vip", true);

// Lọc khách hàng có loyalty points >= 5000
List<Customer> loyalCustomers = customerManager.filter("loyaltyPoints", 5000.0);
```

### Thống kê khách hàng

```java
// Tổng số khách hàng
int totalCustomers = customerManager.getTotalCustomers();

// Số khách hàng VIP
int vipCount = customerManager.getVIPCustomers();

System.out.println("Tổng: " + totalCustomers);
System.out.println("VIP: " + vipCount);
```

### Xóa khách hàng

```java
customerManager.delete("CUST001");
```

---

## 2. Quản lý Đặt phòng

### Tạo đặt phòng

```java
// Giả sử đã có Customer và Room từ các manager khác
Customer customer = customerManager.getById("CUST001");
Room room = roomManager.getById("R101");

// Tạo booking
Booking booking = new Booking(
    "BK001",                           // bookingId
    customer,                          // customer
    room,                              // room
    LocalDate.of(2025, 12, 20),       // checkInDate
    LocalDate.of(2025, 12, 23),       // checkOutDate
    BookingStatus.PENDING              // status
);

// Thêm ghi chú nếu cần
booking.setNotes("Yêu cầu phòng view biển");

// Thêm vào danh sách
bookingManager.add(booking);

System.out.println("Đặt phòng: " + booking.getNumberOfDays() + " đêm");
System.out.println("Giá: " + booking.getTotalPrice() + " VND");
```

### Kiểm tra phòng trống

```java
// Trước khi tạo booking, kiểm tra phòng có sẵn không
LocalDate checkIn = LocalDate.of(2025, 12, 20);
LocalDate checkOut = LocalDate.of(2025, 12, 23);

boolean available = bookingManager.isRoomAvailable(room, checkIn, checkOut);

if (available) {
    System.out.println("Phòng còn trống");
} else {
    System.out.println("Phòng đã được đặt");
}
```

### Lấy danh sách phòng trống

```java
LocalDate checkIn = LocalDate.of(2025, 12, 20);
LocalDate checkOut = LocalDate.of(2025, 12, 23);

// Lấy phòng còn trống
List<Room> availableRooms = bookingManager.getAvailableRooms(checkIn, checkOut);

for (Room room : availableRooms) {
    System.out.println(room.getRoomId() + " - " + room.getPrice());
}
```

### Cập nhật trạng thái booking

```java
Booking booking = bookingManager.getById("BK001");

// Chuyển thành CONFIRMED
booking.setStatus(BookingStatus.CONFIRMED);
bookingManager.update(booking);

// Chuyển thành CHECKED_IN
booking.setStatus(BookingStatus.CHECKED_IN);
bookingManager.update(booking);

// Chuyển thành COMPLETED
booking.setStatus(BookingStatus.COMPLETED);
bookingManager.update(booking);
```

### Tìm kiếm booking

```java
// Tìm theo keyword
List<Booking> results = bookingManager.search("Nguyễn");

// Lọc theo trạng thái
List<Booking> pending = bookingManager.filter("status", BookingStatus.PENDING);
List<Booking> confirmed = bookingManager.filter("status", BookingStatus.CONFIRMED);

// Lấy booking của khách hàng
List<Booking> customerBookings = bookingManager.getCustomerBookings("CUST001");

// Lấy booking theo trạng thái
List<Booking> completed = bookingManager.getBookingsByStatus(BookingStatus.COMPLETED);
```

### Xóa/Hủy booking

```java
// Xóa booking
bookingManager.delete("BK001");

// Hoặc hủy booking (giữ lại record)
Booking booking = bookingManager.getById("BK001");
booking.setStatus(BookingStatus.CANCELLED);
bookingManager.update(booking);
```

---

## 3. Quản lý Hóa đơn

### Tạo hóa đơn từ booking

```java
Booking booking = bookingManager.getById("BK001");

// Tạo hóa đơn từ booking
Invoice invoice = invoiceManager.createInvoiceFromBooking(booking, "INV001");

System.out.println("Hóa đơn ID: " + invoice.getInvoiceId());
System.out.println("Subtotal: " + invoice.getSubtotal());
System.out.println("Thuế (10%): " + invoice.getTaxAmount());
System.out.println("Tổng cộng: " + invoice.getTotalAmount());
```

### Đánh dấu hóa đơn

```java
// Đánh dấu đã phát hành
invoiceManager.markAsIssued("INV001");
// hoặc
Invoice invoice = invoiceManager.getById("INV001");
invoice.markAsIssued();
invoiceManager.update(invoice);

// Đánh dấu đã thanh toán
invoiceManager.markInvoiceAsPaid("INV001");
// hoặc
invoice.markAsPaid();
invoiceManager.update(invoice);
```

### Hủy hóa đơn

```java
invoiceManager.cancelInvoice("INV001");
// hoặc
Invoice invoice = invoiceManager.getById("INV001");
invoice.cancel();
invoiceManager.update(invoice);
```

### Tìm hóa đơn

```java
// Lấy hóa đơn theo booking
Invoice invoice = invoiceManager.getInvoiceByBooking("BK001");

// Lấy hóa đơn của khách hàng
List<Invoice> customerInvoices = invoiceManager.getInvoicesByCustomer("CUST001");

// Lấy hóa đơn theo trạng thái
List<Invoice> paidInvoices = invoiceManager.getInvoicesByStatus(InvoiceStatus.PAID);
List<Invoice> unpaidInvoices = invoiceManager.getInvoicesByStatus(InvoiceStatus.ISSUED);

// Lấy hóa đơn trong khoảng thời gian
LocalDate start = LocalDate.of(2025, 12, 1);
LocalDate end = LocalDate.of(2025, 12, 31);
List<Invoice> monthlyInvoices = invoiceManager.getInvoicesByDateRange(start, end);
```

---

## 4. Kiểm tra Phòng Trống

### Workflow đặt phòng

```java
public Booking createNewBooking(String customerId, String roomId, 
                               LocalDate checkIn, LocalDate checkOut) {
    // Bước 1: Lấy customer
    Customer customer = customerManager.getById(customerId);
    if (customer == null) {
        System.out.println("Khách hàng không tồn tại");
        return null;
    }
    
    // Bước 2: Lấy room
    Room room = roomManager.getById(roomId);
    if (room == null) {
        System.out.println("Phòng không tồn tại");
        return null;
    }
    
    // Bước 3: Kiểm tra phòng trống
    if (!bookingManager.isRoomAvailable(room, checkIn, checkOut)) {
        System.out.println("Phòng không trống trong khoảng thời gian này");
        return null;
    }
    
    // Bước 4: Tạo booking
    String bookingId = "BK" + System.currentTimeMillis();
    Booking booking = new Booking(bookingId, customer, room, checkIn, checkOut, 
                                 BookingStatus.PENDING);
    
    // Bước 5: Lưu booking
    bookingManager.add(booking);
    
    // Bước 6: Tạo invoice
    String invoiceId = "INV" + System.currentTimeMillis();
    invoiceManager.createInvoiceFromBooking(booking, invoiceId);
    
    return booking;
}

// Sử dụng
Booking newBooking = createNewBooking("CUST001", "R101", 
                                      LocalDate.of(2025, 12, 20),
                                      LocalDate.of(2025, 12, 23));
```

---

## 5. Báo cáo Doanh thu

### Doanh thu tổng

```java
// Tổng doanh thu từ booking đã hoàn thành
double totalRevenue = bookingManager.getTotalRevenue();
System.out.println("Tổng doanh thu: " + totalRevenue);

// Tổng doanh thu từ invoice đã thanh toán
double invoiceRevenue = invoiceManager.getTotalRevenue();
System.out.println("Doanh thu invoice: " + invoiceRevenue);
```

### Doanh thu tháng

```java
// Doanh thu tháng 12 năm 2025
double decemberRevenue = bookingManager.getMonthlyRevenue(12, 2025);
System.out.println("Doanh thu tháng 12: " + decemberRevenue);

// Từ invoice
double invoiceRevenue = invoiceManager.getMonthlyRevenue(12, 2025);
System.out.println("Doanh thu invoice tháng 12: " + invoiceRevenue);
```

### Báo cáo chi tiết

```java
// Tổng thuế đã thu
double totalTax = invoiceManager.getTotalTax();
System.out.println("Tổng thuế: " + totalTax);

// Doanh thu chưa thanh toán
double unpaidRevenue = invoiceManager.getUnpaidRevenue();
System.out.println("Doanh thu chưa thanh toán: " + unpaidRevenue);

// Số hóa đơn đã thanh toán
int paidCount = invoiceManager.getPaidInvoices();
System.out.println("Hóa đơn đã thanh toán: " + paidCount);

// Số hóa đơn chưa thanh toán
int unpaidCount = invoiceManager.getUnpaidInvoices();
System.out.println("Hóa đơn chưa thanh toán: " + unpaidCount);
```

### Thống kê booking

```java
// Tổng booking
int totalBookings = bookingManager.getTotalBookings();

// Booking hoàn thành
int completed = bookingManager.getCompletedBookings();

System.out.println("Tổng booking: " + totalBookings);
System.out.println("Hoàn thành: " + completed);
```

---

## 6. Lưu & Tải Dữ liệu

### Khởi tạo DataStorage

```java
// Tạo managers
CustomerManager customerManager = new CustomerManager();
BookingManager bookingManager = new BookingManager();
InvoiceManager invoiceManager = new InvoiceManager();
RoomManager roomManager = new RoomManager();

// Tạo storage
DataStorage storage = new DataStorage(customerManager, bookingManager, 
                                      invoiceManager, roomManager);

// Tải dữ liệu từ file JSON
storage.loadAllData();
```

### Lưu dữ liệu

```java
// Lưu tất cả dữ liệu
storage.saveAllData();

// Hoặc lưu riêng từng loại
storage.saveCustomers();
storage.saveBookings();
storage.saveInvoices();
```

### Tải dữ liệu

```java
// Tải tất cả
storage.loadAllData();

// Hoặc tải riêng
storage.loadCustomers();
storage.loadBookings();
storage.loadInvoices();
```

### Cấu trúc file JSON

**customers.json:**
```json
[
  {
    "customerId": "CUST001",
    "fullName": "Nguyễn Văn A",
    "email": "nguyenvana@email.com",
    "phoneNumber": "0123456789",
    "idCard": "012345678901",
    "address": "123 Đường ABC, TP HCM",
    "registrationDate": "2025-12-15",
    "isVIP": false,
    "loyaltyPoints": 0.0
  }
]
```

**bookings.json:**
```json
[
  {
    "bookingId": "BK001",
    "customerId": "CUST001",
    "roomId": "R101",
    "checkInDate": "2025-12-20",
    "checkOutDate": "2025-12-23",
    "status": "CONFIRMED",
    "totalPrice": 1500000.0,
    "notes": "Yêu cầu phòng view biển"
  }
]
```

**invoices.json:**
```json
[
  {
    "invoiceId": "INV001",
    "bookingId": "BK001",
    "invoiceDate": "2025-12-15",
    "subtotal": 1500000.0,
    "taxRate": 0.1,
    "taxAmount": 150000.0,
    "totalAmount": 1650000.0,
    "status": "PAID",
    "notes": ""
  }
]
```

---

## 💡 Ví dụ Tổng hợp

### Scenario: Đặt phòng hoàn chỉnh

```java
// 1. Tạo customer
Customer customer = new Customer(
    "CUST001", "Trần Thị B", "trantb@email.com", 
    "0987654321", "123456789012", "456 Đường XYZ", 
    LocalDate.now(), false
);
customerManager.add(customer);

// 2. Lấy phòng
Room room = roomManager.getById("R101");

// 3. Kiểm tra phòng trống
if (!bookingManager.isRoomAvailable(room, 
    LocalDate.of(2025, 12, 25),
    LocalDate.of(2025, 12, 27))) {
    System.out.println("Phòng không trống");
    return;
}

// 4. Tạo booking
Booking booking = new Booking(
    "BK001", customer, room,
    LocalDate.of(2025, 12, 25),
    LocalDate.of(2025, 12, 27),
    BookingStatus.CONFIRMED
);
booking.setNotes("Khách VIP, yêu cầu room service");
bookingManager.add(booking);

// 5. Tạo hóa đơn
Invoice invoice = invoiceManager.createInvoiceFromBooking(booking, "INV001");
invoice.setStatus(InvoiceStatus.ISSUED);
invoiceManager.update(invoice);

// 6. Cộng loyalty points
customer.addLoyaltyPoints(booking.getTotalPrice() * 0.01); // 1% của giá
customerManager.update(customer);

// 7. Lưu dữ liệu
storage.saveAllData();

// 8. In thông tin
System.out.println("Booking thành công!");
System.out.println("ID: " + booking.getBookingId());
System.out.println("Khách: " + customer.getFullName());
System.out.println("Phòng: " + room.getRoomId());
System.out.println("Giá: " + booking.getTotalPrice() + " VND");
System.out.println("Hóa đơn: " + invoice.getInvoiceId());
```

---

**Hướng dẫn phiên bản**: 1.0
**Cập nhật**: 15/12/2025
**Tác giả**: Thành viên 2
