package com.hotel.ui;

import com.hotel.model.customer.Customer;
import com.hotel.service.CustomerManager;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog để sửa thông tin khách hàng
 */
public class EditCustomerDialog extends JDialog {
    private JTextField nameField, emailField, phoneField, idCardField, addressField;
    private JCheckBox vipCheckBox;
    private CustomerManager customerManager;
    private CustomerPanel parentPanel;
    private Customer customer;
    
    public EditCustomerDialog(CustomerPanel parentPanel, CustomerManager customerManager, Customer customer) {
        super((Frame) SwingUtilities.getWindowAncestor(parentPanel), "Sửa khách hàng", true);
        this.parentPanel = parentPanel;
        this.customerManager = customerManager;
        this.customer = customer;
        
        initializeUI();
        setSize(500, 350);
        setLocationRelativeTo(null);
    }
    
    private void initializeUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(7, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // ID (read-only)
        mainPanel.add(new JLabel("ID:"));
        JTextField idField = new JTextField(customer.getCustomerId());
        idField.setEditable(false);
        mainPanel.add(idField);
        
        // Name
        mainPanel.add(new JLabel("Họ tên:"));
        nameField = new JTextField(customer.getFullName());
        mainPanel.add(nameField);
        
        // Email
        mainPanel.add(new JLabel("Email:"));
        emailField = new JTextField(customer.getEmail());
        mainPanel.add(emailField);
        
        // Phone
        mainPanel.add(new JLabel("Số điện thoại:"));
        phoneField = new JTextField(customer.getPhoneNumber());
        mainPanel.add(phoneField);
        
        // ID Card
        mainPanel.add(new JLabel("CMND:"));
        idCardField = new JTextField(customer.getIdCard());
        mainPanel.add(idCardField);
        
        // Address
        mainPanel.add(new JLabel("Địa chỉ:"));
        addressField = new JTextField(customer.getAddress());
        mainPanel.add(addressField);
        
        // VIP
        mainPanel.add(new JLabel("VIP:"));
        vipCheckBox = new JCheckBox();
        vipCheckBox.setSelected(customer.isVIP());
        mainPanel.add(vipCheckBox);
        
        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton saveBtn = new JButton("Lưu");
        saveBtn.addActionListener(e -> updateCustomer());
        buttonPanel.add(saveBtn);
        
        JButton cancelBtn = new JButton("Hủy");
        cancelBtn.addActionListener(e -> dispose());
        buttonPanel.add(cancelBtn);
        
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void updateCustomer() {
        customer.setFullName(nameField.getText().trim());
        customer.setEmail(emailField.getText().trim());
        customer.setPhoneNumber(phoneField.getText().trim());
        customer.setIdCard(idCardField.getText().trim());
        customer.setAddress(addressField.getText().trim());
        customer.setVIP(vipCheckBox.isSelected());
        
        if (customerManager.update(customer)) {
            JOptionPane.showMessageDialog(this, "Cập nhật khách hàng thành công!");
            parentPanel.refreshData();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật khách hàng thất bại!", "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
