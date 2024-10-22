package org.apppooproject.DataBaseManagers;

import org.apppooproject.Model.Customer;

import java.sql.*;
import java.util.List;

public class Catalog {

    List<Customer> customerList;

    public static void main(String args[]) {
        //étape 1: charger la classe driver
        //étape 2: créer l'objet de connexion
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/baseSchema?useSSL=false", "root", "vautotwu");

            CustomerManager cM = new CustomerManager();
            cM.addAnElement(conn);
            Statement stmt=conn.createStatement();





            String sql = "SELECT * FROM Customer";
            ResultSet res = stmt.executeQuery(sql);
            while (res.next()) {
                System.out.println(res.getString("first_name") + " " + res.getString("last_name"));
            }



        }
        catch (SQLException e) {
            System.out.println(e.getMessage());


        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }



}
