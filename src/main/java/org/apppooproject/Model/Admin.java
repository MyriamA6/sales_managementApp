package org.apppooproject.Model;

//Admin singleton object to log into the admin session
public class Admin {
    private static Admin instance;
    private final String adminLogin;
    private final String adminPassword;

    //constructor method that defines the login and password of the admin
    private Admin() {
         adminLogin= "root";
         adminPassword = "root";
    }

    public static Admin getInstance() {
        if (instance == null) {
            instance = new Admin();
        }
        return instance;
    }

    //method to try to log to the admin session
    public boolean login(String adminLogin, String adminPassword) {
        return this.adminLogin.equals(adminLogin) && this.adminPassword.equals(adminPassword);
    }
}
