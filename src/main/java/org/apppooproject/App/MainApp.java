package org.apppooproject.App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//Class to launch the application using javaFx
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        //We charge the first GUI
        Parent root = FXMLLoader.load(getClass().getResource("/sceneBuilderFiles/login.fxml"));
        primaryStage.setTitle("Central View");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
