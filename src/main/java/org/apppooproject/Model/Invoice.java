package org.apppooproject.Model;

import java.util.Date;


public class Invoice {

    private long invoiceId;         
    private long customerId;        
    private double totalPrice;      
    private Date invoiceDate;       
    private long orderId;

    public Invoice() {}



    public Invoice(long orderId,long invoiceId, long customerId, double totalPrice, Date invoiceDate) {
        this.orderId = orderId;
        this.invoiceId = invoiceId;
        this.customerId = customerId;
        this.totalPrice = totalPrice;
        this.invoiceDate = invoiceDate;
    }

    public Invoice(long orderId, long customerId, Date invoiceDate) {
        this.orderId = orderId;
        this.invoiceId = invoiceId;
        this.customerId = customerId;
        this.totalPrice = totalPrice;
        this.invoiceDate = invoiceDate;
    }

    /*public void generateInvoice() {
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
    }*/

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

    public long getOrderId() { return orderId;}

    public void setInvoiceId(long invoiceId) { this.invoiceId = invoiceId; }

    public void setOrderId(long orderId) { this.orderId = orderId; }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }
    
    public void setId(long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalPrice = totalAmount;
    }

}
