package org.apppooproject.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {

    public void showLoginWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        createStage(loader);
    }

    public void showCustomerAccountWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("customerAccount.fxml"));
        createStage(loader);
    }

    public void showAppViewWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("appView.fxml"));
        createStage(loader);
    }

    public void showCartViewWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("cartView.fxml"));
        createStage(loader);
    }

    private void createStage(FXMLLoader loader){
        Scene scene = null;
        try{
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Online shop");
        stage.show();
    }



}
