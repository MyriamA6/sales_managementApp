package org.apppooproject.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apppooproject.DataBaseManagers.CustomerManager;
import org.apppooproject.Model.Customer;
import org.apppooproject.Views.AlertShowing;
import org.apppooproject.Views.ViewModel;

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

    private final ViewModel viewModel = ViewModel.getInstance();

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
        connectedCustomer.setFirstName(first_name.getText());
        connectedCustomer.setLastName(last_name.getText());
        connectedCustomer.setEmail(email.getText());
        connectedCustomer.setPhoneNumber(phone_number.getText());
        connectedCustomer.setAddress(address.getText());
        connectedCustomer.setLoginName(login_name.getText());
        connectedCustomer.setUserPassword(new_password.getText());
        customerManager.modifyAnElement(connectedCustomer);
        AlertShowing.showAlert("Account successfully updated","Account has been updated", Alert.AlertType.INFORMATION);
    }

    @FXML
    void deleteTheAccount(ActionEvent event) {
        customerManager.deleteAnElement(connectedCustomer);


    }

    @FXML
    void goToCentralView(ActionEvent event) {
        viewModel.getViewFactory().closeCurrentWindow(event);
        viewModel.getViewFactory().showAppViewWindow();
    }

    @FXML
    void goToOrderView(ActionEvent event) {
        viewModel.getViewFactory().closeCurrentWindow(event);
        viewModel.getViewFactory().showOrdersWindow();
    }

}
