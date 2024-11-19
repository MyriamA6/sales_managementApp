package org.apppooproject.DataBaseManagers;

import org.apppooproject.Model.Order;

import java.sql.*;
import java.util.Date;

public class OrderManager implements DataManager<Order> {
    private Connection co;

    // Constructor to initialize the database connection
    public OrderManager() {
        try {
            this.co = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/baseSchema?useSSL=false", "root", "vautotwu");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addAnElement(Order orderToAdd) {
        try {
            // Insert into the correct table: order_record
            String sql = "INSERT INTO Order_record (customer_id, order_date, total_price, order_state) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setLong(1, orderToAdd.getCustomerId());
            stmt.setDate(2, new java.sql.Date(orderToAdd.getOrderDate().getTime())); // convert java.util.Date to java.sql.Date
            stmt.setDouble(3, orderToAdd.getTotalPrice());
            stmt.setString(4, "in progress"); // Assuming default status is 'in progress'

            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifyAnElement(Order orderToModify) {
        try {
            // Update the status in the correct table: order_record
            String sql = "UPDATE Order_record SET order_state = ? WHERE order_id = ?";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setString(1, orderToModify.getStatus().getStatus());  // Assuming OrderState is a String or an Enum converted to String
            stmt.setLong(2, orderToModify.getOrderId());

            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteAnElement(Order orderToDelete) {
        try {
            // Delete from the correct table: order_record
            String sql = "DELETE FROM Order_record WHERE order_id = ?";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setLong(1, orderToDelete.getOrderId());

            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void getAllElements() {
        try {
            // Correct query to select from Order_record
            String sql = "SELECT * FROM Order_record";
            Statement stmt = co.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                long orderId = rs.getLong("order_id"); // Correct column name
                long customerId = rs.getLong("customer_id"); // Correct column name
                Date orderDate = rs.getDate("order_date"); // Correct column name
                double totalPrice = rs.getDouble("total_price"); // Correct column name
                String status = rs.getString("order_state"); // Correct column name

                // Output order details (or create Order objects as needed)
                System.out.println("Order ID: " + orderId + ", Customer ID: " + customerId + ", Status: " + status + ", Total Price: " + totalPrice);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
