package org.apppooproject.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent; // Correct import
import org.apppooproject.DataBaseManagers.CustomerManager;
import org.apppooproject.Model.Customer;

import java.io.IOException;
import java.sql.*;

public class LoginController {

    @FXML
    private Button connection_button;

    @FXML
    private Label error_label;

    @FXML
    private Hyperlink hyperlinkToAccountCreation;

    @FXML
    private Hyperlink hyperlinkToAdminConnection;

    @FXML
    private TextField login_name;

    @FXML
    private PasswordField password;

    CustomerManager customerManager = new CustomerManager();

    // Action liée au bouton de connexion
    @FXML
    public void connectionToUserAccountAction(ActionEvent event) {
        String username = login_name.getText();
        String pwd = password.getText();


        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/baseSchema?useSSL=false", "root", "vautotwu");

            Customer customer = customerManager.getCustomerByID(username, pwd);
            if (customer!=null) {
                // Charger la nouvelle scène (client.fxml)
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sceneBuilderFiles/appView.fxml"));
                Parent customerViewPage = loader.load();

                // Récupérer la scène actuelle et la remplacer par la nouvelle
                Stage stage = (Stage) connection_button.getScene().getWindow();  // Récupérer la fenêtre actuelle
                Scene customerViewScene = new Scene(customerViewPage);
                stage.setScene(customerViewScene);  // Remplacer la scène actuelle par celle du client
                stage.show();  // Afficher la nouvelle scène

            } else {
                // Si l'utilisateur n'existe pas, afficher le label d'erreur
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.\n" +
                        "Create an account if needed :)");
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();  // Affiche les erreurs SQL ou d'IO
        }
    }

    // Méthode pour afficher des alertes
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Action liée au clic sur l'hyperlien pour la création de compte
    @FXML
    public void handleHyperlinkClick() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sceneBuilderFiles/Customer/signUp.fxml"));
        Parent SignUpPage = null;
        try {
            SignUpPage = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scene SignUpScene = new Scene(SignUpPage);

        Stage stage = (Stage) hyperlinkToAccountCreation.getScene().getWindow();
        stage.setScene(SignUpScene);
        stage.show();
    }
}
