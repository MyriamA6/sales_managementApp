package org.apppooproject.Model;

import org.apppooproject.DataBaseManagers.InvoiceManager;
import org.apppooproject.DataBaseManagers.ProductManager;
import org.apppooproject.Service.OrderState;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Order {

    private long orderId;
    private Customer customer;
    private double totalPrice;
    private Date dateOrder;
    private OrderState state;
    private Map<Long, Integer> content = new HashMap<>();

    public Order(long orderId, Customer customer, double totalPrice, Date dateOrder, OrderState state) {
        this.orderId = orderId;
        this.customer = customer;
        this.totalPrice = totalPrice;
        this.dateOrder = dateOrder;
        this.state = state;
    }

    public Order(Customer customer, double totalPrice, Date dateOrder, OrderState state) {
        this.customer = customer;
        this.totalPrice = totalPrice;
        this.dateOrder = dateOrder;
        this.state = state;

    }

    public void addProductToOrder(Long product, int quantity) {
        content.put(product, content.getOrDefault(product, 0) + quantity);
    }

    public void removeProductFromOrder(Product product) {
        content.remove(product);
    }

    public double calculateTotalPrice() {
        totalPrice = 0;
        for (Map.Entry<Long, Integer> entry : content.entrySet()) {
            totalPrice += ProductManager.getInstance().getProductById(entry.getKey()).getPrice() * entry.getValue();
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

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
        if(state.equalState("paid")) {
            InvoiceManager.getInstance().createInvoice(this);
        }
    }

    public Map<Long, Integer> getContent() {
        return content;
    }

    public Map<Product, Integer> getProducts() {
        Map<Product, Integer> products = new HashMap<>();
        for (Map.Entry<Long, Integer> entry : content.entrySet()) {
            products.put(ProductManager.getInstance().getProductById(entry.getKey()), entry.getValue());
        }
        return products;
    }

    // Ajout de la méthode setContent
    public void setContent(Map<Long, Integer> content) {
        this.content = content;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

}
