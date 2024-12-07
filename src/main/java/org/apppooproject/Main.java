package org.apppooproject;

import org.apppooproject.DataBaseManagers.DatabaseInitializer;

//Main class to start the project
public class Main {

    public static void main(String[] args) {
        //Initialization of the database
        DatabaseInitializer.initializeDatabase();

        //Call to the main launching the application
        //Necessary to allow javaFx to work in a jar
        MainApp.main(args);
    }

}
