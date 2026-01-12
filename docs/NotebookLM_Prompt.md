# PROMPT CHO NOTEBOOKLM - TẠO SLIDE THUYẾT TRÌNH OOP

## Hướng dẫn:
1. Upload file `docs/BaoCao_HotelManagement.md` vào NotebookLM
2. **Upload các file PNG diagram** (danh sách bên dưới)
3. Paste prompt bên dưới

---

## DANH SÁCH FILE PNG CẦN UPLOAD:

| File | Nội dung | Dùng cho Slide |
|------|----------|----------------|
| `PhamViHeThong.drawio.png` | Phạm vi Hệ thống - 6 module | Slide 2 |
| `TongQuan.drawio.png` | Use Case Tổng quan | Slide 5 |
| `LeTan.drawio.png` | Use Case Lễ tân với <<include>> | Slide 6 |
| `QuanLy.drawio.png` | Use Case Quản lý | Slide 6 |
| `DangNhap.drawio.png` | Sequence Đăng nhập | Slide 8 |
| `DatPhong.drawio.png` | Sequence Đặt phòng | Slide 9 |
| `Check-outvaTaoHoaDon.drawio.png` | Sequence Check-out | Phụ lục |
| `Interfaces.drawio.png` | Class Diagram Interfaces | Slide 11 |
| `RoomClass.drawio.png` | Class Diagram Room (Inheritance) | Slide 12 |
| `Entity.drawio.png` | Class Diagram Entity (Composition) | Slide 13 |
| `ClassTongQuan.drawio.png` | Class Diagram Tổng thể | Slide 14 |

---

## PROMPT:

```
Bạn là chuyên gia OOP. Tạo slide thuyết trình cho đồ án "Hệ thống Quản lý Khách sạn".

LƯU Ý QUAN TRỌNG VỀ DIAGRAM:
- Sử dụng TRỰC TIẾP các file PNG đã upload làm hình minh họa
- Các diagram đã vẽ theo CHUẨN UML
- **VIỆT HÓA**: Tên Actor, Use Case, Class đã bằng TIẾNG VIỆT
- **3 ACTOR NGHIỆP VỤ**: Manager, Staff, Service (không có Admin trong sơ đồ)

---
## SLIDE 1-3: GIỚI THIỆU
- Slide 1: Bìa (Tên đề tài, Môn: Lập trình Hướng đối tượng, Nhóm 204: Trần Đình Khánh 20237449, Nguyễn Hữu Linh 20237455)
- Slide 2: Bối cảnh + Sơ đồ Phạm vi (dùng PhamViHeThong.drawio.png)
- Slide 3: Mục tiêu dự án

---
## SLIDE 4-9: PHÂN TÍCH YÊU CẦU  
- Slide 4: Đặc tả Actor - 3 Role nghiệp vụ độc lập (Manager, Staff, Service)
- Slide 5: Use Case Tổng quan (dùng TongQuan.drawio.png)
- Slide 6: Use Case Lễ tân + Quản lý (dùng LeTan.drawio.png + QuanLy.drawio.png)
- Slide 7: Đặc tả Use Case "Đặt phòng" - Bảng 2.3
- Slide 8: Sequence Diagram "Đăng nhập" (dùng DangNhap.drawio.png)
- Slide 9: Sequence Diagram "Đặt phòng" (dùng DatPhong.drawio.png)

---
## SLIDE 10-15: THIẾT KẾ HỆ THỐNG
- Slide 10: Kiến trúc MVC 4 tầng (mục 3.1)
- Slide 11: Class Diagram Interfaces (dùng Interfaces.drawio.png) - TẠI SAO dùng interface
- Slide 12: Class Diagram Room (dùng RoomClass.drawio.png) - TẠI SAO kế thừa
- Slide 13: Class Diagram Entity (dùng Entity.drawio.png) - giải thích Composition
- Slide 14: CLASS DIAGRAM TỔNG THỂ (dùng ClassTongQuan.drawio.png)
- Slide 15: Design Patterns (Factory, Singleton, MVC)

---
## SLIDE 16-18: TRIỂN KHAI
- Slide 16: Luồng dữ liệu UI → Manager → Storage → JSON
- Slide 17: Tổ chức mã nguồn (~41 files, ~4500 LOC) + Công nghệ (Java 21, Swing + FlatLaf, Gson, Maven)
- Slide 18: 4 nguyên lý OOP với ví dụ cụ thể

---
## SLIDE 19-20: KẾT LUẬN
- Slide 19: Tổng kết + Demo
- Slide 20: Q&A

---
YÊU CẦU:
- Mỗi slide max 5-6 bullets
- CHÈN TRỰC TIẾP hình PNG vào slide tương ứng
- Tập trung giải thích TẠI SAO
- Có Speaker Notes
```
