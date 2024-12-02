package org.apppooproject;

import org.apppooproject.DataBaseManagers.DatabaseInitializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("~/projectTest");
        DatabaseInitializer.initializeDatabase();

        //Necessary to allow javaFx to work in a jar
        MainApp.main(args);
    }
}
