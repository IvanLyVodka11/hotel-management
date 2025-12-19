package com.hotel.ui;

import com.hotel.model.customer.Customer;

import javax.swing.*;
import java.awt.*;


/**
 * Dialog hiển thị chi tiết khách hàng
 */
public class CustomerDetailDialog extends JDialog {
    
    public CustomerDetailDialog(CustomerPanel parentPanel, Customer customer) {
        super((Frame) SwingUtilities.getWindowAncestor(parentPanel), "Chi tiết khách hàng", true);
        
        initializeUI(customer);
        setSize(500, 400);
        setLocationRelativeTo(null);
    }
    
    private void initializeUI(Customer customer) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(8, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        addLabeledField(mainPanel, "ID:", customer.getCustomerId());
        addLabeledField(mainPanel, "Họ tên:", customer.getFullName());
        addLabeledField(mainPanel, "Email:", customer.getEmail());
        addLabeledField(mainPanel, "Số điện thoại:", customer.getPhoneNumber());
        addLabeledField(mainPanel, "CMND:", customer.getIdCard());
        addLabeledField(mainPanel, "Địa chỉ:", customer.getAddress());
        addLabeledField(mainPanel, "Trạng thái VIP:", customer.isVIP() ? "✓ Có" : "✗ Không");
        addLabeledField(mainPanel, "Điểm tích lũy:", String.format("%.0f", customer.getLoyaltyPoints()));
        
        // Close button
        JPanel buttonPanel = new JPanel();
        JButton closeBtn = new JButton("Đóng");
        closeBtn.addActionListener(e -> dispose());
        buttonPanel.add(closeBtn);
        
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void addLabeledField(JPanel panel, String label, String value) {
        panel.add(new JLabel(label));
        JTextField field = new JTextField(value);
        field.setEditable(false);
        panel.add(field);
    }
}
