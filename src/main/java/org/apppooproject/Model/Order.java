package org.apppooproject.Model;

import org.apppooproject.DataBaseManagers.InvoiceManager;
import org.apppooproject.DataBaseManagers.ProductManager;
import org.apppooproject.Service.OrderState;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//Customer class that models an instance of the order_record table of the database
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

    //Method to update the total price of the content of the order
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

    //Setter of the state of the order
    //If it is set to "paid" an invoice is generated and associated to this order
    public void setState(OrderState state) {
        this.state = state;
        if(state.equalState("paid")) {
            InvoiceManager.getInstance().createInvoice(this);
        }
    }

    public Map<Long, Integer> getContent() {
        return content;
    }

    // helper getter method to return the content of the order
    // but in the format of a Map<Product,Quantity> and not Map<ProductId,Quantity>
    public Map<Product, Integer> getProducts() {
        Map<Product, Integer> products = new HashMap<>();
        for (Map.Entry<Long, Integer> entry : content.entrySet()) {
            products.put(ProductManager.getInstance().getProductById(entry.getKey()), entry.getValue());
        }
        return products;
    }

    public void setContent(Map<Long, Integer> content) {
        this.content = content;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

}
