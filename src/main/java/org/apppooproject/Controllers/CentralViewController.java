package org.apppooproject;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.apppooproject.Model.Pants;
import org.apppooproject.Model.Product;
import org.apppooproject.Model.Top;

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
    private TableColumn<Product, String> productSize;

    @FXML
    private TableColumn<Product, String> productType;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchField;

    @FXML
    private MenuButton sizeFilter;

    @FXML
    private SplitMenuButton topsButton;

    @FXML
    private Text welcomeText;

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
        productDescription.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

        setupTable();
    }

    @FXML
    void onRowClicked(MouseEvent event) {

    }

    public void setupTable(){
        // Objet de type Top : un haut à manches longues pour homme
        Top top1 = new Top(
                101, // productId
                "Chemise Classique", // name
                29.99, // price
                50, // stock
                "Bleu", // color
                "Chemise classique à manches longues", // description
                "Homme", // gender
                true // isLongSleeves
        );

// Objet de type Pant : un short pour femme
        Pants pants1 = new Pants(
                201, // productId
                "Short d'été", // name
                19.99, // price
                30, // stock
                "Noir", // color
                "Short confortable et léger, idéal pour l'été", // description
                "Femme", // gender
                true // isShorts
        );

// Objet de type Top : un haut à manches courtes pour femme
        Top top2 = new Top(
                102, // productId
                "T-shirt Casual", // name
                15.99, // price
                70, // stock
                "Rouge", // color
                "T-shirt confortable à manches courtes", // description
                "Femme", // gender
                false // isLongSleeves
        );

        products.getItems().addAll(pants1,top1, top2);

    }


}