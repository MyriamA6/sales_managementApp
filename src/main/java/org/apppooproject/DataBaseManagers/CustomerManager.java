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

    public void addPassword(Customer customer) {};




}
