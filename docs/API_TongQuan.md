# API TỔNG QUAN HỆ THỐNG QUẢN LÝ KHÁCH SẠN

## Kiến trúc 4 tầng

```
┌──────────────────────────────────────────────────────────────────┐
│                     UI LAYER (View - Swing)                       │
│   LoginFrame, MainFrame, RoomPanel, BookingPanel, InvoicePanel   │
├──────────────────────────────────────────────────────────────────┤
│                     ↓↓ GỌI API ↓↓                                │
├──────────────────────────────────────────────────────────────────┤
│                   SERVICE LAYER (Controller)                      │
│   RoomManager, BookingManager, CustomerManager, InvoiceManager   │
├──────────────────────────────────────────────────────────────────┤
│                     ↓↓ SỬ DỤNG ↓↓                                │
├──────────────────────────────────────────────────────────────────┤
│                     MODEL LAYER (Entity)                          │
│       Room, Booking, Customer, Invoice, User, Enums              │
├──────────────────────────────────────────────────────────────────┤
│                     ↓↓ LƯU TRỮ ↓↓                                │
├──────────────────────────────────────────────────────────────────┤
│                    STORAGE LAYER (JSON)                           │
│              RoomStorage, DataStorage → JSON files               │
└──────────────────────────────────────────────────────────────────┘
```

---

## INTERFACES (Hợp đồng API)

### 1. `IManageable<T>` - CRUD Operations

```java
public interface IManageable<T> {
    boolean add(T item);           // Thêm mới
    boolean update(T item);        // Cập nhật
    boolean delete(String id);     // Xóa theo ID
    T getById(String id);          // Lấy theo ID
    List<T> getAll();              // Lấy tất cả
    int count();                   // Đếm số lượng
    boolean isEmpty();             // Kiểm tra rỗng
    void clear();                  // Xóa tất cả
}
```

**Lớp implement:**
- `RoomManager implements IManageable<Room>`
- `BookingManager implements IManageable<Booking>`
- `CustomerManager implements IManageable<Customer>`
- `InvoiceManager implements IManageable<Invoice>`

### 2. `ISearchable<T>` - Search & Filter

```java
public interface ISearchable<T> {
    List<T> search(String keyword);              // Tìm theo từ khóa
    List<T> filter(Map<String, Object> criteria); // Lọc theo điều kiện
}
```

**Lớp implement:**
- `RoomManager implements ISearchable<Room>`
- `BookingManager implements ISearchable<Booking>`
- `CustomerManager implements ISearchable<Customer>`

---

## API CHI TIẾT TỪNG MANAGER

### 1. RoomManager API

| Method | Mô tả | UI gọi từ đâu |
|--------|-------|---------------|
| `add(Room room)` | Thêm phòng mới | RoomPanel → AddRoomDialog |
| `update(Room room)` | Cập nhật phòng | RoomPanel → EditRoomDialog |
| `delete(String roomId)` | Xóa phòng | RoomPanel → deleteRoom() |
| `getById(String id)` | Lấy phòng theo ID | BookingPanel, InvoicePanel |
| `getAll()` | Lấy tất cả phòng | RoomPanel.loadData() |
| `search(String keyword)` | Tìm theo từ khóa | RoomPanel.performSearch() |
| `findByType(RoomType type)` | Lọc theo loại | RoomPanel filter |
| `findByStatus(RoomStatus status)` | Lọc theo trạng thái | RoomPanel filter |
| `findAvailableRooms()` | Lấy phòng trống | AddBookingDialog |
| `getStatistics()` | Thống kê phòng | DashboardPanel |

**Ví dụ UI gọi:**
```java
// Trong RoomPanel.java
private void loadData() {
    List<Room> rooms = roomManager.getAll();  // ← GỌI API
    for (Room room : rooms) {
        tableModel.addRow(new Object[]{...});
    }
}

private void deleteRoom() {
    String roomId = (String) tableModel.getValueAt(selectedRow, 0);
    if (roomManager.delete(roomId)) {         // ← GỌI API
        JOptionPane.showMessageDialog(this, "Xóa thành công!");
        loadData();
    }
}
```

---

### 2. BookingManager API

