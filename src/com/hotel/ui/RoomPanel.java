package com.hotel.ui;

import com.hotel.auth.PermissionManager.Permission;
import com.hotel.auth.UserSession;
import com.hotel.model.room.Room;
import com.hotel.service.RoomManager;
import com.hotel.storage.DataStorage;
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
 * Panel quản lý phòng với đầy đủ chức năng CRUD và themed UI
 * 
 * @author OOP Project Team
 * @version 2.0
 */
public class RoomPanel extends JPanel {

    // ==================== COMPONENTS ====================

    private JTable roomTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> filterTypeCombo;
    private JComboBox<String> filterStatusCombo;
    private JLabel statusLabel;

    // ==================== SERVICES ====================

    private final RoomManager roomManager;
    private final DataStorage dataStorage;

    // ==================== CONSTRUCTOR ====================

    public RoomPanel() {
        this.roomManager = RoomManager.getInstance();
        this.dataStorage = new DataStorage(null, null, null, roomManager);

        initializeUI();
        loadData();
    }

    // ==================== UI INITIALIZATION ====================

    private void initializeUI() {
        setLayout(new BorderLayout(AppTheme.Spacing.MD, AppTheme.Spacing.MD));
        setBorder(AppTheme.BorderStyles.EMPTY_MD);
        setBackground(AppTheme.Background.PRIMARY);

        add(createTopPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(AppTheme.Spacing.SM, AppTheme.Spacing.SM));
        topPanel.setOpaque(false);

        // Search panel
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

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, AppTheme.Spacing.SM, 0));
        filterPanel.setOpaque(false);

        JLabel typeLabel = new JLabel("● Loại phòng:");
        typeLabel.setFont(AppTheme.Fonts.BODY);
        filterPanel.add(typeLabel);

        filterTypeCombo = new JComboBox<>(UIConstants.Filters.ROOM_TYPES);
        filterTypeCombo.setFont(AppTheme.Fonts.BODY);
        filterTypeCombo.addActionListener(e -> applyFilters());
        filterPanel.add(filterTypeCombo);

        filterPanel.add(Box.createHorizontalStrut(AppTheme.Spacing.LG));

        JLabel statusFilterLabel = new JLabel("■ Trạng thái:");
        statusFilterLabel.setFont(AppTheme.Fonts.BODY);
        filterPanel.add(statusFilterLabel);

        filterStatusCombo = new JComboBox<>(UIConstants.Filters.ROOM_STATUSES);
        filterStatusCombo.setFont(AppTheme.Fonts.BODY);
        filterStatusCombo.addActionListener(e -> applyFilters());
        filterPanel.add(filterStatusCombo);

        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.SOUTH);

        return topPanel;
    }

    private JScrollPane createTablePanel() {
        tableModel = new DefaultTableModel(UIConstants.Columns.ROOMS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        roomTable = new JTable(tableModel);
        roomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roomTable.getTableHeader().setReorderingAllowed(false);
        AppTheme.applyTableStyle(roomTable);

        // Custom renderer for status column with colors
        roomTable.getColumnModel().getColumn(3).setCellRenderer(new StatusCellRenderer());

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

        JScrollPane scrollPane = new JScrollPane(roomTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(AppTheme.Borders.LIGHT));
        scrollPane.getViewport().setBackground(AppTheme.Background.PAPER);

        return scrollPane;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new BorderLayout(AppTheme.Spacing.MD, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(AppTheme.Spacing.SM, 0, 0, 0));

        // Left - Status with icon
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, AppTheme.Spacing.XS, 0));
        statusPanel.setOpaque(false);

        JLabel iconLabel = new JLabel("★");
        statusPanel.add(iconLabel);

        statusLabel = new JLabel("Tổng số phòng: 0");
        statusLabel.setFont(AppTheme.Fonts.BODY_BOLD);
        statusLabel.setForeground(AppTheme.Text.SECONDARY);
        statusPanel.add(statusLabel);

        buttonPanel.add(statusPanel, BorderLayout.WEST);

        // Right - Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, AppTheme.Spacing.SM, 0));
        actionPanel.setOpaque(false);

        // Kiểm tra quyền
        UserSession session = UserSession.getInstance();
        boolean canManage = session.hasPermission(Permission.MANAGE_ROOMS);

        JButton addBtn = new JButton("+ Thêm mới");
        AppTheme.applySuccessButtonStyle(addBtn);
        addBtn.setPreferredSize(new Dimension(120, 36));
        addBtn.addActionListener(e -> showAddDialog());
        addBtn.setEnabled(canManage);
        if (!canManage)
            addBtn.setToolTipText("Bạn không có quyền thêm phòng");
        actionPanel.add(addBtn);

        JButton editBtn = new JButton("✎ Sửa");
        AppTheme.applyPrimaryButtonStyle(editBtn);
        editBtn.setPreferredSize(new Dimension(90, 36));
        editBtn.addActionListener(e -> editSelectedRoom());
        editBtn.setEnabled(canManage);
        if (!canManage)
            editBtn.setToolTipText("Bạn không có quyền sửa phòng");
        actionPanel.add(editBtn);

        JButton deleteBtn = new JButton("× Xóa");
        AppTheme.applyDangerButtonStyle(deleteBtn);
        deleteBtn.setPreferredSize(new Dimension(90, 36));
        deleteBtn.addActionListener(e -> deleteSelectedRoom());
        deleteBtn.setEnabled(canManage);
        if (!canManage)
            deleteBtn.setToolTipText("Bạn không có quyền xóa phòng");
        actionPanel.add(deleteBtn);

        JButton refreshBtn = new JButton("↻ Làm mới");
        AppTheme.applySecondaryButtonStyle(refreshBtn);
        refreshBtn.setPreferredSize(new Dimension(110, 36));
        refreshBtn.addActionListener(e -> refreshTable());
        actionPanel.add(refreshBtn);

        JButton saveBtn = new JButton("✓ Lưu");
        AppTheme.applyPrimaryButtonStyle(saveBtn);
        saveBtn.setPreferredSize(new Dimension(90, 36));
        saveBtn.addActionListener(e -> saveData());
        saveBtn.setEnabled(canManage);
        if (!canManage)
            saveBtn.setToolTipText("Bạn không có quyền lưu thay đổi");
        actionPanel.add(saveBtn);

        buttonPanel.add(actionPanel, BorderLayout.EAST);

        return buttonPanel;
    }

    // ==================== CUSTOM RENDERERS ====================

    private static class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (!isSelected && value != null) {
                String status = value.toString();
                Color bgColor = AppTheme.getRoomStatusColor(status);

                // Create a badge-like appearance
                setOpaque(true);
                setHorizontalAlignment(CENTER);
                setFont(AppTheme.Fonts.SMALL);

                if (bgColor.equals(AppTheme.Status.SUCCESS)) {
                    setBackground(new Color(232, 245, 233)); // Light green
                    setForeground(new Color(27, 94, 32)); // Dark green
                } else if (bgColor.equals(AppTheme.Status.ERROR)) {
                    setBackground(new Color(255, 235, 238)); // Light red
                    setForeground(new Color(183, 28, 28)); // Dark red
                } else if (bgColor.equals(AppTheme.Status.WARNING)) {
                    setBackground(new Color(255, 248, 225)); // Light amber
                    setForeground(new Color(255, 111, 0)); // Dark amber
                } else if (bgColor.equals(AppTheme.Status.INFO)) {
                    setBackground(new Color(227, 242, 253)); // Light blue
                    setForeground(new Color(13, 71, 161)); // Dark blue
                } else {
                    setBackground(table.getBackground());
                    setForeground(table.getForeground());
                }
            } else {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            }

            return c;
        }
    }

    // ==================== DATA OPERATIONS ====================

    private void loadData() {
        List<Room> rooms = dataStorage.loadRoomsList();
        roomManager.loadRooms(rooms);
        refreshTable();
    }

    private void saveData() {
        List<Room> rooms = roomManager.getAll();
        if (dataStorage.saveRooms(rooms)) {
            JOptionPane.showMessageDialog(this,
                    UIConstants.Messages.SAVE_SUCCESS,
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    UIConstants.Messages.SAVE_ERROR,
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void saveRoomsData() {
        List<Room> rooms = roomManager.getAll();
        dataStorage.saveRooms(rooms);
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Room> rooms = roomManager.getAll();

        for (Room room : rooms) {
            tableModel.addRow(new Object[] {
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
            tableModel.addRow(new Object[] {
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
            boolean matchType = UIConstants.Filters.ALL.equals(typeFilter) ||
                    room.getRoomType().getDisplayName().contains(typeFilter);
            boolean matchStatus = UIConstants.Filters.ALL.equals(statusFilter) ||
                    room.getStatus().getDisplayName().equals(statusFilter);

            if (matchType && matchStatus) {
                tableModel.addRow(new Object[] {
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
        RoomDialog dialog = new RoomDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                UIConstants.Titles.ROOM_DIALOG_ADD);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Room newRoom = dialog.getRoom();
            if (roomManager.add(newRoom)) {
                refreshTable();
                JOptionPane.showMessageDialog(this, UIConstants.Messages.ADD_SUCCESS);
            } else {
                JOptionPane.showMessageDialog(this, UIConstants.Messages.DUPLICATE_ID, "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editSelectedRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, UIConstants.Messages.NO_SELECTION);
            return;
        }

        int modelRow = roomTable.convertRowIndexToModel(selectedRow);
        String roomId = (String) tableModel.getValueAt(modelRow, 0);
        Room room = roomManager.getById(roomId);

        if (room == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy phòng!");
            return;
        }

        RoomDialog dialog = new RoomDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                UIConstants.Titles.ROOM_DIALOG_EDIT,
                room);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Room updatedRoom = dialog.getRoom();
            if (roomManager.update(updatedRoom)) {
                refreshTable();
                JOptionPane.showMessageDialog(this, UIConstants.Messages.UPDATE_SUCCESS);
            }
        }
    }

    private void deleteSelectedRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, UIConstants.Messages.NO_SELECTION);
            return;
        }

        int modelRow = roomTable.convertRowIndexToModel(selectedRow);
        String roomId = (String) tableModel.getValueAt(modelRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                UIConstants.Messages.CONFIRM_DELETE + "\nPhòng: " + roomId,
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (roomManager.delete(roomId)) {
                refreshTable();
                JOptionPane.showMessageDialog(this, UIConstants.Messages.DELETE_SUCCESS);
            }
        }
    }
}
