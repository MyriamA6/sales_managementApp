package org.apppooproject.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apppooproject.Model.Admin;
import org.apppooproject.Service.ViewFactory;

// Class controller in charge of the admin log in interface
public class AdminLoginController {


    @FXML
    private TextField login_name;

    @FXML
    private PasswordField password;

    @FXML
    private Label error_label;

    public void initialize() {
        error_label.setVisible(false);
    }

    //method to try to log into the admin view
    @FXML
    void connectionToUserAccountAction(ActionEvent event) {
        if (Admin.getInstance().login(login_name.getText(), password.getText())) {
            ViewFactory.closeCurrentWindow(event);
            ViewFactory.showAdminProductManager();
        } else {
            // if the login_name or the password are incorrect
            error_label.setVisible(true);
        }

    }

    //method activated when a customer try to log out of its session
    @FXML
    void onClickGoToLoginView(ActionEvent event) {
        ViewFactory.closeCurrentWindow(event);
        ViewFactory.showLoginWindow();
    }

}
