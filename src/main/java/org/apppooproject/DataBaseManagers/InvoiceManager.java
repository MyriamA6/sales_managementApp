package org.apppooproject.DataBaseManagers;

import org.apppooproject.Model.Invoice;
import org.apppooproject.Model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceManager implements DataManager<Invoice> {

    private Connection connection;

    // Constructeur pour initialiser la connexion
    public InvoiceManager(Connection connection) {
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/baseSchema?useSSL=false", "root", "vautotwu");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de connexion à la base de données : " + e.getMessage());
        }
    }

    // Implémentation de la méthode addAnElement pour ajouter une facture
    @Override
    public void addAnElement(Invoice invoice) {
        String query = "INSERT INTO Invoice (order_id, invoice_date) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, invoice.getOrderId());
            statement.setDate(2, new java.sql.Date(invoice.getInvoiceDate().getTime()));
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Implémentation de la méthode modifyAnElement pour modifier une facture
    @Override
    public void modifyAnElement(Invoice invoice) {
        String query = "UPDATE Invoice SET invoice_date = ? WHERE invoice_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, new java.sql.Date(invoice.getInvoiceDate().getTime()));
            statement.setLong(2, invoice.getInvoiceId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Implémentation de la méthode deleteAnElement pour supprimer une facture
    @Override
    public void deleteAnElement(Invoice invoice) {
        String query = "DELETE FROM Invoice WHERE invoice_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, invoice.getInvoiceId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode pour récupérer une facture par son ID
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

    // Méthode pour récupérer toutes les factures
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

    // Méthode pour créer une facture à partir d'une commande
    public Invoice createInvoice(Order order) {
        if (order.getState().equalsIgnoreCase("completed")) {
            Invoice invoice = new Invoice(order.getOrderId(), order.getDateOrder());
            addAnElement(invoice); // Ajoute la facture à la base de données
            return invoice;
        } else {
            throw new IllegalStateException("La commande n'est pas complétée, la facture ne peut pas être créée.");
        }
    }
}
