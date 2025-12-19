# BACKLOG & SPRINT PLANNING - THÀNH VIÊN 2
## Quản lý Đặt phòng, Khách hàng, Hóa đơn (Booking, Customer, Invoice Management)

---

## 📋 PRODUCT BACKLOG

### Epic 1: Customer Model Layer
| ID | User Story | Acceptance Criteria | Points |
|----|------------|---------------------|--------|
| US-1.1 | Tạo Customer class | - Private fields, public getters/setters<br>- Attributes: customerId, fullName, email, phoneNumber, idCard, address, registrationDate, isVIP, loyaltyPoints | 3 |
| US-1.2 | Implement loyalty points system | - addLoyaltyPoints() method<br>- Track customer VIP status | 2 |

### Epic 2: Booking Model Layer
| ID | User Story | Acceptance Criteria | Points |
|----|------------|---------------------|--------|
| US-2.1 | Tạo Booking class | - References to Customer and Room<br>- Attributes: bookingId, checkInDate, checkOutDate, status, totalPrice, notes | 3 |
| US-2.2 | Tính giá tiền booking | - calculateTotalPrice(): dựa trên số ngày<br>- getNumberOfDays() method | 2 |
| US-2.3 | Tạo BookingStatus enum | - PENDING, CONFIRMED, CHECKED_IN, COMPLETED, CANCELLED | 1 |

### Epic 3: Invoice Model Layer
| ID | User Story | Acceptance Criteria | Points |
|----|------------|---------------------|--------|
| US-3.1 | Tạo Invoice class | - References to Booking<br>- Attributes: invoiceId, invoiceDate, subtotal, taxRate, taxAmount, totalAmount, status | 3 |
| US-3.2 | Implement invoice calculations | - calculateAmounts() method<br>- Tax calculation | 2 |
| US-3.3 | Tạo InvoiceStatus enum | - DRAFT, ISSUED, PAID, CANCELLED | 1 |

### Epic 4: Business Logic - Managers
| ID | User Story | Acceptance Criteria | Points |
|----|------------|---------------------|--------|
| US-4.1 | CustomerManager CRUD | - add(), update(), delete(), getById(), getAll() | 3 |
| US-4.2 | BookingManager CRUD | - add(), update(), delete(), getById(), getAll() | 3 |
| US-4.3 | BookingManager availability check | - isRoomAvailable() method<br>- getAvailableRooms() method | 3 |
| US-4.4 | BookingManager revenue tracking | - getTotalRevenue(), getMonthlyRevenue() | 2 |
| US-4.5 | InvoiceManager CRUD | - add(), update(), delete(), getById(), getAll() | 3 |
| US-4.6 | InvoiceManager reporting | - getInvoicesByCustomer(), getInvoicesByStatus()<br>- Revenue tracking methods | 2 |

### Epic 5: Interfaces Implementation
| ID | User Story | Acceptance Criteria | Points |
|----|------------|---------------------|--------|
| US-5.1 | Implement IManageable for managers | - All managers implement IManageable<T> | 2 |
| US-5.2 | Implement ISearchable for managers | - CustomerManager, BookingManager implement ISearchable | 2 |

### Epic 6: Storage & Persistence
| ID | User Story | Acceptance Criteria | Points |
|----|------------|---------------------|--------|
| US-6.1 | DataStorage for Customers | - Load/Save customers to JSON<br>- Proper serialization/deserialization | 4 |
| US-6.2 | DataStorage for Bookings | - Load/Save bookings to JSON<br>- Handle relationships with Customer and Room | 4 |
| US-6.3 | DataStorage for Invoices | - Load/Save invoices to JSON<br>- Handle relationships with Booking | 4 |

### Epic 7: UI - Booking Management
| ID | User Story | Acceptance Criteria | Points |
|----|------------|---------------------|--------|
| US-7.1 | BookingPanel main view | - JTable hiển thị bookings<br>- Toolbar với buttons (Add, Edit, Delete, Search) | 8 |
| US-7.2 | AddBookingDialog | - Form với Customer selection, Room availability check<br>- Date validation | 6 |
| US-7.3 | EditBookingDialog | - Form sửa thông tin booking | 4 |
| US-7.4 | SearchBookingDialog | - Search by booking ID, customer name, room<br>- Filter by status, date range | 4 |

### Epic 8: UI - Customer Management
| ID | User Story | Acceptance Criteria | Points |
|----|------------|---------------------|--------|
| US-8.1 | CustomerPanel main view | - JTable hiển thị customers<br>- Toolbar với buttons | 6 |
| US-8.2 | AddCustomerDialog | - Form thêm khách hàng mới<br>- Validation | 4 |
| US-8.3 | EditCustomerDialog | - Form sửa thông tin khách hàng | 3 |
| US-8.4 | CustomerDetailDialog | - Xem chi tiết + booking history | 4 |

