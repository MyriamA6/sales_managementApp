package org.apppooproject.Controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import org.apppooproject.DataBaseManagers.CustomerManager;
import org.apppooproject.DataBaseManagers.CustomerManagerSingleton;
import org.apppooproject.DataBaseManagers.ProductManager;
import org.apppooproject.DataBaseManagers.ProductManagerSingleton;
import org.apppooproject.Model.Customer;
import org.apppooproject.Model.Product;
import org.apppooproject.Model.Top;

import java.util.ArrayList;
import java.util.HashMap;
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

    @FXML
    void addOne(MouseEvent event) {
        // Logique pour ajouter un produit (ex. augmenter la quantité d'un produit dans le panier)
    }

    @FXML
    void cartContent(ActionEvent event) {
        // Logique pour afficher le contenu du panier
    }

    @FXML
    void onClickAddOneToCart(ActionEvent event) {
        // Logique pour ajouter un produit sélectionné dans le panier
    }

    @FXML
    void onClickGoToCentralView(ActionEvent event) {
        // Logique pour rediriger vers la vue centrale
    }

    @FXML
    void onClickGoToUserAccount(ActionEvent event) {
        // Logique pour rediriger vers le compte utilisateur
    }

    @FXML
    void onClickRemoveOneFromCart(ActionEvent event) {
        // Logique pour retirer une quantité d'un produit dans le panier
    }

    @FXML
    void onClickSuppressFromCart(ActionEvent event) {
        // Logique pour supprimer un produit du panier
    }

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
        products.getItems().addAll(cartProduct); // Vérifiez que getProducts() retourne une List<Product>
    }
}
