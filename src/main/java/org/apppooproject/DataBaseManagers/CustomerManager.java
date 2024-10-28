package org.apppooproject.DataBaseManagers;

import org.apppooproject.Model.Customer;

import java.sql.*;
import java.util.ArrayList;

public class CustomerManager implements DataManager<Customer> {
    private Connection co;
    private static CustomerManager instance;
    private ArrayList<Customer> customers;

    public CustomerManager(){
        try {
            this.co = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/baseSchema?useSSL=false", "root", "vautotwu");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        customers = new ArrayList<Customer>();
        loadCustomerFromBd();
    }


    private void loadCustomerFromBd() {
        Statement stmt = null;
        try {
            stmt = co.createStatement();
            String sql = "SELECT * FROM Customer";
            ResultSet res = stmt.executeQuery(sql);

            while (res.next()) {
                long customerId = res.getLong("customer_id");
                String firstName = res.getString("first_name");
                String lastName = res.getString("last_name");
                String email = res.getString("email");
                String address = res.getString("address");
                String phoneNumber = res.getString("phone_number");
                String loginName = res.getString("login_name");
                String userPassword = res.getString("user_password");

                customers.add(new Customer(customerId, firstName, lastName, email, address, phoneNumber, loginName, userPassword));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void refresh(){
        customers= new ArrayList<Customer>();
        loadCustomerFromBd();
    }

    public Customer getCustomerByID(String username, String password){
        for(Customer customer : customers){
            if (customer.getLoginName().equals(username) && customer.getUserPassword().equals(password)){
                return customer;
            }
        }
        return null;
    }

    /*
    @Override
    public void addAnElement(Customer customer) {
        try {

            String sql = "INSERT INTO Customer(first_name, last_name, email, address, phone_number) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getAddress());
            stmt.setString(5, customer.getPhoneNumber());
            stmt.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


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
