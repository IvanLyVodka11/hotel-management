package com.hotel.ui;

import com.hotel.model.enums.RoomStatus;
import com.hotel.model.enums.RoomType;
import com.hotel.model.room.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Dialog để thêm/sửa thông tin phòng
 * 
 * @author Member1
 * @version 1.0
 */
public class RoomDialog extends JDialog {
    
    // ==================== COMPONENTS ====================
    
    private JTextField roomIdField;
    private JComboBox<RoomType> roomTypeCombo;
    private JSpinner floorSpinner;
    private JTextField basePriceField;
    private JComboBox<RoomStatus> statusCombo;
    private JSpinner bedCountSpinner;
    private JSpinner areaSpinner;
    private JTextArea descriptionArea;
    
    // VIP/Deluxe specific
    private JCheckBox hasViewCheck;
    private JCheckBox hasBathroomCheck;
    private JCheckBox hasJacuzziCheck;
    private JCheckBox hasMinibarCheck;
    private JCheckBox hasLivingRoomCheck;
    private JPanel amenitiesPanel;
    
    // ==================== STATE ====================
    
    private boolean confirmed = false;
    private Room existingRoom;
    private boolean isEditMode;
    
    // ==================== CONSTRUCTORS ====================
    
    /**
     * Constructor cho chế độ thêm mới
     */
    public RoomDialog(Frame parent, String title) {
        this(parent, title, null);
    }
    
    /**
     * Constructor cho chế độ sửa
     */
    public RoomDialog(Frame parent, String title, Room room) {
        super(parent, title, true);
        this.existingRoom = room;
        this.isEditMode = (room != null);
        
        initializeUI();
        
        if (isEditMode) {
            populateFields(room);
        }
        
        pack();
        setLocationRelativeTo(parent);
    }
    
    // ==================== UI INITIALIZATION ====================
    
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Main form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // Room ID
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Mã phòng (*):"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        roomIdField = new JTextField(15);
        roomIdField.setEnabled(!isEditMode); // Không cho sửa ID
        formPanel.add(roomIdField, gbc);
        
        // Room Type
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Loại phòng (*):"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        roomTypeCombo = new JComboBox<>(RoomType.values());
        roomTypeCombo.addActionListener(e -> updateAmenitiesPanel());
        formPanel.add(roomTypeCombo, gbc);
        
        // Floor
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Tầng (*):"), gbc);
        gbc.gridx = 1;
        floorSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        formPanel.add(floorSpinner, gbc);
        
        // Base Price
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Giá cơ bản (VND):"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        basePriceField = new JTextField("500000");
        formPanel.add(basePriceField, gbc);
        
        // Status
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Trạng thái:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        statusCombo = new JComboBox<>(RoomStatus.values());
        formPanel.add(statusCombo, gbc);
        
        // Bed Count
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Số giường:"), gbc);
        gbc.gridx = 1;
        bedCountSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        formPanel.add(bedCountSpinner, gbc);
        
        // Area
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Diện tích (m²):"), gbc);
        gbc.gridx = 1;
        areaSpinner = new JSpinner(new SpinnerNumberModel(20.0, 10.0, 500.0, 5.0));
        formPanel.add(areaSpinner, gbc);
        
        // Description
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Mô tả:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH;
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        formPanel.add(new JScrollPane(descriptionArea), gbc);
        
        // Amenities Panel (for VIP/Deluxe)
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        amenitiesPanel = createAmenitiesPanel();
        formPanel.add(amenitiesPanel, gbc);
        
        add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton confirmBtn = new JButton(isEditMode ? "Cập nhật" : "Thêm");
        confirmBtn.addActionListener(e -> onConfirm());
        buttonPanel.add(confirmBtn);
        
