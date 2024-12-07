package org.apppooproject.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apppooproject.DataBaseManagers.CustomerManager;
import org.apppooproject.Model.Customer;
import org.apppooproject.Service.HelperMethod;
import org.apppooproject.Service.Password;
import org.apppooproject.Service.AlertShowing;
import org.apppooproject.Service.ViewFactory;

//Controller class associated to the Sign Up page
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
    private Label error_label;

    @FXML
    private TextField phone_number;

    private final CustomerManager customerManager = CustomerManager.getInstance();


    //Method to hide the label when the Sign Up page is first opened
    public void initialize() {
        error_label.setVisible(false);
    }


    @FXML
    void createANewCustomer(ActionEvent event) {
        String firstNameText = first_name.getText();
        String lastNameText = last_name.getText();
        String emailText = email.getText();
        String phoneNumberText = phone_number.getText();
        String addressText = address.getText();
        String userIdText = user_id.getText();
        String passwordText = password.getText();

        // First we check if the fields are filled and valid
        if (nothingIsNullOrEmpty(firstNameText, lastNameText, emailText, phoneNumberText, addressText, userIdText, passwordText) &&
                Customer.isPossibleToCreateCustomer(firstNameText, lastNameText, emailText, phoneNumberText)) {

            // Then we check if no other customer has an account with the same email or id
            if ((customerManager.getCustomerByEmail(emailText) == null) && (customerManager.getCustomerByLoginName(userIdText) == null)) {
                Customer c = new Customer(firstNameText, lastNameText, emailText, addressText, phoneNumberText, userIdText, Password.hashPassword(passwordText));
                // Then we create and add the customer to the database
                customerManager.addAnElement(c);
                AlertShowing.showAlert("Creation successful", "New account created!", Alert.AlertType.INFORMATION);
                ViewFactory.closeCurrentWindow(event);
                ViewFactory.showAppViewWindow();
            } else {
                AlertShowing.showAlert("Error", "Email address or ID already used", Alert.AlertType.ERROR);
            }
        } else {
            // Otherwise we indicate to the customer that some fields are not valid
            error_label.setVisible(true);
        }
    }


    //method activated when a customer try to log out of its session
    @FXML
    void onClickGoToLoginView(ActionEvent event) {
        ViewFactory.closeCurrentWindow(event);
        ViewFactory.showLoginWindow();
    }

    //method to check if the given arguments are null
    public boolean nothingIsNullOrEmpty(String ... strings) {
        for (String s : strings) {
            if (s == null) return false;
            else{
                if(HelperMethod.removeExtraSpaces(s).isEmpty()) return false;
            }
        }
        return true;
    }
}
