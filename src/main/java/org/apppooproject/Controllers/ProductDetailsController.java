public class ProductDetailsController {
    @FXML private ImageView productImage;
    @FXML private Label productDescription;

    public void setProduct(Product product) {
        productDescription.setText(product.getDescription());

        String imagePath = "/images/" + product.getName().replaceAll("\\s+", "") + ".jpg";
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        productImage.setImage(image);
    }
}
