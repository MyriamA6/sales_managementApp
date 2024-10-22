package org.apppooproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent; // Correct import

import java.io.IOException;
import java.sql.*;

public class LoginController {

    @FXML
    private Button connexion_button;

    @FXML
    private Hyperlink hyperlinkToAccountCreation;

    @FXML
    private TextField login_name;

    @FXML
    private PasswordField password;

    // Action liée au bouton de connexion
    @FXML
    public void connectionToUserAccountAction(ActionEvent event) {
        String username = login_name.getText();
        String pwd = password.getText();


        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/baseSchema?useSSL=false", "root", "vautotwu");

            // Utilisation de PreparedStatement pour éviter les injections SQL
            String sql = "SELECT * FROM Customer WHERE login_name = ? AND user_password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, pwd);
            ResultSet res = stmt.executeQuery();
            if (res.next()) {
                // Charger la nouvelle scène (client.fxml)
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/DataBaseManagers/appCentralView.fxml"));
                Parent customerViewPage = loader.load();

                // Récupérer la scène actuelle et la remplacer par la nouvelle
                Stage stage = (Stage) connexion_button.getScene().getWindow();  // Récupérer la fenêtre actuelle
                Scene customerViewScene = new Scene(customerViewPage);
                stage.setScene(customerViewScene);  // Remplacer la scène actuelle par celle du client
                stage.show();  // Afficher la nouvelle scène

            } else {
                // Si l'utilisateur n'existe pas, afficher un message d'erreur
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
