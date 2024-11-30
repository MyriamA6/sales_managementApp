package org.apppooproject.Model;

import org.apppooproject.DataBaseManagers.OrderManager;
import org.apppooproject.DataBaseManagers.ProductManager;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Customer {
    private long customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phoneNumber;
    private String loginName;
    private String userPassword;
    private Map<Long, Integer> cart = new HashMap<Long,Integer>();


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

    public int addToCart(Product product){
        if (product.getStock()>0) {
            cart.put(product.getProductId(), cart.getOrDefault(product.getProductId(), 0) + 1);
            product.decrementStock();
            return 1;
        }
        else{
            return 0;
        }
    }

    //a revoir !!
    public void suppressFromCart(Product product){
        product.setStock(cart.get(product.getProductId())+product.getStock());
        cart.remove(product.getProductId());
    }

    public void suppressOneUnitFromCart(Product product){
        if((cart.get(product.getProductId())-1)==0){
            cart.remove(product.getProductId());
        }
        else{
            cart.put(product.getProductId(), cart.get(product.getProductId()) - 1);
        }
        product.setStock(product.getStock()+1);
    }

    public void clearCart(){
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Product product = ProductManager.getInstance().getProductById(entry.getKey());
            product.setStock(product.getStock()+ entry.getValue());
        }
        cart.clear();
    }

    public void payCart() {
        ProductManager.getInstance().refreshProductsStock();
        // Create a new order and set its initial state to "payed"
        Order order = new Order(
                this,
                cartCurrentPrice(),  // Total price calculated from the cart
                new Date(), // Current date as the order date
                "payed" // Initial order state
        );
        order.setContent(cart);
        // Use the OrderManager to persist the order and its contents in the database
        OrderManager orderManager = OrderManager.getInstance();
        orderManager.addAnElement(order);
        // Clear the cart after the order is created
        cart.clear();

    }

    public void storeOrder() {
        // Create a new order and set its initial state to "in progress"
        Order order = new Order(
                this,
                cartCurrentPrice(),  // Total price calculated from the cart
                new Date(), // Current date as the order date
                "in progress" // Initial order state
        );

        order.setContent(cart);
        // Use the OrderManager to persist the order and its contents in the database
        OrderManager orderManager = OrderManager.getInstance();
        orderManager.addAnElement(order);
        // Clear the cart after the order is created
        cart.clear();

    }

    public double cartCurrentPrice() {
        double totalPrice = 0;
        ProductManager productManager = ProductManager.getInstance();
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Product product = productManager.getProductById(entry.getKey());
            totalPrice += product.getPrice() * entry.getValue();
        }
        return totalPrice;
    }

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

}
