# CODE DRAW.IO CHO CÁC DIAGRAM

Dưới đây là code XML để import vào draw.io. Có thể mở draw.io → File → Import from → Device, sau đó chọn file .drawio hoặc copy paste XML.

---

## 1. SƠ ĐỒ PHẠM VI HỆ THỐNG

Lưu file này với tên: `PhamViHeThong.drawio`

```xml
<mxfile host="app.diagrams.net">
  <diagram name="Phạm vi Hệ thống" id="phamvi">
    <mxGraphModel dx="1000" dy="600" grid="1" gridSize="10" guides="1" tooltips="1" connect="1" arrows="1" fold="1" page="1" pageScale="1" pageWidth="850" pageHeight="500">
      <root>
        <mxCell id="0" />
        <mxCell id="1" parent="0" />
        <!-- System Boundary -->
        <mxCell id="system" value="Hệ thống Quản lý Khách sạn" style="swimlane;whiteSpace=wrap;html=1;startSize=30;fillColor=#dae8fc;strokeColor=#6c8ebf;fontSize=16;fontStyle=1;" vertex="1" parent="1">
          <mxGeometry x="50" y="30" width="750" height="280" as="geometry" />
        </mxCell>
        <!-- Row 1 -->
        <mxCell id="m1" value="&lt;b&gt;Quản lý Phòng&lt;/b&gt;&lt;br&gt;&lt;font size=&quot;1&quot;&gt;Room Management&lt;/font&gt;" style="rounded=1;whiteSpace=wrap;html=1;fillColor=#e3f2fd;strokeColor=#1976d2;strokeWidth=2;" vertex="1" parent="system">
          <mxGeometry x="30" y="60" width="160" height="70" as="geometry" />
        </mxCell>
        <mxCell id="m2" value="&lt;b&gt;Quản lý Đặt phòng&lt;/b&gt;&lt;br&gt;&lt;font size=&quot;1&quot;&gt;Booking Management&lt;/font&gt;" style="rounded=1;whiteSpace=wrap;html=1;fillColor=#e8f5e9;strokeColor=#388e3c;strokeWidth=2;" vertex="1" parent="system">
          <mxGeometry x="220" y="60" width="160" height="70" as="geometry" />
        </mxCell>
        <mxCell id="m3" value="&lt;b&gt;Quản lý Khách hàng&lt;/b&gt;&lt;br&gt;&lt;font size=&quot;1&quot;&gt;Customer Management&lt;/font&gt;" style="rounded=1;whiteSpace=wrap;html=1;fillColor=#fff3e0;strokeColor=#f57c00;strokeWidth=2;" vertex="1" parent="system">
          <mxGeometry x="410" y="60" width="160" height="70" as="geometry" />
        </mxCell>
        <!-- Row 2 -->
        <mxCell id="m4" value="&lt;b&gt;Quản lý Hóa đơn&lt;/b&gt;&lt;br&gt;&lt;font size=&quot;1&quot;&gt;Invoice Management&lt;/font&gt;" style="rounded=1;whiteSpace=wrap;html=1;fillColor=#fce4ec;strokeColor=#c2185b;strokeWidth=2;" vertex="1" parent="system">
          <mxGeometry x="30" y="160" width="160" height="70" as="geometry" />
        </mxCell>
        <mxCell id="m5" value="&lt;b&gt;Đăng nhập/Phân quyền&lt;/b&gt;&lt;br&gt;&lt;font size=&quot;1&quot;&gt;Auth &amp; Authorization&lt;/font&gt;" style="rounded=1;whiteSpace=wrap;html=1;fillColor=#e1f5fe;strokeColor=#0288d1;strokeWidth=2;" vertex="1" parent="system">
          <mxGeometry x="220" y="160" width="160" height="70" as="geometry" />
        </mxCell>
        <mxCell id="m6" value="&lt;b&gt;Báo cáo Thống kê&lt;/b&gt;&lt;br&gt;&lt;font size=&quot;1&quot;&gt;Reports &amp; Statistics&lt;/font&gt;" style="rounded=1;whiteSpace=wrap;html=1;fillColor=#f3e5f5;strokeColor=#7b1fa2;strokeWidth=2;" vertex="1" parent="system">
          <mxGeometry x="410" y="160" width="160" height="70" as="geometry" />
        </mxCell>
      </root>
    </mxGraphModel>
  </diagram>
</mxfile>
```

