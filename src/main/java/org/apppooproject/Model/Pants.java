package org.apppooproject.Model;

//Class modeling the table Pants of the database
public class Pants extends Product {
    private boolean isShorts;

    public Pants(long productId, String name, double price, int stock,int size, String color, String description, String gender, boolean isShorts) {
        super(productId, name, price, stock, color, size, description, gender);
        this.isShorts = isShorts;
    }
    public Pants(String name, double price, int stock,int size, String color, String description, String gender, boolean isShorts) {
        super(name, price, stock, color, size, description, gender);
        this.isShorts = isShorts;
    }

    public boolean getIsShorts() {return isShorts;}
}