### Epic 9: UI - Invoice Management
| ID | User Story | Acceptance Criteria | Points |
|----|------------|---------------------|--------|
| US-9.1 | InvoicePanel main view | - JTable hiển thị invoices<br>- Toolbar với buttons | 6 |
| US-9.2 | CreateInvoiceDialog | - Generate invoice từ booking<br>- Show calculations | 4 |
| US-9.3 | InvoiceDetailDialog | - View invoice details + print | 4 |
| US-9.4 | InvoiceReportPanel | - Revenue reports<br>- Paid/Unpaid statistics | 5 |

### Epic 10: Testing
| ID | User Story | Acceptance Criteria | Points |
|----|------------|---------------------|--------|
| US-10.1 | Unit test model classes | - Test constructors, calculations | 3 |
| US-10.2 | Unit test managers | - Test CRUD, search, filter | 4 |
| US-10.3 | Unit test availability logic | - Test room booking conflicts | 2 |

---

## 🏃 SPRINT BREAKDOWN

### SPRINT 1: Foundation Models (Tuần 1-2)
**Goal**: Hoàn thiện tất cả Model classes

#### Tasks:
- [x] Task 1.1: Tạo Customer class
- [x] Task 1.2: Tạo Booking class
- [x] Task 1.3: Tạo Invoice class
- [x] Task 1.4: Tạo BookingStatus enum
- [x] Task 1.5: Tạo InvoiceStatus enum
- [ ] Task 1.6: Write unit tests cho Model classes

**Definition of Done**:
- Tất cả classes compile không lỗi
- Các methods hoạt động đúng
- Unit tests pass 100%

---

### SPRINT 2: Business Logic (Tuần 3-4)
**Goal**: Hoàn thiện Manager classes

#### Tasks:
- [x] Task 2.1: Implement CustomerManager
- [x] Task 2.2: Implement BookingManager
- [x] Task 2.3: Implement BookingManager availability check
- [x] Task 2.4: Implement InvoiceManager
- [x] Task 2.5: Implement IManageable for managers
- [x] Task 2.6: Implement ISearchable for managers
- [ ] Task 2.7: Write unit tests cho managers

**Definition of Done**:
- CRUD operations hoạt động
- Search/Filter hoạt động đúng
- Unit tests pass 100%

---

### SPRINT 3: Storage & Persistence (Tuần 5-6)
**Goal**: Hoàn thiện data storage

#### Tasks:
- [x] Task 3.1: Implement DataStorage (Customers)
- [x] Task 3.2: Implement DataStorage (Bookings)
- [x] Task 3.3: Implement DataStorage (Invoices)
- [ ] Task 3.4: Integration testing với JSON files

**Definition of Done**:
- Data persist sau khi restart
- Load/Save hoạt động đúng
- Integration tests pass

---

### SPRINT 4: UI Development (Tuần 7-8)
**Goal**: Hoàn thiện UI cho tất cả managers

#### Tasks:
- [ ] Task 4.1: Implement BookingPanel
- [ ] Task 4.2: Implement AddBookingDialog
- [ ] Task 4.3: Implement CustomerPanel
- [ ] Task 4.4: Implement AddCustomerDialog
- [ ] Task 4.5: Implement InvoicePanel
- [ ] Task 4.6: Implement InvoiceReportPanel

**Definition of Done**:
- UI hiển thị đúng data
- CRUD từ UI hoạt động
- Data persist khi thao tác

---

### SPRINT 5: Search & Reports (Tuần 9-10)
**Goal**: Hoàn thiện search, filter và reporting

#### Tasks:
- [ ] Task 5.1: Implement SearchBookingDialog
- [ ] Task 5.2: Implement booking filter functionality
- [ ] Task 5.3: Implement CustomerDetailDialog
- [ ] Task 5.4: Implement InvoiceDetailDialog
- [ ] Task 5.5: Implement revenue reports

**Definition of Done**:
- Tất cả search/filter hoạt động
- Reports chính xác
- No bugs

---

## 📊 VELOCITY TRACKING

| Sprint | Planned | Completed | Notes |
|--------|---------|-----------|-------|
| Sprint 1 | 11 pts | 9 pts | Models done |
| Sprint 2 | 15 pts | 12 pts | Managers done |
| Sprint 3 | 12 pts | 9 pts | Storage done |
| Sprint 4 | 18 pts | - | |
| Sprint 5 | 15 pts | - | |
| **Total** | **71 pts** | **30 pts** | |

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

## 🔗 INTEGRATION POINTS VỚI THÀNH VIÊN 1

| My Component | Their Component | Integration |
|--------------|-----------------|-------------|
| Booking | Room | Booking chứa Room reference |
| BookingManager | RoomManager | Check room availability |
| BookingPanel | MainFrame | Navigation từ menu |
| DataStorage | RoomStorage | Shared file handling |

---

**Last Updated**: 15/12/2025
**Status**: Ready for Sprint 1

```
