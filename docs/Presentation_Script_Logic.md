# PROMPT THUYẾT TRÌNH - GIẢI THÍCH LOGIC VÀ TƯ DUY THIẾT KẾ

## Mục đích: Thể hiện SỰ HIỂU BIẾT về OOP, không chỉ làm được mà còn HIỂU TẠI SAO

---

## KỊCH BẢN THUYẾT TRÌNH (10-15 phút)

### PHẦN 1: MỞ ĐẦU - ĐẶT VẤN ĐỀ (2 phút)

**Slide 1-2:**
> "Thưa thầy, khi nhận đề tài Quản lý Khách sạn, câu hỏi đầu tiên em đặt ra không phải là 'viết code gì' mà là 'hệ thống này CẦN những gì và AI là người dùng nó?'"

**Logic cần giải thích:**
- Tại sao bắt đầu từ NGHIỆP VỤ chứ không phải CODE?
- Phân tích: Khách sạn có 3 nhóm người dùng với nhu cầu KHÁC NHAU
  - Lễ tân: Cần nhanh, thao tác nhiều lần trong ngày
  - Quản lý: Cần tổng quan, báo cáo
  - Dịch vụ: Cần đơn giản, chỉ xem thông tin

> "Từ đó em xác định: hệ thống phải PHÂN QUYỀN ngay từ đầu, không phải nghĩ sau."

---

### PHẦN 2: TỪ NGHIỆP VỤ ĐẾN USE CASE (2 phút)

**Slide 3-4:**
> "Sau khi hiểu AI dùng hệ thống, em hỏi tiếp: HỌ LÀM GÌ? Mục đích cuối cùng của họ là gì?"

**Logic cần giải thích:**
- Lễ tân muốn gì? → **Đặt phòng thành công** cho khách
- Quản lý muốn gì? → **Biết doanh thu**, **kiểm soát phòng**
- Mỗi MỤC ĐÍCH = 1 USE CASE

> "Use Case không phải là BƯỚC LÀM (click chuột, nhập form) mà là MỤC TIÊU ĐẠT ĐƯỢC. Ví dụ 'Đặt phòng' là use case, nhưng 'Nhập ngày check-in' chỉ là một bước trong use case đó."

**Giải thích sâu về Use Case "Đặt phòng":**
- Tại sao đây là use case PHỨC TẠP NHẤT?
- Vì nó liên quan đến 3 thực thể: Customer, Room, Booking
- Vì có logic KIỂM TRA TRÙNG (availability check)
- Vì có TÍNH TOÁN (giá tiền dựa trên loại phòng và số ngày)

---

### PHẦN 3: TỪ USE CASE ĐẾN CLASS (3 phút) ⭐ QUAN TRỌNG

**Slide 5-6:**
> "Đây là bước quan trọng nhất: LÀM SAO TỪ USE CASE RA ĐƯỢC CÁC CLASS?"

**Logic cần giải thích:**

**Bước 1: Tìm DANH TỪ trong use case**
```
Use case "Đặt phòng": 
- KHÁCH HÀNG đặt PHÒNG trong khoảng NGÀY, tạo ra BOOKING, sau đó có HÓA ĐƠN

=> Danh từ: Khách hàng, Phòng, Booking, Hóa đơn
=> Đó chính là các CLASS CẦN TẠO!
```

**Bước 2: Xác định QUAN HỆ**
> "Khi em hỏi: 'Booking LÀ GÌ?' - Nó không phải là Phòng, không phải là Khách hàng, nhưng nó CHỨA cả hai."

```
- Booking LÀ một Phòng? ❌ Không → Không dùng Inheritance
- Booking CÓ một Phòng? ✅ Có → Dùng Composition

=> Booking HAS-A Customer, HAS-A Room
```

> "Đây là lý do em dùng COMPOSITION thay vì INHERITANCE cho quan hệ này."

**Bước 3: Xác định HÀNH VI**
```
- Booking cần làm gì? → Tính giá, kiểm tra hợp lệ
- Room cần làm gì? → Tính giá theo loại (Standard/VIP/Deluxe)
```

---

