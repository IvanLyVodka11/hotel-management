# PROMPT CHO NOTEBOOKLM - TẠO SLIDE THUYẾT TRÌNH OOP

## Hướng dẫn sử dụng:
1. Mở NotebookLM (https://notebooklm.google.com)
2. Tạo notebook mới
3. Upload file `docs/BaoCao_HotelManagement.md` làm source
4. Paste prompt bên dưới vào chat

---

## PROMPT CHÍNH (Chi tiết có ảnh diagram):

```
Bạn là chuyên gia về Lập trình Hướng đối tượng (OOP). Hãy tạo slide thuyết trình CHI TIẾT cho đồ án "Hệ thống Quản lý Khách sạn" theo cấu trúc dưới đây. 

YÊU CẦU QUAN TRỌNG:
- Mỗi slide phải có TIÊU ĐỀ, NỘI DUNG CHÍNH, và GHI CHÚ THUYẾT TRÌNH
- PHẢI bao gồm các DIAGRAM/SƠ ĐỒ từ báo cáo (copy nguyên văn các mermaid diagram hoặc ASCII diagram)
- Các diagram sẽ được render thành hình ảnh khi trình chiếu
- Thời lượng: 12-15 phút

---

## CẤU TRÚC 18-20 SLIDES:

### PHẦN 1: GIỚI THIỆU (3 slides)

📌 SLIDE 1: Trang bìa
- Tiêu đề: "HỆ THỐNG QUẢN LÝ KHÁCH SẠN - Hotel Management System"
- Môn học: Lập trình Hướng đối tượng
- Thông tin nhóm, ngày trình bày

📌 SLIDE 2: Bối cảnh và Bài toán
- Bối cảnh: Ngành khách sạn cần quản lý phòng, đặt phòng, khách hàng
- Vấn đề: Quản lý thủ công gây sai sót, chậm trễ
- Giải pháp: Xây dựng phần mềm quản lý khách sạn
- INCLUDE diagram "Phạm vi hệ thống" (ASCII box diagram từ mục 1.3)

📌 SLIDE 3: Mục tiêu và Công nghệ
- Bảng mục tiêu: Chức năng, Kỹ thuật, Công nghệ
- Bảng công nghệ: Java 21, Swing + FlatLaf, Gson, Maven, JUnit 5

---

### PHẦN 2: PHÂN TÍCH YÊU CẦU (6 slides)

📌 SLIDE 4: Đặc tả Actor (Tác nhân) với Generalization
- Actor cha: "Nhân viên" (Employee) - chức năng chung: Đăng nhập
- Actor con (kế thừa): Lễ tân, Quản lý, Nhân viên Dịch vụ
- INCLUDE diagram "Phân cấp Actor" (ASCII diagram từ mục 2.1.2)
- Ghi chú: Giải thích Generalization - Actor con kế thừa tất cả quyền của Actor cha

📌 SLIDE 5: Use Case Tổng quan (Chuẩn UML) - DIAGRAM
- INCLUDE đầy đủ mermaid diagram "Sơ đồ Use Case tổng quan" (mục 2.2.1)
- Gồm 9 use case chính, Actor cha "Nhân viên" và 3 Actor con
- Giải thích ký pháp UML: Association (---), Generalization (▷)
- LƯU Ý: Các Use Case KHÔNG có mũi tên nối với nhau (không phải flowchart)

📌 SLIDE 6: Use Case theo từng Actor - DIAGRAM
- INCLUDE 3 mermaid diagram:
  + Use Case "Lễ tân" với quan hệ <<include>> (mục 2.2.2)
  + Use Case "Quản lý" (mục 2.2.3)
  + Use Case "Nhân viên Dịch vụ" (mục 2.2.4)
- Giải thích quan hệ <<include>>: mũi tên nét đứt, Use Case bắt buộc

📌 SLIDE 7: Đặc tả Use Case - "Đăng nhập" & "Đặt phòng"
- Bảng đặc tả "Đăng nhập" (Bảng 2.1): Tác nhân, Mô tả UC, Tiền điều kiện, Luồng sự kiện, Luồng sự kiện phụ
- Bảng đặc tả "Đặt phòng" (Bảng 2.3): đầy đủ thông tin
- Ghi chú: Giải thích tại sao chọn 2 use case này (authentication quan trọng, đặt phòng phức tạp nhất)

📌 SLIDE 8: Sequence Diagram - "Đăng nhập" - DIAGRAM
- INCLUDE đầy đủ mermaid sequenceDiagram "Đăng nhập" (mục 2.4.1)
- User → LoginFrame → AuthService → UserStorage → MainFrame
- Có alt/else cho thành công/thất bại
- Ghi chú: Giải thích từng bước, highlight luồng xác thực

📌 SLIDE 9: Sequence Diagram - "Đặt phòng" - DIAGRAM
- INCLUDE đầy đủ mermaid sequenceDiagram "Đặt phòng" (mục 2.4.2)
- Lễ tân → BookingPanel → Dialog → BookingManager → RoomManager → DataStorage
- Ghi chú: Highlight các tương tác quan trọng (kiểm tra phòng trống, tính giá)

---

### PHẦN 3: THIẾT KẾ HỆ THỐNG (6 slides)

📌 SLIDE 10: Kiến trúc MVC - DIAGRAM
- INCLUDE đầy đủ ASCII diagram "Kiến trúc 4 tầng" (mục 3.1)
- 4 layer: Presentation (View) → Business (Controller) → Model → Data (Storage)
- Mapping: UI (View), Manager (Controller), Entity (Model), Storage (Data)
- Ghi chú: Lợi ích của MVC (tách biệt concern, dễ bảo trì, test)

📌 SLIDE 11: Class Diagram - Interfaces - DIAGRAM
- INCLUDE mermaid classDiagram "Diagram các Interface" (mục 3.2.1)
- IManageable<T>, ISearchable<T>, IStorable
- Các Manager implement interfaces
- Ghi chú: Tại sao dùng Interface (loose coupling, dễ mở rộng, dependency injection)

📌 SLIDE 12: Class Diagram - Kế thừa Room - DIAGRAM
- INCLUDE mermaid classDiagram "Diagram lớp Room" (mục 3.2.2)
- Room (abstract) → StandardRoom, VIPRoom, DeluxeRoom
- Các phương thức: calculatePrice(), getRoomType(), getDescription()
- Ghi chú: Giải thích tại sao dùng abstract class (tính đa hình cho calculatePrice()), hệ số giá

📌 SLIDE 13: Class Diagram - Quan hệ Entity - DIAGRAM
- INCLUDE mermaid classDiagram "Diagram quan hệ Entity" (mục 3.2.3)
- Customer → Booking → Room, Booking → Invoice
- Composition relationship (has-a)
- Ghi chú: Giải thích tại sao dùng Composition thay vì Inheritance

📌 SLIDE 14: Class Diagram TỔNG THỂ - DIAGRAM
- INCLUDE đầy đủ mermaid classDiagram "Class Diagram TỔNG THỂ toàn dự án" (mục 3.2.4)
- Đây là diagram quan trọng nhất, hiển thị TOÀN BỘ hệ thống
- Gồm: Interfaces, Enums, Model, Factory, Service, Storage, Auth, UI layers
- Relationships: Inheritance, Implements, Composition, Dependencies
- Ghi chú: Tổng kết toàn bộ kiến trúc, giải thích bảng layer

📌 SLIDE 15: Design Patterns - DIAGRAM
- Factory Pattern: RoomFactory (tạo các loại Room khác nhau)
- Singleton Pattern: RoomManager, DataStorage (chỉ 1 instance)
- MVC Pattern: toàn bộ kiến trúc
- Ghi chú: Tại sao cần pattern (tái sử dụng, maintainability)

---

### PHẦN 4: CHI TIẾT TRIỂN KHAI (3 slides)

📌 SLIDE 16: Luồng dữ liệu và Storage - DIAGRAM
- INCLUDE ASCII diagram "Sơ đồ luồng dữ liệu" (mục 4.3.2)
- UI Layer → SERVICE LAYER → STORAGE LAYER → DATA FILES (JSON)
- Serialize/Deserialize với Gson
- Ghi chú: Giải thích cơ chế hoạt động, tại sao chọn JSON (portable, human-readable)

📌 SLIDE 17: Package Structure và Thống kê Code
- INCLUDE cấu trúc thư mục project (mục 4.1 và 5.1)
- Bảng thống kê: ~41 files, ~4500 LOC
- Phân chia: Model (11), Service (7), Storage (2), UI (17), Auth (2), Util (2)
- Công nghệ: Java 21, Swing, Gson, Maven

📌 SLIDE 18: Các nguyên lý OOP được áp dụng
- Bảng 4 tính chất OOP:
  ✓ Encapsulation: private fields, getter/setter
  ✓ Inheritance: Room → StandardRoom, VIPRoom, DeluxeRoom  
  ✓ Polymorphism: calculatePrice() khác nhau theo loại phòng
  ✓ Abstraction: Interface IManageable, abstract class Room
- Ví dụ code ngắn minh họa (nếu có thời gian)

---

### PHẦN 5: KẾT LUẬN (2 slides)

📌 SLIDE 19: Tổng kết và Demo
- Tóm tắt những gì đã làm được
- 4 chức năng chính: Quản lý Phòng, Đặt phòng, Khách hàng, Hóa đơn
- 4 tính chất OOP đã áp dụng
- 3 Design Pattern: Factory, Singleton, MVC
- Demo ứng dụng (nếu có thời gian)

📌 SLIDE 20: Q&A - Hỏi đáp
- Thông tin liên hệ nhóm
- Cảm ơn đã lắng nghe

---

## YÊU CẦU FORMAT SLIDE:
1. Mỗi slide có TỐI ĐA 5-6 bullet points (trừ slide diagram)
2. Các DIAGRAM phải được copy NGUYÊN VĂN từ báo cáo (mermaid hoặc ASCII)
3. Ghi chú thuyết trình ở cuối mỗi slide (Speaker Notes)
4. Ngôn ngữ: Tiếng Việt, thuật ngữ kỹ thuật giữ nguyên tiếng Anh
5. Thời lượng: 12-15 phút

## LƯU Ý QUAN TRỌNG:
- Tập trung vào NGHIỆP VỤ và TẠI SAO thiết kế như vậy
- Không liệt kê code dài, chỉ mô tả ý tưởng
- Các diagram là HÌNH ẢNH CHÍNH trong slide, không phải text phụ
- Use case phức tạp nhất: Đặt phòng (kiểm tra availability, tính giá, tạo invoice)
- Class diagram tổng thể (Slide 14) là slide quan trọng nhất về thiết kế
```

---

## PROMPT NGẮN GỌN (nếu giới hạn ký tự):

```
Tạo 18-20 slide thuyết trình OOP cho đồ án Quản lý Khách sạn. BẮT BUỘC include các diagram từ báo cáo:

PHẦN 1 - GIỚI THIỆU (3 slides):
- Slide 1: Bìa
- Slide 2: Bối cảnh + diagram "Phạm vi hệ thống" (ASCII box)
- Slide 3: Mục tiêu, công nghệ

PHẦN 2 - PHÂN TÍCH (5 slides):
- Slide 4: Đặc tả 3 Actor
- Slide 5: Use Case tổng quan (mermaid flowchart mục 2.2.1)
- Slide 6: Use Case từng actor (3 mermaid diagram mục 2.2.2-2.2.4)
- Slide 7: Đặc tả Use Case "Đăng nhập" + "Đặt phòng" (bảng)
- Slide 8: Sequence Diagram "Đăng nhập" (mermaid mục 2.4.1)
- Slide 9: Sequence Diagram "Đặt phòng" (mermaid mục 2.4.2)

PHẦN 3 - THIẾT KẾ (6 slides):
- Slide 10: Kiến trúc MVC (ASCII diagram mục 3.1)
- Slide 11: Class Diagram Interfaces (mermaid mục 3.2.1)
- Slide 12: Class Diagram Room Inheritance (mermaid mục 3.2.2)
- Slide 13: Class Diagram Entity Relationships (mermaid mục 3.2.3)
- Slide 14: CLASS DIAGRAM TỔNG THỂ (mermaid mục 3.2.4) - QUAN TRỌNG NHẤT
- Slide 15: Design Patterns (Factory, Singleton, MVC)

PHẦN 4 - TRIỂN KHAI (3 slides):
- Slide 16: Luồng dữ liệu + Storage (ASCII diagram mục 4.3.2)
- Slide 17: Package structure + thống kê (41 files, 4500 LOC)
- Slide 18: 4 nguyên lý OOP được áp dụng

PHẦN 5 - KẾT LUẬN (2 slides):
- Slide 19: Tổng kết + Demo
- Slide 20: Q&A

YÊU CẦU: Mỗi slide max 6 bullets, có speaker notes, tất cả diagram là hình ảnh chính.
```

---

## DANH SÁCH CÁC DIAGRAM CẦN INCLUDE:

| Slide | Diagram | Vị trí trong báo cáo | Loại |
|-------|---------|---------------------|------|
| 2 | Phạm vi hệ thống | Mục 1.3 | ASCII box |
| 5 | Use Case tổng quan | Mục 2.2.1 | Mermaid flowchart |
| 6 | Use Case Lễ tân | Mục 2.2.2 | Mermaid flowchart |
| 6 | Use Case Quản lý | Mục 2.2.3 | Mermaid flowchart |
| 6 | Use Case Bộ phận dịch vụ | Mục 2.2.4 | Mermaid flowchart |
| 8 | Sequence Diagram Đăng nhập | Mục 2.4.1 | Mermaid sequence |
| 9 | Sequence Diagram Đặt phòng | Mục 2.4.2 | Mermaid sequence |
| 10 | Kiến trúc MVC 4 tầng | Mục 3.1 | ASCII diagram |
| 11 | Class Diagram Interfaces | Mục 3.2.1 | Mermaid classDiagram |
| 12 | Class Diagram Room | Mục 3.2.2 | Mermaid classDiagram |
| 13 | Class Diagram Entity | Mục 3.2.3 | Mermaid classDiagram |
| **14** | **Class Diagram TỔNG THỂ** | **Mục 3.2.4** | **Mermaid classDiagram** |
| 16 | Sơ đồ luồng dữ liệu | Mục 4.3.2 | ASCII diagram |

**Tổng: 13 diagrams/hình ảnh**