---

## 2. USE CASE TỔNG QUAN

Lưu file này với tên: `UseCaseTongQuan.drawio`

```xml
<mxfile host="app.diagrams.net">
  <diagram name="Use Case Tổng quan" id="uctq">
    <mxGraphModel dx="1200" dy="800" grid="1" gridSize="10" guides="1" tooltips="1" connect="1" arrows="1" fold="1" page="1" pageScale="1" pageWidth="1100" pageHeight="800">
      <root>
        <mxCell id="0" />
        <mxCell id="1" parent="0" />
        
        <!-- System Boundary -->
        <mxCell id="sys" value="Hệ thống Quản lý Khách sạn" style="swimlane;whiteSpace=wrap;html=1;startSize=30;fillColor=none;strokeColor=#2c3e50;strokeWidth=2;fontSize=14;fontStyle=1;" vertex="1" parent="1">
          <mxGeometry x="300" y="20" width="500" height="700" as="geometry" />
        </mxCell>
        
        <!-- Use Cases -->
        <mxCell id="uc1" value="Đăng nhập" style="ellipse;whiteSpace=wrap;html=1;fillColor=#e8eef5;strokeColor=#5a7a9a;strokeWidth=1.5;" vertex="1" parent="sys">
          <mxGeometry x="200" y="50" width="120" height="50" as="geometry" />
        </mxCell>
        <mxCell id="uc2" value="Quản lý Phòng" style="ellipse;whiteSpace=wrap;html=1;fillColor=#e8eef5;strokeColor=#5a7a9a;strokeWidth=1.5;" vertex="1" parent="sys">
          <mxGeometry x="100" y="130" width="130" height="50" as="geometry" />
        </mxCell>
        <mxCell id="uc3" value="Xem Báo cáo" style="ellipse;whiteSpace=wrap;html=1;fillColor=#e8eef5;strokeColor=#5a7a9a;strokeWidth=1.5;" vertex="1" parent="sys">
          <mxGeometry x="280" y="130" width="130" height="50" as="geometry" />
        </mxCell>
        <mxCell id="uc4" value="Đặt phòng" style="ellipse;whiteSpace=wrap;html=1;fillColor=#e8eef5;strokeColor=#5a7a9a;strokeWidth=1.5;" vertex="1" parent="sys">
          <mxGeometry x="200" y="220" width="120" height="50" as="geometry" />
        </mxCell>
        <mxCell id="uc5" value="Check-in" style="ellipse;whiteSpace=wrap;html=1;fillColor=#e8eef5;strokeColor=#5a7a9a;strokeWidth=1.5;" vertex="1" parent="sys">
          <mxGeometry x="200" y="300" width="120" height="50" as="geometry" />
        </mxCell>
        <mxCell id="uc6" value="Check-out" style="ellipse;whiteSpace=wrap;html=1;fillColor=#e8eef5;strokeColor=#5a7a9a;strokeWidth=1.5;" vertex="1" parent="sys">
          <mxGeometry x="200" y="380" width="120" height="50" as="geometry" />
        </mxCell>
        <mxCell id="uc7" value="Quản lý Khách hàng" style="ellipse;whiteSpace=wrap;html=1;fillColor=#e8eef5;strokeColor=#5a7a9a;strokeWidth=1.5;" vertex="1" parent="sys">
          <mxGeometry x="190" y="460" width="140" height="50" as="geometry" />
        </mxCell>
        <mxCell id="uc8" value="Quản lý Hóa đơn" style="ellipse;whiteSpace=wrap;html=1;fillColor=#e8eef5;strokeColor=#5a7a9a;strokeWidth=1.5;" vertex="1" parent="sys">
          <mxGeometry x="195" y="540" width="130" height="50" as="geometry" />
        </mxCell>
        <mxCell id="uc9" value="Cung cấp Dịch vụ" style="ellipse;whiteSpace=wrap;html=1;fillColor=#e8eef5;strokeColor=#5a7a9a;strokeWidth=1.5;" vertex="1" parent="sys">
          <mxGeometry x="100" y="620" width="130" height="50" as="geometry" />
        </mxCell>
        <mxCell id="uc10" value="Hỗ trợ Khách hàng" style="ellipse;whiteSpace=wrap;html=1;fillColor=#e8eef5;strokeColor=#5a7a9a;strokeWidth=1.5;" vertex="1" parent="sys">
          <mxGeometry x="280" y="620" width="130" height="50" as="geometry" />
        </mxCell>
        
        <!-- Actors - 4 Role độc lập theo PermissionManager.Role -->
        <mxCell id="a1" value="Quản trị viên&lt;br&gt;(Admin)" style="shape=umlActor;verticalLabelPosition=bottom;verticalAlign=top;html=1;outlineConnect=0;" vertex="1" parent="1">
          <mxGeometry x="80" y="40" width="40" height="80" as="geometry" />
        </mxCell>
        <mxCell id="a2" value="Quản lý&lt;br&gt;(Manager)" style="shape=umlActor;verticalLabelPosition=bottom;verticalAlign=top;html=1;outlineConnect=0;" vertex="1" parent="1">
          <mxGeometry x="80" y="180" width="40" height="80" as="geometry" />
        </mxCell>
        <mxCell id="a3" value="Lễ tân&lt;br&gt;(Staff)" style="shape=umlActor;verticalLabelPosition=bottom;verticalAlign=top;html=1;outlineConnect=0;" vertex="1" parent="1">
          <mxGeometry x="80" y="320" width="40" height="80" as="geometry" />
        </mxCell>
        <mxCell id="a4" value="Bộ phận Dịch vụ&lt;br&gt;(Service)" style="shape=umlActor;verticalLabelPosition=bottom;verticalAlign=top;html=1;outlineConnect=0;" vertex="1" parent="1">
          <mxGeometry x="80" y="460" width="40" height="80" as="geometry" />
        </mxCell>
        
        <!-- Associations - Mỗi Actor nối trực tiếp với Đăng nhập -->
        <mxCell id="assoc1" style="endArrow=none;html=1;strokeWidth=1.5;" edge="1" parent="1" source="a1" target="uc1">
          <mxGeometry relative="1" as="geometry" />
        </mxCell>
        <mxCell id="assoc1b" style="endArrow=none;html=1;strokeWidth=1.5;" edge="1" parent="1" source="a2" target="uc1">
          <mxGeometry relative="1" as="geometry" />
        </mxCell>
        <mxCell id="assoc1c" style="endArrow=none;html=1;strokeWidth=1.5;" edge="1" parent="1" source="a3" target="uc1">
          <mxGeometry relative="1" as="geometry" />
        </mxCell>
        <mxCell id="assoc1d" style="endArrow=none;html=1;strokeWidth=1.5;" edge="1" parent="1" source="a4" target="uc1">
          <mxGeometry relative="1" as="geometry" />
        </mxCell>
        <!-- Admin - Quản lý Phòng + Báo cáo -->
        <mxCell id="assoc2" style="endArrow=none;html=1;strokeWidth=1.5;" edge="1" parent="1" source="a1" target="uc2">
          <mxGeometry relative="1" as="geometry" />
        </mxCell>
        <mxCell id="assoc3" style="endArrow=none;html=1;strokeWidth=1.5;" edge="1" parent="1" source="a1" target="uc3">
          <mxGeometry relative="1" as="geometry" />
        </mxCell>
        <!-- Manager - Quản lý Phòng + Báo cáo -->
        <mxCell id="assoc4" style="endArrow=none;html=1;strokeWidth=1.5;" edge="1" parent="1" source="a2" target="uc2">
          <mxGeometry relative="1" as="geometry" />
        </mxCell>
        <mxCell id="assoc5" style="endArrow=none;html=1;strokeWidth=1.5;" edge="1" parent="1" source="a2" target="uc3">
          <mxGeometry relative="1" as="geometry" />
        </mxCell>
        <!-- Staff (Lễ tân) - Nghiệp vụ đặt phòng -->
        <mxCell id="assoc6" style="endArrow=none;html=1;strokeWidth=1.5;" edge="1" parent="1" source="a3" target="uc4">
          <mxGeometry relative="1" as="geometry" />
        </mxCell>
        <mxCell id="assoc7" style="endArrow=none;html=1;strokeWidth=1.5;" edge="1" parent="1" source="a3" target="uc5">
          <mxGeometry relative="1" as="geometry" />
        </mxCell>
        <mxCell id="assoc8" style="endArrow=none;html=1;strokeWidth=1.5;" edge="1" parent="1" source="a3" target="uc6">
          <mxGeometry relative="1" as="geometry" />
        </mxCell>
        <mxCell id="assoc9" style="endArrow=none;html=1;strokeWidth=1.5;" edge="1" parent="1" source="a3" target="uc7">
          <mxGeometry relative="1" as="geometry" />
        </mxCell>
        <mxCell id="assoc10" style="endArrow=none;html=1;strokeWidth=1.5;" edge="1" parent="1" source="a3" target="uc8">
          <mxGeometry relative="1" as="geometry" />
        </mxCell>
        <!-- Service - Dịch vụ -->
        <mxCell id="assoc11" style="endArrow=none;html=1;strokeWidth=1.5;" edge="1" parent="1" source="a4" target="uc9">
          <mxGeometry relative="1" as="geometry" />
        </mxCell>
        <mxCell id="assoc12" style="endArrow=none;html=1;strokeWidth=1.5;" edge="1" parent="1" source="a4" target="uc10">
          <mxGeometry relative="1" as="geometry" />
        </mxCell>
      </root>
    </mxGraphModel>
  </diagram>
</mxfile>
```

