package com.hotel.ui.base;

import com.hotel.ui.theme.AppTheme;
import com.hotel.ui.theme.UIConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Lớp trừu tượng cơ sở cho các panel hiển thị bảng với chức năng CRUD.
 * Cung cấp hạ tầng chung cho tìm kiếm, lọc, hiển thị bảng và các nút hành động.
 * Các lớp con cần implement các phương thức trừu tượng cho entity cụ thể.
 * 
 * @param <T> Kiểu entity mà panel quản lý
 * @author OOP Project Team
 * @version 2.0
 */
public abstract class BaseTablePanel<T> extends JPanel {

    // ==================== CÁC THÀNH PHẦN UI ====================

    protected JTable table; // Bảng hiển thị dữ liệu
    protected DefaultTableModel tableModel; // Model của bảng
    protected JTextField searchField; // Ô tìm kiếm
    protected JLabel statusLabel; // Label hiển thị trạng thái
    protected TableRowSorter<DefaultTableModel> sorter; // Bộ sắp xếp bảng

    // ==================== CONSTRUCTOR ====================

    protected BaseTablePanel() {
        initializeUI();
        loadData();
    }

    // ==================== CÁC PHƯƠNG THỨC TRỪU TƯỢNG ====================

    /**
     * Lấy tên các cột của bảng
     * 
     * @return Mảng tên cột
     */
    protected abstract String[] getColumnNames();

    /**
     * Chuyển đổi entity thành dữ liệu dòng trong bảng
     * 
     * @param item Entity cần chuyển đổi
     * @return Mảng dữ liệu cho dòng
     */
    protected abstract Object[] toRowData(T item);

    /**
     * Tải tất cả dữ liệu từ storage/service
     * 
     * @return Danh sách entity
     */
    protected abstract List<T> loadAllData();

    /**
     * Tìm kiếm dữ liệu theo từ khóa
     * 
     * @param keyword Từ khóa tìm kiếm
     * @return Danh sách kết quả
     */
    protected abstract List<T> searchData(String keyword);

    /**
     * Lấy entity từ dòng được chọn
     * 
     * @return Entity được chọn hoặc null
     */
    protected abstract T getSelectedEntity();

    /**
     * Xử lý khi nhấn nút Thêm
     */
    protected abstract void onAdd();

    /**
     * Xử lý khi nhấn nút Sửa
     * 
     * @param entity Entity cần sửa
     */
    protected abstract void onEdit(T entity);

    /**
     * Xử lý khi nhấn nút Xóa
     * 
     * @param entity Entity cần xóa
     */
    protected abstract void onDelete(T entity);

    /**
     * Xử lý khi nhấn nút Lưu
     */
    protected abstract void onSave();

    /**
     * Lấy text hiển thị trạng thái (VD: "Tổng: 10 mục")
     * 
     * @return Chuỗi trạng thái
     */
    protected abstract String getStatusText();

    /**
     * Lấy tiêu đề của panel
     * 
     * @return Tiêu đề
     */
    protected abstract String getPanelTitle();

    // ==================== KHỞI TẠO UI ====================

