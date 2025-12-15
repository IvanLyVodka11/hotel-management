package com.hotel.model.invoice;

import com.hotel.model.booking.Booking;
import com.hotel.model.customer.Customer;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Lớp đại diện cho hóa đơn
 * Contains invoice details for a booking
 */
public class Invoice {
    private String invoiceId;
    private Booking booking;
    private LocalDate invoiceDate;
    private double subtotal;
    private double taxRate;
    private double taxAmount;
    private double totalAmount;
    private InvoiceStatus status;
    private String notes;

    /**
     * Enum để đại diện cho trạng thái hóa đơn
     */
    public enum InvoiceStatus {
        DRAFT("Nháp"),
        ISSUED("Đã phát hành"),
        PAID("Đã thanh toán"),
        CANCELLED("Đã hủy");

        private final String displayName;

        InvoiceStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /**
     * Constructor đầy đủ thông tin
     */
    public Invoice(String invoiceId, Booking booking, LocalDate invoiceDate, double taxRate) {
        this.invoiceId = invoiceId;
        this.booking = booking;
        this.invoiceDate = invoiceDate;
        this.taxRate = taxRate;
        this.status = InvoiceStatus.DRAFT;
        this.notes = "";
        calculateAmounts();
    }

    /**
     * Tính toán các khoản tiền
     */
    private void calculateAmounts() {
        if (booking != null) {
            this.subtotal = booking.getTotalPrice();
            this.taxAmount = subtotal * taxRate;
            this.totalAmount = subtotal + taxAmount;
        }
    }

    /**
     * Cập nhật trạng thái thành "Đã thanh toán"
     */
    public void markAsPaid() {
        this.status = InvoiceStatus.PAID;
    }

    /**
     * Cập nhật trạng thái thành "Đã phát hành"
     */
    public void markAsIssued() {
        this.status = InvoiceStatus.ISSUED;
    }

    /**
     * Hủy hóa đơn
     */
    public void cancel() {
        this.status = InvoiceStatus.CANCELLED;
    }

    // Getters and Setters
    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
        calculateAmounts();
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
        calculateAmounts();
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getAmountBeforeTax() {
        return subtotal;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Customer getCustomer() {
        return booking != null ? booking.getCustomer() : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invoice invoice = (Invoice) o;
        return Objects.equals(invoiceId, invoice.invoiceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invoiceId);
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceId='" + invoiceId + '\'' +
                ", booking=" + booking.getBookingId() +
                ", invoiceDate=" + invoiceDate +
                ", subtotal=" + subtotal +
                ", taxAmount=" + taxAmount +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                '}';
    }
}
