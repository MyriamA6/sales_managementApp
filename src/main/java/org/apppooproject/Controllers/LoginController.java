package org.apppooproject.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import org.apppooproject.DataBaseManagers.CustomerManager;
import org.apppooproject.Model.Customer;
import org.apppooproject.Service.Password;
import org.apppooproject.Service.ViewFactory;


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
        String pwd = Password.hashPassword(password.getText());
        Customer customer = customerManager.getCustomerByLogin(username, pwd);
        if (customer!=null) {
            customerManager.setConnectedCustomer(customer);
            ViewFactory.closeCurrentWindow(event);
            ViewFactory.showAppViewWindow();
        } else {
            // Si l'utilisateur n'existe pas, afficher le label d'erreur
            error_label.setVisible(true);
        }
    }


    // Action liée au clic sur l'hyperlien pour la création de compte
    @FXML
    public void handleHyperlinkAdminConnection(ActionEvent event) {
        ViewFactory.closeCurrentWindow(event);
        ViewFactory.showAdminLoginWindow();
    }

    // Action liée au clic sur l'hyperlien pour la création de compte
    @FXML
    public void handleHyperlinkAccountCreation(ActionEvent event) {
        ViewFactory.closeCurrentWindow(event);
        ViewFactory.showSignUpWindow();
    }



}
