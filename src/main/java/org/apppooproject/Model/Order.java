package org.apppooproject.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    private long orderId;
    private long customerId;
    private Date orderDate;
    private double totalPrice;
    private OrderStatus status;
    private Invoice invoice;
    private List<Product> content; 

    public enum OrderStatus {
        IN_PROGRESS("in progress"),
        CONFIRMED("confirmed"),
        CANCELED("canceled"),
        DELIVERED("delivered");

        private String status;

        // Constructeur privé
        OrderStatus(String status) {
            this.status = status;
        }

        // Getter pour l'attribut
        public String getStatus() {
            return status;
        }
    }

    
    public Order(long orderId, long customerId) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = new Date(); 
        this.status = OrderStatus.IN_PROGRESS;
        this.content = new ArrayList<>(); 
        this.totalPrice = 0.0;
        this.invoice = new Invoice(orderId,customerId,orderDate);
    }

    
    public double calculateTotalPrice() {
    	totalPrice = 0.0; 
        for (Product product : content) {
        	totalPrice += product.getPrice();
        }
        this.invoice.setTotalPrice(totalPrice);
        return totalPrice;
    }


    public void validateOrder() {
        if (status == OrderStatus.IN_PROGRESS) {
            status = OrderStatus.CONFIRMED;
        }
    }


    public void cancelOrder() {
        if (status == OrderStatus.IN_PROGRESS) {
            status = OrderStatus.CANCELED;
        }
    }


    public void addProductToOrder(Product product, int quantity) {
        for (int i = 0; i < quantity; i++) {
            content.add(product);
        }
        calculateTotalPrice(); 
    }


    public void removeProductFromOrder(Product product) {
        content.remove(product);
        calculateTotalPrice(); 
    }

 
    public long getOrderId() {
        return orderId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public List<Product> getContent() {
        return content;
    }
}