package com.hotel.ui;

import com.hotel.model.invoice.Invoice;
import com.hotel.model.invoice.Invoice.InvoiceStatus;
import com.hotel.service.BookingManager;
import com.hotel.service.InvoiceManager;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Panel quản lý hóa đơn với CRUD operations
 * 
 * @author Member2
 * @version 1.0
 */
public class InvoicePanel extends JPanel {
    
    // ==================== CONSTANTS ====================
    
    private static final String[] COLUMN_NAMES = {
            "ID", "Khách hàng", "Ngày lập", "Tổng tiền", "Thuế", "Trạng thái"
    };
    
    // ==================== COMPONENTS ====================
    
    private JTable invoiceTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> statusFilter;
    private JLabel statusLabel;
    
    // ==================== SERVICES ====================
    
    private final InvoiceManager invoiceManager;
    private final BookingManager bookingManager;
    
    // ==================== CONSTRUCTOR ====================
    
    public InvoicePanel() {
        this(new BookingManager(), new InvoiceManager());
    }

    public InvoicePanel(BookingManager bookingManager, InvoiceManager invoiceManager) {
        this.bookingManager = bookingManager;
        this.invoiceManager = invoiceManager;

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
        searchField = new JTextField(15);
        searchField.addActionListener(e -> performSearch());
        searchPanel.add(searchField);
        
        JButton searchBtn = new JButton("Tìm");
        searchBtn.addActionListener(e -> performSearch());
        searchPanel.add(searchBtn);
        
        // Status filter
        searchPanel.add(new JLabel("| Trạng thái:"));
        statusFilter = new JComboBox<>(new String[]{"Tất cả", "Nháp", "Đã phát hành", "Đã thanh toán", "Hủy bỏ"});
        statusFilter.addActionListener(e -> filterByStatus());
        searchPanel.add(statusFilter);
        
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
        
        invoiceTable = new JTable(tableModel);
        invoiceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        invoiceTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        JScrollPane scrollPane = new JScrollPane(invoiceTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton createBtn = new JButton("➕ Tạo từ đặt phòng");
        createBtn.addActionListener(e -> createFromBooking());
        buttonPanel.add(createBtn);
        
        JButton markIssuedBtn = new JButton("🏷️ Phát hành");
        markIssuedBtn.addActionListener(e -> markAsIssued());
        buttonPanel.add(markIssuedBtn);
        
        JButton markPaidBtn = new JButton("✓ Thanh toán");
        markPaidBtn.addActionListener(e -> markAsPaid());
        buttonPanel.add(markPaidBtn);
        
        JButton cancelBtn = new JButton("❌ Hủy");
        cancelBtn.addActionListener(e -> cancelInvoice());
        buttonPanel.add(cancelBtn);
        
        JButton reportBtn = new JButton("📊 Báo cáo");
        reportBtn.addActionListener(e -> showReports());
        buttonPanel.add(reportBtn);
        
        JButton refreshBtn = new JButton("🔄 Làm mới");
        refreshBtn.addActionListener(e -> loadData());
        buttonPanel.add(refreshBtn);
        
        // Status label
        statusLabel = new JLabel("Tổng: 0 hóa đơn");
        
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(buttonPanel, BorderLayout.CENTER);
        southPanel.add(statusLabel, BorderLayout.SOUTH);
        
        return southPanel;
    }
    
    // ==================== DATA OPERATIONS ====================
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<Invoice> invoices = invoiceManager.getAll();
        
        for (Invoice invoice : invoices) {
            Object[] row = {
                    invoice.getInvoiceId(),
                    invoice.getBooking().getCustomer().getFullName(),
                    invoice.getInvoiceDate(),
                    String.format("%,d", (long) invoice.getAmountBeforeTax()),
                    String.format("%,d", (long) invoice.getTaxAmount()),
                    getStatusText(invoice.getStatus())
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
        
        // Search by invoice ID hoặc customer name
        List<Invoice> results = invoiceManager.getAll().stream()
                .filter(inv -> inv.getInvoiceId().contains(keyword) ||
                        inv.getBooking().getCustomer().getFullName().contains(keyword))
                .toList();
        
        for (Invoice invoice : results) {
            Object[] row = {
                    invoice.getInvoiceId(),
                    invoice.getBooking().getCustomer().getFullName(),
                    invoice.getInvoiceDate(),
                    String.format("%,d", (long) invoice.getAmountBeforeTax()),
                    String.format("%,d", (long) invoice.getTaxAmount()),
                    getStatusText(invoice.getStatus())
            };
            tableModel.addRow(row);
        }
        
        statusLabel.setText("Tìm được: " + results.size() + " hóa đơn");
    }
    
    private void filterByStatus() {
        int selectedIndex = statusFilter.getSelectedIndex();
        if (selectedIndex == 0) {
            loadData();
            return;
        }
        
        InvoiceStatus status = InvoiceStatus.values()[selectedIndex];
        tableModel.setRowCount(0);
        Map<String, Object> filter = new HashMap<>();
        filter.put("status", status);
        
        List<Invoice> filtered = invoiceManager.getAll().stream()
                .filter(inv -> inv.getStatus() == status)
                .toList();
        
        for (Invoice invoice : filtered) {
            Object[] row = {
                    invoice.getInvoiceId(),
                    invoice.getBooking().getCustomer().getFullName(),
                    invoice.getInvoiceDate(),
                    String.format("%,d", (long) invoice.getAmountBeforeTax()),
                    String.format("%,d", (long) invoice.getTaxAmount()),
                    getStatusText(invoice.getStatus())
            };
            tableModel.addRow(row);
        }
        
        statusLabel.setText("Trạng thái: " + filtered.size() + " hóa đơn");
    }
    
    private void updateStatus() {
        int total = invoiceManager.getTotalInvoices();
        statusLabel.setText("Tổng: " + total + " hóa đơn");
    }
    
    private String getStatusText(InvoiceStatus status) {
        return switch (status) {
            case DRAFT -> "Nháp";
            case ISSUED -> "Đã phát hành";
            case PAID -> "Đã thanh toán";
            case CANCELLED -> "Hủy bỏ";
        };
    }
    
    // ==================== DIALOG OPERATIONS ====================
    
    private void createFromBooking() {
        // Dialog để chọn booking
        JComboBox<String> bookingCombo = new JComboBox<>();
        
        List<String> bookingIds = bookingManager.getAll().stream()
                .map(b -> b.getBookingId() + " - " + b.getCustomer().getFullName())
                .toList();
        
        for (String item : bookingIds) {
            bookingCombo.addItem(item);
        }
        
        int option = JOptionPane.showConfirmDialog(this, bookingCombo, 
                "Chọn đặt phòng để tạo hóa đơn", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION && bookingCombo.getSelectedItem() != null) {
            String bookingId = ((String) bookingCombo.getSelectedItem()).split(" - ")[0];
            Invoice invoice = invoiceManager.createInvoiceFromBooking(
                    bookingManager.getById(bookingId), 
                    "INV-" + System.currentTimeMillis()
            );
            
            if (invoiceManager.add(invoice)) {
                JOptionPane.showMessageDialog(this, "Tạo hóa đơn thành công!");
                loadData();
            }
        }
    }
    
    private void markAsIssued() {
        int selectedRow = invoiceTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Chọn hóa đơn cần phát hành!", "Thông báo", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String invoiceId = (String) tableModel.getValueAt(selectedRow, 0);
        Invoice invoice = invoiceManager.getById(invoiceId);
        
        if (invoice != null && invoice.getStatus() == InvoiceStatus.DRAFT) {
            invoice.markAsIssued();
            if (invoiceManager.update(invoice)) {
                JOptionPane.showMessageDialog(this, "Phát hành thành công!");
                loadData();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Chỉ có thể phát hành hóa đơn nháp!", "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void markAsPaid() {
        int selectedRow = invoiceTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Chọn hóa đơn cần thanh toán!", "Thông báo", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String invoiceId = (String) tableModel.getValueAt(selectedRow, 0);
        Invoice invoice = invoiceManager.getById(invoiceId);
        
        if (invoice != null && invoice.getStatus() == InvoiceStatus.ISSUED) {
            invoice.markAsPaid();
            if (invoiceManager.update(invoice)) {
                JOptionPane.showMessageDialog(this, "Thanh toán thành công!");
                loadData();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Chỉ có thể thanh toán hóa đơn đã phát hành!", "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cancelInvoice() {
        int selectedRow = invoiceTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Chọn hóa đơn cần hủy!", "Thông báo", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String invoiceId = (String) tableModel.getValueAt(selectedRow, 0);
        Invoice invoice = invoiceManager.getById(invoiceId);
        
        if (invoice != null && invoice.getStatus() != InvoiceStatus.CANCELLED) {
            int option = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn hủy hóa đơn này?", 
                    "Xác nhận hủy", JOptionPane.YES_NO_OPTION);
            
            if (option == JOptionPane.YES_OPTION) {
                invoice.cancel();
                if (invoiceManager.update(invoice)) {
                    JOptionPane.showMessageDialog(this, "Hủy thành công!");
                    loadData();
                }
            }
        }
    }
    
    private void showReports() {
        double totalRevenue = invoiceManager.getTotalRevenue();
        double totalTax = invoiceManager.getTotalTax();
        double unpaidRevenue = invoiceManager.getUnpaidRevenue();
        int totalInvoices = invoiceManager.getTotalInvoices();
        
        String report = String.format(
                "📊 BÁO CÁO HÓA ĐƠN\n\n" +
                "Tổng hóa đơn: %d\n" +
                "Tổng doanh thu: %,.0f VND\n" +
                "Tổng thuế: %,.0f VND\n" +
                "Chưa thanh toán: %,.0f VND\n\n" +
                "Tỉ lệ thanh toán: %.1f%%",
                totalInvoices,
                totalRevenue,
                totalTax,
                unpaidRevenue,
                totalRevenue > 0 ? ((totalRevenue - unpaidRevenue) * 100.0 / totalRevenue) : 0
        );
        
        JOptionPane.showMessageDialog(this, report, "Báo cáo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // ==================== PUBLIC METHODS ====================
    
    public void refreshData() {
        loadData();
    }

    public void showReportDialogFromMenu() {
        showReports();
    }
}
