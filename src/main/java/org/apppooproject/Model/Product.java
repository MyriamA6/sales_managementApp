package org.apppooproject.Model;

public class Product {
    private long productId;
    private String name;
    private double price;
    private int stock;
    private String color;
    private String description;
    private String gender;

    public Product(long productId, String name, double price, int stock, String color, String description, String gender) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.color = color;
        this.description = description;
        this.gender = gender;
    }

    public long getProductId() {
        return productId;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public String getName() {
        return name;
    }

    public String getColor() {return color;}

    public String getDescription() { return description;}
    public String getGender() {return gender;}



}
