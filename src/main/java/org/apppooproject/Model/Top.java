package org.apppooproject.Model;

public class Top extends Product {
    private boolean isLongSleeves;

    public Top(long productId, String name, double price, int stock, String color, String description, String gender,boolean isLongSleeves) {
        super(productId, name, price, stock, color, description, gender);
        this.isLongSleeves = false;
    }

    public boolean getIsLongSleeves(){ return isLongSleeves; }
}
