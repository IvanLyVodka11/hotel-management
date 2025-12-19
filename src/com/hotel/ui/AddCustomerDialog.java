package com.hotel.ui;

import com.hotel.model.customer.Customer;
import com.hotel.service.CustomerManager;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

/**
 * Dialog để thêm khách hàng mới
 */
public class AddCustomerDialog extends JDialog {
    private JTextField idField, nameField, emailField, phoneField, idCardField, addressField;
    private JCheckBox vipCheckBox;
    private CustomerManager customerManager;
    private CustomerPanel parentPanel;
    
    public AddCustomerDialog(CustomerPanel parentPanel, CustomerManager customerManager) {
        super((Frame) SwingUtilities.getWindowAncestor(parentPanel), "Thêm khách hàng", true);
        this.parentPanel = parentPanel;
        this.customerManager = customerManager;
        
        initializeUI();
        setSize(500, 400);
        setLocationRelativeTo(null);
    }
    
    private void initializeUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(8, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // ID
        mainPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        mainPanel.add(idField);
        
        // Name
        mainPanel.add(new JLabel("Họ tên:"));
        nameField = new JTextField();
        mainPanel.add(nameField);
        
        // Email
        mainPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        mainPanel.add(emailField);
        
        // Phone
        mainPanel.add(new JLabel("Số điện thoại:"));
        phoneField = new JTextField();
        mainPanel.add(phoneField);
        
        // ID Card
        mainPanel.add(new JLabel("CMND:"));
        idCardField = new JTextField();
        mainPanel.add(idCardField);
        
        // Address
        mainPanel.add(new JLabel("Địa chỉ:"));
        addressField = new JTextField();
        mainPanel.add(addressField);
        
        // VIP
        mainPanel.add(new JLabel("VIP:"));
        vipCheckBox = new JCheckBox();
        mainPanel.add(vipCheckBox);
        
        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton saveBtn = new JButton("Lưu");
        saveBtn.addActionListener(e -> saveCustomer());
        buttonPanel.add(saveBtn);
        
        JButton cancelBtn = new JButton("Hủy");
        cancelBtn.addActionListener(e -> dispose());
        buttonPanel.add(cancelBtn);
        
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void saveCustomer() {
        if (idField.getText().trim().isEmpty() || nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập ID và Họ tên!", "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Customer customer = new Customer(
                idField.getText().trim(),
                nameField.getText().trim(),
                emailField.getText().trim(),
                phoneField.getText().trim(),
                idCardField.getText().trim(),
                addressField.getText().trim(),
                LocalDate.now(),
                vipCheckBox.isSelected()
        );
        
        if (customerManager.add(customer)) {
            JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
            parentPanel.refreshData();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm khách hàng thất bại!", "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
