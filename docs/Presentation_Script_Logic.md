# KỊCH BẢN THUYẾT TRÌNH OOP - HỆ THỐNG QUẢN LÝ KHÁCH SẠN

**Mục đích**: Thể hiện SỰ HIỂU BIẾT về OOP - không chỉ làm được mà còn HIỂU TẠI SAO

## SLIDE 1-3: GIỚI THIỆU
- **Slide 1**: Bìa
- **Slide 2**: Bối cảnh → 3 actor (Lễ tân, Quản lý, NV Dịch vụ) với nhu cầu KHÁC NHAU → Cần PHÂN QUYỀN
- **Slide 3**: Mục tiêu: CRUD + 4 OOP + Design Patterns

---

## SLIDE 4-9: PHÂN TÍCH
- **Slide 4**: Actor Generalization → Nhân viên là cha, 3 con kế thừa
- **Slide 5-6**: Use Case = MỤC TIÊU, không phải bước làm. "Đặt phòng" phức tạp nhất vì có check availability + tính giá
- **Slide 7**: Đặc tả Use Case "Đặt phòng" (Bảng)
- **Slide 8-9**: Sequence Diagram → Các object giao tiếp thế nào

**Key point**: Từ Use Case → tìm DANH TỪ → ra CLASS (Customer, Room, Booking, Invoice)

---

## SLIDE 10-15: THIẾT KẾ ⭐

**Slide 10: Kiến trúc MVC**
```
UI → Manager → Model → Storage
```
→ Mỗi tầng có trách nhiệm riêng, dễ debug

**Slide 11: Interface** - TẠI SAO?
→ Các Manager đều có add/delete/get → Dùng `IManageable<T>` để LOOSE COUPLING

**Slide 12: Abstract Room** - TẠI SAO kế thừa?
→ Thay vì if-else kiểm tra roomType, mỗi class tự override `calculatePrice()` → POLYMORPHISM, dễ mở rộng

**Slide 13: Composition** - TẠI SAO không kế thừa?
→ Booking LÀ Phòng? ❌ Không. Booking CÓ Phòng? ✅ Có → HAS-A, dùng Composition

**Slide 14: Class Diagram tổng thể** (QUAN TRỌNG NHẤT)

**Slide 15: Design Patterns**: Factory, Singleton, MVC

---

## SLIDE 16-18: TRIỂN KHAI

**Slide 16: Storage Layer**
```
UI → Manager → Storage → JSON files
```
- Serialize/Deserialize với Gson
- Tại sao JSON? → Human-readable, portable. Đổi sang MySQL? → Chỉ sửa Storage layer

**Slide 17: Tổ chức code**
- ~41 files, ~4500 LOC
- Packages: auth, model, service, storage, ui, util
- Công nghệ: Java 21, Swing + FlatLaf, Gson, Maven

**Slide 18: 4 OOP**
| Tính chất | Ví dụ |
|-----------|-------|
| Encapsulation | private fields, getter/setter |
| Inheritance | Room → Standard/VIP/Deluxe |
| Polymorphism | calculatePrice() khác nhau |
| Abstraction | IManageable, abstract Room |

---

## SLIDE 19-20: KẾT LUẬN
- **Slide 19**: 4 chức năng + 4 OOP + 3 Pattern + Demo
- **Slide 20**: Q&A

---

## CÂU HỎI DỰ KIẾN

| Câu hỏi | Trả lời |
|---------|---------|
| Tại sao JSON? | Đủ cho demo, đổi DB chỉ sửa Storage |
| Polymorphism ở đâu? | calculatePrice() của Room |
| Tại sao Interface? | Loose coupling, dễ mở rộng |
| Booking-Invoice quan hệ gì? | Composition (HAS-A) |
