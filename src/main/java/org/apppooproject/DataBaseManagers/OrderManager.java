package org.apppooproject.DataBaseManagers;

import org.apppooproject.DataBaseManagers.DataManager;

import java.sql.*;
import java.util.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class OrderManager {
	/*private static OrderManager instance = null;
    private Connection co;
    private static long numberOfOrders = 0;

    
    public OrderManager(Connection connection) {
        this.co = connection;
    }
    
 // Singleton Pattern so that only one instance of OrderManager is created
    public static OrderManager getInstance(Connection connection) {
        if (instance == null) {
            instance = new OrderManager(connection);
        }
        return instance;
    }

    
    @Override
    public void addAnElement(Connection co) {
    	try {
            String sql = "INSERT INTO Orders (customerId, orderDate, totalPrice, status) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setLong(1, /* Customer ID );
            stmt.setDate(2, new java.sql.Date(System.currentTimeMillis()));
            stmt.setDouble(3, /* Order total );
            stmt.setString(4, "in progress"); // Initial order status

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    
    @Override
    public void modifyAnElement(Connection co) {
        try {
            System.out.println("Enter Order ID to modify:");
            Scanner scan = new Scanner(System.in);
            long orderId = scan.nextLong();

            String sql = "UPDATE Orders SET status = ? WHERE id = ?";
            PreparedStatement stmt = co.prepareStatement(sql);
            System.out.println("Enter new status (in progress, confirmed, processed): ");
            stmt.setString(1, scan.next());
            stmt.setLong(2, orderId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    
    @Override
    public void deleteAnElement(Connection co) {
        try {
            System.out.println("Enter Order ID to delete:");
            Scanner scan = new Scanner(System.in);
            long orderId = scan.nextLong();

            String sql = "DELETE FROM Orders WHERE id = ?";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setLong(1, orderId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    
    public double calculateTotalPrice(long orderId) {
        double totalPrice = 0.0;
        try {
            String sql = "SELECT p.price, o.quantity FROM Order_Content o JOIN Product p ON o.product_id = p.id WHERE o.order_id = ?";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setLong(1, orderId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                totalPrice += price * quantity;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return totalPrice;
    }

    
    public void validateOrder(long orderId) {
        try {
            String sql = "UPDATE Orders SET status = 'confirmed' WHERE id = ?";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setLong(1, orderId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    
    public void cancelOrder(long orderId) {
        try {
            String sql = "UPDATE Orders SET status = 'cancelled' WHERE id = ? AND status = 'in progress'";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setLong(1, orderId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                System.out.println("The order cannot be cancelled because it has already been confirmed or processed.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    
    public void addProductToOrder(long orderId, long productId, int quantity) {
        try {
            String sql = "INSERT INTO Order_Content (orderId, productId, quantity) VALUES (?, ?, ?)";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setLong(1, orderId);
            stmt.setLong(2, productId);
            stmt.setInt(3, quantity);

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    
    public void removeProductFromOrder(long orderId, long productId) {
        try {
            String sql = "DELETE FROM Order_Content WHERE orderId = ? AND productId = ?";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setLong(1, orderId);
            stmt.setLong(2, productId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    
    public List<Long> getOrdersByStatus(String status) {
        List<Long> orders = new ArrayList<>();
        try {
            String sql = "SELECT id FROM Orders WHERE status = ?";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                orders.add(rs.getLong("id"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return orders;
    }
    
    
    public List<Long> getOrdersByStatus(Customer customer) {
        List<Long> orders = new ArrayList<>();
        try {
            String sql = "SELECT id FROM Orders WHERE customerId = ?";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setLong(1, customer.getUserId()); // Supposant que Customer a une méthode getUserId()
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                orders.add(rs.getLong("id"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return orders;
    }
    */
}
