package org.apppooproject.Controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.apppooproject.DataBaseManagers.CustomerManager;
import org.apppooproject.DataBaseManagers.ProductManager;
import org.apppooproject.Model.Customer;
import org.apppooproject.Model.Pants;
import org.apppooproject.Model.Product;
import org.apppooproject.Model.Top;
import org.apppooproject.MyExceptions.InfoNotCompleteException;
import org.apppooproject.Service.AlertShowing;
import org.apppooproject.Service.ViewFactory;

//Controller class managing the product manager interface of the admin
public class AdminProductsController {


    @FXML
    private RadioMenuItem black_button;

    @FXML
    private RadioMenuItem blue_button;

    @FXML
    private RadioMenuItem creation_button;

    @FXML
    private TextArea description;

    @FXML
    private RadioMenuItem green_button;

    @FXML
    private RadioMenuItem grey_button;

    @FXML
    private RadioMenuItem l_button;

    @FXML
    private RadioMenuItem m_button;

    @FXML
    private RadioMenuItem modification_button;

    @FXML
    private RadioMenuItem orange_button;

    @FXML
    private MenuButton pantsSubMenu;

    @FXML
    private RadioMenuItem pink_button;

    @FXML
    private TextField price;

    @FXML
    private TextField productNameField;
    @FXML private TableView<Product> products;

    @FXML private TableColumn<Product, String> productColor,productGender, productDescription, productName, productType;
    @FXML private TableColumn<Product, Double> productPrice;
    @FXML private TableColumn<Product, Integer> productSize,productStock;
    @FXML
    private TextField quantity;

    @FXML
    private RadioMenuItem red_button;

    @FXML
    private RadioMenuItem regular_button;

    @FXML
    private RadioMenuItem s_button;

    @FXML
    private RadioMenuItem shorts_button;

    @FXML
    private RadioMenuItem sweater_button;

    @FXML
    private MenuButton topSubMenu;

    @FXML
    private RadioMenuItem tshirt_button;
    @FXML
    private MenuButton type_button;

    @FXML
    private RadioMenuItem white_button;

    @FXML
    private RadioMenuItem xl_button;

    @FXML
    private RadioMenuItem xs_button;

    @FXML
    private RadioMenuItem yellow_button;

    @FXML
    private RadioMenuItem male_button;

    @FXML
    private RadioMenuItem female_button;

    @FXML
    private RadioMenuItem unisex_button;

    @FXML
    private TextField searchField;

    private final CustomerManager customerManager = CustomerManager.getInstance();
    private final Customer connectedCustomer = customerManager.getConnectedCustomer();
    private final ProductManager productManager = ProductManager.getInstance();