    /**
     * Khởi tạo giao diện người dùng
     */
    private void initializeUI() {
        setLayout(new BorderLayout(AppTheme.Spacing.MD, AppTheme.Spacing.MD));
        setBorder(AppTheme.BorderStyles.EMPTY_MD);
        setBackground(AppTheme.Background.PRIMARY);

        add(createTopPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    /**
     * Tạo panel phía trên (tìm kiếm + lọc)
     */
    protected JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(AppTheme.Spacing.SM, AppTheme.Spacing.SM));
        topPanel.setOpaque(false);

        // Panel tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, AppTheme.Spacing.SM, 0));
        searchPanel.setOpaque(false);

        JLabel searchLabel = new JLabel("▶ Tìm kiếm:");
        searchLabel.setFont(AppTheme.Fonts.BODY);
        searchPanel.add(searchLabel);

        searchField = new JTextField(20);
        searchField.setFont(AppTheme.Fonts.BODY);
        searchField.setPreferredSize(new Dimension(200, AppTheme.Size.INPUT_HEIGHT));
        searchField.addActionListener(e -> performSearch());
        searchPanel.add(searchField);

        JButton searchBtn = new JButton(UIConstants.Buttons.SEARCH);
        AppTheme.applyPrimaryButtonStyle(searchBtn);
        searchBtn.setPreferredSize(new Dimension(90, 32));
        searchBtn.addActionListener(e -> performSearch());
        searchPanel.add(searchBtn);

        topPanel.add(searchPanel, BorderLayout.NORTH);

        // Lớp con có thể thêm panel lọc bằng cách override createFilterPanel()
        JPanel filterPanel = createFilterPanel();
        if (filterPanel != null) {
            topPanel.add(filterPanel, BorderLayout.SOUTH);
        }

        return topPanel;
    }

    /**
     * Override để thêm các control lọc
     * 
     * @return Panel lọc hoặc null
     */
    protected JPanel createFilterPanel() {
        return null;
    }

