package SalesManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class OrderManager implements DataManager {
    private static long numberOfInvoices = 0;

    /*
    private long id;
    private long customerId;
    private int totalPrice;
    private Date date;
    private String state;
    private String shippingAddress;
    private HashMap<Product,Integer> products;
    private String status;*/
    Scanner scan =new Scanner(System.in);
    InvoiceManager invoiceManager = new InvoiceManager();

    public void addAnOrder(Connection co) {
            try {
                System.out.println("Enter Customer ID: ");
                long customerId = scan.nextLong();
                scan.nextLine(); // Consommer le saut de ligne

                System.out.println("Enter Product ID: ");
                long productId = scan.nextLong();
                scan.nextLine(); // Consommer le saut de ligne

                System.out.println("Enter Quantity: ");
                int quantity = scan.nextInt();
                scan.nextLine(); // Consommer le saut de ligne

                System.out.println("Enter Order Date (YYYY-MM-DD): ");
                String orderDate = scan.nextLine();

                String sql = "INSERT INTO Order_record(customer_id, product_id, quantity, order_date) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = co.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                stmt.setLong(1, customerId);
                stmt.setLong(2, productId);
                stmt.setInt(3, quantity);
                stmt.setDate(4, java.sql.Date.valueOf(orderDate));

                stmt.executeUpdate();

                // Récupérer l'ID de la commande générée
                var rs = stmt.getGeneratedKeys();
                long orderId = 0;
                if (rs.next()) {
                    orderId = rs.getLong(1);
                }

                // Valider la commande et créer la facture correspondante
                validateOrder(co, orderId);

            } catch (SQLException e) {
                System.out.println("Error adding order: " + e.getMessage());
            }
        }

    public void validateOrder(Connection co, long orderId) {
            try {
                // Valider la commande et confirmer sa création
                System.out.println("Order " + orderId + " has been validated.");

                // Création d'une facture pour la commande validée
                invoiceManager.addAnElement(co, orderId);

            } catch (Exception e) {
                System.out.println("Error validating order: " + e.getMessage());
            }
        }


    @Override
    public void addAnElement(Connection co) {

    }

    @Override
    public void modifyAnElement(Connection co) {

    }

    @Override
    public void deleteAnElement(Connection co) {

    }
}

