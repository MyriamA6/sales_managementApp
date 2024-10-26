package org.apppooproject.Controllers;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class CentralViewController {
    @FXML
    private ComboBox<String> priceFilterComboBox;

    @FXML
    private ComboBox<String> colorFilterComboBox;

    @FXML
    private ComboBox<String> sizeFilterComboBox;

    @FXML
    public void initialize() {
        priceFilterComboBox.setItems(FXCollections.observableArrayList("Under $20", "$20 - $50", "$50 - $100", "Above $100"));
        colorFilterComboBox.setItems(FXCollections.observableArrayList("Red", "Blue", "Green", "Black", "White"));
        sizeFilterComboBox.setItems(FXCollections.observableArrayList("XS", "S", "M", "L", "XL"));
        }


}
