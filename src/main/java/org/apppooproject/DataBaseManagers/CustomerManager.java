package org.apppooproject.DataBaseManagers;

import org.apppooproject.Model.Customer;

import java.sql.*;
import java.util.ArrayList;

public class CustomerManager implements DataManager<Customer> {
    private Connection co;
    private static CustomerManager instance;
    private Customer connectedCustomer;

    public CustomerManager(){
        try {
            this.co = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/baseSchema?useSSL=false", "root", "vautotwu");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Customer getCustomerByID(String username, String password) {
        String sql = "SELECT * FROM Customer WHERE login_name = ? AND user_password = ?";
        try (PreparedStatement stmt = co.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet res = stmt.executeQuery();

            // Vérifier s'il y a un résultat
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

    public Customer getCustomerByEmail(String email) {
        String sql = "SELECT * FROM Customer WHERE email = ?";
        PreparedStatement stmt= null;
        try {
            stmt = co.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet res = stmt.executeQuery();
            Customer customer = createNewCustomer(res);
            stmt.close();
            return customer;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Customer createNewCustomer(ResultSet res){
        try {
            if (res.next()){
                long customerId = res.getLong("customer_id");
                String firstName = res.getString("first_name");
                String lastName = res.getString("last_name");
                String email = res.getString("email");
                String address = res.getString("address");
                String phoneNumber = res.getString("phone_number");
                String loginName = res.getString("login_name");
                String userPassword = res.getString("user_password");
                Customer newCustomer=new Customer(customerId, firstName, lastName, email, address, phoneNumber, loginName, userPassword);
                return newCustomer;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public void setConnectedCustomer(Customer connectedCustomer) {
        this.connectedCustomer = connectedCustomer;
    }

    public Customer getConnectedCustomer() {
        return connectedCustomer;
    }


    @Override
    public void addAnElement(Customer c) {
        try {
            connectedCustomer = c;
            String sql = "INSERT INTO Customer(first_name, last_name, email, address, phone_number,login_name, user_password) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setString(1, c.getFirstName());
            stmt.setString(2, c.getLastName());
            stmt.setString(3, c.getEmail());
            stmt.setString(4, c.getAddress());
            stmt.setString(5, c.getPhoneNumber());
            stmt.setString(6, c.getLoginName());
            stmt.setString(7, c.getUserPassword());
            stmt.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void modifyAnElement(Customer c) {
        try {
            connectedCustomer = c;
            String sql = "SELECT * from Customer where login_name=? and user_password=?";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setString(1, c.getLoginName());
            stmt.setString(2, c.getUserPassword());
            stmt.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAnElement(Customer c) {
        try {
            connectedCustomer = c;
            String sql = "DELETE * from Customer where login_name=? and user_password=?";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setString(1, c.getLoginName());
            stmt.setString(2, c.getUserPassword());
            stmt.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /*
    //A REFAIRE !!
    @Override
    public void modifyAnElement(Customer customer) { //ajouter un attribut pour indiquer l'élément souhaité
        scan.nextLine();
        String newFirstName= null, newLastName=null, newAddress=null, newEmail=null, newPhone=null;

        for (String choice : choicesToModify) {
            switch (choice.trim()) {
                case "1":
                    newFirstName = scan.nextLine();
                    customer.setFirstName(newFirstName);
                    break;
                case "2":
                    newLastName = scan.nextLine();
                    customer.setLastName(newLastName);
                    break;
                case "3":
                    newAddress = scan.nextLine();
                    customer.setAddress(newAddress);
                    break;
                case "4":
                    newEmail = scan.nextLine();
                    customer.setEmail(newEmail);
                    break;
                case "5":
                    newPhone = scan.nextLine();
                    customer.setPhoneNumber(newPhone);
                    break;
                default:
                    System.out.println("Invalid option: " + choice);
                    break;
            }
        }
        addAnElement(customer);


    }

    //A REFAIRE!!
    @Override
    public void deleteAnElement(Customer customer) {
        try {

            String sql = "DELETE Customer WHERE login_name = ?";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setString(1, customer.getLoginName());
            stmt.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

*/




}
