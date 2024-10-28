package org.apppooproject.Model;

public class Top extends Product {
    private boolean isTshirt;

    public Top(long productId, String name, double price, int stock,int size, String color, String description, String gender,boolean isTshirt) {
        super(productId, name, price, stock, color,size, description, gender);
        this.isTshirt = isTshirt;
    }

    public boolean getIsTshirt(){ return isTshirt; }
}
