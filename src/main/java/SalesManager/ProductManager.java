package SalesManager;

import java.sql.Connection;

public abstract class ProductManager implements DataManager{
    private long productId;
    private String name;
    private double price;
    private int stock;

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

    @Override
    public void addAnElement(Connection co) {

    }

    @Override
    public void modifyAnElement(Connection co) {

    }

    @Override
    public void deleteAnElement(Connection co) {

    }


}
