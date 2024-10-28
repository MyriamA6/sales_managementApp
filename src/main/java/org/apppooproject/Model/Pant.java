package org.apppooproject.Model;

public class Pant extends Product {
    private boolean isShorts;

    public Pant(long productId, String name, double price, int stock, String color, String description, String gender, boolean isShorts) {
        super(productId, name, price, stock, color, description, gender);
        this.isShorts = isShorts;
    }

    public boolean getIsShorts() {return isShorts;}
}
