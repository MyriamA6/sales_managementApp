package org.apppooproject.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import org.apppooproject.DataBaseManagers.CustomerManager;
import org.apppooproject.Model.Customer;
import org.apppooproject.Views.ViewModel;


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

    private final CustomerManager customerManager = CustomerManager.getInstance();

    public void initialize() {
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
            ViewModel.getInstance().getViewFactory().closeCurrentWindow(event);
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
