package org.apppooproject;

import org.apppooproject.DataBaseManagers.DatabaseInitializer;

//Main class to start the project
public class Main {
    public static void main(String[] args) {
      DatabaseInitializer.initializeDatabase();

        //Necessary to allow javaFx to work in a jar
        MainApp.main(args);
    }
}
