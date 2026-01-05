# PROMPT CHO NOTEBOOKLM - TẠO SLIDE THUYẾT TRÌNH OOP

## Hướng dẫn sử dụng:
1. Mở NotebookLM (https://notebooklm.google.com)
2. Tạo notebook mới
3. Upload file `docs/BaoCao_HotelManagement.md` làm source
4. Paste prompt bên dưới vào chat

---

## PROMPT:

```
Bạn là chuyên gia về Lập trình Hướng đối tượng (OOP). Hãy tạo slide thuyết trình cho đồ án "Hệ thống Quản lý Khách sạn" theo đúng cấu trúc và nội dung sau. Mỗi slide cần có tiêu đề, nội dung chính và ghi chú cho người thuyết trình.

## CẤU TRÚC SLIDE (12-15 slides):

### SLIDE 1: Trang bìa
- Tiêu đề: "HỆ THỐNG QUẢN LÝ KHÁCH SẠN - Hotel Management System"
- Môn học: Lập trình Hướng đối tượng
- Thông tin nhóm

### SLIDE 2: Giới thiệu bối cảnh & bài toán
- Bối cảnh: Ngành khách sạn cần quản lý phòng, đặt phòng, khách hàng
- Bài toán: Xây dựng hệ thống phần mềm quản lý với đầy đủ chức năng CRUD
- Mục tiêu kỹ thuật: Áp dụng 4 tính chất OOP

### SLIDE 3: Đặc tả Actor (Tác nhân)
- 3 actor chính: Lễ tân, Quản lý, Bộ phận dịch vụ
- Mô tả vai trò và chức năng của từng actor
- Sử dụng hình vẽ stick figure như trong báo cáo

### SLIDE 4: Use Case tổng quan hệ thống
- Sơ đồ Use Case tổng quan (copy từ báo cáo)
- Liệt kê 9 use case chính
- Mũi tên kết nối actor với use case

### SLIDE 5: Đặc tả Use Case quan trọng - "Đăng nhập"
- Bảng đặc tả: Tác nhân, Mô tả UC, Tiền điều kiện, Luồng sự kiện, Luồng sự kiện phụ
- Giải thích tại sao use case này quan trọng (authentication, phân quyền)

### SLIDE 6: Đặc tả Use Case quan trọng - "Đặt phòng"
- Bảng đặc tả đầy đủ
- Nghiệp vụ phức tạp: kiểm tra phòng trống, tính giá, tạo booking

### SLIDE 7: Biểu đồ tuần tự (Sequence Diagram) - "Đăng nhập"
- Copy sequence diagram từ báo cáo
- Giải thích từng bước: User → LoginFrame → AuthService → UserStorage → MainFrame

### SLIDE 8: Biểu đồ tuần tự - "Đặt phòng"
- Sequence diagram cho flow đặt phòng
- Highlight các tương tác giữa BookingPanel, BookingManager, RoomManager, DataStorage

### SLIDE 9: Phân tích và chia lớp đối tượng
- Tại sao chia thành các class: Room, Customer, Booking, Invoice?
- Giải thích theo nghiệp vụ: mỗi entity tương ứng một khái niệm trong thế giới thực
- Nguyên lý Single Responsibility

### SLIDE 10: Class Diagram - Kế thừa Room
- Diagram: Room (abstract) → StandardRoom, VIPRoom, DeluxeRoom
- Giải thích tại sao dùng abstract class: tính đa hình cho calculatePrice()
- Hệ số giá: Standard (x1.0), VIP (x1.2), Deluxe (x1.5)

### SLIDE 11: Class Diagram - Quan hệ Entity
- Diagram: Customer → Booking → Room, Booking → Invoice
- Giải thích Composition: Booking chứa Customer và Room
- Tại sao dùng Composition thay vì Inheritance

### SLIDE 12: Các Interface và Design Pattern
- Interface: IManageable<T>, ISearchable<T>, IStorable
- Design Pattern: Factory (RoomFactory), Singleton (Manager), MVC
- Tại sao cần Interface: loose coupling, dễ mở rộng

### SLIDE 13: Kiến trúc MVC
- Diagram 4 tầng: Presentation → Business → Model → Data
- Mapping: UI (View), Manager (Controller), Entity (Model), Storage (Data)
- Lợi ích: tách biệt concern, dễ bảo trì

### SLIDE 14: Tổ chức mã nguồn
- Package structure: model/, service/, storage/, ui/, auth/, util/
- Thống kê: ~41 files, ~4500 LOC
- Công nghệ: Java 21, Swing, Gson, Maven

### SLIDE 15: Tổng kết
- 4 tính chất OOP đã áp dụng:
  ✓ Encapsulation: private fields, getter/setter
  ✓ Inheritance: Room → StandardRoom, VIPRoom, DeluxeRoom
  ✓ Polymorphism: calculatePrice() khác nhau theo loại phòng
  ✓ Abstraction: Interface IManageable, abstract class Room
- Design Pattern: Factory, Singleton, MVC
- Demo (nếu có thời gian)

---

## YÊU CẦU FORMAT:
- Mỗi slide có TỐI ĐA 5-6 bullet points
- Sử dụng hình ảnh/diagram khi có thể
- Ghi chú cho người thuyết trình ở cuối mỗi slide
- Ngôn ngữ: Tiếng Việt, thuật ngữ kỹ thuật giữ nguyên tiếng Anh
- Thời lượng thuyết trình: 10-15 phút

## LƯU Ý QUAN TRỌNG:
- Tập trung vào NGHIỆP VỤ và TẠI SAO thiết kế như vậy
- Không liệt kê code, chỉ mô tả ý tưởng
- Use case nào phức tạp nhất: Đặt phòng (vì có kiểm tra availability, tính giá, tạo invoice)
- Giải thích rõ mối liên hệ giữa các class
```

---

## PROMPT NGẮN GỌN (nếu NotebookLM giới hạn ký tự):

```
Tạo 15 slide thuyết trình OOP cho đồ án Quản lý Khách sạn theo flow:
1. Bối cảnh & bài toán
2. Đặc tả 3 Actor (Lễ tân, Quản lý, Bộ phận dịch vụ)
3. Use Case tổng quan
4. Đặc tả Use Case "Đăng nhập" và "Đặt phòng" (bảng chi tiết)
5. Sequence Diagram 2 use case trên
6. Giải thích tại sao chia class: Room, Customer, Booking, Invoice
7. Class Diagram: Room inheritance, Entity relationships
8. Interface & Design Pattern (Factory, Singleton, MVC)
9. Kiến trúc 4 tầng
10. Tổ chức mã nguồn & tổng kết OOP

Mỗi slide max 5 bullets, có ghi chú thuyết trình. Focus vào WHY, không liệt kê code.
```
