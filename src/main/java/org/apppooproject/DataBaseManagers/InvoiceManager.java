package org.apppooproject.DataBaseManagers;

import org.apppooproject.Model.Invoice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class InvoiceManager implements DataManager<Invoice> {
    Scanner scan = new Scanner(System.in);
    Connection co;
    // Step 1: Private static instance
    private static InvoiceManager instance;

    // Step 2: Private constructor
    private InvoiceManager() {
        // Initialization, if needed
    }

    // Step 3: Static method to get the instance
    public static InvoiceManager getInstance() {
        if (instance == null) {
            instance = new InvoiceManager();
        }
        return instance;
    }

    public void addAnElement(Invoice invoice) {
        try {
            String invoiceDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));

            String sql = "INSERT INTO Invoice(order_id, invoice_date) VALUES (?, ?)";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setLong(1, invoice.getOrderId());
            stmt.setDate(2, java.sql.Date.valueOf(invoiceDate));

            stmt.executeUpdate();
            System.out.println("Invoice added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding invoice: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void modifyAnElement(Invoice invoice) {
        try {
            System.out.println("Enter the ID of the invoice you want to modify: ");
            long invoiceId = scan.nextLong();
            scan.nextLine();

            System.out.println("Which field do you want to modify?");
            System.out.println("1. Order ID");
            System.out.println("2. Invoice Date");
            System.out.println("Enter the numbers of the fields you want to modify (comma-separated, e.g., 1,2):");

            String choices = scan.nextLine();
            String[] choicesToModify = choices.split(",");

            StringBuilder sql = new StringBuilder("UPDATE Invoice SET ");
            boolean firstField = true;

            String newOrderId = null, newInvoiceDate = null;

            for (String choice : choicesToModify) {
                switch (choice.trim()) {
                    case "1":
                        System.out.println("Enter new Order ID: ");
                        newOrderId = scan.nextLine();
                        if (!firstField) sql.append(", ");
                        sql.append("order_id = ?");
                        firstField = false;
                        break;
                    case "2":
                        System.out.println("Enter new Invoice Date (YYYY-MM-DD): ");
                        newInvoiceDate = scan.nextLine();
                        if (!firstField) sql.append(", ");
                        sql.append("invoice_date = ?");
                        firstField = false;
                        break;
                    default:
                        System.out.println("Invalid option: " + choice);
                        break;
                }
            }

            sql.append(" WHERE invoice_id = ?");

            PreparedStatement stmt = co.prepareStatement(sql.toString());

            int paramIndex = 1;
            if (newOrderId != null) {
                stmt.setLong(paramIndex++, Long.parseLong(newOrderId));
            }
            if (newInvoiceDate != null) {
                stmt.setDate(paramIndex++, java.sql.Date.valueOf(newInvoiceDate));
            }

            stmt.setLong(paramIndex, invoiceId);

            int res = stmt.executeUpdate();
            if (res > 0) {
                System.out.println("Invoice updated successfully.");
            } else {
                System.out.println("No invoice found with the given ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error while updating invoice: " + e.getMessage());
        }
    }

    @Override
    public void deleteAnElement(Invoice invoice) {
        try {
            System.out.println("Enter the ID of the invoice you want to delete: ");
            long invoiceId = scan.nextLong();
            scan.nextLine();

            String sql = "DELETE FROM Invoice WHERE invoice_id = ?";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setLong(1, invoiceId);

            int res = stmt.executeUpdate();
            if (res > 0) {
                System.out.println("Invoice deleted successfully.");
            } else {
                System.out.println("No invoice found with the given ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error while deleting invoice: " + e.getMessage());
        }
    }

}
