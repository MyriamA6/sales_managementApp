package org.apppooproject.Model;

import java.util.ArrayList;

public class Customer {
    private long customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phoneNumber;
    private String loginName;
    private String userPassword;
    private ArrayList<Product> cart;


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

    public void addToCart(Product product){

    }

    public void suppressFromCart(Product product){}

    public Orders payCart(){
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
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }


}
