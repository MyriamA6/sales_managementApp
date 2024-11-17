package org.apppooproject.Controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apppooproject.DataBaseManagers.CustomerManager;
import org.apppooproject.DataBaseManagers.ProductManager;
import org.apppooproject.Model.Customer;
import org.apppooproject.Model.Product;
import org.apppooproject.Model.Top;
import org.apppooproject.Views.ViewModel;


import java.util.ArrayList;
import java.util.Arrays;

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
    private CheckMenuItem pantsSubMenu;

    @FXML
    private RadioMenuItem pink_button;

    @FXML
    private TextField price;

    @FXML
    private TextField productNameField;
    @FXML private TableView<Product> products;

    @FXML private TableColumn<Product, String> productColor, productDescription, productName, productType;
    @FXML private TableColumn<Product, Double> productPrice;
    @FXML private TableColumn<Product, Integer> productSize;
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
    private MenuItem tshirt_button;

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

    private final CustomerManager customerManager = CustomerManager.getInstance();
    private final Customer connectedCustomer = customerManager.getConnectedCustomer();
    private final ProductManager productManager = ProductManager.getInstance();

    // Declare filter buttons and products list
    private ArrayList<CheckMenuItem> buttons;


    @FXML
    public void initialize() {

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


        ToggleGroup choosenMode = new ToggleGroup();
        creation_button.setToggleGroup(choosenMode);
        modification_button.setToggleGroup(choosenMode);

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
    }

    public void modificationChoosen(){
        if (modification_button.isSelected()){
            Product selectedProduct = products.getSelectionModel().getSelectedItem();
            productNameField.setText(selectedProduct.getName());
            price.setText(String.valueOf(selectedProduct.getPrice()));
            quantity.setText(String.valueOf(selectedProduct.getSize()));
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
        }
    }

    @FXML
    void onClickApplyChanges(ActionEvent event) {
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
                selectedProduct.setColor("green");
            }
            else if (red_button.isSelected()){
                selectedProduct.setColor("red");
            }
            else if (yellow_button.isSelected()){
                selectedProduct.setColor("yellow");
            }
            else if (white_button.isSelected()){
                selectedProduct.setColor("white");
            }
            else if (orange_button.isSelected()){
                selectedProduct.setColor("orange");
            }
            else if (grey_button.isSelected()) {
                selectedProduct.setColor("grey");
            }
            else if (black_button.isSelected()){
                selectedProduct.setColor("black");
            }
            else if (pink_button.isSelected()){
                selectedProduct.setColor("pink");
            }
            else if(blue_button.isSelected()){
                selectedProduct.setColor("blue");
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

        }

    }

    @FXML
    void onClickDeleteSelectedItem(ActionEvent event) {

    }

}
