package org.apppooproject.Controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apppooproject.DataBaseManagers.CustomerManager;
import org.apppooproject.DataBaseManagers.ProductManager;
import org.apppooproject.Model.Customer;
import org.apppooproject.Model.Product;
import org.apppooproject.Model.Top;
import org.apppooproject.Views.ViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CentralViewController {

    // Declare UI components
    @FXML private Button accountButton, cartButton, searchButton, addOneButton;
    @FXML private MenuButton colorFilter, priceFilter, sizeFilter;
    @FXML private SplitMenuButton pantsButton, topsButton;
    @FXML private TableView<Product> products;
    @FXML private TableColumn<Product, String> productColor, productDescription, productName, productType;
    @FXML private TableColumn<Product, Double> productPrice;
    @FXML private TableColumn<Product, Integer> productSize;
    @FXML private CheckMenuItem isRegular_button, isShorts_button, isSweater_button, isTshirt_button;
    @FXML private CheckMenuItem white_button, yellow_button, blue_button, green_button, black_button;
    @FXML private CheckMenuItem red_button, pink_button, orange_button, grey_button;
    @FXML private CheckMenuItem xs_button, s_button, m_button, l_button, xl_button;
    @FXML private RadioMenuItem less25_button, btw25_50_button, btw50_100_button, more100_button;
    @FXML private Text welcomeText;
    @FXML private TextField searchField;

    // Declare manager instances
    private final CustomerManager customerManager = CustomerManager.getInstance();
    private final ProductManager productManager = ProductManager.getInstance();
    private final ViewModel viewModel = ViewModel.getInstance();

    private final Customer connectedCustomer = customerManager.getConnectedCustomer();

    // Declare filter buttons and products list
    private ArrayList<CheckMenuItem> buttons;


    @FXML
    public void initialize() {
        buttons = new ArrayList<>(Arrays.asList(
                white_button, yellow_button, blue_button, green_button, black_button,
                red_button, pink_button, orange_button, grey_button,
                xs_button, s_button, m_button, l_button, xl_button,
                isShorts_button, isRegular_button, isTshirt_button, isSweater_button
        ));

        // Personalisation of the welcoming text
        welcomeText.setText("Welcome, "+ customerManager.getConnectedCustomer().getFirstName() + " " +
                customerManager.getConnectedCustomer().getLastName());

        // Configuration of the column that will contain the type of the products
        productType.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            String type = (product instanceof Top) ? "Top" : "Pants";
            return new SimpleStringProperty(type);
        });

        //Blocking the selection of several buttons for the price filtering
        ToggleGroup priceGroup = new ToggleGroup();
        less25_button.setToggleGroup(priceGroup);
        btw25_50_button.setToggleGroup(priceGroup);
        btw50_100_button.setToggleGroup(priceGroup);
        more100_button.setToggleGroup(priceGroup);

        // Configuration of the columns
        productName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        productPrice.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        productSize.setCellValueFactory(cellData-> new SimpleIntegerProperty(cellData.getValue().getSize()).asObject());
        productColor.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getColor()));
        productDescription.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

        setupTable();
    }

    /*@FXML
    void addSelectedProductToCart(ActionEvent event) {
        Product selectedProduct = products.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            int productAdded= connectedCustomer.addToCart(selectedProduct);
            updateTable();
        }
    }*/

    private void setupTable(){
        products.getItems().addAll(productManager.getProductsInStock());
    }

    @FXML
    void onClickGoToCart(ActionEvent event) {
        viewModel.getViewFactory().closeCurrentWindow(event);
        viewModel.getViewFactory().showCartViewWindow();
    }

    @FXML
    void onClickGoToUserAccount(ActionEvent event) {
        viewModel.getViewFactory().closeCurrentWindow(event);
        viewModel.getViewFactory().showCustomerAccountWindow();
    }

    @FXML
    void onClickLogOut(ActionEvent event) {
        if(!(connectedCustomer.getCart().isEmpty())){
            connectedCustomer.storeOrder();
        }
        viewModel.getViewFactory().closeCurrentWindow(event);
        viewModel.getViewFactory().showLoginWindow();
    }


    // Print of the product selected with the necessary information
    public void onProductClicked(javafx.scene.input.MouseEvent mouseEvent) {
        Product selectedProduct = products.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            showProductDetails(selectedProduct);
        }
    }

    private void showProductDetails(Product product) {
        try {
            Stage currentStage = (Stage) products.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sceneBuilderFiles/productDetails.fxml"));
            Parent root = loader.load();
            ProductDetailsController controller = loader.getController();
            controller.setProduct(product);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Product Details");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        if(!(isTshirt_button.isSelected() || isSweater_button.isSelected() || isRegular_button.isSelected() || isShorts_button.isSelected())) {
            selectedProductsByType.addAll(productManager.getProductsInStock());
        }
        products.getItems().addAll(showByPrice(showBySize(showByColor(selectedProductsByType))));
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
        if (grey_button.isSelected()){
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
            filteredProducts.addAll(productManager.showBySize(productsToFilter,34));
        }
        if(s_button.isSelected()){
            filteredProducts.addAll(productManager.showBySize(productsToFilter,36));
        }
        if(m_button.isSelected()){
            filteredProducts.addAll(productManager.showBySize(productsToFilter,38));
        }
        if(l_button.isSelected()){
            filteredProducts.addAll(productManager.showBySize(productsToFilter,40));
        }
        if(xl_button.isSelected()){
            filteredProducts.addAll(productManager.showBySize(productsToFilter,42));
        }
        if(!(xs_button.isSelected() || s_button.isSelected() || m_button.isSelected() || l_button.isSelected() || xl_button.isSelected())) {
            filteredProducts.addAll(productsToFilter);
        }
        return filteredProducts;
    }

    public ArrayList<Product> showByPrice(ArrayList<Product> productsToFilter){
        ArrayList<Product> filteredProducts = new ArrayList<>();
        if(less25_button.isSelected()){
            filteredProducts.addAll(productManager.showLessThanGivenPrice(productsToFilter,25));
        }
        else if(more100_button.isSelected()){
            filteredProducts.addAll(productManager.showMoreThanGivenPrice(productsToFilter,100));
        }
        else if(btw25_50_button.isSelected()){
            filteredProducts.addAll(productManager.showBetweenGivenPrice(productsToFilter,25,50));
        }
        else if(btw50_100_button.isSelected()){
            filteredProducts.addAll(productManager.showBetweenGivenPrice(productsToFilter,50,100));
        }
        else{
            filteredProducts.addAll(productsToFilter);
        }
        return filteredProducts;
    }

    @FXML
    void giveProductsByKeywords(ActionEvent event) {
        products.getItems().clear();
        products.getItems().addAll(productManager.searchByKeyWords(searchField.getText()));
        products.refresh();
    }

    //helper method to deselect all the buttons when filter are reset
    private void deselectAllButtons() {
        for (CheckMenuItem button : buttons) {
            button.setSelected(false);
        }
        less25_button.setSelected(false);
        btw25_50_button.setSelected(false);
        btw50_100_button.setSelected(false);
        more100_button.setSelected(false);
    }

    @FXML
    void resetFilters(ActionEvent event) {
        products.getItems().clear();
        products.getItems().addAll(productManager.getAllProducts());
        deselectAllButtons();
        searchField.clear();
        products.refresh();
    }

    //helper method to update the table of products
    //Used after each action of modification
    public void updateTable(){
        products.getItems().clear();
        setupTable();
        products.refresh();
    }


}