---

## 3. USE CASE LỄ TÂN (với <<include>>)

Lưu file này với tên: `UseCaseLeTan.drawio`

```xml
<mxfile host="app.diagrams.net">
  <diagram name="Use Case Lễ tân" id="uclt">
    <mxGraphModel dx="900" dy="600" grid="1" gridSize="10" guides="1" tooltips="1" connect="1" arrows="1" fold="1" page="1" pageScale="1" pageWidth="900" pageHeight="600">
      <root>
        <mxCell id="0" />
        <mxCell id="1" parent="0" />
        
        <!-- System Boundary -->
        <mxCell id="sys" value="Hệ thống Quản lý Khách sạn" style="swimlane;whiteSpace=wrap;html=1;startSize=30;fillColor=none;strokeColor=#2c3e50;strokeWidth=2;fontSize=14;fontStyle=1;" vertex="1" parent="1">
          <mxGeometry x="240" y="20" width="400" height="520" as="geometry" />
        </mxCell>
        
        <!-- Use Cases -->
        <mxCell id="uc1" value="Đăng nhập" style="ellipse;whiteSpace=wrap;html=1;fillColor=#e8eef5;strokeColor=#5a7a9a;strokeWidth=1.5;" vertex="1" parent="sys">
          <mxGeometry x="140" y="40" width="120" height="45" as="geometry" />
        </mxCell>
        <mxCell id="uc2" value="Tìm phòng trống" style="ellipse;whiteSpace=wrap;html=1;fillColor=#e8eef5;strokeColor=#5a7a9a;strokeWidth=1.5;" vertex="1" parent="sys">
          <mxGeometry x="140" y="110" width="120" height="45" as="geometry" />
        </mxCell>
        <mxCell id="uc3" value="Đặt phòng mới" style="ellipse;whiteSpace=wrap;html=1;fillColor=#e8eef5;strokeColor=#5a7a9a;strokeWidth=1.5;" vertex="1" parent="sys">
          <mxGeometry x="140" y="180" width="120" height="45" as="geometry" />
        </mxCell>
        <mxCell id="uc4" value="Check-in" style="ellipse;whiteSpace=wrap;html=1;fillColor=#e8eef5;strokeColor=#5a7a9a;strokeWidth=1.5;" vertex="1" parent="sys">
          <mxGeometry x="140" y="250" width="120" height="45" as="geometry" />
        </mxCell>
        <mxCell id="uc5" value="Check-out" style="ellipse;whiteSpace=wrap;html=1;fillColor=#e8eef5;strokeColor=#5a7a9a;strokeWidth=1.5;" vertex="1" parent="sys">
          <mxGeometry x="140" y="320" width="120" height="45" as="geometry" />
        </mxCell>
        <mxCell id="uc6" value="Tạo hóa đơn" style="ellipse;whiteSpace=wrap;html=1;fillColor=#e8eef5;strokeColor=#5a7a9a;strokeWidth=1.5;" vertex="1" parent="sys">
          <mxGeometry x="140" y="390" width="120" height="45" as="geometry" />
        </mxCell>
        <mxCell id="uc7" value="Quản lý Khách hàng" style="ellipse;whiteSpace=wrap;html=1;fillColor=#e8eef5;strokeColor=#5a7a9a;strokeWidth=1.5;" vertex="1" parent="sys">
          <mxGeometry x="130" y="460" width="140" height="45" as="geometry" />
        </mxCell>
        
        <!-- Include relationships -->
        <mxCell id="inc1" value="«include»" style="endArrow=open;endSize=12;dashed=1;html=1;strokeWidth=1.5;verticalAlign=bottom;" edge="1" parent="sys" source="uc3" target="uc2">
          <mxGeometry x="-0.2" relative="1" as="geometry">
            <mxPoint as="offset" />
          </mxGeometry>
        </mxCell>
        <mxCell id="inc2" value="«include»" style="endArrow=open;endSize=12;dashed=1;html=1;strokeWidth=1.5;verticalAlign=bottom;" edge="1" parent="sys" source="uc5" target="uc6">
          <mxGeometry x="-0.2" relative="1" as="geometry">
            <mxPoint as="offset" />
          </mxGeometry>
        </mxCell>
        
        <!-- Actor Lễ tân (Role: STAFF) - không có Actor cha -->
        <mxCell id="a1" value="Lễ tân&lt;br&gt;(Staff)" style="shape=umlActor;verticalLabelPosition=bottom;verticalAlign=top;html=1;outlineConnect=0;" vertex="1" parent="1">
          <mxGeometry x="80" y="200" width="40" height="80" as="geometry" />
        </mxCell>
        
        <!-- Associations - Lễ tân nối trực tiếp với tất cả Use Case -->
        <mxCell id="assoc1" style="endArrow=none;html=1;strokeWidth=1.5;" edge="1" parent="1" source="a1" target="uc1">
          <mxGeometry relative="1" as="geometry" />
        </mxCell>
        <mxCell id="assoc2" style="endArrow=none;html=1;strokeWidth=1.5;" edge="1" parent="1" source="a1" target="uc2">
          <mxGeometry relative="1" as="geometry" />
        </mxCell>
        <mxCell id="assoc3" style="endArrow=none;html=1;strokeWidth=1.5;" edge="1" parent="1" source="a1" target="uc3">
          <mxGeometry relative="1" as="geometry" />
        </mxCell>
        <mxCell id="assoc4" style="endArrow=none;html=1;strokeWidth=1.5;" edge="1" parent="1" source="a1" target="uc4">
          <mxGeometry relative="1" as="geometry" />
        </mxCell>
        <mxCell id="assoc5" style="endArrow=none;html=1;strokeWidth=1.5;" edge="1" parent="1" source="a1" target="uc5">
          <mxGeometry relative="1" as="geometry" />
        </mxCell>
        <mxCell id="assoc6" style="endArrow=none;html=1;strokeWidth=1.5;" edge="1" parent="1" source="a1" target="uc7">
          <mxGeometry relative="1" as="geometry" />
        </mxCell>
      </root>
    </mxGraphModel>
  </diagram>
</mxfile>
```

