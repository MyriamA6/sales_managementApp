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
import org.apppooproject.Service.ViewFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

//Controller class that manages the action operated on the "appView" interface
public class CentralViewController {

    // Declare UI components
    @FXML private TableView<Product> products;
    @FXML private TableColumn<Product, String> productColor, productDescription, productName, productType;
    @FXML private TableColumn<Product, Double> productPrice;
    @FXML private TableColumn<Product, Integer> productSize;
    @FXML private TableColumn<Product, String> productGender;
    @FXML private CheckMenuItem isRegular_button, isShorts_button, isSweater_button, isTshirt_button;
    @FXML private CheckMenuItem white_button, yellow_button, blue_button, green_button, black_button;
    @FXML private CheckMenuItem red_button, pink_button, orange_button, grey_button;
    @FXML private CheckMenuItem xs_button, s_button, m_button, l_button, xl_button;
    @FXML private RadioMenuItem less25_button, btw25_50_button, btw50_100_button, more100_button;
    @FXML private CheckMenuItem female_button, male_button,unisex_button;
    @FXML private Text welcomeText;
    @FXML private TextField searchField;

    // Declare manager instances
    private final CustomerManager customerManager = CustomerManager.getInstance();
    private final ProductManager productManager = ProductManager.getInstance();

    private final Customer connectedCustomer = customerManager.getConnectedCustomer();

    // Declare filter buttons and products list
    private ArrayList<CheckMenuItem> buttons;


    @FXML
    public void initialize() {
        buttons = new ArrayList<>(Arrays.asList(
                white_button, yellow_button, blue_button, green_button, black_button,
                red_button, pink_button, orange_button, grey_button,
                xs_button, s_button, m_button, l_button, xl_button,
                isShorts_button, isRegular_button, isTshirt_button, isSweater_button,female_button,male_button,unisex_button
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



        // Configuration of the columns
        productName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        productPrice.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        productSize.setCellValueFactory(cellData-> new SimpleIntegerProperty(cellData.getValue().getSize()).asObject());
        productColor.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getColor()));
        productDescription.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        productGender.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGender()));
        setupTable();
    }


    private void setupTable(){
        products.getItems().addAll(productManager.getProductsInStock());
    }

    @FXML
    void onClickGoToCart(ActionEvent event) {
        ViewFactory.closeCurrentWindow(event);
        ViewFactory.showCartViewWindow();
    }

    @FXML
    void onClickGoToUserAccount(ActionEvent event) {
        ViewFactory.closeCurrentWindow(event);
        ViewFactory.showCustomerAccountWindow();
    }

    @FXML
    void onClickLogOut(ActionEvent event) {
        if(!(connectedCustomer.getCart().isEmpty())){
            connectedCustomer.storeOrder();
        }
        ViewFactory.closeCurrentWindow(event);
        ViewFactory.showLoginWindow();
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

    // Show the result of the filtering of the products of the database
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
        products.getItems().addAll(showByGender(showByPrice(showBySize(showByColor(selectedProductsByType)))));
        products.refresh();
    }

    // Return a filtered list of product according to the colors selected
    public ArrayList<Product> showByColor(ArrayList<Product> productsToFilter) {
        ArrayList<Product> filteredProducts = new ArrayList<>();

        // Keep only the products whose color has been selected
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
        if(!(orange_button.isSelected() || pink_button.isSelected() || yellow_button.isSelected() || blue_button.isSelected() || green_button.isSelected() || black_button.isSelected() || red_button.isSelected() || grey_button.isSelected() || white_button.isSelected())) {
            filteredProducts.addAll(productsToFilter);
        }
        return filteredProducts;
    }

    // Return a filtered list of product according to the size selected on the interface
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

    // Return a filtered list of product according to the ranges of price selected
    public ArrayList<Product> showByPrice(ArrayList<Product> productsToFilter){
        ArrayList<Product> filteredProducts = new ArrayList<>();
        if(less25_button.isSelected()){
            filteredProducts.addAll(productManager.showLessThanGivenPrice(productsToFilter,25));
        }
        if(more100_button.isSelected()){
            filteredProducts.addAll(productManager.showMoreThanGivenPrice(productsToFilter,100));
        }
        if(btw25_50_button.isSelected()){
            filteredProducts.addAll(productManager.showBetweenGivenPrice(productsToFilter,25,50));
        }
        if(btw50_100_button.isSelected()){
            filteredProducts.addAll(productManager.showBetweenGivenPrice(productsToFilter,50,100));
        }
        if(!(btw50_100_button.isSelected() || less25_button.isSelected() || more100_button.isSelected() || btw25_50_button.isSelected())){
            filteredProducts.addAll(productsToFilter);
        }
        return filteredProducts;
    }

    // Return a filtered list of product according to the genders selected
    public ArrayList<Product> showByGender(ArrayList<Product> productsToFilter){
        ArrayList<Product> filteredProducts = new ArrayList<>();
        if(male_button.isSelected()){
            filteredProducts.addAll(productManager.showProductByGender(productsToFilter,"male"));
        }
        if(female_button.isSelected()){
            filteredProducts.addAll(productManager.showProductByGender(productsToFilter,"female"));
        }
        if(unisex_button.isSelected()){
            filteredProducts.addAll(productManager.showProductByGender(productsToFilter,"unisex"));
        }
        if(!(unisex_button.isSelected() || male_button.isSelected() || female_button.isSelected())){
            filteredProducts.addAll(productsToFilter);
        }
        return filteredProducts;
    }

    // Show a filtered list of product according to the keywords written in the search field
    @FXML
    void giveProductsByKeywords(ActionEvent event) {
        products.getItems().clear();
        products.getItems().addAll(productManager.searchByKeyWords(searchField.getText(),productManager.getProductsInStock()));
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
        products.getItems().addAll(productManager.getProductsInStock());
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