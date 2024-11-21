package org.apppooproject.DataBaseManagers;

import java.sql.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class DatabaseInitializer {

    // Cette méthode initialise la base de données H2
    public static void initializeDatabase() {
        String url = "jdbc:h2:~/projectdbTest"; // Base de données H2 (mode fichier)
        String user = "sa";
        String password = "";

        // Chargement de backup.sql depuis le classpath
        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             InputStream inputStream = DatabaseInitializer.class.getClassLoader().getResourceAsStream("backup.sql")) {

            if (inputStream == null) {
                throw new FileNotFoundException("Le fichier backup.sql est introuvable dans le classpath.");
            }

            // Lire le fichier SQL et construire le script
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuilder sqlScript = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty() && !line.trim().startsWith("--")) {
                    sqlScript.append(line).append("\n");
                }
            }

            // Diviser et exécuter les commandes SQL
            String[] sqlCommands = sqlScript.toString().split(";");
            for (String command : sqlCommands) {
                if (!command.trim().isEmpty()) {
                    statement.execute(command.trim());
                }
            }

            System.out.println("Base de données initialisée avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode pour établir une connexion à H2
    public static Connection getH2Connection() {
        try {
            String url = "jdbc:h2:~/projectdbTest"; // Base de données H2 (mode fichier)
            String user = "sa";
            String password = "";
            // Connexion à une base de données H2 (en mémoire ou fichier)
            return DriverManager.getConnection(url, user, password);
            // "sa" est l'utilisateur par défaut pour H2, le mot de passe est vide par défaut
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la base de données H2 : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
