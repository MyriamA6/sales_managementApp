package org.apppooproject.Model;

public abstract class Product {
    private long productId;
    private String name;
    private double price;
    private int stock;
    private String color;
    private String description;
    private String gender;
    private int size;

    public Product(long productId, String name, double price, int stock, String color,int size, String description, String gender) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.size=size;
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

    public int getSize() {
        return size;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDescription() { return description;}
    public String getGender() {return gender;}

    public void decrementStock() {
        stock--;
    }





}
