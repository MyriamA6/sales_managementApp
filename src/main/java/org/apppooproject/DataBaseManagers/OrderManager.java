package org.apppooproject.DataBaseManagers;

import org.apppooproject.Model.Order;
import org.apppooproject.Model.Product;
import org.apppooproject.Service.OrderState;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderManager implements DataManager<Order> {
    private static OrderManager instance;
    private final Connection connection;

    // Singleton pattern constructor to ensure only one instance of OrderManager
    private OrderManager() {
        this.connection = DatabaseInitializer.getH2Connection();
    }

    // Returns the singleton instance of OrderManager
    public static OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    // Add a new order to the database
    @Override
    public void addAnElement(Order order) {
        String sql = "INSERT INTO Order_record (customer_id, total_price, order_date, order_state) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, order.getCustomerId());
            stmt.setDouble(2, order.getTotalPrice());
            stmt.setDate(3, new java.sql.Date(order.getDateOrder().getTime()));
            stmt.setString(4, order.getState().getState());
            stmt.executeUpdate();

            // Retrieve generated order ID
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                order.setOrderId(generatedKeys.getLong(1));
                if( order.getState().equalState("paid")){
                    InvoiceManager.getInstance().createInvoice(order);
                }
            }

            // Insert products in Content table
            for (Long product : order.getContent().keySet()) {
                addContent(order.getOrderId(), product, order.getContent().get(product));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Insert the product content (product ID and quantity) into the Content table
    private void addContent(long orderId, long productId, int quantity) throws SQLException {
        String sql = "INSERT INTO Content (order_id, product_id, quantity_ordered) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, orderId);
            stmt.setLong(2, productId);
            stmt.setInt(3, quantity);
            stmt.executeUpdate();
        }
    }

    // Modify an existing order in the database
    @Override
    public void modifyAnElement(Order order) {
        String sql = "UPDATE Order_record SET total_price = ?, order_state = ? WHERE order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, order.getTotalPrice());
            stmt.setString(2, order.getState().getState());
            stmt.setLong(3, order.getOrderId());
            stmt.executeUpdate();

            // Update the products associated with the order in the Content table
            removeOrderContent(order.getOrderId());
            for (Long product : order.getContent().keySet()) {
                addContent(order.getOrderId(), product, order.getContent().get(product));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Remove the content (products) associated with an order from the Content table
    private void removeOrderContent(long orderId) throws SQLException {
        String sql = "DELETE FROM Content WHERE order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, orderId);
            stmt.executeUpdate();
        }
    }

    // Delete an order from the database, including its content
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

    //Returns all the Order stored in the database
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
                        OrderState.giveCorrespondingState(rs.getString("order_state"))
                );
                order.setContent(getOrderContent(order.getOrderId()));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }


    //Returns a specific order by its ID
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
                        OrderState.giveCorrespondingState(rs.getString("order_state"))
                );
                order.setContent(getOrderContent(order.getOrderId()));
                return order;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Returns a list of orders for a specific customer using its ID
    public ArrayList<Order> getOrdersByCustomerId(long customerId) {
        ArrayList<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Order_record WHERE customer_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Order order = new Order(
                        rs.getLong("order_id"),
                        CustomerManager.getInstance().getCustomerById(rs.getLong("customer_id")),
                        rs.getDouble("total_price"),
                        rs.getDate("order_date"),
                        OrderState.giveCorrespondingState(rs.getString("order_state"))
                );
                order.setContent(getOrderContent(order.getOrderId()));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    // Returns a list of orders for a specific customer using its username
    public ArrayList<Order> getOrdersByCustomerUsername(String username) {
        ArrayList<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Order_record o Join customer c on o.customer_id=c.customer_id where c.login_name = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                if (rs.getString("login_name").equals(username)) {
                    System.out.println(rs.getString("login_name"));
                    System.out.println(username);

                    Order order = new Order(
                            rs.getLong("order_id"),
                            CustomerManager.getInstance().getCustomerById(rs.getLong("customer_id")),
                            rs.getDouble("total_price"),
                            rs.getDate("order_date"),
                            OrderState.giveCorrespondingState(rs.getString("order_state"))
                    );
                    order.setContent(getOrderContent(order.getOrderId()));
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    // Update the content of an order, checking the availability of its content
    // Only used for order with an IN_PROGRESS state
    public void updateOrderContent(Order order){
        Map<Product,Integer> products =order.getProducts();
        for(Map.Entry<Product,Integer> entry : products.entrySet()){
            Product product = entry.getKey();
            int productStock=ProductManager.getInstance().getStockOfProduct(product.getProductId());
            if (productStock==0){
                order.getContent().remove(product.getProductId());
            }
            else if(entry.getValue()>productStock){
                order.getContent().put(product.getProductId(),productStock);
            }
        }
        order.calculateTotalPrice();
    }


    // Retrieve the content associated with an order
    // e.g. the list of id  of the products contained in the order and their quantity
    private HashMap<Long, Integer> getOrderContent(long orderId) {
        HashMap<Long, Integer> content = new HashMap<>();
        String sql = "SELECT * from Content WHERE order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, orderId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Long product=rs.getLong("product_id");
                content.put(product, rs.getInt("quantity_ordered"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return content;
    }


}
