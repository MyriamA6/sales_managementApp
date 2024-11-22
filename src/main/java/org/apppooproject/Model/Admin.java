package org.apppooproject.Model;

//Admin object to log in to the admin session
public class Admin {
    private static Admin instance;
    private final String adminLogin;
    private final String adminPassword;

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

    //method to try to log in
    public boolean login(String adminLogin, String adminPassword) {
        return this.adminLogin.equals(adminLogin) && this.adminPassword.equals(adminPassword);
    }
}
