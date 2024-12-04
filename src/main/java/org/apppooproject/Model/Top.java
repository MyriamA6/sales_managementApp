package org.apppooproject.Model;


//Class modeling the top instances of the database
public class Top extends Product {
    private final boolean isTshirt; //boolean used to distinguish t-shirts from sweatshirts

    //Constructor to create a Top from an instance of the database
    public Top(long productId, String name, double price, int stock,int size, String color, String description, String gender,boolean isTshirt) {
        super(productId, name, price, stock, color,size, description, gender);
        this.isTshirt = isTshirt;
    }

    //Constructor to create a Top from the data entered by the admin
    //The id is not set because only the database can set it with the corresponding primary key
    //generated once the product has been added to it
    public Top(String name, double price, int stock,int size, String color, String description, String gender,boolean isTshirt) {
        super(name, price, stock, color,size, description, gender);
        this.isTshirt = isTshirt;
    }

    //getter of the boolean isTshirt
    public boolean getIsTshirt(){ return isTshirt; }
}
