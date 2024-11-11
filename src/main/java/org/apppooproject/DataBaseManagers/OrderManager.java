package org.apppooproject.DataBaseManagers;

import org.apppooproject.DataBaseManagers.DataManager;

import java.sql.*;
import java.util.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class OrderManager implements DataManager {
    private Connection co;

    public OrderManager(Connection connection) {
        this.co = connection;
    }

    @Override
    public void addAnElement(Connection co, Object element) {
        try {
            Order order = (Order) element;
            String sql = "INSERT INTO Orders (customerId, orderDate, totalPrice, status) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setLong(1, order.getCustomerId());
            stmt.setDate(2, new java.sql.Date(order.getOrderDate().getTime()));
            stmt.setDouble(3, order.getTotalPrice());
            stmt.setString(4, "in progress"); // Initial order status

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifyAnElement(Connection co, Object element) {
        try {
            Order order = (Order) element;
            String sql = "UPDATE Orders SET status = ? WHERE id = ?";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setString(1, order.getStatus());
            stmt.setLong(2, order.getOrderId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteAnElement(Connection co, Object element) {
        try {
            Order order = (Order) element;
            String sql = "DELETE FROM Orders WHERE id = ?";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setLong(1, order.getOrderId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void getAllElements(Connection co) {
        try {
            String sql = "SELECT * FROM Orders";
            Statement stmt = co.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                long orderId = rs.getLong("id");
                long customerId = rs.getLong("customerId");
                Date orderDate = rs.getDate("orderDate");
                double totalPrice = rs.getDouble("totalPrice");
                String status = rs.getString("status");

                // Use this data to create and display Order objects if necessary
                System.out.println("Order ID: " + orderId + ", Customer ID: " + customerId + ", Status: " + status);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

