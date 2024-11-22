package org.apppooproject;

import org.apppooproject.DataBaseManagers.DatabaseInitializer;

public class Main {
    public static void main(String[] args) {
        //Initialization of the database
        DatabaseInitializer.initializeDatabase();

        //Necessary to allow javaFx to work in a jar
        MainApp.main(args);
    }
}
