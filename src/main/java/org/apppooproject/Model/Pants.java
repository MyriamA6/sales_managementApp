package org.apppooproject.Model;

public class Pants extends Product {
    private boolean isShorts;

    public Pants(long productId, String name, double price, int stock,int size, String color, String description, String gender, boolean isShorts) {
        super(productId, name, price, stock, color, size, description, gender);
        this.isShorts = isShorts;
    }

    public boolean getIsShorts() {return isShorts;}
}
