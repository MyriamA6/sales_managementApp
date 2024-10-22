package org.apppooproject.Model;

import org.apppooproject.DataBaseManagers.InvoiceManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Orders {
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



}
