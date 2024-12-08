package org.apppooproject.DataBaseManagers;

/*
 Interface for managing database operations for different entities.
 This interface defines basic operations, creation, deletion, and modification
 to be implemented by specific database manager classes.
 T is the type of the entity being managed : Invoice, Order, Product, Customer
 */
public interface DataManager<T> {

    //Adds a new element to the database.
    public void addAnElement(T t);
    //Modifies an existing element in the database
    public void modifyAnElement(T t);
    //deletes an element from the database.
    public void deleteAnElement(T t);
}
