package org.apppooproject.DataBaseManagers;

import javafx.scene.control.Alert;
import org.apppooproject.Model.Pants;
import org.apppooproject.Model.Product;
import org.apppooproject.Model.Top;
import org.apppooproject.Service.AlertShowing;
import org.apppooproject.Service.HelperMethod;

import java.sql.*;
import java.util.ArrayList;

public class ProductManager implements DataManager<Product> {
    private static ProductManager instance;  // unique instance of productmanager
    private final Connection co;             // Object to allow us to connect to the database
    private ArrayList<Product> products;     // List which will contain all products from the database

    // Private constructor to only allow the creation of an instance via the singleton design pattern
    private ProductManager() {
        this.co=DatabaseInitializer.getH2Connection();
        products = new ArrayList<Product>();
        loadProductFromBd();  // Fill the list of products with the ones from the dataBase
    }

    public static ProductManager getInstance() {
        if (instance == null) {
            instance = new ProductManager();
        }
        return instance;
    }

    // Loads all products from the database into the 'products' list.
    private void loadProductFromBd() {
        Statement stmt = null;
        try {
            stmt = co.createStatement();
            String sql = "SELECT * FROM Product";
            ResultSet res = stmt.executeQuery(sql);

            while (res.next()) {
                // Determines if the product is Pants or Top and adds it to the products list
                Product p =pantsOrTop(res.getLong("product_id"), co);
                if(p!=null){
                    products.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Determines if a product with the given productId is a Pants or Top, and returns the appropriate object.
    public Product pantsOrTop(long productId, Connection co) {
        Statement stmt = null;
        try {
            stmt = co.createStatement();
            // We check if the product is Pants
            String sqlPants = "SELECT * FROM Pants JOIN Product ON Pants.product_id = Product.product_id WHERE Pants.product_id = " + productId;
            ResultSet res = stmt.executeQuery(sqlPants);

            if (res.next()) {
                boolean isShorts = "Shorts".equalsIgnoreCase(res.getString("length"));
                // Creates and returns a Pants object if matched
                return new Pants(
                        res.getLong("product_id"),
                        res.getString("name"),
                        res.getDouble("price"),
                        res.getInt("stock"),
                        res.getInt("size"),
                        res.getString("color"),
                        res.getString("description"),
                        res.getString("gender"),
                        isShorts
                );
            }

            // We check if the product is a Top
            String sqlTop = "SELECT * FROM Top JOIN Product ON Top.product_id = Product.product_id WHERE Top.product_id = " + productId;
            res = stmt.executeQuery(sqlTop);

            if (res.next()) {
                boolean isTshirt = "T-shirt".equalsIgnoreCase(res.getString("sleevesType"));
                // Creates and returns a Top object if matched
                return new Top(
                        res.getLong("product_id"),
                        res.getString("name"),
                        res.getDouble("price"),
                        res.getInt("stock"),
                        res.getInt("size"),
                        res.getString("color"),
                        res.getString("description"),
                        res.getString("gender"),
                        isTshirt
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null; // Returns null if no matching product is found
    }

    // Returns a list of products that are in stock (stock > 0).
    public int getStockOfProduct(long productId) {
        try{
            String sql = "SELECT * FROM Product where product_id = " + productId;
            Statement stmt = co.createStatement();
            ResultSet res = stmt.executeQuery(sql);
            if (res.next()) {
                return res.getInt("stock");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    // Update the product list by reloading all products from the database.
    public void refresh() {
        products.clear(); //we first clear the list of products
        loadProductFromBd(); //we reload
    }

    // Returns the list of all products.
    public ArrayList<Product> getAllProducts() {
        return products;
    }

    // Returns a list of products that are in stock (stock > 0).
    public ArrayList<Product> getProductsInStock() {
        ArrayList<Product> inStock = new ArrayList<>();
        for (Product p : products) {
            if (p.getStock() > 0) {
                inStock.add(p);
            }
        }
        return inStock;
    }

    // Returns a product by its ID if it exists in the products list.
    public Product getProductById(long productId) {
        for (Product p : products) {
            if (p.getProductId() == productId) {
                return p;
            }
        }
        return null;
    }



    //method that returns the products containing all the given keywords
    //if it contains logical operators then the method return the list of products
    // respecting the logical query with searchWithLogicalOperator
    public ArrayList<Product> searchByKeyWords(String searchDemand, ArrayList<Product> products) {
        //if the searchDemand is empty we return all the products
        if (HelperMethod.removeExtraSpaces(searchDemand).isEmpty()) {
            return products;
        }

        if (searchDemand.toLowerCase().contains("and") || searchDemand.toLowerCase().contains("or")) {
            return searchWithLogicalOperators(searchDemand,products);
        }


        ArrayList<Product> keyWordsCorrespondingProducts = new ArrayList<>();
        String[] separatedWords = searchDemand.split(" ");
        ArrayList<String> keyWords = new ArrayList<>();

        //For each keyword only the alphanumerical character are kept (we remove the extra spaces)
        for (String s : separatedWords) {
            String cleanedWord = HelperMethod.removeExtraSpaces(s);
            if (!cleanedWord.isEmpty()) { // Check if the word is not empty
                keyWords.add(cleanedWord.toLowerCase());
            }
        }

        // Check each product to find the ones containing the keywords
        for (Product p : products) {
            boolean containsKeyWord = true;
            for (String keyword : keyWords) {
                //if the product does not contain one of the keyWords we try the next product
                if (!(p.getDescription().toLowerCase().contains(keyword) ||
                        p.getName().toLowerCase().contains(keyword))) {
                    containsKeyWord = false;
                    break;
                }
            }
            if (containsKeyWord) {
                keyWordsCorrespondingProducts.add(p);
            }
        }

        return keyWordsCorrespondingProducts;
    }

    //We collect all products respecting the logical conditions of searchDemand
    public ArrayList<Product> searchWithLogicalOperators(String searchDemand,ArrayList<Product> products) {
        ArrayList<Product> resultingProducts = new ArrayList<>();
        String[] andSeparatedWords = searchDemand.toLowerCase().split("and");

        //For each product we check if it contains all the given keywords
        // in the name or description of the product
        for (Product p : products) {
            boolean matchesAllAndConditions = true;

            for (String andCondition : andSeparatedWords) {
                String cleanedAndCondition = HelperMethod.removeExtraSpaces(andCondition);
                boolean matchesOrCondition = false;

                //if there is any "or" we check if one of the keywords is contained in the description
                //or the name of the product
                if (cleanedAndCondition.contains("or")) {
                    String[] orSeparatedWords = cleanedAndCondition.split("or");
                    for (String orCondition : orSeparatedWords) {
                        String cleanedOrCondition = HelperMethod.removeExtraSpaces(orCondition);
                        if (p.getDescription().toLowerCase().contains(cleanedOrCondition) ||
                                p.getName().toLowerCase().contains(cleanedOrCondition)) {
                            matchesOrCondition = true;
                            break;
                        }
                    }
                } else {
                    // We verify  "and" condition not containing any "or"
                    if (p.getDescription().toLowerCase().contains(cleanedAndCondition) ||
                            p.getName().toLowerCase().contains(cleanedAndCondition)) {
                        matchesOrCondition = true;
                    }
                }

                // if one of the "or" conditions is not respected we set the whole conditions to false
                if (!matchesOrCondition) {
                    matchesAllAndConditions = false;
                    break;
                }
            }

            if (matchesAllAndConditions) {
                resultingProducts.add(p);
            }
        }

        return resultingProducts;
    }


    // Filters products to return only Pants based on a given criterion.
    public ArrayList<Product> showOnlyPants(int criteria) {
        ArrayList<Product> pants = new ArrayList<>();
        ArrayList<Product> productsInStock = getProductsInStock();
        for (Product p : productsInStock) {
            if (p.getStock() > 0 && p instanceof Pants) {
                Pants pant = (Pants) p;
                switch (criteria) {
                    case 1:
                        if (pant.getIsShorts()) {
                            pants.add(pant); // Adds only shorts
                        }
                        break;
                    case 2:
                        if (!pant.getIsShorts()) {
                            pants.add(pant); // Adds only long pants
                        }
                        break;
                    default:
                        pants.add(pant); // Adds all pants
                }
            }
        }
        return pants;
    }

    // Filters a list of products to return only those of the specified size.
    public ArrayList<Product> showBySize(ArrayList<Product> productsToFilter, int size) {
        ArrayList<Product> productsOfGivenSize = new ArrayList<>();
        for (Product p : productsToFilter) {
            if (p.getSize() == size) {
                productsOfGivenSize.add(p);
            }
        }
        return productsOfGivenSize;
    }

    // Filters a list of products to return only those of the specified color.
    public ArrayList<Product> showProductByColor(ArrayList<Product> productsToFilter, String color) {
        ArrayList<Product> productsOfGivenColor = new ArrayList<>();
        for (Product p : productsToFilter) {
            if (p.getColor().equalsIgnoreCase(color)) {
                productsOfGivenColor.add(p);
            }
        }
        return productsOfGivenColor;
    }

    // Filters products to return only Tops based on a given criterion.
    public ArrayList<Product> showOnlyTops(int criteria) {
        ArrayList<Product> tops = new ArrayList<>();
        ArrayList<Product> productsInStock = getProductsInStock();

        for (Product p : productsInStock) {
            if (p.getStock() > 0 && p instanceof Top) {
                Top top = (Top) p;
                switch (criteria) {
                    case 1:
                        if (top.getIsTshirt()) {
                            tops.add(top); // Adds only T-shirts
                        }
                        break;
                    case 2:
                        if (!top.getIsTshirt()) {
                            tops.add(top); // Adds only non-T-shirt tops
                        }
                        break;
                    default:
                        tops.add(top); // Adds all tops
                }
            }
        }
        return tops;
    }


    // Method to collect all products having a smaller price than the one given
    public ArrayList<Product> showMoreThanGivenPrice(ArrayList<Product> productsToFilter,int price){
        ArrayList<Product> productsOfGivenPrice = new ArrayList<>();
        for (Product p : productsToFilter) {
            if (p.getPrice() >= price) {
                productsOfGivenPrice.add(p);
            }
        }
        return productsOfGivenPrice;
    }

    // Method to collect all products having a higher price than the one given
    public ArrayList<Product> showLessThanGivenPrice(ArrayList<Product> productsToFilter,int price){
        ArrayList<Product> productsOfGivenPrice = new ArrayList<>();
        for (Product p : productsToFilter) {
            if (p.getPrice() < price) {
                productsOfGivenPrice.add(p);
            }
        }
        return productsOfGivenPrice;
    }

    // Method to collect all products having a price in the interval of given prices
    public ArrayList<Product> showBetweenGivenPrice(ArrayList<Product> productsToFilter,int price1, int price2){
        ArrayList<Product> productsOfGivenPrice = new ArrayList<>();
        for (Product p : productsToFilter) {
            if (p.getPrice() >=price1 && p.getPrice() <price2) {
                productsOfGivenPrice.add(p);
            }
        }
        return productsOfGivenPrice;
    }

    // Filters a list of products to return only those of the specified gender.
    public ArrayList<Product> showProductByGender(ArrayList<Product> productsToFilter, String gender) {
        ArrayList<Product> productsOfGivenGender = new ArrayList<>();
        for (Product p : productsToFilter) {
            if (p.getGender().equalsIgnoreCase(gender)) {
                productsOfGivenGender.add(p);
            }
        }
        return productsOfGivenGender;
    }


    // Updates the stock of each product in the products list in the database.
    public void refreshProductsStock() {
        for (Product p : products) {
            updateProductStock(p);
        }
    }

    // Helper method to update the stock of a specific product in the database.
    private void updateProductStock(Product p) {
        String sql = "UPDATE product SET stock = ? WHERE product_id = ?";
        try (PreparedStatement stmt = co.prepareStatement(sql)) {
            stmt.setInt(1, p.getStock());
            stmt.setLong(2, p.getProductId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Adds an element to the database
    //if it is an object of type Pants, its corresponding entity is added to the table Pants
    //a similar action is applied for Tops objects
    @Override
    public void addAnElement(Product product) {
        try {
            //First : insertion of the product in the table of products
            String sqlProduct = "INSERT INTO Product (name, price, stock, gender, color, size, description) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmtProduct = co.prepareStatement(sqlProduct, Statement.RETURN_GENERATED_KEYS);
            stmtProduct.setString(1, product.getName());
            stmtProduct.setDouble(2, product.getPrice());
            stmtProduct.setInt(3, product.getStock());
            stmtProduct.setString(4, product.getGender());
            stmtProduct.setString(5, product.getColor());
            stmtProduct.setInt(6, product.getSize());
            stmtProduct.setString(7, product.getDescription());
            stmtProduct.executeUpdate();

            //Second : we get the generated key to insert the needed line in the table pants or top
            //According to the type of the product
            ResultSet generatedKeys = stmtProduct.getGeneratedKeys();
            if (generatedKeys.next()) {
                long productId = generatedKeys.getLong(1);
                product.setProductId(productId);

                //if the product is a Pants in the database, we add its type
                //otherwise we do the same for a Top
                if (product instanceof Pants) {
                    Pants pants = (Pants) product;
                    String length = pants.getIsShorts() ? "Shorts" : "Regular";
                    String sqlPants = "INSERT INTO Pants (product_id, length) VALUES (?, ?)";
                    PreparedStatement stmtPants = co.prepareStatement(sqlPants);
                    stmtPants.setLong(1, productId);
                    stmtPants.setString(2, length);
                    stmtPants.executeUpdate();
                } else if (product instanceof Top) {
                    Top top = (Top) product;
                    String sleevesType = top.getIsTshirt() ? "T-shirt" : "Sweater";
                    String sqlTop = "INSERT INTO Top (product_id, sleevesType) VALUES (?, ?)";
                    PreparedStatement stmtTop = co.prepareStatement(sqlTop);
                    stmtTop.setLong(1, productId);
                    stmtTop.setString(2, sleevesType);
                    stmtTop.executeUpdate();
                }
                products.add(product);
            }
        } catch (SQLException e) {
            System.out.println("Error adding product: " + e.getMessage());
            AlertShowing.showAlert("Error","Error the product is already referenced in the database", Alert.AlertType.ERROR);
        }
    }


    //Modifies an existing element from the database
    // Also modifies the corresponding entity in the table Pants or Tops
    @Override
    public void modifyAnElement(Product product) {
        try {
            // First : update of the product in the general table Product
            String sqlProduct = "UPDATE Product SET name = ?, price = ?, stock = ?, gender = ?, color = ?, size = ?, description = ? " +
                    "WHERE product_id = ?";
            PreparedStatement stmtProduct = co.prepareStatement(sqlProduct);
            stmtProduct.setString(1, product.getName());
            stmtProduct.setDouble(2, product.getPrice());
            stmtProduct.setInt(3, product.getStock());
            stmtProduct.setString(4, product.getGender());
            stmtProduct.setString(5, product.getColor());
            stmtProduct.setInt(6, product.getSize());
            stmtProduct.setString(7, product.getDescription());
            stmtProduct.setLong(8, product.getProductId());
            stmtProduct.executeUpdate();

            //Second : Update of the product characteristics in the right table Pants or Top
            if (product instanceof Pants) {
                Pants pants = (Pants) product;
                String length = pants.getIsShorts() ? "Shorts" : "Regular";
                String sqlPants = "UPDATE Pants SET length = ? WHERE product_id = ?";
                PreparedStatement stmtPants = co.prepareStatement(sqlPants);
                stmtPants.setString(1, length);
                stmtPants.setLong(2, product.getProductId());
                stmtPants.executeUpdate();
            } else if (product instanceof Top) {
                Top top = (Top) product;
                String sleevesType = top.getIsTshirt() ? "T-shirt" : "Sweater";
                String sqlTop = "UPDATE Top SET sleevesType = ? WHERE product_id = ?";
                PreparedStatement stmtTop = co.prepareStatement(sqlTop);
                stmtTop.setString(1, sleevesType);
                stmtTop.setLong(2, product.getProductId());
                stmtTop.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error modifying product: " + e.getMessage());
        }
    }


    // Deletes a product from the database.
    // And its corresponding entities in the tables Pants or Tops.
    @Override
    public void deleteAnElement(Product product) {
        try {
            // First: Delete from the Pants table if the product is of type Pants
            if (product instanceof Pants) {
                String sqlPants = "DELETE FROM Pants WHERE product_id = ?";
                PreparedStatement stmtPants = co.prepareStatement(sqlPants);
                stmtPants.setLong(1, product.getProductId());
                stmtPants.executeUpdate();
                stmtPants.close();
                System.out.println("deleted pants");
            }

            // Otherwise: Delete from the Top table if the product is of type Top
            if (product instanceof Top) {
                String sqlTop = "DELETE FROM Top WHERE product_id = ?";
                PreparedStatement stmtTop = co.prepareStatement(sqlTop);
                stmtTop.setLong(1, product.getProductId());
                stmtTop.executeUpdate();
                System.out.println("deleted top");
                stmtTop.close();
            }

            // Finally: Delete from the Product table
            String sqlProduct = "DELETE FROM Product WHERE product_id = ?";
            PreparedStatement stmtProduct = co.prepareStatement(sqlProduct);
            stmtProduct.setLong(1, product.getProductId());
            stmtProduct.executeUpdate();
            AlertShowing.showAlert("Product deleted","Product successfully deleted", Alert.AlertType.INFORMATION);
            refresh();
            stmtProduct.close();

        } catch (SQLException e) {
            //If it isn't possible to delete the product, we set its stock to 0.
            AlertShowing.showAlert("Complete deletion impossible", "Setting the stock of the product to 0", Alert.AlertType.WARNING);
            product.setStock(0);
            modifyAnElement(product);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
