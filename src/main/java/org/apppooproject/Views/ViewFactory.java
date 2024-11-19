package org.apppooproject.Views;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {

    public void showLoginWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sceneBuilderFiles/login.fxml"));
        createStage(loader);
    }

    public void showCustomerAccountWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sceneBuilderFiles/Customer/customerAccount.fxml"));
        createStage(loader);
    }

    public void showAppViewWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sceneBuilderFiles/appView.fxml"));
        createStage(loader);
    }

    public void showCartViewWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sceneBuilderFiles/Customer/cartView.fxml"));
        createStage(loader);
    }

    public void showSignUpWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sceneBuilderFiles/Customer/signUp.fxml"));
        createStage(loader);
    }

    public void showOrdersWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sceneBuilderFiles/Customer/ordersView.fxml"));
        createStage(loader);
    }

    public void showAdminLoginWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sceneBuilderFiles/Admin/adminLogin.fxml"));
        createStage(loader);
    }

    public void showAdminProductManager(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sceneBuilderFiles/Admin/productManager.fxml"));
        createStage(loader);
    }

    public void showAdminOrdersManager(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sceneBuilderFiles/Admin/ordersManager.fxml"));
        createStage(loader);
    }

    public void closeCurrentWindow(ActionEvent event) {
        // Ferme la fenêtre actuelle
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
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

    public void closeStage(Stage stage){
        stage.close();
    }



}
