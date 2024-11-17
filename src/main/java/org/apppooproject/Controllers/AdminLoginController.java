package org.apppooproject.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apppooproject.Model.Admin;
import org.apppooproject.Model.Customer;
import org.apppooproject.Views.ViewModel;

public class AdminLoginController {

    @FXML
    private Button connection_button;

    @FXML
    private TextField login_name;

    @FXML
    private PasswordField password;

    @FXML
    private Label error_label;

    @FXML
    void connectionToUserAccountAction(ActionEvent event) {
        if (Admin.getInstance().login(login_name.getText(), password.getText())) {
            ViewModel.getInstance().getViewFactory().closeCurrentWindow(event);
            ViewModel.getInstance().getViewFactory().showAdminProductManager();
        } else {
            // Si l'utilisateur n'existe pas, afficher le label d'erreur
            error_label.setVisible(true);
        }

    }

}