    /**
     * Tạo panel chứa bảng
     */
    protected JScrollPane createTablePanel() {
        tableModel = new DefaultTableModel(getColumnNames(), 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép sửa trực tiếp trong bảng
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        AppTheme.applyTableStyle(table);

        // Áp dụng các renderer tùy chỉnh
        configureTableRenderers();

        // Bật sắp xếp
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        // Double click để sửa
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    T entity = getSelectedEntity();
                    if (entity != null) {
                        onEdit(entity);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(AppTheme.Borders.LIGHT));
        scrollPane.getViewport().setBackground(AppTheme.Background.PAPER);

        return scrollPane;
    }

    /**
     * Override để cấu hình các cell renderer tùy chỉnh
     */
    protected void configureTableRenderers() {
        // Mặc định: không có renderer tùy chỉnh
    }

    /**
     * Tạo panel chứa các nút hành động
     */
    protected JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new BorderLayout(AppTheme.Spacing.MD, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(AppTheme.Spacing.SM, 0, 0, 0));

        // Bên trái - Trạng thái
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, AppTheme.Spacing.XS, 0));
        statusPanel.setOpaque(false);

        JLabel iconLabel = new JLabel("★");
        statusPanel.add(iconLabel);

        statusLabel = new JLabel(getStatusText());
        statusLabel.setFont(AppTheme.Fonts.BODY_BOLD);
        statusLabel.setForeground(AppTheme.Text.SECONDARY);
        statusPanel.add(statusLabel);

        buttonPanel.add(statusPanel, BorderLayout.WEST);

        // Bên phải - Các nút hành động
        JPanel actionPanel = createActionButtons();
        buttonPanel.add(actionPanel, BorderLayout.EAST);

        return buttonPanel;
    }

    /**
     * Tạo các nút hành động (Thêm, Sửa, Xóa, Làm mới, Lưu)
     */
    protected JPanel createActionButtons() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, AppTheme.Spacing.SM, 0));
        actionPanel.setOpaque(false);

        // Nút Thêm
        JButton addBtn = new JButton("+ Thêm mới");
        AppTheme.applySuccessButtonStyle(addBtn);
        addBtn.setPreferredSize(new Dimension(120, 36));
        addBtn.addActionListener(e -> onAdd());
        actionPanel.add(addBtn);

        // Nút Sửa
        JButton editBtn = new JButton("✎ Sửa");
        AppTheme.applyPrimaryButtonStyle(editBtn);
        editBtn.setPreferredSize(new Dimension(90, 36));
        editBtn.addActionListener(e -> {
            T entity = getSelectedEntity();
            if (entity != null) {
                onEdit(entity);
            } else {
                JOptionPane.showMessageDialog(this, UIConstants.Messages.NO_SELECTION);
            }
        });
        actionPanel.add(editBtn);

        // Nút Xóa
        JButton deleteBtn = new JButton("× Xóa");
        AppTheme.applyDangerButtonStyle(deleteBtn);
        deleteBtn.setPreferredSize(new Dimension(90, 36));
        deleteBtn.addActionListener(e -> {
            T entity = getSelectedEntity();
            if (entity != null) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        UIConstants.Messages.CONFIRM_DELETE,
                        "Xác nhận",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    onDelete(entity);
                }
            } else {
                JOptionPane.showMessageDialog(this, UIConstants.Messages.NO_SELECTION);
            }
        });
        actionPanel.add(deleteBtn);

        // Nút Làm mới
        JButton refreshBtn = new JButton("↻ Làm mới");
        AppTheme.applySecondaryButtonStyle(refreshBtn);
        refreshBtn.setPreferredSize(new Dimension(110, 36));
        refreshBtn.addActionListener(e -> refreshTable());
        actionPanel.add(refreshBtn);

        // Nút Lưu
        JButton saveBtn = new JButton("✓ Lưu");
        AppTheme.applyPrimaryButtonStyle(saveBtn);
        saveBtn.setPreferredSize(new Dimension(90, 36));
        saveBtn.addActionListener(e -> {
            onSave();
            JOptionPane.showMessageDialog(this, UIConstants.Messages.SAVE_SUCCESS);
        });
        actionPanel.add(saveBtn);

        return actionPanel;
    }

    // ==================== THAO TÁC DỮ LIỆU ====================

    /**
     * Tải dữ liệu vào bảng
     */
    protected void loadData() {
        List<T> data = loadAllData();
        populateTable(data);
    }

    /**
     * Đổ dữ liệu vào bảng
     * 
     * @param data Danh sách dữ liệu
     */
    protected void populateTable(List<T> data) {
        tableModel.setRowCount(0);
        for (T item : data) {
            tableModel.addRow(toRowData(item));
        }
        updateStatus();
    }

    /**
     * Làm mới bảng
     */
    public void refreshTable() {
        loadData();
    }

    /**
     * Cập nhật label trạng thái
     */
    protected void updateStatus() {
        statusLabel.setText(getStatusText());
    }

    /**
     * Thực hiện tìm kiếm
     */
    protected void performSearch() {
        String keyword = searchField.getText().trim();
        List<T> results = searchData(keyword);
        populateTable(results);
    }

    // ==================== CÁC PHƯƠNG THỨC HỖ TRỢ ====================

    /**
     * Lấy index dòng được chọn (đã chuyển đổi về model)
     * 
     * @return Index dòng hoặc -1 nếu không có
     */
    protected int getSelectedRow() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0)
            return -1;
        return table.convertRowIndexToModel(viewRow);
    }

    /**
     * Lấy giá trị tại ô cụ thể
     * 
     * @param row    Index dòng
     * @param column Index cột
     * @return Giá trị tại ô
     */
    protected Object getValueAt(int row, int column) {
        return tableModel.getValueAt(row, column);
    }

    /**
     * Renderer để hiển thị trạng thái với màu sắc (badge)
     */
    protected static class StatusCellRenderer extends DefaultTableCellRenderer {
        private final java.util.function.Function<String, Color> colorMapper;

        /**
         * Tạo renderer với hàm ánh xạ màu
         * 
         * @param colorMapper Hàm trả về màu dựa trên giá trị trạng thái
         */
        public StatusCellRenderer(java.util.function.Function<String, Color> colorMapper) {
            this.colorMapper = colorMapper;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (!isSelected && value != null) {
                String status = value.toString();
                Color baseColor = colorMapper.apply(status);

                setOpaque(true);
                setHorizontalAlignment(CENTER);
                setFont(AppTheme.Fonts.SMALL);

                // Nền nhạt với chữ đậm hơn
                int r = baseColor.getRed(), g = baseColor.getGreen(), b = baseColor.getBlue();
                setBackground(new Color(r, g, b, 40));
                setForeground(baseColor.darker());
            } else {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            }

            return c;
        }
    }
}
