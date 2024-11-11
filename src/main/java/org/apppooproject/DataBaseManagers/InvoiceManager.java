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


public class InvoiceManager implements DataManager {

    private static InvoiceManager instance;
    private Connection co;

    public static InvoiceManager getInstance() {
        if (instance == null) {
            instance = new InvoiceManager();
        }
        return instance;
    }

    @Override
    public void addAnElement(Connection co, Invoice invoice) {
        this.co = co;
        String query = "INSERT INTO invoices (customer_id, total_price, date_invoice, status) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement stmt = co.prepareStatement(query);
            stmt.setLong(1, invoice.getCustomerId());
            stmt.setInt(2, invoice.calculateTotalPrice());
            stmt.setDate(3, new java.sql.Date(invoice.getDateInvoice().getTime()));
            stmt.setString(4, invoice.getStatus().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifyAnElement(Connection co, Invoice invoice) {
        this.co = co;
        String query = "UPDATE invoices SET total_price = ?, status = ? WHERE id = ?";
        try {
            PreparedStatement stmt = co.prepareStatement(query);
            stmt.setInt(1, invoice.calculateTotalPrice());
            stmt.setString(2, invoice.getStatus().toString());
            stmt.setLong(3, invoice.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAnElement(Connection co, Invoice invoice) {
        this.co = co;
        String query = "DELETE FROM invoices WHERE id = ?";
        try {
            PreparedStatement stmt = co.prepareStatement(query);
            stmt.setLong(1, invoice.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Invoice> getAllInvoices(Connection co, Customer customer) {
        List<Invoice> invoices = new ArrayList<>();
        String query = "SELECT * FROM invoices WHERE customer_id = ?";
        try {
            PreparedStatement stmt = co.prepareStatement(query);
            stmt.setLong(1, customer.getId());
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                long customerId = resultSet.getLong("customer_id");
                int totalPrice = resultSet.getInt("total_price");
                Date dateInvoice = resultSet.getDate("date_invoice");
                Invoice.Status status = Invoice.Status.valueOf(resultSet.getString("status"));
                Invoice invoice = new Invoice(id, customerId, totalPrice, new Date(dateInvoice.getTime()), status);
                invoices.add(invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }

    public Invoice getInvoiceById(Connection co, int invoiceId) {
        Invoice invoice = null;
        String query = "SELECT * FROM invoices WHERE id = ?";
        try {
            PreparedStatement stmt = co.prepareStatement(query);
            stmt.setInt(1, invoiceId);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                long id = resultSet.getLong("id");
                long customerId = resultSet.getLong("customer_id");
                int totalPrice = resultSet.getInt("total_price");
                Date dateInvoice = resultSet.getDate("date_invoice");
                Invoice.Status status = Invoice.Status.valueOf(resultSet.getString("status"));
                invoice = new Invoice(id, customerId, totalPrice, new Date(dateInvoice.getTime()), status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoice;
    }
}

