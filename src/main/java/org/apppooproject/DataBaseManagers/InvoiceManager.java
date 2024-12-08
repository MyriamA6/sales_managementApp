package org.apppooproject.DataBaseManagers;

import org.apppooproject.Model.Invoice;
import org.apppooproject.Model.Order;
import org.apppooproject.Service.OrderState;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//InvoiceManager Class, allows the communication between the application and the invoice table in the database
public class InvoiceManager implements DataManager<Invoice> {

    private final Connection connection;
    private static InvoiceManager instance;
    private static Invoice selectedInvoice;

    // Constructor method to connect the Invoice manager to the database
    private InvoiceManager() {
        this.connection = DatabaseInitializer.getH2Connection();
    }

    //method implementing the getInstance method from the singleton design pattern
    public static InvoiceManager getInstance() {
        if (instance == null) {
            instance = new InvoiceManager();
        }
        return instance;
    }

    // Implementation of the method to add an instance of Invoice in the table Invoice of the database
    @Override
    public void addAnElement(Invoice invoice) {
        String query = "INSERT INTO Invoice (order_id, invoice_date) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, invoice.getOrderId());
            statement.setDate(2, new java.sql.Date(invoice.getInvoiceDate().getTime()));
            statement.executeUpdate();
            System.out.println("Invoice added successfully");
        } catch (SQLException e) {
            System.out.println("Error while adding invoice: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    // Implementation of the method to modify an instance of Invoice in the table Invoice of the database
    @Override
    public void modifyAnElement(Invoice invoice) {
        String query = "UPDATE Invoice SET invoice_date = ? WHERE invoice_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, new java.sql.Date(invoice.getInvoiceDate().getTime()));
            statement.setLong(2, invoice.getInvoiceId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while modifying invoice: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    // Implementation of the method to delete an instance of Invoice in the table Invoice of the database
    @Override
    public void deleteAnElement(Invoice invoice) {
        String query = "DELETE FROM Invoice WHERE invoice_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            long invoiceId = invoice.getInvoiceId();
            statement.setLong(1, invoiceId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while deleting an invoice: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    // Method to get an invoice from the database using its id
    public Invoice getInvoiceById(long invoiceId) {
        String query = "SELECT * FROM Invoice WHERE invoice_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, invoiceId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                long orderId = resultSet.getLong("order_id");
                java.sql.Date invoiceDate = resultSet.getDate("invoice_date");
                return new Invoice(invoiceId, orderId, invoiceDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to get all the invoice from the database
    public List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        String query = "SELECT * FROM Invoice";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long invoiceId = resultSet.getLong("invoice_id");
                long orderId = resultSet.getLong("order_id");
                java.sql.Date invoiceDate = resultSet.getDate("invoice_date");
                invoices.add(new Invoice(invoiceId, orderId, invoiceDate));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invoices;
    }

    // Creates an invoice from a given instance of order
    public Invoice createInvoice(Order order) {
        if (order.getState().equals(OrderState.PAID)) {
            Invoice invoice = new Invoice(order.getOrderId(), order.getDateOrder());
            addAnElement(invoice);
            return invoice;
        } else {
            throw new IllegalStateException("Invoice cannot be created, order is incomplete.");
        }
    }

    //Returns an invoice from the orderId of the Order associated to it
    public Invoice getInvoiceByOrderId(long orderId) {
        String query = "SELECT * FROM invoice WHERE order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, orderId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                long invoiceId = resultSet.getLong("invoice_id");
                java.sql.Date invoiceDate = resultSet.getDate("invoice_date");
                return new Invoice(invoiceId, orderId, invoiceDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setSelectedInvoice(Invoice selectedInvoice) {
        InvoiceManager.selectedInvoice = selectedInvoice;
    }
    public Invoice getSelectedInvoice() {
        return InvoiceManager.selectedInvoice;
    }
}