### PHẦN 4: TẠI SAO DÙNG ABSTRACT CLASS CHO ROOM? (2 phút) ⭐

**Slide 7:**
> "Đây là câu hỏi em tự hỏi: Tại sao không tạo 1 class Room với thuộc tính 'roomType'?"

**So sánh 2 cách:**

**Cách 1: Không dùng kế thừa**
```java
class Room {
    String roomType;
    double getPrice(int days) {
        if (roomType.equals("STANDARD")) return 500000 * days;
        else if (roomType.equals("VIP")) return 1000000 * days * 1.2;
        else if (roomType.equals("DELUXE")) return 1500000 * days * 1.5;
    }
}
// VẤN ĐỀ: Mỗi lần thêm loại phòng mới → phải SỬA code cũ
// Vi phạm nguyên lý Open-Closed Principle
```

**Cách 2: Dùng kế thừa (Em chọn cách này)**
```java
abstract class Room {
    abstract double calculatePrice(int days);
}

class VIPRoom extends Room {
    double calculatePrice(int days) { return 1000000 * days * 1.2; }
}
// Thêm loại phòng mới? → CHỈ CẦN TẠO CLASS MỚI, không sửa code cũ!
```

> "Đây chính là POLYMORPHISM - cùng method calculatePrice() nhưng mỗi loại phòng tính KHÁC NHAU. Hệ thống không cần biết đang xử lý loại phòng gì, chỉ cần gọi calculatePrice() là đủ."

---

### PHẦN 5: TẠI SAO CẦN INTERFACE? (2 phút) ⭐

**Slide 8:**
> "Em nhận thấy: RoomManager, BookingManager, CustomerManager đều có các method GIỐNG NHAU: add(), delete(), getById(), getAll()"

**Logic:**
```
Nếu không có Interface:
- RoomManager có add(Room room)
- BookingManager có add(Booking booking)  
- CustomerManager có add(Customer customer)
→ 3 cách viết KHÁC NHAU, không thể tái sử dụng

Khi dùng Interface IManageable<T>:
- Định nghĩa 1 lần: add(T item), delete(String id), getAll()
- Tất cả Manager đều tuân theo CÙNG HỢP ĐỒNG
→ Có thể viết code chung, dễ test, dễ mở rộng
```

> "Interface không chỉ là bắt buộc theo yêu cầu đồ án, mà nó giúp em LOOSE COUPLING - các module không phụ thuộc lẫn nhau."

---

### PHẦN 6: GIẢI THÍCH SEQUENCE DIAGRAM (2 phút)

**Slide 9:**
> "Sequence Diagram cho thấy các OBJECT GIAO TIẾP với nhau như thế nào."

**Ví dụ: Đặt phòng**
```
User → BookingPanel (View)
     → BookingManager (Controller) 
     → RoomManager (hỏi phòng trống)
     → DataStorage (lưu)
     → Quay lại thông báo thành công
```

> "Em tách thành nhiều lớp như vậy để:
> 1. BookingManager KHÔNG CẦN BIẾT dữ liệu lưu ở đâu (JSON hay Database)
> 2. UI KHÔNG CẦN BIẾT logic kiểm tra phòng trống
> 3. Nếu sau này đổi từ JSON sang MySQL → chỉ sửa DataStorage"

---

### PHẦN 7: KIẾN TRÚC VÀ DESIGN PATTERN (1 phút)

**Slide 10:**
```
┌─────────────────┐
│   UI (View)     │ ← Chỉ lo hiển thị
├─────────────────┤
│ Manager (Logic) │ ← Xử lý nghiệp vụ
├─────────────────┤
│ Model (Entity)  │ ← Chứa dữ liệu
├─────────────────┤
│ Storage (Data)  │ ← Đọc/ghi file
└─────────────────┘
```

> "Mỗi tầng có TRÁCH NHIỆM RIÊNG. Nếu có bug về tính giá → em biết chắc phải sửa ở tầng Model/Manager, không phải tầng UI."

**Design Pattern:**
- **Factory**: Tạo phòng theo loại mà không cần if-else
- **Singleton**: RoomManager chỉ có 1 instance để tránh conflict dữ liệu

