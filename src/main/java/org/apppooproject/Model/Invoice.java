package main.java.SalesManager;

import java.util.Date;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

public class Invoice {

    private long invoiceId;         
    private long customerId;        
    private double totalPrice;      
    private Date invoiceDate;       
    private boolean paymentStatus; 


    public Invoice(long invoiceId, long customerId, double totalPrice, Date invoiceDate) {
        this.invoiceId = invoiceId;
        this.customerId = customerId;
        this.totalPrice = totalPrice;
        this.invoiceDate = invoiceDate;
        this.paymentStatus = false; 
    }

    public void generateInvoice() {
        System.out.println("Generating invoice for Customer ID: " + customerId);
        System.out.println("Invoice ID: " + invoiceId);
        System.out.println("Total Amount: " + totalPrice);
        System.out.println("Invoice Date: " + invoiceDate);
    }

    public void displayInvoice() {
        System.out.println("Invoice Details:");
        System.out.println("Invoice ID: " + invoiceId);
        System.out.println("Customer ID: " + customerId);
        System.out.println("Total Amount: " + totalPrice);
        System.out.println("Invoice Date: " + invoiceDate);
        System.out.println("Payment Validated: " + (paymentStatus ? "Yes" : "No"));
    }


    public void validatePayment() {
        this.paymentStatus = true; 
        System.out.println("Payment for Invoice ID " + invoiceId + " has been validated.");
    }


    public long getInvoiceId() {
        return invoiceId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public boolean isPaymentValidated() {
        return paymentStatus;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }
}
