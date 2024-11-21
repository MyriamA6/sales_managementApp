package org.apppooproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apppooproject.DataBaseManagers.DatabaseInitializer;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Initialization of the database
        // Charger le fichier FXML depuis le chemin des ressources
        Parent root = FXMLLoader.load(getClass().getResource("/sceneBuilderFiles/login.fxml"));
        primaryStage.setTitle("Central View");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