---

### PHẦN 8: TỔNG KẾT - EM ĐÃ HỌC ĐƯỢC GÌ (1 phút)

**Slide 11:**
> "Qua đồ án này, em hiểu rằng OOP không chỉ là 4 tính chất để học thuộc lòng, mà là CÁCH TƯ DUY khi thiết kế phần mềm:"

1. **Encapsulation**: Giấu chi tiết, chỉ cho phép truy cập qua getter/setter
   - *Ví dụ*: `customer.loyaltyPoints` là private, chỉ có thể thay đổi qua `addLoyaltyPoints()` để đảm bảo không bị âm

2. **Inheritance**: Tái sử dụng code, tránh lặp
   - *Ví dụ*: 3 loại Room đều có roomId, floor, status → định nghĩa 1 lần trong abstract Room

3. **Polymorphism**: Cùng method, khác hành vi
   - *Ví dụ*: `room.calculatePrice(3)` trả về giá khác nhau tùy loại phòng

4. **Abstraction**: Ẩn chi tiết, chỉ lộ interface
   - *Ví dụ*: IManageable định nghĩa "phải có add/delete/get" nhưng không quan tâm làm thế nào

---

## CÂU HỎI DỰ KIẾN TỪ THẦY & CÁCH TRẢ LỜI

| Câu hỏi | Cách trả lời |
|---------|--------------|
| "Tại sao em dùng JSON thay vì Database?" | "Vì đồ án tập trung vào OOP, JSON đủ để demo. Nhưng nhờ kiến trúc phân tầng, nếu cần đổi sang MySQL, em chỉ sửa tầng Storage mà không ảnh hưởng logic." |
| "Booking và Invoice quan hệ thế nào?" | "Composition: 1 Booking tạo ra 1 Invoice. Invoice CHỨA Booking để lấy thông tin tính tiền." |
| "Polymorphism thể hiện ở đâu?" | "Method calculatePrice() ở Room. Khi gọi booking.getRoom().calculatePrice(days), hệ thống tự động gọi đúng method của StandardRoom/VIPRoom/DeluxeRoom." |
| "Tại sao cần Interface?" | "Để các Manager tuân theo cùng hợp đồng, dễ test và mở rộng. Sau này thêm ServiceManager cũng chỉ cần implement IManageable." |

---

## LƯU Ý KHI THUYẾT TRÌNH

✅ **NÊN làm:**
- Luôn hỏi "TẠI SAO?" trước khi giải thích "LÀM GÌ?"
- Dùng ví dụ CỤ THỂ từ code thực tế
- So sánh "nếu không làm như vậy thì sao?"

❌ **KHÔNG nên:**
- Đọc code từng dòng
- Liệt kê tính năng mà không giải thích logic
- Nói "em làm theo mẫu" mà không hiểu tại sao

---

## PROMPT NGẮN CHO NOTEBOOKLM:

```
Tạo kịch bản thuyết trình 10 phút cho đồ án OOP Quản lý Khách sạn. Focus vào GIẢI THÍCH LOGIC, không liệt kê code.

Cấu trúc:
1. Từ nghiệp vụ khách sạn → xác định 3 Actor và nhu cầu của họ
2. Từ nhu cầu → định nghĩa Use Case (mục tiêu, không phải bước làm)
3. Từ Use Case → tìm danh từ → tạo Class (Customer, Room, Booking, Invoice)
4. Giải thích TẠI SAO dùng Inheritance cho Room (so sánh với if-else)
5. Giải thích TẠI SAO dùng Composition cho Booking-Customer (so sánh với Inheritance)
6. Giải thích TẠI SAO cần Interface IManageable (loose coupling)
7. Sequence Diagram: Các object giao tiếp thế nào khi đặt phòng?
8. Tổng kết: 4 tính chất OOP thể hiện ở đâu trong code?

Mỗi phần phải có:
- Câu hỏi mở đầu (vd: "Tại sao em chọn cách này?")
- Ví dụ code cụ thể
- So sánh "nếu không làm thế thì sao?"
```
