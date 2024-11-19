package org.apppooproject.Model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Order {

    private long orderId;
    private Customer customer;
    private double totalPrice;
    private Date dateOrder;
    private String state;
    private Map<Product, Integer> content = new HashMap<>();

    public Order(long orderId, Customer customer, double totalPrice, Date dateOrder, String state) {
        this.orderId = orderId;
        this.customer = customer;
        this.totalPrice = totalPrice;
        this.dateOrder = dateOrder;
        this.state = state;
    }

    public void addProductToOrder(Product product, int quantity) {
        content.put(product, content.getOrDefault(product, 0) + quantity);
    }

    public void removeProductFromOrder(Product product) {
        content.remove(product);
    }

    public double calculateTotalPrice() {
        totalPrice = 0;
        for (Map.Entry<Product, Integer> entry : content.entrySet()) {
            totalPrice += entry.getKey().getPrice() * entry.getValue();
        }
        return totalPrice;
    }

    public long getOrderId() {
        return orderId;
    }

    public long getCustomerId() {
        return customer.getCustomerId();
    }

    public Customer getCustomer() {
        return customer;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Date getDateOrder() {
        return dateOrder;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Map<Product, Integer> getContent() {
        return content;
    }

    // Ajout de la méthode setContent
    public void setContent(Map<Product, Integer> content) {
        this.content = content;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }


}
