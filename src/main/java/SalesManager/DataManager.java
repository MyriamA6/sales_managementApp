package SalesManager;

import java.sql.Connection;

public interface DataManager {

    public void addAnElement(Connection co);
    public void modifyAnElement(Connection co);
    public void deleteAnElement(Connection co);
}
