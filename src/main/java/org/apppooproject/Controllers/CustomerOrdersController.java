package org.apppooproject.Controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apppooproject.DataBaseManagers.CustomerManager;
import org.apppooproject.DataBaseManagers.InvoiceManager;
import org.apppooproject.DataBaseManagers.OrderManager;
import org.apppooproject.Model.Customer;
import org.apppooproject.Model.Invoice;
import org.apppooproject.Model.Order;
import org.apppooproject.Views.AlertShowing;
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

        orders.getItems().addAll(customerOrders);
    }

    private void reloadOrders() {
        orders.getItems().clear();
        loadCustomerOrders();
    }

    @FXML
    void onClickGenerateAssociatedInvoice(ActionEvent event) {
        Order order = orders.getSelectionModel().getSelectedItem();
        if (order != null && !(order.getState()).equalsIgnoreCase("in progress")) {
            Invoice invoice = InvoiceManager.getInstance().getInvoiceByOrderId(order.getOrderId());
            InvoiceManager.getInstance().setSelectedInvoice(invoice);
            if(invoice==null){
                System.out.println("Invoice is null");
                return;
            }
            invoice.generateInvoice();
            viewModel.getViewFactory().showInvoiceDisplay();
        }
        else{
            AlertShowing.showAlert("Not paid yet","Order not paid yet", Alert.AlertType.WARNING);
        }
    }
    @FXML
    void onClickDeleteOrder(ActionEvent event) {
        Order order = orders.getSelectionModel().getSelectedItem();
        if (order != null && !(order.getState()).equalsIgnoreCase("in progress")) {
            orderManager.deleteAnElement(order);
            reloadOrders();
        }
        else{
            AlertShowing.showAlert("Order already paid or delivered","Order already paid or delivered", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onClickPayOrder(ActionEvent event) {
        Order order = orders.getSelectionModel().getSelectedItem();
        if (order != null && (order.getState()).equalsIgnoreCase("in progress")) {
            order.setState("paid");
            OrderManager.getInstance().modifyAnElement(order);
            AlertShowing.showAlert("Order successfully paid","Order successfully paid", Alert.AlertType.CONFIRMATION);
            reloadOrders();
        }
        else{
            AlertShowing.showAlert("Order already paid or delivered","Order already paid or delivered", Alert.AlertType.ERROR);
        }

    }

    @FXML
    void onClickModifyOrder(ActionEvent event) {
        Order order = orders.getSelectionModel().getSelectedItem();
        if (order != null && (order.getState()).equalsIgnoreCase("in progress")) {
            if(!connectedCustomer.getCart().isEmpty()){
                connectedCustomer.storeOrder();
            }
            connectedCustomer.addAllToCart(order.getProducts());
            AlertShowing.showAlert("Warning","Some products may not be available anymore.", Alert.AlertType.WARNING);
            orderManager.deleteAnElement(order);
            viewModel.getViewFactory().closeCurrentWindow(event);
            viewModel.getViewFactory().showCartViewWindow();
            reloadOrders();
        }
        else{
            AlertShowing.showAlert("Order already paid or delivered","Order already paid or delivered", Alert.AlertType.ERROR);
        }

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
