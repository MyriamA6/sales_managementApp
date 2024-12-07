package org.apppooproject.DataBaseManagers;

import java.sql.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class DatabaseInitializer {

    //initialization of the h2 database

    public static void initializeDatabase() {
        String dbPath = System.getProperty("user.home") + "/projetTest2.mv.db"; // Path to the created dataBase
        String url = "jdbc:h2:~/projetTest2";
        String user = "sa";
        String password = "";

        // Checking if the database already exist to not recreate it
        File dbFile = new File(dbPath);
        if (dbFile.exists()) {
            System.out.println("Database file already exists");
            return;
        }

        // Connection to the database and initialization using a h2 script
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             InputStream inputStream = DatabaseInitializer.class.getClassLoader().getResourceAsStream("backup.sql")) {

            if (inputStream == null) {
                throw new FileNotFoundException("The script file does not exist");
            }

            //We get the sql script from backup.sql
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuilder sqlScript = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty() && !line.trim().startsWith("--")) {
                    sqlScript.append(line).append("\n");
                }
            }

            // Divide and execute sql queries
            String[] sqlCommands = sqlScript.toString().split(";");
            for (String command : sqlCommands) {
                if (!command.trim().isEmpty()) {
                    statement.execute(command.trim());
                }
            }

            System.out.println("The dataBase has been initialized.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to get the connection from the created dataBase
    public static Connection getH2Connection() {
        try {
            String url = "jdbc:h2:~/projetTest2"; //url to the created database
            String user = "sa";
            String password = "";

            //connection to the database
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("Error when trying to connect to the database" + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
