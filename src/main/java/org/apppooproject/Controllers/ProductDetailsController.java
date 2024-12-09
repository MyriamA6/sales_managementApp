package org.apppooproject.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apppooproject.DataBaseManagers.CustomerManager;
import org.apppooproject.Model.Product;
import org.apppooproject.Service.AlertShowing;
import org.apppooproject.Service.ViewFactory;

//Class Controller to show a selected product and its details
public class ProductDetailsController {

    @FXML
    private TextArea productDescription;

    @FXML
    private ImageView productImage;

    @FXML
    private Label productName_label;

    @FXML
    private Label error_label;

    @FXML
    private Label size_label;

    @FXML
    private Label noStock_label;



    @FXML
    private Button add_button;

    private Product productSelected;

    public void initialize() {
        error_label.setVisible(false);
        noStock_label.setVisible(false);
    }

    // Setting up the details of the products for the interface
    public void setProduct(Product product) {
        this.productSelected = product;
        productDescription.setText(product.getDescription());
        productDescription.setEditable(false);

        String imagePath = "/images/" + product.getName() + " " + product.getColor() + ".jpg";

        Image image;
        try {
            image = new Image(getClass().getResourceAsStream(imagePath));
            if (image.isError()) {
                throw new Exception("Image not found: " + imagePath);
            }
        } catch (Exception e) {
            image = new Image(getClass().getResourceAsStream("/images/error.jpg"));
            error_label.setVisible(true);
            error_label.setText("No image is available");
        }

        productImage.setImage(image);
        productName_label.setText(product.getName());
        size_label.setText("Size : " + String.valueOf(product.getSize()));
        int quantity_in_cart = CustomerManager.getInstance().getConnectedCustomer().getCart().getOrDefault(product.getProductId(),0);
        if (product.getStock()-quantity_in_cart==0) {
            add_button.setDisable(true);
            noStock_label.setVisible(true);
        }
    }


    // On action of the selected button, add one unit of the selected product to the cart
    @FXML
    void onClickAddOneToCart(ActionEvent event) {
        if (productSelected != null) {
            int addedTocart= CustomerManager.getInstance().getConnectedCustomer().addToCart(productSelected);
            if (addedTocart > 0) {
                AlertShowing.showAlert("Product added","Product successfully added to cart !", Alert.AlertType.INFORMATION);
            }
            else{
                add_button.setDisable(true);
                noStock_label.setVisible(true);
                AlertShowing.showAlert("Product is now out of stock","It was the last product available", Alert.AlertType.INFORMATION);
            }
        }
    }

    @FXML
    void onClickGoToShoppingView(ActionEvent event){
        ViewFactory.closeCurrentWindow(event);
        ViewFactory.showAppViewWindow();
    }
}
