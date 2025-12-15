package com.hotel.ui;

import com.hotel.model.customer.Customer;
import com.hotel.service.CustomerManager;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Panel quản lý khách hàng với CRUD operations
 * 
 * @author Member2
 * @version 1.0
 */
public class CustomerPanel extends JPanel {
    
    // ==================== CONSTANTS ====================
    
    private static final String[] COLUMN_NAMES = {
            "ID", "Họ tên", "Email", "Số điện thoại", "CMND", "Địa chỉ", "VIP", "Điểm TL"
    };
    
    // ==================== COMPONENTS ====================
    
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JLabel statusLabel;
    
    // ==================== SERVICES ====================
    
    private final CustomerManager customerManager;
    
    // ==================== CONSTRUCTOR ====================
    
    public CustomerPanel() {
        this(new CustomerManager());
    }

    public CustomerPanel(CustomerManager customerManager) {
        this.customerManager = customerManager;

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
        
        // Filter VIP checkbox
        JCheckBox vipCheckBox = new JCheckBox("Chỉ VIP");
        vipCheckBox.addActionListener(e -> {
            if (vipCheckBox.isSelected()) {
                filterVIPCustomers();
            } else {
                loadData();
            }
        });
        searchPanel.add(vipCheckBox);
        
        topPanel.add(searchPanel, BorderLayout.CENTER);
        
        return topPanel;
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        
        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        customerTable = new JTable(tableModel);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        JScrollPane scrollPane = new JScrollPane(customerTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton addBtn = new JButton("➕ Thêm");
        addBtn.addActionListener(e -> openAddDialog());
        buttonPanel.add(addBtn);
        
        JButton editBtn = new JButton("✏️ Sửa");
        editBtn.addActionListener(e -> openEditDialog());
        buttonPanel.add(editBtn);
        
        JButton deleteBtn = new JButton("🗑️ Xóa");
        deleteBtn.addActionListener(e -> deleteCustomer());
        buttonPanel.add(deleteBtn);
        
        JButton viewBtn = new JButton("👁️ Chi tiết");
        viewBtn.addActionListener(e -> viewCustomerDetails());
        buttonPanel.add(viewBtn);
        
        JButton refreshBtn = new JButton("🔄 Làm mới");
        refreshBtn.addActionListener(e -> loadData());
        buttonPanel.add(refreshBtn);
        
        // Status label
        statusLabel = new JLabel("Tổng: 0 khách hàng");
        
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(buttonPanel, BorderLayout.CENTER);
        southPanel.add(statusLabel, BorderLayout.SOUTH);
        
        return southPanel;
    }
    
    // ==================== DATA OPERATIONS ====================
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<Customer> customers = customerManager.getAll();
        
        for (Customer customer : customers) {
            Object[] row = {
                    customer.getCustomerId(),
                    customer.getFullName(),
                    customer.getEmail(),
                    customer.getPhoneNumber(),
                    customer.getIdCard(),
                    customer.getAddress(),
                    customer.isVIP() ? "✓" : "✗",
                    String.format("%.0f", customer.getLoyaltyPoints())
            };
            tableModel.addRow(row);
        }
        
        updateStatus();
    }
    
    private void performSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadData();
            return;
        }
        
        tableModel.setRowCount(0);
        List<Customer> results = customerManager.search(keyword);
        
        for (Customer customer : results) {
            Object[] row = {
                    customer.getCustomerId(),
                    customer.getFullName(),
                    customer.getEmail(),
                    customer.getPhoneNumber(),
                    customer.getIdCard(),
                    customer.getAddress(),
                    customer.isVIP() ? "✓" : "✗",
                    String.format("%.0f", customer.getLoyaltyPoints())
            };
            tableModel.addRow(row);
        }
        
        statusLabel.setText("Tìm được: " + results.size() + " khách hàng");
    }
    
    private void filterVIPCustomers() {
        tableModel.setRowCount(0);
        Map<String, Object> filter = new HashMap<>();
        filter.put("vip", true);
        
        List<Customer> vipCustomers = customerManager.filter(filter);
        
        for (Customer customer : vipCustomers) {
            Object[] row = {
                    customer.getCustomerId(),
                    customer.getFullName(),
                    customer.getEmail(),
                    customer.getPhoneNumber(),
                    customer.getIdCard(),
                    customer.getAddress(),
                    "✓",
                    String.format("%.0f", customer.getLoyaltyPoints())
            };
            tableModel.addRow(row);
        }
        
        statusLabel.setText("VIP: " + vipCustomers.size() + " khách hàng");
    }
    
    private void updateStatus() {
        int total = customerManager.getTotalCustomers();
        int vip = customerManager.getVIPCustomers();
        statusLabel.setText(String.format("Tổng: %d khách hàng (VIP: %d)", total, vip));
    }
    
    // ==================== DIALOG OPERATIONS ====================
    
    private void openAddDialog() {
        new AddCustomerDialog(this, customerManager).setVisible(true);
    }
    
    private void openEditDialog() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Chọn khách hàng cần sửa!", "Thông báo", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String customerId = (String) tableModel.getValueAt(selectedRow, 0);
        Customer customer = customerManager.getById(customerId);
        
        if (customer != null) {
            new EditCustomerDialog(this, customerManager, customer).setVisible(true);
        }
    }
    
    private void deleteCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Chọn khách hàng cần xóa!", "Thông báo", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String customerId = (String) tableModel.getValueAt(selectedRow, 0);
        String customerName = (String) tableModel.getValueAt(selectedRow, 1);
        
        int option = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc muốn xóa khách hàng '" + customerName + "'?", 
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            if (customerManager.delete(customerId)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void viewCustomerDetails() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Chọn khách hàng cần xem!", "Thông báo", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String customerId = (String) tableModel.getValueAt(selectedRow, 0);
        Customer customer = customerManager.getById(customerId);
        
        if (customer != null) {
            new CustomerDetailDialog(this, customer).setVisible(true);
        }
    }
    
    // ==================== PUBLIC METHODS ====================
    
    public void refreshData() {
        loadData();
    }
}
