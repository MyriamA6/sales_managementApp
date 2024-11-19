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
import org.apppooproject.Views.ViewModel;

import java.util.ArrayList;

public class AdminOrdersController {

    @FXML
    private RadioButton confirmed_button;

    @FXML
    private RadioButton delivered_button;

    @FXML
    private RadioButton inProgress_button;

    @FXML private TableView<Order> ordersTable;
    @FXML private TableColumn<Order, Long> orderID;
    @FXML private TableColumn<Order, String> orderDate;
    @FXML private TableColumn<Order, Double> orderTotalPrice;
    @FXML private TableColumn<Order, String> orderStatus;

    @FXML
    private Button productManager_button;

    // Declare manager instances
    private final OrderManager orderManager = OrderManager.getInstance();
    private final ViewModel viewModel = ViewModel.getInstance();


    @FXML
    public void initialize() {
        // Set the table columns
        orderID.setCellValueFactory(cellData -> new SimpleLongProperty(cellData.getValue().getOrderId()).asObject());
        orderDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateOrder().toString()));
        orderTotalPrice.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotalPrice()).asObject());
        orderStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getState()));

        ToggleGroup statusToggleGroup = new ToggleGroup();
        confirmed_button.setToggleGroup(statusToggleGroup);
        delivered_button.setToggleGroup(statusToggleGroup);
        inProgress_button.setToggleGroup(statusToggleGroup);
        // Load orders for the connected customer
        loadOrders();
    }

    private void loadOrders() {
        // Fetch all orders for the connected customer
        ArrayList<Order> orders = orderManager.getAllElements();

        // Display the orders in the TableView
        ordersTable.getItems().clear();
        ordersTable.getItems().addAll(orders);
    }

    private void updateTable(){
        loadOrders();
        ordersTable.refresh();
    }

    @FXML
    void onClickGoToProductManager(ActionEvent event) {
        viewModel.getViewFactory().closeCurrentWindow(event);
        viewModel.getViewFactory().showAdminProductManager();
    }

    @FXML
    void onRowClicked(MouseEvent event) {
        inProgress_button.setDisable(false);
        delivered_button.setDisable(false);
        Order order = ordersTable.getSelectionModel().getSelectedItem();
        if (order != null) {
            if (order.getState().equalsIgnoreCase("in progress")){
                inProgress_button.setSelected(true);
            }
            else if (order.getState().equalsIgnoreCase("confirmed")){
                confirmed_button.setSelected(true);
                inProgress_button.setDisable(true);
            }
            else if (order.getState().equalsIgnoreCase("delivered")){
                delivered_button.setSelected(true);
                confirmed_button.setDisable(true);
            }
        }
    }

    @FXML
    void onClickChangeOrderStatus(ActionEvent event) {
        Order order = ordersTable.getSelectionModel().getSelectedItem();
        if (confirmed_button.isSelected()) {
            order.setState("confirmed");
            orderManager.modifyAnElement(order);
            InvoiceManager.getInstance().createInvoice(order);
        }
        else
            if(delivered_button.isSelected()){
            order.setState("delivered");
            orderManager.modifyAnElement(order);}
        updateTable();
        inProgress_button.setDisable(false);
        delivered_button.setDisable(false);
    }

}

