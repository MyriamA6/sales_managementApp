package org.apppooproject.Controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import org.apppooproject.DataBaseManagers.CustomerManager;
import org.apppooproject.DataBaseManagers.CustomerManagerSingleton;
import org.apppooproject.DataBaseManagers.ProductManager;
import org.apppooproject.DataBaseManagers.ProductManagerSingleton;
import org.apppooproject.Model.Customer;
import org.apppooproject.Model.Order;
import org.apppooproject.Model.Product;
import org.apppooproject.Model.Top;

import java.util.ArrayList;
import java.util.Map;

public class CartController {

    @FXML
    private Button delete_button;

    @FXML
    private Button menu_button;

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

    private final CustomerManager customerManager = CustomerManagerSingleton.getInstance().getCustomerManager();
    private final Customer connectedCustomer = customerManager.getConnectedCustomer();
    private final ProductManager productManager = ProductManagerSingleton.getInstance().getProductManager();

    @FXML
    public void initialize() {
        // Configurer la colonne productType pour afficher "Top" ou "Pants" selon le type d'objet
        productType.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            String type = (product instanceof Top) ? "Top" : "Pants";
            return new SimpleStringProperty(type);
        });

        // Configurer les autres colonnes
        productName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        productPrice.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        productColor.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getColor()));
        productSize.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getSize()).asObject()); // Assurez-vous que getSize() existe pour Product
        productQuantity.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(
                        (int) connectedCustomer.getCart().get(cellData.getValue().getProductId())).asObject());

        setupTable();
    }

    public void setupTable() {
        // Ajouter les produits du panier du client connecté
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

    @FXML
    void onClickPayCart(ActionEvent event) {
        if (connectedCustomer.getCart().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cart is empty");
            alert.setHeaderText(null);
            alert.setContentText("Cart is empty");
            alert.showAndWait(); // Affiche l'alerte et attend que l'utilisateur la ferme
        }
        else{
            Order newOrder = connectedCustomer.payCart();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Order successful");
            alert.setHeaderText(null);
            alert.setContentText("Thank you for your order !" );
            alert.showAndWait(); // Affiche l'alerte et attend que l'utilisateur la ferme
            resetCart();
        }
    }

    public void resetCart(){
        connectedCustomer.clearCart();
        products.getItems().clear();
        products.refresh();
    }

    @FXML
    void onClickAddOneToCart(ActionEvent event) {
        Product selectedProduct = products.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            connectedCustomer.addToCart(selectedProduct);
        }
    }

    @FXML
    void onClickRemoveOneFromCart(ActionEvent event) {
        Product selectedProduct = products.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            connectedCustomer.suppressOneUnitFromCart(selectedProduct);
        }

    }

    @FXML
    void onClickSuppressFromCart(ActionEvent event) {
        Product selectedProduct = products.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            connectedCustomer.suppressFromCart(selectedProduct);
        }

    }

    @FXML
    void onClickGoToCentralView(ActionEvent event) {
    }

    @FXML
    void onClickGoToUserAccount(ActionEvent event) {

    }


}
