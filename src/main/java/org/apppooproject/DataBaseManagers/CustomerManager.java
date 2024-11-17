package org.apppooproject.DataBaseManagers;

import org.apppooproject.Model.Customer;
import java.sql.*;

public class CustomerManager implements DataManager<Customer> {
    private static CustomerManager instance;  // Instance unique de CustomerManager
    private final Connection co;               // Connexion à la base de données
    private Customer connectedCustomer;        // Le client connecté

    // Constructeur privé pour empêcher l'instanciation directe
    private CustomerManager() {
        try {
            this.co = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/baseSchema?useSSL=false", "root", "vautotwu");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Méthode pour obtenir l'instance unique de CustomerManager
    public static CustomerManager getInstance() {
        if (instance == null) {
            instance = new CustomerManager();  // Création de l'instance unique
        }
        return instance;
    }

    // Method to find a customer from the database based on the given username and password.
    // Returns the customer if found, otherwise returns null.
    public Customer getCustomerByID(String username, String password) {
        String sql = "SELECT * FROM Customer WHERE login_name = ? AND user_password = ?";
        try (PreparedStatement stmt = co.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet res = stmt.executeQuery();

            if (res.next()) {
                long customerId = res.getLong("customer_id");
                String firstName = res.getString("first_name");
                String lastName = res.getString("last_name");
                String email = res.getString("email");
                String address = res.getString("address");
                String phoneNumber = res.getString("phone_number");
                String loginName = res.getString("login_name");
                String userPassword = res.getString("user_password");

                connectedCustomer = new Customer(customerId, firstName, lastName, email, address, phoneNumber, loginName, userPassword);
                stmt.close();
                return connectedCustomer;
            } else {
                System.out.println("No client found with this username or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Return a customer from the database based on the provided email, if it exists.
    public Customer getCustomerByEmail(String email) {
        String sql = "SELECT * FROM Customer WHERE email = ?";
        PreparedStatement stmt = null;
        try {
            stmt = co.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet res = stmt.executeQuery();
            Customer customer = createNewCustomer(res); // Helper method to create a Customer from ResultSet
            stmt.close();
            return customer;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to create a Customer object from a ResultSet.
    // Returns a Customer if data is found; otherwise, returns null.
    public Customer createNewCustomer(ResultSet res) {
        try {
            if (res.next()) {
                long customerId = res.getLong("customer_id");
                String firstName = res.getString("first_name");
                String lastName = res.getString("last_name");
                String email = res.getString("email");
                String address = res.getString("address");
                String phoneNumber = res.getString("phone_number");
                String loginName = res.getString("login_name");
                String userPassword = res.getString("user_password");
                return new Customer(customerId, firstName, lastName, email, address, phoneNumber, loginName, userPassword);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // Sets the connected customer, after a successful connection.
    public void setConnectedCustomer(Customer connectedCustomer) {
        this.connectedCustomer = connectedCustomer;
    }

    // Returns the currently connected customer.
    public Customer getConnectedCustomer() {
        return connectedCustomer;
    }

    // Adds a new Customer to the database.
    @Override
    public void addAnElement(Customer c) {
        try {
            connectedCustomer = c;
            String sql = "INSERT INTO Customer(first_name, last_name, email, address, phone_number, login_name, user_password) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = setPreparedStatement(c, sql); // Prepares SQL statement with Customer data
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Updates the information of the given customer in the database.
    @Override
    public void modifyAnElement(Customer c) {
        try {
            connectedCustomer = c;
            String sql = "UPDATE Customer SET first_name = ?, last_name = ?, email = ?, address = ?, phone_number = ?, login_name = ?, user_password = ? WHERE customer_id = ?";
            PreparedStatement stmt = setPreparedStatement(c, sql);
            stmt.setLong(8, c.getCustomerId());  // Sets the Customer ID for the WHERE clause
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Method to reduce redundancy and set parameters for PreparedStatement based on a Customer's data.
    private PreparedStatement setPreparedStatement(Customer c, String sql) throws SQLException {
        PreparedStatement stmt = co.prepareStatement(sql);
        stmt.setString(1, c.getFirstName());
        stmt.setString(2, c.getLastName());
        stmt.setString(3, c.getEmail());
        stmt.setString(4, c.getAddress());
        stmt.setString(5, c.getPhoneNumber());
        stmt.setString(6, c.getLoginName());
        stmt.setString(7, c.getUserPassword());
        return stmt;
    }

    // Deletes a Customer record from the database based on the customer ID.
    @Override
    public void deleteAnElement(Customer c) {
        try {
            connectedCustomer = c;
            String sql = "DELETE FROM Customer WHERE customer_id = ?";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setLong(1, c.getCustomerId());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
