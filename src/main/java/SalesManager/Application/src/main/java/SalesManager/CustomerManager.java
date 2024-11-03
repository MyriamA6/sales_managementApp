package SalesManager;

import java.sql.*;
import java.util.Scanner;

public class CustomerManager implements DataManager{
    private static long numberOfCustomers = 0;
    Scanner scan = new Scanner(System.in);

    public CustomerManager(){}

    @Override
    public void addAnElement(Connection co) {
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
    public void modifyAnElement(Connection co) {

    }

    @Override
    public void deleteAnElement(Connection co) {
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
