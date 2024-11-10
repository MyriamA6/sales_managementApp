package org.apppooproject.Controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import org.apppooproject.DataBaseManagers.CustomerManager;
import org.apppooproject.DataBaseManagers.CustomerManagerSingleton;
import org.apppooproject.DataBaseManagers.ProductManager;
import org.apppooproject.DataBaseManagers.ProductManagerSingleton;
import org.apppooproject.Model.Customer;
import org.apppooproject.Model.Product;
import org.apppooproject.Model.Top;
import org.apppooproject.Views.ViewModel;

import java.util.ArrayList;

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
    private CheckMenuItem white_button;

    @FXML
    private CheckMenuItem yellow_button;

    @FXML
    private CheckMenuItem blue_button;

    @FXML
    private CheckMenuItem green_button;

    @FXML
    private CheckMenuItem black_button;

    @FXML
    private CheckMenuItem red_button;

    @FXML
    private CheckMenuItem pink_button;

    @FXML
    private CheckMenuItem orange_button;

    @FXML
    private CheckMenuItem grey_button;

    @FXML
    private CheckMenuItem xs_button;

    @FXML
    private CheckMenuItem s_button;

    @FXML
    private CheckMenuItem m_button;

    @FXML
    private CheckMenuItem l_button;

    @FXML
    private CheckMenuItem xl_button;

    @FXML
    private Text welcomeText;

    private final CustomerManager customerManager = CustomerManagerSingleton.getInstance().getCustomerManager();

    private final Customer connectedCustomer = customerManager.getConnectedCustomer();

    private boolean noButtonSelected = true;

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
    void applySelectedFilters(ActionEvent event) {
        products.getItems().clear();

        ArrayList<Product> selectedProductsByType =new ArrayList<>();

        if(isShorts_button.isSelected()) {
            selectedProductsByType.addAll(productManager.showOnlyPants(1));
        }
        if(isRegular_button.isSelected()) {
            selectedProductsByType.addAll(productManager.showOnlyPants(2));
        }
        if (isTshirt_button.isSelected()) {
            selectedProductsByType.addAll(productManager.showOnlyTops(1));
        }
        if (isSweater_button.isSelected()) {
            selectedProductsByType.addAll(productManager.showOnlyTops(2));
        }
        if(!(isTshirt_button.isSelected() || isSweater_button.isSelected() || isRegular_button.isSelected() || isTshirt_button.isSelected())) {
            selectedProductsByType.addAll(productManager.getProducts());
        }
        products.getItems().addAll(showBySize(showByColor(selectedProductsByType)));
        products.refresh();
    }

    public ArrayList<Product> showByColor(ArrayList<Product> productsToFilter) {
        ArrayList<Product> filteredProducts = new ArrayList<>();

        // Vérifiez quelle couleur est sélectionnée et affichez les produits correspondants
        if (white_button.isSelected()) {
            filteredProducts.addAll(productManager.showProductByColor(productsToFilter,"WHITE"));
        }
        if (yellow_button.isSelected()) {
            filteredProducts.addAll(productManager.showProductByColor(productsToFilter,"YELLOW"));
        }
        if (blue_button.isSelected()) {
            filteredProducts.addAll(productManager.showProductByColor(productsToFilter,"BLUE"));
        }
        if (green_button.isSelected()) {
            filteredProducts.addAll(productManager.showProductByColor(productsToFilter,"GREEN"));
        }
        if (black_button.isSelected()) {
            filteredProducts.addAll(productManager.showProductByColor(productsToFilter,"BLACK"));
        }
        if (red_button.isSelected()) {
            filteredProducts.addAll(productManager.showProductByColor(productsToFilter,"RED"));
        }
        if (pink_button.isSelected()) {
            filteredProducts.addAll(productManager.showProductByColor(productsToFilter,"PINK"));
        }
        if (orange_button.isSelected()) {
            filteredProducts.addAll(productManager.showProductByColor(productsToFilter,"ORANGE"));
        }
        if(grey_button.isSelected()){
            filteredProducts.addAll(productManager.showProductByColor(productsToFilter,"GREY"));
        }
        if(!(orange_button.isSelected() || pink_button.isSelected() || yellow_button.isSelected() || blue_button.isSelected() || green_button.isSelected() || black_button.isSelected() || red_button.isSelected() || grey_button.isSelected())) {
            filteredProducts.addAll(productsToFilter);
        }
        return filteredProducts;
    }

    public ArrayList<Product> showBySize (ArrayList<Product> productsToFilter) {
        ArrayList<Product> filteredProducts = new ArrayList<>();
        if(xs_button.isSelected()){
            filteredProducts.addAll(productManager.showBySize(34));
        }
        if(s_button.isSelected()){
            filteredProducts.addAll(productManager.showBySize(36));
        }
        if(m_button.isSelected()){
            filteredProducts.addAll(productManager.showBySize(38));
        }
        if(l_button.isSelected()){
            filteredProducts.addAll(productManager.showBySize(40));
        }
        if(xl_button.isSelected()){
            filteredProducts.addAll(productManager.showBySize(42));
        }
        if(!(xs_button.isSelected() || s_button.isSelected() || m_button.isSelected() || l_button.isSelected() || xl_button.isSelected())) {
            filteredProducts.addAll(productsToFilter);
        }
        return filteredProducts;
    }

    @FXML
    void resetFilters(ActionEvent event) {
        products.getItems().clear();
        products.getItems().addAll(productManager.getProducts());
        products.refresh();
    }

    /*@FXML
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

    }*/

    public void resetTable(){
        if (!((isShorts_button.isSelected()) || (isRegular_button.isSelected()) || (isSweater_button.isSelected()) || (isTshirt_button.isSelected()))){
            products.getItems().clear();
            products.getItems().addAll(productManager.getProducts());
        }

    }








}