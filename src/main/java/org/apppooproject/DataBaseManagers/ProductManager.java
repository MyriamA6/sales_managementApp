package org.apppooproject.DataBaseManagers;

import org.apppooproject.Model.Product;

import java.sql.Connection;

public abstract class ProductManager implements DataManager<Product>{
    private Connection conn;

    @Override
    public void addAnElement(Product product) {

    }

    @Override
    public void modifyAnElement(Product product) {

    }

    @Override
    public void deleteAnElement(Product product) {

    }


}
