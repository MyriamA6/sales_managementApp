package org.apppooproject.Controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.apppooproject.DataBaseManagers.CustomerManager;
import org.apppooproject.DataBaseManagers.CustomerManagerSingleton;
import org.apppooproject.DataBaseManagers.ProductManager;
import org.apppooproject.DataBaseManagers.ProductManagerSingleton;
import org.apppooproject.Model.Customer;
import org.apppooproject.Model.Pants;
import org.apppooproject.Model.Product;
import org.apppooproject.Model.Top;
import org.apppooproject.Views.ViewModel;

import java.net.URL;
import java.util.ResourceBundle;

public class CentralViewController {

    @FXML
    private Button accountButton;

    @FXML
    private Button cartButton;

    @FXML
    private MenuButton colorFilter;

    @FXML
    private SplitMenuButton pantsButton;

    @FXML
    private MenuButton priceFilter;

    @FXML
    private TableView<Product> products;

    @FXML
    private TableColumn<Product, String> productColor;

    @FXML
    private TableColumn<Product, String> productDescription;

    @FXML
    private TableColumn<Product, String> productName;

    @FXML
    private TableColumn<Product, Double> productPrice;

    @FXML
    private TableColumn<Product, Integer> productSize;

    @FXML
    private TableColumn<Product, String> productType;

    @FXML
    private CheckMenuItem isRegular_button;

    @FXML
    private CheckMenuItem isShorts_button;

    @FXML
    private CheckMenuItem isSweater_button;

    @FXML
    private CheckMenuItem isTshirt_button;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchField;

    @FXML
    private MenuButton sizeFilter;

    @FXML
    private SplitMenuButton topsButton;

    @FXML
    private Button addOneButton;

    @FXML
    private Text welcomeText;

    private final CustomerManager customerManager = CustomerManagerSingleton.getInstance().getCustomerManager();

    private final Customer connectedCustomer = customerManager.getConnectedCustomer();

    ProductManager productManager= ProductManagerSingleton.getInstance().getProductManager();

    @FXML
    public void initialize() {
        // Configurer la colonne productType pour afficher "Top" ou "Pants" selon le type d'objet
        welcomeText.setText("Welcome, "+ customerManager.getConnectedCustomer().getFirstName() + " " +
                customerManager.getConnectedCustomer().getLastName());
        productType.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            String type = (product instanceof Top) ? "Top" : "Pants";
            return new SimpleStringProperty(type);
        });

        // Configurer les autres colonnes
        productName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        productPrice.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        productSize.setCellValueFactory(cellData-> new SimpleIntegerProperty(cellData.getValue().getSize()).asObject());
        productColor.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getColor()));
        productDescription.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

        setupTable();
    }

    @FXML
    void addSelectedProductToCart(ActionEvent event) {
        Product selectedProduct = products.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            connectedCustomer.addToCart(selectedProduct);
            productManager.getProductById(selectedProduct.getProductId()).decrementStock();
        }

    }

    public void setupTable(){
        products.getItems().addAll(productManager.getProducts());
    }

    @FXML
    void onClickGoToCart(ActionEvent event) {
        ViewModel.getInstance().getViewFactory().showCartViewWindow();
    }

    @FXML
    void onClickGoToUserAccount(ActionEvent event) {
        ViewModel.getInstance().getViewFactory().showAppViewWindow();
    }

    @FXML
    void showRegulars(ActionEvent event) {
        if (isRegular_button.isSelected() && (isShorts_button.isSelected() || isSweater_button.isSelected() || isTshirt_button.isSelected())){
            products.getItems().addAll(productManager.showOnlyPants(2));
        }
        else if (isRegular_button.isSelected()){
            products.getItems().clear();
            products.getItems().addAll(productManager.showOnlyPants(2));
        }
        else if (isShorts_button.isSelected()){
            showShorts(event);
        }
        else if (isSweater_button.isSelected()){
            showSweaters(event);
        }
        else if (isTshirt_button.isSelected()){
            showTshirts(event);
        }
        resetTable(); //remet tout dans la table si rien n'est selectionné
        products.refresh();
    }

    @FXML
    void showShorts(ActionEvent event) {
        if (isShorts_button.isSelected() && (isRegular_button.isSelected() || isSweater_button.isSelected() || isTshirt_button.isSelected())){
            products.getItems().addAll(productManager.showOnlyPants(1));
        }
        else if (isShorts_button.isSelected()){
            products.getItems().clear();
            products.getItems().addAll(productManager.showOnlyPants(1));
        }
        else if (isRegular_button.isSelected()){
            showRegulars(event);
        }
        else if (isSweater_button.isSelected()){
            showSweaters(event);
        }
        else if (isTshirt_button.isSelected()){
            showTshirts(event);
        }
        resetTable(); //remet tout dans la table si rien n'est selectionné
        products.refresh();
    }

    @FXML
    void showSweaters(ActionEvent event) {
        if (isSweater_button.isSelected() && (isRegular_button.isSelected() || isShorts_button.isSelected() || isTshirt_button.isSelected())){
            products.getItems().addAll(productManager.showOnlyTops(2));
        }
        else if (isSweater_button.isSelected()){
            products.getItems().clear();
            products.getItems().addAll(productManager.showOnlyTops(2));
        }
        else if (isRegular_button.isSelected()){
            showRegulars(event);
        }
        else if (isShorts_button.isSelected()){
            showShorts(event);
        }
        else if (isTshirt_button.isSelected()){
            showTshirts(event);
        }
        resetTable(); //remet tout dans la table si rien n'est selectionné
        products.refresh();

    }

    @FXML
    void showTshirts(ActionEvent event) {
        if (isTshirt_button.isSelected() && (isRegular_button.isSelected() || isShorts_button.isSelected() || isSweater_button.isSelected())){
            products.getItems().addAll(productManager.showOnlyTops(1));
        }
        else if (isTshirt_button.isSelected()){
            products.getItems().clear();
            products.getItems().addAll(productManager.showOnlyTops(1));
        }
        else if (isRegular_button.isSelected()){
            showRegulars(event);
        }
        else if (isShorts_button.isSelected()){
            showShorts(event);
        }
        else if (isSweater_button.isSelected()){
            showSweaters(event);
        }
        resetTable(); //remet tout dans la table si rien n'est selectionné
        products.refresh();

    }

    public void resetTable(){
        if (!((isShorts_button.isSelected()) || (isRegular_button.isSelected()) || (isSweater_button.isSelected()) || (isTshirt_button.isSelected()))){
            products.getItems().clear();
            products.getItems().addAll(productManager.getProducts());
        }
    }




}