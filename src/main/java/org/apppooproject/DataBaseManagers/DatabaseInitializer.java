package org.apppooproject.DataBaseManagers;

import java.sql.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class DatabaseInitializer {

    //initialization of the h2 database

    public static void initializeDatabase() {
        String url = "jdbc:h2:~/projectTest"; // Base de données H2 (mode fichier)
        String user = "sa";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             InputStream inputStream = DatabaseInitializer.class.getClassLoader().getResourceAsStream("backup.sql")) {

            if (inputStream == null) {
                throw new FileNotFoundException("Le fichier backup.sql est introuvable dans le classpath.");
            }

            //we get the sql script from backup.sql
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
            String url = "jdbc:h2:~/projectTest"; //url to the created database
            String user = "sa";
            String password = "";

            //connection to the database
            return DriverManager.getConnection(url, user, password);
            // "sa" is the default user for h2 and the password is empty by default
        } catch (SQLException e) {
            System.out.println("Error when trying to connect to the database" + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
