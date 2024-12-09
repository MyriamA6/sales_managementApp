package org.apppooproject.Service;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// Class to centralize the navigation between fxml pages
public class ViewFactory {

    public static void showLoginWindow() {
        FXMLLoader loader = new FXMLLoader(ViewFactory.class.getResource("/sceneBuilderFiles/login.fxml"));
        createStage(loader);
    }

    public static void showCustomerAccountWindow() {
        FXMLLoader loader = new FXMLLoader(ViewFactory.class.getResource("/sceneBuilderFiles/Customer/customerAccount.fxml"));
        createStage(loader);
    }

    public static void showAppViewWindow() {
        FXMLLoader loader = new FXMLLoader(ViewFactory.class.getResource("/sceneBuilderFiles/appView.fxml"));
        createStage(loader);
    }

    public static void showCartViewWindow() {
        FXMLLoader loader = new FXMLLoader(ViewFactory.class.getResource("/sceneBuilderFiles/Customer/cartView.fxml"));
        createStage(loader);
    }

    public static void showSignUpWindow() {
        FXMLLoader loader = new FXMLLoader(ViewFactory.class.getResource("/sceneBuilderFiles/Customer/signUp.fxml"));
        createStage(loader);
    }

    public static void showOrdersWindow() {
        FXMLLoader loader = new FXMLLoader(ViewFactory.class.getResource("/sceneBuilderFiles/Customer/ordersView.fxml"));
        createStage(loader);
    }

    public static void showAdminLoginWindow() {
        FXMLLoader loader = new FXMLLoader(ViewFactory.class.getResource("/sceneBuilderFiles/Admin/adminLogin.fxml"));
        createStage(loader);
    }

    public static void showAdminProductManager() {
        FXMLLoader loader = new FXMLLoader(ViewFactory.class.getResource("/sceneBuilderFiles/Admin/productManager.fxml"));
        createStage(loader);
    }

    public static void showAdminOrdersManager() {
        FXMLLoader loader = new FXMLLoader(ViewFactory.class.getResource("/sceneBuilderFiles/Admin/ordersManager.fxml"));
        createStage(loader);
    }

    public static void showInvoiceDisplay() {
        FXMLLoader loader = new FXMLLoader(ViewFactory.class.getResource("/sceneBuilderFiles/Customer/invoiceDisplay.fxml"));
        createStage(loader);
    }

    public static FXMLLoader showProductDetailsWindow() {
        FXMLLoader loader = new FXMLLoader(ViewFactory.class.getResource("/sceneBuilderFiles/productDetails.fxml"));
        createStage(loader);
        return loader;
    }

    public static void closeCurrentWindow(ActionEvent event) {
        // Ferme la fenêtre actuelle
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    private static void createStage(FXMLLoader loader) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Online shop");
        stage.show();
    }

    public static void closeStage(Stage stage) {
        stage.close();
    }
}
