package org.apppooproject.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.ActionEvent; // Correct import
import org.apppooproject.DataBaseManagers.CustomerManager;
import org.apppooproject.DataBaseManagers.CustomerManagerSingleton;
import org.apppooproject.Model.Customer;
import org.apppooproject.Views.ViewModel;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

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

    private final CustomerManager customerManager = CustomerManagerSingleton.getInstance().getCustomerManager();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        error_label.setVisible(false);
    }

    // Action liée au bouton de connexion
    @FXML
    public void connectionToUserAccountAction(ActionEvent event) {
        String username = login_name.getText();
        String pwd = password.getText();
        Customer customer = customerManager.getCustomerByID(username, pwd);
        if (customer!=null) {
            customerManager.setConnectedCustomer(customer);
            ViewModel.getInstance().getViewFactory().showAppViewWindow();
        } else {
            // Si l'utilisateur n'existe pas, afficher le label d'erreur
            error_label.setVisible(true);
        }
    }


    // Action liée au clic sur l'hyperlien pour la création de compte
    @FXML
    public void handleHyperlinkAdminConnection() {
        ViewModel.getInstance().getViewFactory().showAdminLoginWindow();
    }

    // Action liée au clic sur l'hyperlien pour la création de compte
    @FXML
    public void handleHyperlinkAccountCreation() {
        ViewModel.getInstance().getViewFactory().showSignUpWindow();
    }



}
