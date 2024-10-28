package org.apppooproject.DataBaseManagers;

public final class ProductManagerSingleton {

    private static ProductManagerSingleton instance;
    private final ProductManager productManager;
    private ProductManagerSingleton() {
        productManager = new ProductManager();
    }
    public static ProductManagerSingleton getInstance() {
        if (instance == null) {
            instance = new ProductManagerSingleton();
        }
        return instance;
    }

    public ProductManager getProductManager() {
        return productManager;
    }

}
