package com.hotel.ui;

import com.hotel.model.room.Room;
import com.hotel.service.RoomManager;
import com.hotel.storage.RoomStorage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Panel quản lý phòng với đầy đủ chức năng CRUD
 * 
 * @author Member1
 * @version 1.0
 */
public class RoomPanel extends JPanel {
    
    // ==================== CONSTANTS ====================
    
    private static final String[] COLUMN_NAMES = {
            "Mã phòng", "Loại phòng", "Tầng", "Trạng thái", "Giá (VND/đêm)", "Số giường", "Diện tích (m²)"
    };
    
    // ==================== COMPONENTS ====================
    
    private JTable roomTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> filterTypeCombo;
    private JComboBox<String> filterStatusCombo;
    private JLabel statusLabel;
    
    // ==================== SERVICES ====================
    
    private final RoomManager roomManager;
    private final RoomStorage roomStorage;
    
    // ==================== CONSTRUCTOR ====================
    
    public RoomPanel() {
        this.roomManager = RoomManager.getInstance();
        this.roomStorage = new RoomStorage();

        initializeUI();
        loadData();
    }
    
    // ==================== UI INITIALIZATION ====================
    
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Top panel - Search & Filter
        add(createTopPanel(), BorderLayout.NORTH);
        
        // Center - Table
        add(createTablePanel(), BorderLayout.CENTER);
        
