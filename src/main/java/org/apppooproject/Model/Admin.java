package org.apppooproject.Model;


public class Admin {
    private static Admin instance;
    private String adminLogin;
    private String adminPassword;

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

    public boolean login(String adminLogin, String adminPassword) {
        return this.adminLogin.equals(adminLogin) && this.adminPassword.equals(adminPassword);
    }
}
