package org.apppooproject.Controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apppooproject.DataBaseManagers.CustomerManager;
import org.apppooproject.DataBaseManagers.OrderManager;
import org.apppooproject.Model.Customer;
import org.apppooproject.Model.Order;
import org.apppooproject.Views.ViewModel;

import java.util.ArrayList;

public class CustomerOrdersController {

    // Declare UI components
    @FXML private Button account_button, menu_button;
    @FXML private TableView<Order> orders;
    @FXML private TableColumn<Order, Long> orderID;
    @FXML private TableColumn<Order, String> orderDate;
    @FXML private TableColumn<Order, Double> orderTotalPrice;
    @FXML private TableColumn<Order, String> orderStatus;

    // Declare manager instances
    private final OrderManager orderManager = OrderManager.getInstance();
    private final ViewModel viewModel = ViewModel.getInstance();

    private Customer connectedCustomer;

    @FXML
    public void initialize() {
        // Set up the UI components
        connectedCustomer = CustomerManager.getInstance().getConnectedCustomer();
        // Set the table columns
        orderID.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getOrderId()).asObject());
        orderDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateOrder().toString()));
        orderTotalPrice.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotalPrice()).asObject());
        orderStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getState()));

        // Load orders for the connected customer
        loadCustomerOrders();
    }

    private void loadCustomerOrders() {
        // Fetch all orders for the connected customer
        ArrayList<Order> customerOrders = orderManager.getOrdersByCustomerId(connectedCustomer.getCustomerId());

        // Display the orders in the TableView
        orders.getItems().clear();
        orders.getItems().addAll(customerOrders);
    }

    @FXML
    void onClickGoToAccount(ActionEvent event) {
        viewModel.getViewFactory().closeCurrentWindow(event);
        viewModel.getViewFactory().showCustomerAccountWindow();
    }

    @FXML
    void onClickGoToMenu(ActionEvent event) {
        viewModel.getViewFactory().closeCurrentWindow(event);
        viewModel.getViewFactory().showAppViewWindow();
    }
}
