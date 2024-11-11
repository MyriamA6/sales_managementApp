package org.apppooproject.DataBaseManagers;

import org.apppooproject.Model.Customer;
import org.apppooproject.Model.Invoice;
import org.apppooproject.Model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class InvoiceManager {
    private Connection co;

    // Constructor to initialize the database connection
    public InvoiceManager(Connection co) {
        this.co = co;
    }

    // Add an invoice to the database
    public void addAnElement(Invoice invoice) {
        try {
            String sql = "INSERT INTO Invoice (order_id, invoice_date) VALUES (?, ?)";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setLong(1, invoice.getOrderId());  // Corrected from customerId to orderId
            stmt.setDate(2, new java.sql.Date(invoice.getInvoiceDate().getTime()));

            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Modify an existing invoice
    public void modifyAnElement(Invoice invoice) {
        try {
            String sql = "UPDATE Invoice SET order_id = ?, invoice_date = ? WHERE invoice_id = ?";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setLong(1, invoice.getOrderId());
            stmt.setDate(2, new java.sql.Date(invoice.getInvoiceDate().getTime()));
            stmt.setLong(3, invoice.getInvoiceId());

            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Delete an invoice from the database
    public void deleteAnElement(Invoice invoice) {
        try {
            String sql = "DELETE FROM Invoice WHERE invoice_id = ?";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setLong(1, invoice.getInvoiceId());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Get all invoices from the database
    public List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Invoice";
            Statement stmt = co.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setId(rs.getLong("invoice_id"));
                invoice.setOrderId(rs.getLong("order_id"));  // Corrected column name
                invoice.setInvoiceDate(rs.getDate("invoice_date"));
                invoice.setTotalAmount(rs.getDouble("total_price"));  // Corrected column name
                invoices.add(invoice);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return invoices;
    }

    // Get a specific invoice by its ID
    public Invoice getInvoiceById(long invoiceId) {
        Invoice invoice = null;
        try {
            String sql = "SELECT * FROM Invoice WHERE invoice_id = ?";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setLong(1, invoiceId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                invoice = new Invoice();
                invoice.setId(rs.getLong("invoice_id"));
                invoice.setOrderId(rs.getLong("order_id"));
                invoice.setInvoiceDate(rs.getDate("invoice_date"));
                invoice.setTotalAmount(rs.getDouble("total_price"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return invoice;
    }

    /*// Generate an invoice document in a specified file
    public void generateInvoice(Order order, Customer customer, List<OrderContent> orderContents, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String orderDate = dateFormat.format(order.getOrderDate());

            // Writing invoice details to the file
            writer.write("Invoice\n");
            writer.write(String.format("%50s\n", "Order No: " + order.getOrderId()));
            writer.write(String.format("%50s\n", "Date: " + orderDate));
            writer.write("\n");

            writer.write(String.format("%-50s\n", "Customer: " + customer.getFirstName() + " " + customer.getLastName()));
            writer.write(String.format("%-50s\n", "Address: " + customer.getAddress()));
            writer.write("\n");

            writer.write(String.format("%-30s %-15s %-10s %-10s\n", "Description", "Price", "QTY", "Total"));
            writer.write("----------------------------------------------------------\n");

            double totalOrder = 0.0;
            for (OrderContent content : orderContents) {
                double itemTotal = content.getPrice() * content.getQuantity();
                totalOrder += itemTotal;
                writer.write(String.format("%-30s %-15s %-10d %-10s\n", content.getProductName(),
                        content.getPrice(), content.getQuantity(), itemTotal));
            }

            writer.write("\n");
            writer.write(String.format("%45s: %.2f\n", "Total Order", totalOrder));

            System.out.println("Invoice generated successfully at " + filePath);

        } catch (IOException e) {
            System.out.println("Error generating invoice: " + e.getMessage());
        }
    }*/
}
