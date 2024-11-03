package SalesManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    private long orderId;
    private long customerId;
    private Date orderDate;
    private double totalPrice;
    private OrderStatus status; 
    private List<Product> content; 

    public enum OrderStatus {
        IN_PROGRESS,
        CONFIRMED,
        CANCELED,
        PROCESSED
    }

    
    public Order(long orderId, long customerId) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = new Date(); 
        this.status = OrderStatus.IN_PROGRESS;
        this.content = new ArrayList<>(); 
        this.totalPrice = 0.0; 
    }

    
    public double calculateTotalPrice() {
    	totalPrice = 0.0; 
        for (Product product : content) {
        	totalPrice += product.getPrice();
        }
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


    public static Order makeAnOrder(List<Product> products, Customer customer) {
        Order order = new Order(/* generate new order ID here */, customer.getId());
        for (Product product : products) {
            order.addProductToOrder(product, 1); 
        }
        order.calculateTotalPrice(); 
        return order;
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