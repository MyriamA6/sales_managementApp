package org.apppooproject.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apppooproject.DataBaseManagers.CustomerManager;
import org.apppooproject.Model.Customer;
import org.apppooproject.Views.ViewModel;

public class CustomerController {

    @FXML
    private TextField address;

    @FXML
    private Button applyChanges_button;

    @FXML
    private Button deleteProfile_button;

    @FXML
    private TextField email;

    @FXML
    private TextField first_name;

    @FXML
    private Button invoice_button;

    @FXML
    private TextField last_name;

    @FXML
    private Button menu_button;

    @FXML
    private PasswordField new_password;

    @FXML
    private Button orders_button;

    @FXML
    private TextField phone_number;

    @FXML
    private TextField login_name;

    private final CustomerManager customerManager = CustomerManager.getInstance();

    private final Customer connectedCustomer = customerManager.getConnectedCustomer();

    private final ViewModel viewModel = ViewModel.getInstance();

    @FXML
    public void initialize() {
        first_name.setText(connectedCustomer.getFirstName());
        last_name.setText(connectedCustomer.getLastName());
        email.setText(connectedCustomer.getEmail());
        phone_number.setText(connectedCustomer.getPhoneNumber());
        address.setText(connectedCustomer.getAddress());
        login_name.setText(connectedCustomer.getLoginName());
    }

    @FXML
    void applyChanges(ActionEvent event) {
        connectedCustomer.setFirstName(first_name.getText());
        connectedCustomer.setLastName(last_name.getText());
        connectedCustomer.setEmail(email.getText());
        connectedCustomer.setPhoneNumber(phone_number.getText());
        connectedCustomer.setAddress(address.getText());
        connectedCustomer.setLoginName(login_name.getText());
        System.out.println(new_password.getText());
        System.out.println(new_password.getText().isEmpty());
        System.out.println(new_password.getText().equals(" "));
        connectedCustomer.setUserPassword(new_password.getText());
        customerManager.modifyAnElement(connectedCustomer);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Account successfully updated");
        alert.setHeaderText(null); // Pas de texte dans l'en-tête
        alert.setContentText("Account has been updated");
        alert.showAndWait(); // Affiche l'alerte et attend que l'utilisateur la ferme
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
    void goToInvoiceView(ActionEvent event) {

    }

    @FXML
    void goToOrderView(ActionEvent event) {
        viewModel.getViewFactory().closeCurrentWindow(event);
        viewModel.getViewFactory().showOrdersWindow();
    }

}
