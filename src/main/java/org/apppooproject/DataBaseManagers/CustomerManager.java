package org.apppooproject.DataBaseManagers;

import org.apppooproject.Model.Customer;

import java.sql.*;
import java.util.Scanner;

public class CustomerManager implements DataManager<Customer> {
    private static long numberOfCustomers = 0;
    Scanner scan = new Scanner(System.in);
    private Connection co;
    public CustomerManager(){}

    @Override
    public void addAnElement(Customer customer) {
        try {

            System.out.println("Enter First Name: ");
            String firstName = scan.nextLine();
            System.out.println("Enter Last Name: ");
            String lastName = scan.nextLine();
            System.out.println("Enter Address: ");
            String address = scan.nextLine();
            System.out.println("Enter Email: ");
            String email = scan.nextLine();
            System.out.println("Enter Phone Number: ");
            String phone = scan.nextLine();

            String sql = "INSERT INTO Customer(first_name, last_name, email, address, phone_number) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, email);
            stmt.setString(4, address);
            stmt.setString(5, phone);
            stmt.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void modifyAnElement(Customer customer) {
        try {
            System.out.println("Enter the email of the customer you want to modify: ");
            long customerMail = scan.nextLong();
            scan.nextLine();

            System.out.println("Which field do you want to modify?");
            System.out.println("1. First Name");
            System.out.println("2. Last Name");
            System.out.println("3. Address");
            System.out.println("4. Email");
            System.out.println("5. Phone Number");
            System.out.println("Enter the numbers of the fields you want to modify (comma-separated, e.g., 1,3,5):");

            String choices = scan.nextLine();
            String[] choicesToModify = choices.split(",");

            StringBuilder sql = new StringBuilder("UPDATE Customer SET ");
            boolean firstField = true;

            String newFirstName= null, newLastName=null, newAddress=null, newEmail=null, newPhone=null;

            for (String choice : choicesToModify) {
                switch (choice.trim()) {
                    case "1":
                        System.out.println("Enter new First Name: ");
                        newFirstName = scan.nextLine();
                        if (!firstField) sql.append(", ");
                        sql.append("first_name = ?");
                        firstField = false;
                        break;
                    case "2":
                        System.out.println("Enter new Last Name: ");
                        newLastName = scan.nextLine();
                        if (!firstField) sql.append(", ");
                        sql.append("last_name = ?");
                        firstField = false;
                        break;
                    case "3":
                        System.out.println("Enter new Address: ");
                        newAddress = scan.nextLine();
                        if (!firstField) sql.append(", ");
                        sql.append("address = ?");
                        firstField = false;
                        break;
                    case "4":
                        System.out.println("Enter new Email: ");
                        newEmail = scan.nextLine();
                        if (!firstField) sql.append(", ");
                        sql.append("email = ?");
                        firstField = false;
                        break;
                    case "5":
                        System.out.println("Enter new Phone Number: ");
                        newPhone = scan.nextLine();
                        if (!firstField) sql.append(", ");
                        sql.append("phone_number = ?");
                        firstField = false;
                        break;
                    default:
                        System.out.println("Invalid option: " + choice);
                        break;
                }
            }

            sql.append(" WHERE email = ?");

            PreparedStatement stmt = co.prepareStatement(sql.toString());

            int paramIndex = 1;
            if (newFirstName != null) {
                stmt.setString(paramIndex++, newFirstName);
            }
            if (newLastName != null) {
                stmt.setString(paramIndex++, newLastName);
            }
            if (newAddress != null) {
                stmt.setString(paramIndex++, newAddress);
            }
            if (newEmail != null) {
                stmt.setString(paramIndex++, newEmail);
            }
            if (newPhone != null) {
                stmt.setString(paramIndex++, newPhone);
            }

            stmt.setLong(paramIndex, customerMail);

            // Execute the update query
            int res = stmt.executeUpdate();
            if (res > 0) {
                System.out.println("Customer information updated successfully.");
            } else {
                System.out.println("No customer found with the given email.");
            }

        } catch (SQLException e) {
            System.out.println("Error while updating customer: " + e.getMessage());
        }
    }

    @Override
    public void deleteAnElement(Customer customer) {
        try {
            System.out.println("Enter First Name: ");
            String firstName = scan.nextLine();
            System.out.println("Enter Last Name: ");
            String lastName = scan.nextLine();
            System.out.println("Enter Address: ");
            String address = scan.nextLine();

            String sql = "DELETE Customer WHERE first_name = ? AND last_name = ? AND address = ?";
            PreparedStatement stmt = co.prepareStatement(sql);
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, address);
            stmt.executeUpdate();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
