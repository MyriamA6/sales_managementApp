package org.apppooproject.DataBaseManagers;

import org.apppooproject.Model.Order;

import java.sql.Connection;

public interface DataManager<T> {

    public void addAnElement(T t);
    public void modifyAnElement(T t);
    public void deleteAnElement(T t);
}
