package org.apppooproject.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    
    private long id;
    private long customerId;
    private Date dateOrder;
    private int totalPrice;
    private String champ;
    private State state;
    private List<Product> content;

    public enum State {
    	IN_PROGRESS, // En cours
        CONFIRMED, // Validée
        CANCELED, // Annulée
        PROCESSED // Traitée
    }

    
    public Order(long id, long customerId, int totalPrice, Date dateOrder, State state) {
        this.id = id;
        this.customerId = customerId;
        this.totalPrice = totalPrice;
        this.dateOrder = dateOrder;
        this.state = state;
        this.content = new ArrayList<>();
    }

    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public Date getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(Date dateOrder) {
        this.dateOrder = dateOrder;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getChamp() {
        return champ;
    }

    public void setChamp(String champ) {
        this.champ = champ;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public List<Product> getContent() {
        return content;
    }

    public void setContent(List<Product> content) {
        this.content = content;
    }


    public int calculateTotalPrice() {
        int total = 0;
        for (Product product : content) {
            total += product.getPrice(); 
        }
        return total;
    }


    public void validateOrder() {
        if (this.state == State.IN_PROGRESS) {
            this.state = State.CONFIRMED;
        }
    }


    public void cancelOrder() {
        if (this.state == State.IN_PROGRESS) {
            this.state = State.CANCELED;
        }
    }


    public void addProductToOrder(Product product, int quantity) {
        for (int i = 0; i < quantity; i++) {
            this.content.add(product);
        }
    }


    public static Order makeAnOrder(List<Product> products, Customer customer) {
        Order order = new Order(0, customer.getId(), 0, new Date(), State.IN_PROGRESS);
        for (Product product : products) {
            order.addProductToOrder(product, 1); 
        }
        order.totalPrice = order.calculateTotalPrice();
        return order;
    }


    public void removeProductFromOrder(Product product) {
        this.content.remove(product);
        this.totalPrice = calculateTotalPrice(); 
    }
}
