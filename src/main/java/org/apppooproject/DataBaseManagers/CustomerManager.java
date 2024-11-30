package org.apppooproject.DataBaseManagers;

import org.apppooproject.Model.Customer;
import org.apppooproject.Service.Password;

import java.sql.*;

public class CustomerManager implements DataManager<Customer> {
    private static CustomerManager instance;  // Instance of CustomerManager
    private final Connection co;               // Connection to the dataBase
    private Customer connectedCustomer;        // The connected client

    //implementation of the singleton design pattern
    private CustomerManager() {
        this.co = DatabaseInitializer.getH2Connection();
    }


    public static CustomerManager getInstance() {
        if (instance == null) {
            instance = new CustomerManager();  // Creation of the instance of CustomerManager
        }
        return instance;
    }

    // Method to find a customer from the database based on the given username and password.
    // Returns the customer if found, otherwise returns null.
    public Customer getCustomerByLogin(String username, String password) {
        String sql = "SELECT * FROM Customer WHERE login_name = ? and user_password= ?";
        try (PreparedStatement stmt = co.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet res = stmt.executeQuery();
            connectedCustomer = createCustomerFromResultSet(res);
            stmt.close();
            return connectedCustomer;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    //method to get a customer from the database using its ID
    public Customer getCustomerById(long id) {
        String sql = "SELECT * FROM Customer WHERE customer_id = ?";
        PreparedStatement stmt = null;
        try {
            stmt = co.prepareStatement(sql);
            stmt.setLong(1, id);
            ResultSet res = stmt.executeQuery();
            Customer customer = createCustomerFromResultSet(res);
            stmt.close();
            return customer;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Return a customer from the database based on the provided email, if it exists.
    public Customer getCustomerByEmail(String email) {
        String sql = "SELECT * FROM Customer WHERE email = ?";
        PreparedStatement stmt = null;
        try {
            stmt = co.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet res = stmt.executeQuery();
            Customer customer = createCustomerFromResultSet(res); // Helper method to create a Customer from ResultSet
            stmt.close();
            return customer;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Return a customer from the database based on the provided email, if it exists.
    public Customer getCustomerByLoginName(String loginName) {
        String sql = "SELECT * FROM Customer WHERE login_name = ?";
        PreparedStatement stmt = null;
        try {
            stmt = co.prepareStatement(sql);
            stmt.setString(1, loginName);
            ResultSet res = stmt.executeQuery();
            Customer customer = createCustomerFromResultSet(res); // Helper method to create a Customer from ResultSet
            stmt.close();
            return customer;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    //method to create an instance of Customer using a resultSet obtained with a sql query
    private Customer createCustomerFromResultSet(ResultSet res) throws SQLException {
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
