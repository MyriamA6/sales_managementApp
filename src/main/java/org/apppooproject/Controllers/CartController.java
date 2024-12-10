package org.apppooproject.Controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import org.apppooproject.DataBaseManagers.CustomerManager;
import org.apppooproject.DataBaseManagers.ProductManager;
import org.apppooproject.Model.Customer;
import org.apppooproject.Model.Product;
import org.apppooproject.Model.Top;
import org.apppooproject.Service.AlertShowing;
import org.apppooproject.Service.ViewFactory;

import java.util.ArrayList;
import java.util.Map;

//Controller class tha manage the actions of the interface "My cart" of a customer
public class CartController {


    @FXML
    private TableColumn<Product, String> productColor;

    @FXML
    private TableColumn<Product, Integer> productQuantity;

    @FXML
    private TableColumn<Product, String> productName;

    @FXML
    private TableColumn<Product, Double> productPrice;

    @FXML
    private TableColumn<Product, Integer> productSize;

    @FXML
    private TableColumn<Product, String> productType;

    @FXML
    private TableView<Product> products;

    @FXML
    private Text cart_price;

    private final CustomerManager customerManager = CustomerManager.getInstance();
    private final Customer connectedCustomer = customerManager.getConnectedCustomer();
    private final ProductManager productManager = ProductManager.getInstance();

    // Initialize the table and set up the product display
    @FXML
    public void initialize() {
        productType.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            String type = (product instanceof Top) ? "Top" : "Pants";
            return new SimpleStringProperty(type);
        });

        productName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        productPrice.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        productColor.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getColor()));
        productSize.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getSize()).asObject()); // Assurez-vous que getSize() existe pour Product
        productQuantity.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(
                        (int) connectedCustomer.getCart().get(cellData.getValue().getProductId())).asObject());

        setupTable();
        cart_price.setText("Total : "+connectedCustomer.cartCurrentPrice()+" €");

    }

    // Fill the cart table with the content in the cart of the connected customer
    public void setupTable() {
        ArrayList<Product> cartProduct=new ArrayList<>();
        Map<Long,Integer> cart= connectedCustomer.getCart();
        for (Long productId : cart.keySet()){
            Product product = productManager.getProductById(productId);
            if(product!=null){
                cartProduct.add(product);
            }

        }
        products.getItems().addAll(cartProduct);
    }

    // Handle the "Pay Cart" action when the user clicks on the pay button
    @FXML
    void onClickPayCart(ActionEvent event) {
        if (connectedCustomer.getCart().isEmpty()){
            AlertShowing.showAlert("Cart is empty","Cart is empty",Alert.AlertType.WARNING);
        }
        else{
            connectedCustomer.payCart();
            AlertShowing.showAlert("Order successful","Thank you for your order !",Alert.AlertType.INFORMATION);
            resetCart();
        }
    }

    // Handle the "Store Cart" action when the user clicks on the keep order for later button
    @FXML
    void onClickStoreOrder(ActionEvent event) {
        if (connectedCustomer.getCart().isEmpty()){
            AlertShowing.showAlert("Cart is empty","Cart is empty",Alert.AlertType.WARNING);
        }
        else{
            connectedCustomer.storeOrder();
            AlertShowing.showAlert("Confirmation","Order successfully stored",Alert.AlertType.INFORMATION);
            resetCart();
        }

    }

    // Empty the content of the cart table
    public void resetCart(){
        connectedCustomer.clearCart();
        products.getItems().clear();
        products.refresh();
        cart_price.setText("Total : "+connectedCustomer.cartCurrentPrice()+" €");
    }

    // Update the cart table in case of any changes
    public void updateCart(){
        products.getItems().clear();
        setupTable();
        products.refresh();
        cart_price.setText("Total : "+connectedCustomer.cartCurrentPrice()+" €");
    }

    // Adds one unit of the selected product to the cart
    @FXML
    void onClickAddOneToCart(ActionEvent event) {
        Product selectedProduct = products.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            int productAdded = connectedCustomer.addToCart(selectedProduct);
            if (productAdded == 0) {
                AlertShowing.showAlert("No product can be added to cart","There is no other unit for this product",Alert.AlertType.WARNING);
                }
        }
        updateCart();
    }

    // Remove one unit of the selected product from the cart
    @FXML
    void onClickRemoveOneFromCart(ActionEvent event) {
        Product selectedProduct = products.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            connectedCustomer.suppressOneUnitFromCart(selectedProduct);
        }
        updateCart();
    }

    // Remove the selected product from the cart
    @FXML
    void onClickSuppressFromCart(ActionEvent event) {
        Product selectedProduct = products.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            connectedCustomer.suppressFromCart(selectedProduct);
        }
        updateCart();
    }

    @FXML
    void onClickGoToCentralView(ActionEvent event) {
        ViewFactory.closeCurrentWindow(event);
        ViewFactory.showAppViewWindow();
    }

    @FXML
    void onClickGoToUserAccount(ActionEvent event) {
        ViewFactory.closeCurrentWindow(event);
        ViewFactory.showCustomerAccountWindow();
    }


}
