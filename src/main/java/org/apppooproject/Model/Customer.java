package org.apppooproject.Model;

import org.apppooproject.DataBaseManagers.OrderManager;
import org.apppooproject.DataBaseManagers.ProductManager;
import org.apppooproject.Service.OrderState;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//Customer class that models an instance of the Customer table of the database
public class Customer {
    private long customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phoneNumber;
    private String loginName;
    private String userPassword;
    private Map<Long, Integer> cart = new HashMap<Long,Integer>(); //variable corresponding to the current cart of the customer
    //The keys of the map are the id of the products in the cart
    //The values corresponds to the quantity of the corresponding object contained in the cart


    //Constructor for a Customer object without its id
    public Customer(String firstName, String lastName, String email, String address, String phoneNumber, String loginName, String userPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.loginName = loginName;
        this.userPassword = userPassword;
    }

    public Customer(long customerId, String firstName, String lastName, String email, String address, String phoneNumber, String loginName, String userPassword) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.loginName = loginName;
        this.userPassword = userPassword;
    }

    public Map<Long, Integer> getCart() {
        return cart;
    }

    //method to add a product to a cart
    //return 0 if the stock of the product is null and 1 otherwise
    public int addToCart(Product product){
        int quantity_in_cart=cart.getOrDefault(product.getProductId(), 0);
        if ((product.getStock()-quantity_in_cart)>0) {
            cart.put(product.getProductId(), quantity_in_cart + 1);
            if((product.getStock()-quantity_in_cart-1)==0) {
                return 0;
            }
            return 1;
        }
        else{
            return 0;
        }
    }

    //method to completely delete a product from the cart
    public void suppressFromCart(Product product){
        cart.remove(product.getProductId());
    }

    //method to reduce the quantity of the product choosen by one
    public void suppressOneUnitFromCart(Product product){
        if((cart.get(product.getProductId())-1)==0){
            cart.remove(product.getProductId());
        }
        else{
            cart.put(product.getProductId(), cart.get(product.getProductId()) - 1);
        }
    }

    //method to add a map of products to the cart
    //used to add a map of products from an order to the cart
    public void addAllToCart(Map<Product,Integer> product){
        for (Map.Entry<Product, Integer> entry : product.entrySet()) {
            addSeveralToCart(entry.getKey(), entry.getValue());
        }
    }

    //method to add several unit of a same product to the cart
    public void addSeveralToCart(Product product, int quantity){
        int cpt =0;
        while (cpt<quantity) {
            if(addToCart(product)==0){
                cpt=quantity;
            }
            cpt++;
        }
    }


    //method to empty the cart
    public void clearCart(){
        cart.clear();
    }

    //method to allow the customer to pay its cart
    //Will create a corresponding order with the "paid" status and add it to the database
    public void payCart() {
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Product product = ProductManager.getInstance().getProductById(entry.getKey());
            product.setStock(product.getStock()-entry.getValue());
        }
        ProductManager.getInstance().refreshProductsStock();
        // Create a new order and set its initial state to "paid"
        Order order = new Order(
                this,
                cartCurrentPrice(),
                new Date(),
                OrderState.PAID
        );

        order.setContent(cart);
        // Use the OrderManager to store the order and its contents in the database and marked as "paid"
        OrderManager orderManager = OrderManager.getInstance();
        orderManager.addAnElement(order);
        // Clear the cart after the order is created
        cart.clear();

    }

    //method to store the cart of the customer as an "in progress" order to allow it to change it later
    public void storeOrder() {
        // Create a new order and set its initial state to "in progress"
        Order order = new Order(
                this,
                cartCurrentPrice(),
                new Date(),
                OrderState.IN_PROGRESS
        );

        order.setContent(cart);
        // Use the OrderManager to store the order and its contents in the database and mark it as "in progress"
        OrderManager orderManager = OrderManager.getInstance();
        orderManager.addAnElement(order);
        // Clear the cart after the order is created
        cart.clear();

    }

    //method to compute the current total price value of the cart
    public double cartCurrentPrice() {
        double totalPrice = 0;
        ProductManager productManager = ProductManager.getInstance();
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Product product = productManager.getProductById(entry.getKey());
            totalPrice += product.getPrice() * entry.getValue();
        }
        return totalPrice;
    }

    //getters and setters
    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if(!(lastName==null)) this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if(email!=null) this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if(address!=null) this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if(phoneNumber!=null) this.phoneNumber = phoneNumber;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        if(loginName!=null) this.loginName = loginName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        if(userPassword!=null && !userPassword.isEmpty()) this.userPassword = userPassword;
    }

    public long getUserId() { return customerId; }

    // Method to check if it is possible to create a customer with the given parameters
    public static boolean isPossibleToCreateCustomer(String firstName, String lastName, String email, String phoneNumber) {

        // Check if firstName and lastName contain only letters (A-Z or a-z)
        if (!firstName.matches("[a-zA-Z]+") || !lastName.matches("[a-zA-Z]+")) {
            return false;
        }

        // Check if email contains "@" and "." and that they are surrounded by alphanumerical symbols
        String emailRegex = "^[a-zA-Z]+[a-zA-Z0-9._-]*[a-zA-Z0-9]+@[a-zA-Z]+\\.[a-zA-Z]+\\.?[a-zA-Z]*$";
        if (!email.matches(emailRegex)) {
            return false;
        }

        // Check if phoneNumber contains only digits (no spaces, no symbols), starts with a 0 and have a total of 9 digits
        if (!phoneNumber.matches("0[0-9]{9}")) {
            return false;
        }

        return true;
    }



}
