package org.apppooproject.DataBaseManagers;

import org.apppooproject.Model.Invoice;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.util.Date;

public class InvoiceManager {
    private Connection co;

    public InvoiceManager(Connection co) {
        this.co = co;
    }

    public void addAnElement(Invoice invoice) {
        try {
            String sql = "INSERT INTO Invoices (customer_id, invoice_date, total_amount, status) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setLong(1, invoice.getCustomerId());
            stmt.setDate(2, new java.sql.Date(invoice.getInvoiceDate().getTime()));
            stmt.setDouble(3, invoice.getTotalAmount());
            stmt.setString(4, invoice.getStatus().toString());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void modifyAnElement(Invoice invoice) {
        try {
            String sql = "UPDATE Invoices SET customer_id = ?, invoice_date = ?, total_amount = ?, status = ? WHERE id = ?";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setLong(1, invoice.getCustomerId());
            stmt.setDate(2, new java.sql.Date(invoice.getInvoiceDate().getTime()));
            stmt.setDouble(3, invoice.getTotalAmount());
            stmt.setString(4, invoice.getStatus().toString());
            stmt.setLong(5, invoice.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteAnElement(Invoice invoice) {
        try {
            String sql = "DELETE FROM Invoices WHERE id = ?";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setLong(1, invoice.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Invoices";
            Statement stmt = co.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setId(rs.getLong("id"));
                invoice.setCustomerId(rs.getLong("customer_id"));
                invoice.setInvoiceDate(rs.getDate("invoice_date"));
                invoice.setTotalAmount(rs.getDouble("total_amount"));
                invoice.setStatus(InvoiceStatus.valueOf(rs.getString("status")));
                invoices.add(invoice);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return invoices;
    }

    public Invoice getInvoiceById(long invoiceId) {
        Invoice invoice = null;
        try {
            String sql = "SELECT * FROM Invoices WHERE id = ?";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setLong(1, invoiceId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                invoice = new Invoice();
                invoice.setId(rs.getLong("id"));
                invoice.setCustomerId(rs.getLong("customer_id"));
                invoice.setInvoiceDate(rs.getDate("invoice_date"));
                invoice.setTotalAmount(rs.getDouble("total_amount"));
                invoice.setStatus(InvoiceStatus.valueOf(rs.getString("status")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return invoice;
    }
}
