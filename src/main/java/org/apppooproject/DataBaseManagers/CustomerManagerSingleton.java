package org.apppooproject.DataBaseManagers;

public final class CustomerManagerSingleton {

    private static CustomerManagerSingleton instance;
    private final CustomerManager customerManager;
    private CustomerManagerSingleton() {
        customerManager = new CustomerManager();
    }
    public static CustomerManagerSingleton getInstance() {
        if (instance == null) {
            instance = new CustomerManagerSingleton();
        }
        return instance;
    }
    public CustomerManager getCustomerManager() {
        return customerManager;
    }


}