        // Bottom - Buttons
        add(createButtonPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(10, 5));
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("🔍 Tìm kiếm:"));
        searchField = new JTextField(20);
        searchField.addActionListener(e -> performSearch());
        searchPanel.add(searchField);
        
        JButton searchBtn = new JButton("Tìm");
        searchBtn.addActionListener(e -> performSearch());
        searchPanel.add(searchBtn);
        
        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Loại phòng:"));
        filterTypeCombo = new JComboBox<>(new String[]{"Tất cả", "Standard", "VIP", "Deluxe"});
        filterTypeCombo.addActionListener(e -> applyFilters());
        filterPanel.add(filterTypeCombo);
        
        filterPanel.add(new JLabel("Trạng thái:"));
        filterStatusCombo = new JComboBox<>(new String[]{"Tất cả", "Trống", "Đang sử dụng", "Bảo trì", "Đang dọn"});
        filterStatusCombo.addActionListener(e -> applyFilters());
        filterPanel.add(filterStatusCombo);
        
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.SOUTH);
        
        return topPanel;
    }
    
    private JScrollPane createTablePanel() {
        // Create table model
        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Read-only
            }
        };
        
        // Create table
        roomTable = new JTable(tableModel);
        roomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roomTable.setRowHeight(25);
        roomTable.getTableHeader().setReorderingAllowed(false);
        
        // Enable sorting
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        roomTable.setRowSorter(sorter);
        
        // Double click to edit
        roomTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editSelectedRoom();
                }
            }
        });
        
        return new JScrollPane(roomTable);
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new BorderLayout());
        
        // Left - Status
        statusLabel = new JLabel("Tổng số phòng: 0");
        buttonPanel.add(statusLabel, BorderLayout.WEST);
        
        // Right - Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton addBtn = new JButton("➕ Thêm phòng");
        addBtn.addActionListener(e -> showAddDialog());
        actionPanel.add(addBtn);
        
        JButton editBtn = new JButton("✏️ Sửa");
        editBtn.addActionListener(e -> editSelectedRoom());
        actionPanel.add(editBtn);
        
        JButton deleteBtn = new JButton("🗑️ Xóa");
        deleteBtn.addActionListener(e -> deleteSelectedRoom());
        actionPanel.add(deleteBtn);
        
        JButton refreshBtn = new JButton("🔄 Làm mới");
        refreshBtn.addActionListener(e -> refreshTable());
        actionPanel.add(refreshBtn);
        
        JButton saveBtn = new JButton("💾 Lưu");
        saveBtn.addActionListener(e -> saveData());
        actionPanel.add(saveBtn);
        
        buttonPanel.add(actionPanel, BorderLayout.EAST);
        
        return buttonPanel;
    }
    
    // ==================== DATA OPERATIONS ====================
    
    private void loadData() {
        List<Room> rooms = roomStorage.loadRooms();
        roomManager.loadRooms(rooms);
        refreshTable();
    }
    
    private void saveData() {
        List<Room> rooms = roomManager.getAll();
        if (roomStorage.saveRooms(rooms)) {
            JOptionPane.showMessageDialog(this, 
                    "Đã lưu dữ liệu thành công!", 
                    "Thông báo", 
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                    "Lỗi khi lưu dữ liệu!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void saveRoomsData() {
        saveData();
    }
    
    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Room> rooms = roomManager.getAll();
        
        for (Room room : rooms) {
            tableModel.addRow(new Object[]{
                    room.getRoomId(),
                    room.getRoomType().getDisplayName(),
                    room.getFloor(),
                    room.getStatus().getDisplayName(),
                    String.format("%,.0f", room.getBasePrice()),
                    room.getBedCount(),
                    String.format("%.1f", room.getArea())
            });
        }
        
        updateStatusLabel();
    }
    
    private void updateStatusLabel() {
        int total = roomManager.count();
        long available = roomManager.findAvailableRooms().size();
        statusLabel.setText(String.format("Tổng: %d phòng | Trống: %d phòng", total, available));
    }
    
    // ==================== SEARCH & FILTER ====================
    
    private void performSearch() {
        String keyword = searchField.getText().trim();
        
        tableModel.setRowCount(0);
        List<Room> rooms = roomManager.search(keyword);
        
        for (Room room : rooms) {
            tableModel.addRow(new Object[]{
                    room.getRoomId(),
                    room.getRoomType().getDisplayName(),
                    room.getFloor(),
                    room.getStatus().getDisplayName(),
                    String.format("%,.0f", room.getBasePrice()),
                    room.getBedCount(),
                    String.format("%.1f", room.getArea())
            });
        }
    }
    
    private void applyFilters() {
        String typeFilter = (String) filterTypeCombo.getSelectedItem();
        String statusFilter = (String) filterStatusCombo.getSelectedItem();
        
        tableModel.setRowCount(0);
        List<Room> rooms = roomManager.getAll();
        
        for (Room room : rooms) {
            boolean matchType = "Tất cả".equals(typeFilter) || 
                    room.getRoomType().getDisplayName().contains(typeFilter);
            boolean matchStatus = "Tất cả".equals(statusFilter) || 
                    room.getStatus().getDisplayName().equals(statusFilter);
            
            if (matchType && matchStatus) {
                tableModel.addRow(new Object[]{
                        room.getRoomId(),
                        room.getRoomType().getDisplayName(),
                        room.getFloor(),
                        room.getStatus().getDisplayName(),
                        String.format("%,.0f", room.getBasePrice()),
                        room.getBedCount(),
                        String.format("%.1f", room.getArea())
                });
            }
        }
    }
    
    // ==================== CRUD DIALOGS ====================
    
    private void showAddDialog() {
        RoomDialog dialog = new RoomDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm phòng mới");
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            Room newRoom = dialog.getRoom();
            if (roomManager.add(newRoom)) {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Thêm phòng thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Mã phòng đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void editSelectedRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng cần sửa!");
            return;
        }
        
        // Convert view index to model index (for sorting)
        int modelRow = roomTable.convertRowIndexToModel(selectedRow);
        String roomId = (String) tableModel.getValueAt(modelRow, 0);
        Room room = roomManager.getById(roomId);
        
        if (room == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy phòng!");
            return;
        }
        
        RoomDialog dialog = new RoomDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sửa thông tin phòng", room);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            Room updatedRoom = dialog.getRoom();
            if (roomManager.update(updatedRoom)) {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            }
        }
    }
    
    private void deleteSelectedRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng cần xóa!");
            return;
        }
        
        int modelRow = roomTable.convertRowIndexToModel(selectedRow);
        String roomId = (String) tableModel.getValueAt(modelRow, 0);
        
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa phòng " + roomId + "?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (roomManager.delete(roomId)) {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Đã xóa phòng!");
            }
        }
    }
}