| Method | Mô tả | UI gọi từ đâu |
|--------|-------|---------------|
| `add(Booking booking)` | Tạo đặt phòng | AddBookingDialog.saveBooking() |
| `update(Booking booking)` | Cập nhật | EditBookingDialog.updateBooking() |
| `delete(String bookingId)` | Xóa booking | BookingPanel.deleteBooking() |
| `getById(String id)` | Lấy booking | InvoicePanel |
| `getAll()` | Lấy tất cả | BookingPanel.loadData() |
| `search(String keyword)` | Tìm kiếm | BookingPanel.performSearch() |
| `isRoomAvailable(room, checkIn, checkOut)` | Kiểm tra trống | AddBookingDialog |
| `getAvailableRooms(checkIn, checkOut)` | Phòng trống | AddBookingDialog |
| `getBookingsByStatus(status)` | Lọc theo status | BookingPanel.filterByStatus() |
| `getTotalRevenue()` | Tổng doanh thu | DashboardPanel |
| `getMonthlyRevenue(month, year)` | Doanh thu tháng | ReportPanel |

**Ví dụ UI gọi:**
```java
// Trong AddBookingDialog.java
private void refreshAvailableRooms() {
    LocalDate checkIn = toLocalDate((Date) checkInSpinner.getValue());
    LocalDate checkOut = toLocalDate((Date) checkOutSpinner.getValue());
    
    // ← GỌI API: Lấy danh sách phòng trống trong khoảng thời gian
    List<Room> availableRooms = bookingManager.getAvailableRooms(checkIn, checkOut);
    
    roomCombo.removeAllItems();
    for (Room room : availableRooms) {
        roomCombo.addItem(room.getRoomId() + " - " + room.getRoomType());
    }
}

private void saveBooking() {
    Booking booking = new Booking(id, customer, room, checkIn, checkOut, PENDING);
    
    if (bookingManager.add(booking)) {        // ← GỌI API: Thêm booking
        dataStorage.saveAllData();            // ← GỌI API: Lưu vào JSON
        JOptionPane.showMessageDialog(this, "Đặt phòng thành công!");
    }
}
```

---

### 3. CustomerManager API

| Method | Mô tả | UI gọi từ đâu |
|--------|-------|---------------|
| `add(Customer customer)` | Thêm khách hàng | CustomerPanel, AddBookingDialog |
| `update(Customer customer)` | Cập nhật | CustomerPanel |
| `delete(String customerId)` | Xóa | CustomerPanel |
| `getById(String id)` | Lấy khách | BookingPanel |
| `getAll()` | Lấy tất cả | CustomerPanel, AddBookingDialog |
| `search(String keyword)` | Tìm kiếm | CustomerPanel |
| `getTotalCustomers()` | Đếm tổng | DashboardPanel |
| `getVIPCustomers()` | Đếm VIP | DashboardPanel |

**Ví dụ UI gọi:**
```java
// Trong AddBookingDialog.java
private void loadCustomers() {
    List<Customer> customers = customerManager.getAll();  // ← GỌI API
    for (Customer c : customers) {
        customerCombo.addItem(c.getFullName() + " (" + c.getCustomerId() + ")");
    }
}
```

---

### 4. InvoiceManager API

| Method | Mô tả | UI gọi từ đâu |
|--------|-------|---------------|
| `add(Invoice invoice)` | Thêm hóa đơn | InvoicePanel |
| `update(Invoice invoice)` | Cập nhật | InvoicePanel |
| `createInvoiceFromBooking(booking, id)` | Tạo từ booking | InvoicePanel |
| `getById(String id)` | Lấy hóa đơn | InvoicePanel |
| `getAll()` | Lấy tất cả | InvoicePanel |
| `markInvoiceAsPaid(String id)` | Đánh dấu đã thanh toán | InvoicePanel |
| `cancelInvoice(String id)` | Hủy hóa đơn | InvoicePanel |
| `getTotalRevenue()` | Tổng doanh thu | DashboardPanel |
| `getUnpaidRevenue()` | Doanh thu chưa thu | DashboardPanel |
| `getMonthlyRevenue(month, year)` | Doanh thu tháng | ReportPanel |

**Ví dụ UI gọi:**
```java
// Trong InvoicePanel.java
private void createInvoiceFromBooking() {
    Booking booking = bookingManager.getById(bookingId);    // ← GỌI API
    
    String invoiceId = "INV-" + System.currentTimeMillis();
    Invoice invoice = invoiceManager.createInvoiceFromBooking(booking, invoiceId);  // ← GỌI API
    
    dataStorage.saveAllData();                              // ← GỌI API: Lưu
    JOptionPane.showMessageDialog(this, "Tạo hóa đơn thành công!");
}

private void markAsPaid() {
    invoiceManager.markInvoiceAsPaid(invoiceId);            // ← GỌI API
    dataStorage.saveAllData();
    loadData();
}
```

---

## LUỒNG DỮ LIỆU ĐIỂN HÌNH

### Luồng "Đặt phòng mới":

