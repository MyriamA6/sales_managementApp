package org.apppooproject.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apppooproject.DataBaseManagers.CustomerManager;
import org.apppooproject.Model.Customer;
import org.apppooproject.Views.ViewModel;


public class SignUpController {

    @FXML
    private TextField address;

    @FXML
    private TextField email;

    @FXML
    private TextField first_name;

    @FXML
    private TextField last_name;

    @FXML
    private TextField password;

    @FXML
    private Label error_already_exist_label;

    @FXML
    private TextField login_name;

    @FXML
    private TextField phone_number;

    @FXML
    private Button sign_up_button;

    private final CustomerManager customerManager = CustomerManager.getInstance();

    private final Customer connectedCustomer = customerManager.getConnectedCustomer();

    @FXML
    public void initialize() {
        error_already_exist_label.setVisible(false);
    }

    //verifier si le client n'est pas déjà dans la base, si ces coordonnées n'y sont pas déjà, mail/identifiant....
    @FXML
    void createANewCustomer(ActionEvent event) {
        if (customerManager.getCustomerByEmail(email.getText()) == null) {
            Customer c =new Customer(first_name.getText(),last_name.getText(),email.getText(), address.getText(), phone_number.getText(), login_name.getText(), password.getText());
            customerManager.addAnElement(c);
            ViewModel.getInstance().getViewFactory().showAppViewWindow();
        }
        else{
            error_already_exist_label.setVisible(true);

        }





    }

}