---

## 4. USE CASE QUẢN LÝ

Lưu file này với tên: `UseCaseQuanLy.drawio`

```xml
<mxfile host="app.diagrams.net">
  <diagram name="Use Case Quản lý" id="ucql">
    <mxGraphModel dx="900" dy="500" grid="1" gridSize="10" guides="1" tooltips="1" connect="1" arrows="1" fold="1" page="1" pageScale="1" pageWidth="900" pageHeight="500">
      <root>
        <mxCell id="0" />
        <mxCell id="1" parent="0" />
        
        <!-- System Boundary -->
        <mxCell id="sys" value="Hệ thống Quản lý Khách sạn" style="swimlane;whiteSpace=wrap;html=1;startSize=30;fillColor=none;strokeColor=#2c3e50;strokeWidth=2;fontSize=14;fontStyle=1;" vertex="1" parent="1">
          <mxGeometry x="240" y="20" width="400" height="420" as="geometry" />
        </mxCell>
        
        <!-- Use Cases -->
        <mxCell id="uc1" value="Đăng nhập" style="ellipse;whiteSpace=wrap;html=1;fillColor=#e8eef5;strokeColor=#5a7a9a;strokeWidth=1.5;" vertex="1" parent="sys">
          <mxGeometry x="140" y="40" width="120" height="45" as="geometry" />
        </mxCell>
        <mxCell id="uc2" value="Thêm phòng" style="ellipse;whiteSpace=wrap;html=1;fillColor=#e8eef5;strokeColor=#5a7a9a;strokeWidth=1.5;" vertex="1" parent="sys">
          <mxGeometry x="60" y="120" width="120" height="45" as="geometry" />
        </mxCell>
        <mxCell id="uc3" value="Sửa phòng" style="ellipse;whiteSpace=wrap;html=1;fillColor=#e8eef5;strokeColor=#5a7a9a;strokeWidth=1.5;" vertex="1" parent="sys">
          <mxGeometry x="220" y="120" width="120" height="45" as="geometry" />
        </mxCell>
        <mxCell id="uc4" value="Xóa phòng" style="ellipse;whiteSpace=wrap;html=1;fillColor=#e8eef5;strokeColor=#5a7a9a;strokeWidth=1.5;" vertex="1" parent="sys">
          <mxGeometry x="60" y="200" width="120" height="45" as="geometry" />
        </mxCell>
        <mxCell id="uc5" value="Xem báo cáo doanh thu" style="ellipse;whiteSpace=wrap;html=1;fillColor=#e8eef5;strokeColor=#5a7a9a;strokeWidth=1.5;" vertex="1" parent="sys">
          <mxGeometry x="200" y="200" width="150" height="45" as="geometry" />
        </mxCell>
        <mxCell id="uc6" value="Xem thống kê phòng" style="ellipse;whiteSpace=wrap;html=1;fillColor=#e8eef5;strokeColor=#5a7a9a;strokeWidth=1.5;" vertex="1" parent="sys">
          <mxGeometry x="125" y="280" width="150" height="45" as="geometry" />
        </mxCell>
        
        <!-- Actor Quản lý (Role: MANAGER) - không có Actor cha -->
        <mxCell id="a1" value="Quản lý&lt;br&gt;(Manager)" style="shape=umlActor;verticalLabelPosition=bottom;verticalAlign=top;html=1;outlineConnect=0;" vertex="1" parent="1">
          <mxGeometry x="80" y="150" width="40" height="80" as="geometry" />
        </mxCell>
        
        <!-- Associations - Quản lý nối trực tiếp với tất cả Use Case -->
        <mxCell id="assoc1" style="endArrow=none;html=1;strokeWidth=1.5;" edge="1" parent="1" source="a1" target="uc1">
          <mxGeometry relative="1" as="geometry" />
        </mxCell>
        <mxCell id="assoc2" style="endArrow=none;html=1;strokeWidth=1.5;" edge="1" parent="1" source="a1" target="uc2">
          <mxGeometry relative="1" as="geometry" />
        </mxCell>
        <mxCell id="assoc3" style="endArrow=none;html=1;strokeWidth=1.5;" edge="1" parent="1" source="a1" target="uc3">
          <mxGeometry relative="1" as="geometry" />
        </mxCell>
        <mxCell id="assoc4" style="endArrow=none;html=1;strokeWidth=1.5;" edge="1" parent="1" source="a1" target="uc4">
          <mxGeometry relative="1" as="geometry" />
        </mxCell>
        <mxCell id="assoc5" style="endArrow=none;html=1;strokeWidth=1.5;" edge="1" parent="1" source="a1" target="uc5">
          <mxGeometry relative="1" as="geometry" />
        </mxCell>
        <mxCell id="assoc6" style="endArrow=none;html=1;strokeWidth=1.5;" edge="1" parent="1" source="a1" target="uc6">
          <mxGeometry relative="1" as="geometry" />
        </mxCell>
      </root>
    </mxGraphModel>
  </diagram>
</mxfile>
```

---

## HƯỚNG DẪN SỬ DỤNG

1. **Mở draw.io** (https://app.diagrams.net)
2. **Tạo file mới** hoặc chọn **File → New**
3. **Import XML**:
   - Copy code XML bên trên
   - Chọn **File → Import from → Text**
   - Paste XML và nhấn **Import**
4. **Chỉnh sửa** theo ý muốn
5. **Export** ra PNG, SVG hoặc PDF

### Ký hiệu UML trong draw.io:
- **Actor (Stick Figure)**: Shapes → UML → Actor
- **Use Case (Ellipse)**: Shapes → General → Ellipse
- **System Boundary (Rectangle)**: Shapes → General → Rectangle hoặc Swimlane
- **Generalization Arrow**: Arrow với đầu tam giác rỗng (hollow triangle)
- **Include/Extend**: Arrow nét đứt (dashed) với label
