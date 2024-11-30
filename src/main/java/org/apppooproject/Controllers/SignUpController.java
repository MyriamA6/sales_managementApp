package org.apppooproject.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.apppooproject.DataBaseManagers.CustomerManager;
import org.apppooproject.Model.Customer;
import org.apppooproject.Service.Password;
import org.apppooproject.Views.AlertShowing;
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
    private TextField user_id;

    @FXML
    private TextField phone_number;

    @FXML
    private Button sign_up_button;

    private final CustomerManager customerManager = CustomerManager.getInstance();

    //Method to create a new customer if it isn't already registered
    @FXML
    void createANewCustomer(ActionEvent event) {
        if ((customerManager.getCustomerByEmail(email.getText()) == null) && (customerManager.getCustomerByLoginName(user_id.getText()) == null)) {
            Customer c =new Customer(first_name.getText(),last_name.getText(),email.getText(), address.getText(), phone_number.getText(), user_id.getText(), Password.hashPassword(password.getText()));
            customerManager.addAnElement(c);
            ViewModel.getInstance().getViewFactory().closeCurrentWindow(event);
            ViewModel.getInstance().getViewFactory().showAppViewWindow();
        }
        else{
            AlertShowing.showAlert("Error", "Email address or ID already used", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onClickGoToLoginView(ActionEvent event) {
        ViewModel.getInstance().getViewFactory().closeCurrentWindow(event);
        ViewModel.getInstance().getViewFactory().showLoginWindow();
    }

}
