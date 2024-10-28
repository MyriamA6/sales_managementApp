package org.apppooproject.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Order {
    private static long numberOfInvoices = 0;
    private long id;
    private long customerId;
    private int totalPrice;
    private Date date;
    private String state;
    private String shippingAddress;
    private ArrayList<Product> clothes;
    private String status;
    Scanner scan =new Scanner(System.in);

    /*public java.sql.Date getDate() {
        return date;
    }*/

    public int getTotalPrice() {
        return totalPrice;
    }

    public long getCustomerId() {
        return customerId;
    }

    public long getId() {
        return id;
    }

    public static long getNumberOfInvoices() {
        return numberOfInvoices;
    }

    public String getState() {
        return state;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public ArrayList<Product> getClothes() {
        return clothes;
    }

    public String getStatus() {
        return status;
    }
}
