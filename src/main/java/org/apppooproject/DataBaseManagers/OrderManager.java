package org.apppooproject.DataBaseManagers;

import com.mysql.cj.x.protobuf.MysqlxCrud;
import org.apppooproject.Model.Invoice;
import org.apppooproject.Model.Orders;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.util.Scanner;

public class OrderManager implements DataManager<Orders> {
    Scanner scan =new Scanner(System.in);

    Invoice invoice = new Invoice();

    Connection co;
    // Step 1: Private static instance
    private static OrderManager instance;

    // Step 2: Private constructor
    private OrderManager() {
        // Initialization, if needed
    }

    // Step 3: Static method to get the instance
    public static OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }




    public void addAnOrder(Orders order) {
            try {

                Date orderDate = new Date(System.currentTimeMillis());

                String sql = "INSERT INTO Order_record(customer_id, product_id, quantity, order_date) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = co.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                stmt.setLong(1, customerId);
                stmt.setLong(2, productId);
                stmt.setInt(3, quantity);
                stmt.setDate(4, orderDate);

                stmt.executeUpdate();

                var rs = stmt.getGeneratedKeys();
                long orderId = 0;
                if (rs.next()) {
                    orderId = rs.getLong(1);
                }

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

