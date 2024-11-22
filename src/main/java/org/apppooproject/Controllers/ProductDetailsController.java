package org.apppooproject.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apppooproject.DataBaseManagers.CustomerManager;
import org.apppooproject.Model.Product;
import org.apppooproject.Views.AlertShowing;
import org.apppooproject.Views.ViewModel;

public class ProductDetailsController {

    @FXML
    private RadioMenuItem l_button;

    @FXML
    private RadioMenuItem m_button;

    @FXML
    private TextArea productDescription;

    @FXML
    private ImageView productImage;

    @FXML
    private Label productName_label;

    @FXML
    private RadioMenuItem s_button;

    @FXML
    private MenuButton sizeFilter;

    @FXML
    private RadioMenuItem xl_button;

    @FXML
    private RadioMenuItem xs_button;

    @FXML
    private Button add_button;

    private Product productSelected;

    public void setProduct(Product product) {
        this.productSelected = product;
        productDescription.setText(product.getDescription());
        String imagePath = "/images/" + product.getName().replaceAll(" ", "_") + ".jpg";
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        productImage.setImage(image);
        productName_label.setText(product.getName());

        /*ToggleGroup sizeGroup = new ToggleGroup();
        xs_button.setToggleGroup(sizeGroup);
        xl_button.setToggleGroup(sizeGroup);
        s_button.setToggleGroup(sizeGroup);
        l_button.setToggleGroup(sizeGroup);
        m_button.setToggleGroup(sizeGroup);*/

        if (productSelected.getStock()==0){
            add_button.setDisable(true);
        }
    }

    @FXML
    void onClickAddOneToCart(ActionEvent event) {
        if (productSelected != null) {
            int addedTocart= CustomerManager.getInstance().getConnectedCustomer().addToCart(productSelected);
            if (addedTocart > 0) {
                AlertShowing.showAlert("Product added","Product successfully added to cart !", Alert.AlertType.INFORMATION);
            }
            else{
                add_button.setDisable(true);
                AlertShowing.showAlert("Product added","Product successfully added to cart !", Alert.AlertType.INFORMATION);
            }
        }
    }

    @FXML
    void onClickGoToShoppingView(ActionEvent event){
        ViewModel.getInstance().getViewFactory().closeCurrentWindow(event);
        ViewModel.getInstance().getViewFactory().showAppViewWindow();
    }
}
