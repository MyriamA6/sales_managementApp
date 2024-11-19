package org.apppooproject.Model;

import java.util.Date;
import java.util.List;

public class Invoice {

    private long id;
    private long customerId;
    private int totalPrice;
    private Date dateInvoice;

    public Invoice(long id, long customerId, int totalPrice, Date dateInvoice) {
        this.id = id;
        this.customerId = customerId;
        this.totalPrice = totalPrice;
        this.dateInvoice = dateInvoice;
    }

    public long getId() {
        return id;
    }

    public long getCustomerId() {
        return customerId;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getDateInvoice() {
        return dateInvoice;
    }

    public void setDateInvoice(Date dateInvoice) {
        this.dateInvoice = dateInvoice;
    }


    public void generateInvoice(Order order) {
        String invoiceText = "Invoice\n";
        invoiceText += "Order Number: " + order.getOrderId() + "\n";
        invoiceText += "Date: " + order.getDateCommande() + "\n\n";


        Customer customer = order.getCustomer();
        invoiceText += "Customer: " + customer.getFirstName() + " " + customer.getLastName() + "\n";
        invoiceText += "Address: " + customer.getAddress() + "\n\n";


        invoiceText += String.format("%-20s %-10s %-5s %-10s\n", "Description", "Price", "QTY", "Total");
        for (Product product : order.getProductList()) {
            int totalPriceForProduct = product.getPrice() * product.getQuantity();
            invoiceText += String.format("%-20s %-10d %-5d %-10d\n", product.getName(), product.getPrice(), product.getQuantity(), totalPriceForProduct);
        }


        invoiceText += "\nTotal Order: " + order.calculateTotalPrice() + "€\n";


        try (BufferedWriter writer = new BufferedWriter(new FileWriter("invoice_" + this.id + ".txt"))) {
            writer.write(invoiceText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void displayInvoice(Order order) {
        System.out.println("Invoice");
        System.out.println("Order Number: " + order.getOrderId());
        System.out.println("Date: " + order.getDateCommande());
        System.out.println("\n");


        Customer customer = order.getCustomer();
        System.out.println("Customer: " + customer.getFirstName() + " " + customer.getLastName());
        System.out.println("Address: " + customer.getAddress());
        System.out.println("\n");


        System.out.printf("%-20s %-10s %-5s %-10s\n", "Description", "Price", "QTY", "Total");
        for (Product product : order.getProductList()) {
            int totalPriceForProduct = product.getPrice() * product.getQuantity();
            System.out.printf("%-20s %-10d %-5d %-10d\n", product.getName(), product.getPrice(), product.getQuantity(), totalPriceForProduct);
        }


        System.out.println("\nTotal Order: " + order.calculateTotalPrice() + "€");
    }
}