        JButton cancelBtn = new JButton("Hủy");
        cancelBtn.addActionListener(e -> dispose());
        buttonPanel.add(cancelBtn);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Initialize amenities visibility
        updateAmenitiesPanel();
    }
    
    private JPanel createAmenitiesPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBorder(new TitledBorder("Tiện nghi cao cấp"));
        
        hasViewCheck = new JCheckBox("View đẹp");
        hasBathroomCheck = new JCheckBox("Phòng tắm riêng cao cấp");
        hasJacuzziCheck = new JCheckBox("Bồn tắm Jacuzzi");
        hasMinibarCheck = new JCheckBox("Minibar");
        hasLivingRoomCheck = new JCheckBox("Phòng khách riêng");
        
        panel.add(hasViewCheck);
        panel.add(hasBathroomCheck);
        panel.add(hasJacuzziCheck);
        panel.add(hasMinibarCheck);
        panel.add(hasLivingRoomCheck);
        
        return panel;
    }
    
    private void updateAmenitiesPanel() {
        RoomType selectedType = (RoomType) roomTypeCombo.getSelectedItem();
        boolean showAmenities = (selectedType == RoomType.VIP || selectedType == RoomType.DELUXE);
        
        amenitiesPanel.setVisible(showAmenities);
        
        // Update default price based on type
        if (!isEditMode) {
            switch (selectedType) {
                case STANDARD:
                    basePriceField.setText("500000");
                    bedCountSpinner.setValue(1);
                    areaSpinner.setValue(20.0);
                    break;
                case VIP:
                    basePriceField.setText("1000000");
                    bedCountSpinner.setValue(2);
                    areaSpinner.setValue(35.0);
                    hasViewCheck.setSelected(true);
                    hasBathroomCheck.setSelected(true);
                    break;
                case DELUXE:
                    basePriceField.setText("1500000");
                    bedCountSpinner.setValue(2);
                    areaSpinner.setValue(50.0);
                    hasViewCheck.setSelected(true);
                    hasBathroomCheck.setSelected(true);
                    hasJacuzziCheck.setSelected(true);
                    hasMinibarCheck.setSelected(true);
                    hasLivingRoomCheck.setSelected(true);
                    break;
            }
        }
        
        // Adjust Jacuzzi, Minibar, LivingRoom visibility
        hasJacuzziCheck.setEnabled(selectedType == RoomType.DELUXE);
        hasMinibarCheck.setEnabled(selectedType == RoomType.DELUXE);
        hasLivingRoomCheck.setEnabled(selectedType == RoomType.DELUXE);
        
        pack();
    }
    
    // ==================== DATA HANDLING ====================
    
    private void populateFields(Room room) {
        roomIdField.setText(room.getRoomId());
        roomTypeCombo.setSelectedItem(room.getRoomType());
        floorSpinner.setValue(room.getFloor());
        basePriceField.setText(String.valueOf((long) room.getBasePrice()));
        statusCombo.setSelectedItem(room.getStatus());
        bedCountSpinner.setValue(room.getBedCount());
        areaSpinner.setValue(room.getArea());
        descriptionArea.setText(room.getDescription());
        
        // VIP/Deluxe specific
        if (room instanceof VIPRoom) {
            VIPRoom vipRoom = (VIPRoom) room;
            hasViewCheck.setSelected(vipRoom.hasView());
            hasBathroomCheck.setSelected(vipRoom.hasPrivateBathroom());
        } else if (room instanceof DeluxeRoom) {
            DeluxeRoom deluxeRoom = (DeluxeRoom) room;
            hasViewCheck.setSelected(deluxeRoom.hasView());
            hasBathroomCheck.setSelected(deluxeRoom.hasPrivateBathroom());
            hasJacuzziCheck.setSelected(deluxeRoom.hasJacuzzi());
            hasMinibarCheck.setSelected(deluxeRoom.hasMinibar());
            hasLivingRoomCheck.setSelected(deluxeRoom.hasLivingRoom());
        }
        
        updateAmenitiesPanel();
    }
    
    private void onConfirm() {
        // Validation
        String roomId = roomIdField.getText().trim();
        if (roomId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            roomIdField.requestFocus();
            return;
        }
        
        double basePrice;
        try {
            basePrice = Double.parseDouble(basePriceField.getText().replace(",", "").trim());
            if (basePrice < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá phòng không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            basePriceField.requestFocus();
            return;
        }
        
        confirmed = true;
        dispose();
    }
    
    /**
     * Lấy đối tượng Room từ form
     */
    public Room getRoom() {
        String roomId = roomIdField.getText().trim().toUpperCase();
        RoomType type = (RoomType) roomTypeCombo.getSelectedItem();
        int floor = (Integer) floorSpinner.getValue();
        double basePrice = Double.parseDouble(basePriceField.getText().replace(",", "").trim());
        RoomStatus status = (RoomStatus) statusCombo.getSelectedItem();
        int bedCount = (Integer) bedCountSpinner.getValue();
        double area = (Double) areaSpinner.getValue();
        String description = descriptionArea.getText().trim();
        
        Room room;
        switch (type) {
            case VIP:
                VIPRoom vipRoom = new VIPRoom(roomId, floor, basePrice, description, bedCount, area);
                vipRoom.setHasView(hasViewCheck.isSelected());
                vipRoom.setHasPrivateBathroom(hasBathroomCheck.isSelected());
                room = vipRoom;
                break;
            case DELUXE:
                DeluxeRoom deluxeRoom = new DeluxeRoom(roomId, floor, basePrice, description, bedCount, area);
                deluxeRoom.setHasView(hasViewCheck.isSelected());
                deluxeRoom.setHasPrivateBathroom(hasBathroomCheck.isSelected());
                deluxeRoom.setHasJacuzzi(hasJacuzziCheck.isSelected());
                deluxeRoom.setHasMinibar(hasMinibarCheck.isSelected());
                deluxeRoom.setHasLivingRoom(hasLivingRoomCheck.isSelected());
                room = deluxeRoom;
                break;
            default:
                room = new StandardRoom(roomId, floor, basePrice, description, bedCount, area);
                break;
        }
        
        room.setStatus(status);
        return room;
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    /**
     * Lấy phòng gốc đang chỉnh sửa
     * @return phòng gốc hoặc null nếu là chế độ thêm mới
     */
    public Room getExistingRoom() {
        return existingRoom;
    }
}
