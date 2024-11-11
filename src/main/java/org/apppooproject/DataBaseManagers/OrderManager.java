package org.apppooproject.DataBaseManagers;

import org.apppooproject.Model.Order;

import java.sql.*;
import java.util.Date;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderManager implements DataManager {

    private static OrderManager instance;
    private Connection co;
    


    public static OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    @Override
    public void addAnElement(Connection co, Order o) {
        this.co = co;
        String query = "INSERT INTO orders (customer_id, total_price, date_order, state) VALUES (?, ?, ?, ?)";
        try {
            var stmt = co.prepareStatement(query);
            stmt.setLong(1, o.getCustomerId());
            stmt.setInt(2, o.calculateTotalPrice());
            stmt.setDate(3, new java.sql.Date(o.getDate().getTime()));
            stmt.setString(4, o.getState().toString());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifyAnElement(Connection co, Order o) {
        this.co = co;
        String query = "UPDATE orders SET total_price = ?, state = ? WHERE id = ?";
        try {
            var stmt = co.prepareStatement(query);
            stmt.setInt(1, o.calculateTotalPrice());
            stmt.setString(2, o.getState().toString());
            stmt.setLong(3, o.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAnElement(Connection co, Order o) {
        this.co = co;
        String query = "DELETE FROM orders WHERE id = ?";
        try {
            var stmt = co.prepareStatement(query);
            stmt.setLong(1, o.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<Order> getOrdersByStatus(Customer c, String status) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE customer_id = ? AND state = ?";
        try {
            var stmt = co.prepareStatement(query);
            stmt.setLong(1, c.getId());
            stmt.setString(2, status);
            var resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                long customerId = resultSet.getLong("customer_id");
                int totalPrice = resultSet.getInt("total_price");
                java.sql.Date dateOrder = resultSet.getDate("date_order");
                Order order = new Order(id, customerId, totalPrice, new java.util.Date(dateOrder.getTime()), Order.State.valueOf(resultSet.getString("state")));
                orders.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }


    public Order getOrderById(long orderId) {
        Order order = null;
        String query = "SELECT * FROM orders WHERE id = ?";
        try {
            var stmt = co.prepareStatement(query);
            stmt.setLong(1, orderId);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                long id = resultSet.getLong("id");
                long customerId = resultSet.getLong("customer_id");
                int totalPrice = resultSet.getInt("total_price");
                java.sql.Date dateOrder = resultSet.getDate("date_order");
                order = new Order(id, customerId, totalPrice, new java.util.Date(dateOrder.getTime()), Order.State.valueOf(resultSet.getString("state")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }
}