    // Initialize the product table and set up the other elements displayed
    @FXML
    public void initialize() {

        productType.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            String type = (product instanceof Top) ? "Top" : "Pants";
            return new SimpleStringProperty(type);
        });

        //setting up the table view
        productName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        productPrice.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        productSize.setCellValueFactory(cellData-> new SimpleIntegerProperty(cellData.getValue().getSize()).asObject());
        productStock.setCellValueFactory(cellData-> new SimpleIntegerProperty(cellData.getValue().getStock()).asObject());
        productGender.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGender()));
        productColor.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getColor()));
        productDescription.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

        setupTable();
        ToggleGroup choosenMode = new ToggleGroup();
        creation_button.setToggleGroup(choosenMode);
        modification_button.setToggleGroup(choosenMode);

        creation_button.setSelected(true);

        ToggleGroup sizeGroup = new ToggleGroup();
        xs_button.setToggleGroup(sizeGroup);
        s_button.setToggleGroup(sizeGroup);
        l_button.setToggleGroup(sizeGroup);
        m_button.setToggleGroup(sizeGroup);
        xl_button.setToggleGroup(sizeGroup);

        ToggleGroup colorGroup = new ToggleGroup();
        red_button.setToggleGroup(colorGroup);
        pink_button.setToggleGroup(colorGroup);
        green_button.setToggleGroup(colorGroup);
        grey_button.setToggleGroup(colorGroup);
        blue_button.setToggleGroup(colorGroup);
        black_button.setToggleGroup(colorGroup);
        yellow_button.setToggleGroup(colorGroup);
        white_button.setToggleGroup(colorGroup);
        orange_button.setToggleGroup(colorGroup);

        ToggleGroup genderGroup =new ToggleGroup();
        male_button.setToggleGroup(genderGroup);
        female_button.setToggleGroup(genderGroup);
        unisex_button.setToggleGroup(genderGroup);

        ToggleGroup typeProductGroup =new ToggleGroup();
        regular_button.setToggleGroup(typeProductGroup);
        shorts_button.setToggleGroup(typeProductGroup);
        sweater_button.setToggleGroup(typeProductGroup);
        tshirt_button.setToggleGroup(typeProductGroup);
    }

    // Set up the table with all products from the product table of the database.
    public void setupTable(){
        products.getItems().addAll(productManager.getAllProducts());
    }

    // Search for products based on keywords or logical demand written in the search field
    @FXML
    void giveProductsByKeywords(ActionEvent event) {
        products.getItems().clear();
        products.getItems().addAll(productManager.searchByKeyWords(searchField.getText(), productManager.getAllProducts()));
        products.refresh();
    }

    // If the modification mode is chosen, this function fills all the fields of the interface with
    // the characteristics of the selected product
    public void modificationChosen(){
        Product selectedProduct = products.getSelectionModel().getSelectedItem();
        if (modification_button.isSelected() && selectedProduct != null){
            productNameField.setText(selectedProduct.getName());
            price.setText(String.valueOf(selectedProduct.getPrice()));
            quantity.setText(String.valueOf(selectedProduct.getStock()));
            description.setText(selectedProduct.getDescription());
            switch(selectedProduct.getSize()){
                case 34:
                    xs_button.setSelected(true);
                    break;
                case 36:
                    s_button.setSelected(true);
                    break;
                case 38:
                    m_button.setSelected(true);
                    break;
                case 40:
                    l_button.setSelected(true);
                    break;
                case 42:
                    xl_button.setSelected(true);
                    break;
            }
            String color = selectedProduct.getColor();
            if(color.equalsIgnoreCase("white")){
                white_button.setSelected(true);
            }
            else if (color.equalsIgnoreCase("yellow")){
                yellow_button.setSelected(true);
            }
            else if (color.equalsIgnoreCase("blue")){
                blue_button.setSelected(true);
            }
            else if (color.equalsIgnoreCase("green")){
                green_button.setSelected(true);
            }
            else if (color.equalsIgnoreCase("red")){
                red_button.setSelected(true);
            }
            else if (color.equalsIgnoreCase("pink")){
                pink_button.setSelected(true);
            }
            else if (color.equalsIgnoreCase("orange")){
                orange_button.setSelected(true);
            }
            else if (color.equalsIgnoreCase("grey")){
                grey_button.setSelected(true);
            }
            else if (color.equalsIgnoreCase("black")){
                black_button.setSelected(true);
            }

            String gender = selectedProduct.getGender();
            if(gender.equalsIgnoreCase("male")){
                male_button.setSelected(true);
            }
            else if (gender.equalsIgnoreCase("female")){
                female_button.setSelected(true);
            }
            else if (gender.equalsIgnoreCase("unisex")){
                unisex_button.setSelected(true);
            }

            if (selectedProduct instanceof Top){
                pantsSubMenu.setDisable(true);
                if(((Top) selectedProduct).getIsTshirt()){
                    tshirt_button.setSelected(true);
                }
                else{
                    sweater_button.setSelected(true);
                }
            }
            else{
                topSubMenu.setDisable(true);
                if(((Pants) selectedProduct).getIsShorts()){
                    shorts_button.setSelected(true);
                }
                else{
                    regular_button.setSelected(true);
                }
            }
        }
    }

    // Apply the changes to the selected product or add a new product to the database
    @FXML
    void onClickApplyChanges(ActionEvent event) {
        try{
            isAFieldNull();
        }
        catch (InfoNotCompleteException e) {
            AlertShowing.showAlert("Error", "The product cannot be changed or created", Alert.AlertType.ERROR);
            return;
        }

        if(modification_button.isSelected()){
            Product selectedProduct = products.getSelectionModel().getSelectedItem();
            selectedProduct.setName(productNameField.getText());
            if(!price.getText().isEmpty()){
                selectedProduct.setPrice(Double.parseDouble(price.getText()));
            }
            selectedProduct.setDescription(description.getText());
            if(!quantity.getText().isEmpty()){
                selectedProduct.setStock(Integer.parseInt(quantity.getText()));
            }
            if (green_button.isSelected()){
                selectedProduct.setColor("Green");
            }
            else if (red_button.isSelected()){
                selectedProduct.setColor("Red");
            }
            else if (yellow_button.isSelected()){
                selectedProduct.setColor("Yellow");
            }
            else if (white_button.isSelected()){
                selectedProduct.setColor("White");
            }
            else if (orange_button.isSelected()){
                selectedProduct.setColor("Orange");
            }
            else if (grey_button.isSelected()) {
                selectedProduct.setColor("Grey");
            }
            else if (black_button.isSelected()){
                selectedProduct.setColor("Black");
            }
            else if (pink_button.isSelected()){
                selectedProduct.setColor("Pink");
            }
            else if(blue_button.isSelected()){
                selectedProduct.setColor("Blue");
            }

            if(xs_button.isSelected()){
                selectedProduct.setSize(34);
            }
            else if(s_button.isSelected()){
                selectedProduct.setSize(36);
            }
            else if(m_button.isSelected()){
                selectedProduct.setSize(38);
            }
            else if(l_button.isSelected()){
                selectedProduct.setSize(40);
            }
            else if(xl_button.isSelected()){
                selectedProduct.setSize(42);
            }
            if(male_button.isSelected()){
                selectedProduct.setGender("Male");
            }
            else if(female_button.isSelected()){
                selectedProduct.setGender("Female");
            }
            else if(unisex_button.isSelected()){
                selectedProduct.setGender("Unisex");
            }
            pantsSubMenu.setDisable(false);
            topSubMenu.setDisable(false);
            productManager.modifyAnElement(selectedProduct);
            AlertShowing.showAlert("Product modified", "The product has been modified successfully", Alert.AlertType.INFORMATION);
        }
        else{
            Product newProduct=null;

            // Creating a new product
            if (shorts_button.isSelected() || regular_button.isSelected()) {
                // Creating a new Pants product
                String name = productNameField.getText();
                double priceValue = Double.parseDouble(price.getText());
                int stockValue = Integer.parseInt(quantity.getText());
                String color = getColorFromSelection();
                int size = getSizeFromSelection();
                String descriptionText = description.getText();
                String gender = getGenderFromSelection();
                boolean isShorts = shorts_button.isSelected(); // Determine if the pants are shorts

                newProduct = new Pants(name, priceValue, stockValue, size, color, descriptionText, gender, isShorts);
            }
            else if (tshirt_button.isSelected() || sweater_button.isSelected()) {
                // Creating a new Top product
                String name = productNameField.getText();
                double priceValue = Double.parseDouble(price.getText());
                int stockValue = Integer.parseInt(quantity.getText());
                String color = getColorFromSelection();
                int size = getSizeFromSelection();
                String descriptionText = description.getText();
                String gender = getGenderFromSelection();
                boolean isTshirt = tshirt_button.isSelected(); // Determine if it's a T-shirt or sweater

                newProduct = new Top(name, priceValue, stockValue, size, color, descriptionText, gender, isTshirt);
            }
            assert newProduct != null;
            productManager.addAnElement(newProduct);
            AlertShowing.showAlert("Product added", "The product has been added successfully", Alert.AlertType.INFORMATION);

        }
        updateTable();
        resetFields();
        type_button.setDisable(false);

    }

    @FXML
    void onClickGoToOrdersManager(ActionEvent event) {
        ViewFactory.closeCurrentWindow(event);
        ViewFactory.showAdminOrdersManager();
    }


    // Helper method to reset all input fields in the form
    private void resetFields() {
        productNameField.clear();
        price.clear();
        quantity.clear();
        description.clear();
        xs_button.setSelected(false);
        s_button.setSelected(false);
        m_button.setSelected(false);
        l_button.setSelected(false);
        xl_button.setSelected(false);
        black_button.setSelected(false);
        blue_button.setSelected(false);
        green_button.setSelected(false);
        grey_button.setSelected(false);
        pink_button.setSelected(false);
        red_button.setSelected(false);
        white_button.setSelected(false);
        yellow_button.setSelected(false);
        male_button.setSelected(false);
        female_button.setSelected(false);
        unisex_button.setSelected(false);
        shorts_button.setSelected(false);
        regular_button.setSelected(false);
        sweater_button.setSelected(false);
        tshirt_button.setSelected(false);
    }

    @FXML
    void onClickAdminLogOut(ActionEvent event){
        ViewFactory.closeCurrentWindow(event);
        ViewFactory.showLoginWindow();
    }

    @FXML
    void onRowClicked(MouseEvent event) {
        if(modification_button.isSelected()){
            modificationChosen();
        }
    }

    private String getColorFromSelection() {
        if (green_button.isSelected()) return "Green";
        if (red_button.isSelected()) return "Red";
        if (yellow_button.isSelected()) return "Yellow";
        if (white_button.isSelected()) return "White";
        if (orange_button.isSelected()) return "Orange";
        if (grey_button.isSelected()) return "Grey";
        if (black_button.isSelected()) return "Black";
        if (pink_button.isSelected()) return "Pink";
        if (blue_button.isSelected()) return "Blue";
        return "";
    }

    private int getSizeFromSelection() {
        if (xs_button.isSelected()) return 34;
        if (s_button.isSelected()) return 36;
        if (m_button.isSelected()) return 38;
        if (l_button.isSelected()) return 40;
        if (xl_button.isSelected()) return 42;
        return 0;
    }

    private String getGenderFromSelection() {
        if (male_button.isSelected()) return "Male";
        if (female_button.isSelected()) return "Female";
        if (unisex_button.isSelected()) return "Unisex";
        return "";
    }



    @FXML
    void onClickDeleteSelectedItem(ActionEvent event) {
        Product selectedProduct = products.getSelectionModel().getSelectedItem();
        if(selectedProduct != null){
            if (selectedProduct.getStock()==0){
                AlertShowing.showAlert("Redundant query","Product is already not available for customers", Alert.AlertType.INFORMATION);
            }
            productManager.deleteAnElement(selectedProduct);
        }
        resetFields();
        updateTable();
    }

    public void updateTable(){
        products.getItems().clear();
        setupTable();
        products.refresh();
    }

    // Check if any required fields are empty or invalid
    public void isAFieldNull() throws InfoNotCompleteException {
        if(productNameField.getText().equalsIgnoreCase("") || !(sweater_button.isSelected() || shorts_button.isSelected() || regular_button.isSelected() || tshirt_button.isSelected()) || !(male_button.isSelected() || female_button.isSelected() || unisex_button.isSelected())){
            throw new InfoNotCompleteException();
        }
        if(!(quantity.getText().matches("[0-9]+")) || !(price.getText().matches("[1-9]+[0-9]*.?[0-9]*")) || !(yellow_button.isSelected() || orange_button.isSelected() || grey_button.isSelected()
        || green_button.isSelected() || pink_button.isSelected() || red_button.isSelected() || white_button.isSelected() || black_button.isSelected()
        )|| !(xs_button.isSelected() || s_button.isSelected() || xl_button.isSelected() || m_button.isSelected() || l_button.isSelected())){
            throw new InfoNotCompleteException();
        }
    }

}
