package org.apppooproject.Controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.apppooproject.DataBaseManagers.InvoiceManager;
import org.apppooproject.DataBaseManagers.OrderManager;
import org.apppooproject.Model.Order;
import org.apppooproject.Service.AlertShowing;
import org.apppooproject.Service.HelperMethod;
import org.apppooproject.Service.OrderState;
import org.apppooproject.Service.ViewFactory;

import java.util.ArrayList;

// Controller class in charge of the order manager interface of the Admin
public class AdminOrdersController {

    @FXML
    private RadioButton payed_button;

    @FXML
    private RadioButton delivered_button;

    @FXML
    private RadioButton inProgress_button;

    //Table displaying the orders of the database
    @FXML private TableView<Order> ordersTable;
    @FXML private TableColumn<Order, Long> orderID;
    @FXML private TableColumn<Order, String> orderDate;
    @FXML private TableColumn<Order, Double> orderTotalPrice;
    @FXML private TableColumn<Order, String> orderStatus;
    @FXML private TableColumn<Order, String> orderUsername;
    //Text field to search for an order
    @FXML private TextField searchField;


    private final OrderManager orderManager = OrderManager.getInstance();


    // Initialization of the table columns and setting up the interface
    @FXML
    public void initialize() {
        // Set the table columns
        orderID.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getOrderId()).asObject());
        orderDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateOrder().toString()));
        orderTotalPrice.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotalPrice()).asObject());
        orderStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getState().getState()));
        orderUsername.setCellValueFactory(cellData -> new SimpleStringProperty(
               cellData.getValue().getCustomer().getLoginName()
        ));
        ToggleGroup statusToggleGroup = new ToggleGroup();
        payed_button.setToggleGroup(statusToggleGroup);
        delivered_button.setToggleGroup(statusToggleGroup);
        inProgress_button.setToggleGroup(statusToggleGroup);
        // Load orders for the connected customer
        loadOrders();
    }

    // Method to load the orders associated to a customer
    private void loadOrders() {
        try {
            ArrayList<Order> orders = orderManager.getAllElements();
            ordersTable.getItems().clear();
            if (orders != null) {
                ordersTable.getItems().addAll(orders);
            }
        } catch (Exception e) {
            AlertShowing.showAlert("Error", "Failed to load orders: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    //Reload the orders visible in the table of the table view of the interface
    private void updateTable(){
        loadOrders();
        ordersTable.refresh();
    }

    @FXML
    void onClickGoToProductManager(ActionEvent event) {
        ViewFactory.closeCurrentWindow(event);
        ViewFactory.showAdminProductManager();
    }

    // Method to filter orders based on the customer's username written in the search field
    @FXML
    void giveOrdersByUsername(ActionEvent event) {
        if (!(HelperMethod.removeExtraSpaces(searchField.getText()).isEmpty())){
            ordersTable.getItems().clear();
            ordersTable.getItems().addAll(orderManager.getOrdersByCustomerUsername(searchField.getText()));
            ordersTable.refresh();
        }
        else{
            updateTable();
        }
    }

    //Displays the current state of the selected order
    @FXML
    void onRowClicked(MouseEvent event) {
        inProgress_button.setDisable(false);
        payed_button.setDisable(false);
        delivered_button.setDisable(false);
        Order order = ordersTable.getSelectionModel().getSelectedItem();
        if (order != null) {
            if (order.getState().equals(OrderState.IN_PROGRESS)){
                inProgress_button.setSelected(true);
                payed_button.setDisable(true);
                delivered_button.setDisable(true);
            }
            else if (order.getState().equals(OrderState.PAID)){
                payed_button.setSelected(true);
                inProgress_button.setDisable(true);
            }
            else if (order.getState().equals(OrderState.DELIVERED)){
                delivered_button.setSelected(true);
                payed_button.setDisable(true);
                inProgress_button.setDisable(true);
            }
        }
    }

    //Updates the order status of the selected order
    @FXML
    void onClickChangeOrderStatus(ActionEvent event) {
        Order order = ordersTable.getSelectionModel().getSelectedItem();
        if (payed_button.isSelected() && order.getState().equals(OrderState.IN_PROGRESS)) {
            order.setState(OrderState.PAID);
            orderManager.modifyAnElement(order);
            InvoiceManager.getInstance().createInvoice(order);
        }
        else
            if(delivered_button.isSelected() && order.getState().equals(OrderState.PAID)){
            order.setState(OrderState.DELIVERED);
            orderManager.modifyAnElement(order);}
        updateTable();
        inProgress_button.setDisable(false);
        delivered_button.setDisable(false);
    }

    //Deletes the selected order if it has the state IN_PROGRESS
    @FXML
    void onClickDeleteOrder(ActionEvent event) {
        Order order = ordersTable.getSelectionModel().getSelectedItem();
        if (order != null) {
            if(!order.getState().equals(OrderState.IN_PROGRESS)){
                AlertShowing.showAlert("Impossible","Order already paid or delivered", Alert.AlertType.ERROR);
            }
            else{
                orderManager.deleteAnElement(order);
                AlertShowing.showAlert("Success","Order successfully deleted", Alert.AlertType.INFORMATION);
                updateTable();
            }
        }


    }

}

