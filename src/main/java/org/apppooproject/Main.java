package org.apppooproject;

import org.apppooproject.DataBaseManagers.DatabaseInitializer;

public class Main {
    public static void main(String[] args) {
        DatabaseInitializer.initializeDatabase();

        MainApp.main(args);
    }
}