```
┌─────────────┐    ┌──────────────────┐    ┌────────────────┐    ┌─────────────┐
│ BookingPanel│ →  │AddBookingDialog  │ →  │ BookingManager │ →  │ DataStorage │
│ (UI)        │    │ (UI Dialog)      │    │ (Business)     │    │ (JSON)      │
└─────────────┘    └──────────────────┘    └────────────────┘    └─────────────┘
     │                     │                       │                     │
     │ click "Thêm"        │                       │                     │
     │────────────────────►│                       │                     │
     │                     │                       │                     │
     │                     │ getAvailableRooms()   │                     │
     │                     │──────────────────────►│                     │
     │                     │                       │                     │
     │                     │ customerManager.getAll()                    │
     │                     │──────────────────────►│                     │
     │                     │                       │                     │
     │                     │ click "Lưu"           │                     │
     │                     │ add(booking)          │                     │
     │                     │──────────────────────►│                     │
     │                     │                       │                     │
     │                     │                       │ saveAllData()       │
     │                     │                       │────────────────────►│
     │                     │                       │                     │ → bookings.json
```

### Luồng "Check-out và Tạo hóa đơn":

```
┌──────────────┐    ┌─────────────────┐    ┌────────────────┐    ┌─────────────┐
│ InvoicePanel │ →  │ BookingManager  │    │ InvoiceManager │ →  │ DataStorage │
└──────────────┘    └─────────────────┘    └────────────────┘    └─────────────┘
       │                    │                      │                     │
       │ getById(bookingId) │                      │                     │
       │───────────────────►│                      │                     │
       │◄───────────────────│ return Booking       │                     │
       │                    │                      │                     │
       │ createInvoiceFromBooking(booking)         │                     │
       │──────────────────────────────────────────►│                     │
       │◄──────────────────────────────────────────│ return Invoice      │
       │                    │                      │                     │
       │ markInvoiceAsPaid(invoiceId)              │                     │
       │──────────────────────────────────────────►│                     │
       │                    │                      │                     │
       │                    │                      │ saveAllData()       │
       │                    │                      │────────────────────►│
```

---

## CÁCH UI KHỞI TẠO VÀ GỌI MANAGER

### MainFrame.java - Khởi tạo tất cả Manager:

```java
public class MainFrame extends JFrame {
    private RoomManager roomManager;
    private BookingManager bookingManager;
    private CustomerManager customerManager;
    private InvoiceManager invoiceManager;
    private DataStorage dataStorage;

    public MainFrame() {
        // 1. Khởi tạo Manager
        roomManager = RoomManager.getInstance();  // Singleton
        bookingManager = new BookingManager(roomManager);
        customerManager = new CustomerManager();
        invoiceManager = new InvoiceManager();
        
        // 2. Khởi tạo Storage và load data
        dataStorage = new DataStorage(roomManager, bookingManager, 
                                       customerManager, invoiceManager);
        dataStorage.loadAllData();  // Load từ JSON
        
        // 3. Truyền Manager vào UI Panel
        tabbedPane.addTab("Phòng", new RoomPanel(roomManager));
        tabbedPane.addTab("Đặt phòng", new BookingPanel(bookingManager, roomManager, customerManager));
        tabbedPane.addTab("Khách hàng", new CustomerPanel(customerManager));
        tabbedPane.addTab("Hóa đơn", new InvoicePanel(invoiceManager, bookingManager));
    }
}
```

### Panel nhận Manager qua Constructor:

```java
public class BookingPanel extends JPanel {
    private BookingManager bookingManager;
    private RoomManager roomManager;
    private CustomerManager customerManager;

    // Nhận Manager từ MainFrame
    public BookingPanel(BookingManager bm, RoomManager rm, CustomerManager cm) {
        this.bookingManager = bm;
        this.roomManager = rm;
        this.customerManager = cm;
        
        initializeUI();
        loadData();  // Gọi API ngay khi khởi tạo
    }
    
    private void loadData() {
        List<Booking> bookings = bookingManager.getAll();  // Gọi API
        // Hiển thị lên table...
    }
}
```

---

## TÓM TẮT

| Layer | Thành phần | Trách nhiệm |
|-------|-----------|-------------|
| **UI** | Panel, Dialog, Frame | Hiển thị + nhận input từ user |
| **Service** | Manager classes | Business logic + CRUD |
| **Model** | Entity classes | Chứa dữ liệu (Room, Booking, ...) |
| **Storage** | DataStorage, RoomStorage | Đọc/ghi JSON |

**UI chỉ gọi Manager, KHÔNG trực tiếp thao tác dữ liệu!**
