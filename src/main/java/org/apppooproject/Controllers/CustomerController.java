package org.apppooproject.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apppooproject.DataBaseManagers.CustomerManager;
import org.apppooproject.Model.Customer;
import org.apppooproject.Service.AlertShowing;
import org.apppooproject.Service.HelperMethod;
import org.apppooproject.Service.ViewFactory;

//Class Controller to control the javaFX UI of the customer's account's interface
public class CustomerController {

    @FXML
    private TextField address;

    @FXML
    private TextField email;

    @FXML
    private TextField first_name;

    @FXML
    private TextField last_name;

    @FXML
    private PasswordField new_password;

    @FXML
    private TextField phone_number;

    @FXML
    private TextField login_name;

    private final CustomerManager customerManager = CustomerManager.getInstance();

    private final Customer connectedCustomer = customerManager.getConnectedCustomer();

    //Initialization of the field the customer's information
    @FXML
    public void initialize() {
        first_name.setText(connectedCustomer.getFirstName());
        last_name.setText(connectedCustomer.getLastName());
        email.setText(connectedCustomer.getEmail());
        phone_number.setText(connectedCustomer.getPhoneNumber());
        address.setText(connectedCustomer.getAddress());
        login_name.setText(connectedCustomer.getLoginName());
    }

    //method that is activated when a customer
    @FXML
    void applyChanges(ActionEvent event) {

        String firstName = first_name.getText();
        String lastName = last_name.getText();
        String e_mail = email.getText();
        String phoneNumber = phone_number.getText();
        String addressText = address.getText();
        String loginName = login_name.getText();
        String userPassword = new_password.getText();

        if (HelperMethod.nothingIsNullOrEmpty(firstName,lastName,e_mail,phoneNumber,addressText,loginName) && Customer.isPossibleToCreateCustomer(firstName,lastName,e_mail,phoneNumber)){
            connectedCustomer.setFirstName(firstName);
            connectedCustomer.setLastName(lastName);
            connectedCustomer.setEmail(e_mail);
            connectedCustomer.setPhoneNumber(phoneNumber);
            connectedCustomer.setAddress(addressText);
            connectedCustomer.setLoginName(loginName);
            connectedCustomer.setUserPassword(userPassword);
            customerManager.modifyAnElement(connectedCustomer);
            AlertShowing.showAlert("Account successfully updated","Account has been updated", Alert.AlertType.INFORMATION);
        }
        else{
            AlertShowing.showAlert("Account cannot be updated","Some fields are empty or invalid", Alert.AlertType.ERROR);
        }

    }

    @FXML
    void deleteTheAccount(ActionEvent event) {
        customerManager.deleteAnElement(connectedCustomer);
        AlertShowing.showAlert("Account deleted","Account successfully deleted", Alert.AlertType.INFORMATION);
        ViewFactory.closeCurrentWindow(event);
        ViewFactory.showLoginWindow();
    }

    @FXML
    void goToCentralView(ActionEvent event) {
        ViewFactory.closeCurrentWindow(event);
        ViewFactory.showAppViewWindow();
    }

    @FXML
    void goToOrderView(ActionEvent event) {
        ViewFactory.closeCurrentWindow(event);
        ViewFactory.showOrdersWindow();
    }

}
