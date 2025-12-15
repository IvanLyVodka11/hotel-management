package com.hotel.service;

import com.hotel.model.booking.Booking;
import com.hotel.model.invoice.Invoice;
import com.hotel.model.invoice.Invoice.InvoiceStatus;
import com.hotel.service.interfaces.IManageable;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Lớp quản lý hóa đơn
 * Handles CRUD operations for invoices
 */
public class InvoiceManager implements IManageable<Invoice> {
    private List<Invoice> invoices;
    private static final double DEFAULT_TAX_RATE = 0.1; // 10%

    public InvoiceManager() {
        this.invoices = new ArrayList<>();
    }

    public InvoiceManager(BookingManager bookingManager) {
        this.invoices = new ArrayList<>();
    }

    @Override
    public boolean add(Invoice invoice) {
        if (invoice != null) {
            // Check if invoice with same ID doesn't exist
            boolean exists = invoices.stream().anyMatch(inv -> inv.getInvoiceId().equals(invoice.getInvoiceId()));
            if (!exists) {
                return invoices.add(invoice);
            }
        }
        return false;
    }

    @Override
    public boolean update(Invoice invoice) {
        if (invoice != null) {
            for (int i = 0; i < invoices.size(); i++) {
                if (invoices.get(i).getInvoiceId().equals(invoice.getInvoiceId())) {
                    invoices.set(i, invoice);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean delete(String invoiceId) {
        return invoices.removeIf(i -> i.getInvoiceId().equals(invoiceId));
    }

    @Override
    public Invoice getById(String invoiceId) {
        return invoices.stream()
                .filter(i -> i.getInvoiceId().equals(invoiceId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Invoice> getAll() {
        return new ArrayList<>(invoices);
    }

    /**
     * Tạo hóa đơn từ đặt phòng
     */
    public Invoice createInvoiceFromBooking(Booking booking, String invoiceId) {
        Invoice invoice = new Invoice(invoiceId, booking, LocalDate.now(), DEFAULT_TAX_RATE);
        add(invoice);
        return invoice;
    }

    /**
     * Lấy hóa đơn theo booking
     */
    public Invoice getInvoiceByBooking(String bookingId) {
        return invoices.stream()
                .filter(i -> i.getBooking().getBookingId().equals(bookingId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Lấy hóa đơn theo khách hàng
     */
    public List<Invoice> getInvoicesByCustomer(String customerId) {
        return invoices.stream()
                .filter(i -> i.getCustomer() != null && i.getCustomer().getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }

    /**
     * Lấy hóa đơn theo trạng thái
     */
    public List<Invoice> getInvoicesByStatus(InvoiceStatus status) {
        return invoices.stream()
                .filter(i -> i.getStatus() == status)
                .collect(Collectors.toList());
    }

    /**
     * Lấy hóa đơn trong khoảng thời gian
     */
    public List<Invoice> getInvoicesByDateRange(LocalDate startDate, LocalDate endDate) {
        return invoices.stream()
                .filter(i -> !i.getInvoiceDate().isBefore(startDate) &&
                        !i.getInvoiceDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    /**
     * Tính tổng doanh thu theo tất cả hóa đơn
     */
    public double getTotalRevenue() {
        return invoices.stream()
                .filter(i -> i.getStatus() == InvoiceStatus.PAID)
                .mapToDouble(Invoice::getTotalAmount)
                .sum();
    }

    /**
     * Tính tổng thuế đã thu
     */
    public double getTotalTax() {
        return invoices.stream()
                .filter(i -> i.getStatus() == InvoiceStatus.PAID)
                .mapToDouble(Invoice::getTaxAmount)
                .sum();
    }

    /**
     * Tính tổng doanh thu chưa thanh toán
     */
    public double getUnpaidRevenue() {
        return invoices.stream()
                .filter(i -> i.getStatus() != InvoiceStatus.PAID)
                .mapToDouble(Invoice::getTotalAmount)
                .sum();
    }

    /**
     * Đánh dấu hóa đơn là đã thanh toán
     */
    public void markInvoiceAsPaid(String invoiceId) {
        Invoice invoice = getById(invoiceId);
        if (invoice != null) {
            invoice.markAsPaid();
            update(invoice);
        }
    }

    /**
     * Hủy hóa đơn
     */
    public void cancelInvoice(String invoiceId) {
        Invoice invoice = getById(invoiceId);
        if (invoice != null) {
            invoice.cancel();
            update(invoice);
        }
    }

    /**
     * Tính tổng doanh thu trong tháng
     */
    public double getMonthlyRevenue(int month, int year) {
        return invoices.stream()
                .filter(i -> i.getStatus() == InvoiceStatus.PAID &&
                        i.getInvoiceDate().getMonth().getValue() == month &&
                        i.getInvoiceDate().getYear() == year)
                .mapToDouble(Invoice::getTotalAmount)
                .sum();
    }

    @Override
    public int count() {
        return invoices.size();
    }
    
    @Override
    public boolean isEmpty() {
        return invoices.isEmpty();
    }
    
    @Override
    public void clear() {
        invoices.clear();
    }

    public int getTotalInvoices() {
        return invoices.size();
    }

    public int getPaidInvoices() {
        return (int) invoices.stream().filter(i -> i.getStatus() == InvoiceStatus.PAID).count();
    }

    public int getUnpaidInvoices() {
        return (int) invoices.stream().filter(i -> i.getStatus() != InvoiceStatus.PAID).count();
    }
}
