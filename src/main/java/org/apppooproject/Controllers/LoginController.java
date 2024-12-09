package org.apppooproject.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import org.apppooproject.DataBaseManagers.CustomerManager;
import org.apppooproject.Model.Customer;
import org.apppooproject.Service.Password;
import org.apppooproject.Service.ViewFactory;

// Controller class to handle the connection of a user to its account
public class LoginController {

    @FXML
    private Label error_label;

    @FXML
    private TextField login_name;

    @FXML
    private PasswordField password;

    private final CustomerManager customerManager = CustomerManager.getInstance();

    public void initialize() {
        error_label.setVisible(false);
    }

    //On action of the associated button tries to connect to the account of a customer
    @FXML
    public void connectionToUserAccountAction(ActionEvent event) {
        String username = login_name.getText();
        String pwd = Password.hashPassword(password.getText()); //hashing of the given password
        Customer customer = customerManager.getCustomerByLogin(username, pwd); // trying to match the username with the given password to a customer of the database
        if (customer!=null) {
            customerManager.setConnectedCustomer(customer);
            ViewFactory.closeCurrentWindow(event);
            ViewFactory.showAppViewWindow();
        } else {
            error_label.setVisible(true);
        }
    }


    // On click of the associated hyperlink, we move to the Admin connection interface
    @FXML
    public void handleHyperlinkAdminConnection(ActionEvent event) {
        ViewFactory.closeCurrentWindow(event);
        ViewFactory.showAdminLoginWindow();
    }

    // On click of the associated hyperlink, we move to the signup interface
    @FXML
    public void handleHyperlinkAccountCreation(ActionEvent event) {
        ViewFactory.closeCurrentWindow(event);
        ViewFactory.showSignUpWindow();
    }



}
