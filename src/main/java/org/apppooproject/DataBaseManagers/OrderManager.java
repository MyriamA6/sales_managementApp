package org.apppooproject.DataBaseManagers;

import org.apppooproject.Model.Order;
import org.apppooproject.Model.Product;
import org.apppooproject.Model.Pants;
import org.apppooproject.Model.Top;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class OrderManager implements DataManager<Order> {
    private static OrderManager instance;
    private final Connection connection;

    private OrderManager() {
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/baseSchema?useSSL=false", "root", "vautotwu");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de connexion à la base de données : " + e.getMessage());
        }
    }

    public static OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    @Override
    public void addAnElement(Order order) {
        String sql = "INSERT INTO Order_record (customer_id, total_price, order_date, order_state) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, order.getCustomerId());
            stmt.setDouble(2, order.getTotalPrice());
            stmt.setDate(3, new java.sql.Date(order.getDateOrder().getTime()));
            stmt.setString(4, order.getState());
            stmt.executeUpdate();

            // Retrieve generated order ID
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                order.setOrderId(generatedKeys.getLong(1));
            }

            // Insert products in Content table
            for (Product product : order.getContent().keySet()) {
                addContent(order.getOrderId(), product.getProductId(), order.getContent().get(product));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addContent(long orderId, long productId, int quantity) throws SQLException {
        String sql = "INSERT INTO Content (order_id, product_id, quantity_ordered) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, orderId);
            stmt.setLong(2, productId);
            stmt.setInt(3, quantity);
            stmt.executeUpdate();
        }
    }

    @Override
    public void modifyAnElement(Order order) {
        String sql = "UPDATE Order_record SET total_price = ?, order_state = ? WHERE order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, order.getTotalPrice());
            stmt.setString(2, order.getState());
            stmt.setLong(3, order.getOrderId());
            stmt.executeUpdate();

            // Mettre à jour les contenus associés
            removeOrderContent(order.getOrderId());
            for (Product product : order.getContent().keySet()) {
                addContent(order.getOrderId(), product.getProductId(), order.getContent().get(product));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void removeOrderContent(long orderId) throws SQLException {
        String sql = "DELETE FROM Content WHERE order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, orderId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteAnElement(Order order) {
        try {
            removeOrderContent(order.getOrderId());

            String sql = "DELETE FROM Order_record WHERE order_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setLong(1, order.getOrderId());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Order> getAllElements() {
        ArrayList<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Order_record";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Order order = new Order(
                        rs.getLong("order_id"),
                        CustomerManager.getInstance().getCustomerById(rs.getLong("customer_id")),
                        rs.getDouble("total_price"),
                        rs.getDate("order_date"),
                        rs.getString("order_state")
                );
                order.setContent(getOrderContent(order.getOrderId()));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }


    public Order getElementById(long id) {
        String sql = "SELECT * FROM Order_record WHERE order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Order order = new Order(
                        rs.getLong("order_id"),
                        CustomerManager.getInstance().getCustomerById(rs.getLong("customer_id")),
                        rs.getDouble("total_price"),
                        rs.getDate("order_date"),
                        rs.getString("order_state")
                );
                order.setContent(getOrderContent(order.getOrderId()));
                return order;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Order> getOrdersByCustomerId(long customerId) {
        ArrayList<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Order_record WHERE customer_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Créer un objet Order à partir des données de la commande
                Order order = new Order(
                        rs.getLong("order_id"),
                        CustomerManager.getInstance().getCustomerById(rs.getLong("customer_id")), // Récupérer le client
                        rs.getDouble("total_price"),
                        rs.getDate("order_date"),
                        rs.getString("order_state")
                );
                // Ajouter le contenu des commandes à l'objet Order
                order.setContent(getOrderContent(order.getOrderId()));
                // Ajouter la commande à la liste des commandes
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }


    private HashMap<Product, Integer> getOrderContent(long orderId) {
        HashMap<Product, Integer> content = new HashMap<>();
        String sql = "SELECT c.product_id, c.quantity_ordered, p.name, p.price, p.color, p.size, p.description, p.gender, p.stock, " +
                "pa.length, t.sleevesType " +
                "FROM Content c " +
                "JOIN Product p ON c.product_id = p.product_id " +
                "LEFT JOIN Pants pa ON p.product_id = pa.product_id " +
                "LEFT JOIN Top t ON p.product_id = t.product_id " +
                "WHERE c.order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, orderId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product product;
                if (rs.getString("length") != null) {
                    product = new Pants(
                            rs.getLong("product_id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getInt("stock"),
                            rs.getInt("size"),
                            rs.getString("color"),
                            rs.getString("description"),
                            rs.getString("gender"),
                            rs.getString("length").equalsIgnoreCase("Shorts")
                    );
                } else if (rs.getString("sleevesType") != null) {
                    product = new Top(
                            rs.getLong("product_id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getInt("stock"),
                            rs.getInt("size"),
                            rs.getString("color"),
                            rs.getString("description"),
                            rs.getString("gender"),
                            rs.getString("sleevesType").equalsIgnoreCase("T-shirt")
                    );
                } else {
                    throw new RuntimeException("Produit inconnu !");
                }
                content.put(product, rs.getInt("quantity_ordered"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return content;
    }
}
