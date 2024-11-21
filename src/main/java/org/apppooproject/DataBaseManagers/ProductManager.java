package org.apppooproject.DataBaseManagers;

import org.apppooproject.Model.Pants;
import org.apppooproject.Model.Product;
import org.apppooproject.Model.Top;

import java.sql.*;
import java.util.ArrayList;

public class ProductManager implements DataManager<Product> {
    private static ProductManager instance;  // Instance unique de ProductManager
    private final Connection co;             // Objet de connexion à la base de données
    private ArrayList<Product> products;     // Liste de tous les produits récupérés depuis la base de données

    // Constructeur privé pour empêcher l'instanciation directe
    private ProductManager() {
        this.co=DatabaseInitializer.getH2Connection();
        products = new ArrayList<Product>();
        loadProductFromBd();  // Charger les produits depuis la base de données
    }

    // Méthode pour récupérer l'instance unique de ProductManager
    public static ProductManager getInstance() {
        if (instance == null) {
            instance = new ProductManager();  // Créer l'instance unique si elle n'existe pas
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
                products.add(pantsOrTop(res.getLong("product_id"), co));
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
            // Query to check if the product is Pants
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

            // Query to check if the product is a Top
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

    // Refreshes the product list by reloading all products from the database.
    public void refresh() {
        products = new ArrayList<Product>();
        loadProductFromBd();
    }

    // Returns the list of all products.
    public ArrayList<Product> getProducts() {
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

    public ArrayList<Product> searchByKeyWords(String searchDemand) {
        if (removeExtraSpaces(searchDemand).isEmpty()) {
            return products;
        }


        if (searchDemand.toLowerCase().contains("and") || searchDemand.toLowerCase().contains("or")) {
            return searchWithLogicalOperators(searchDemand);
        }

        ArrayList<Product> keyWordsCorrespondingProducts = new ArrayList<>();
        String[] separatedWords = searchDemand.split(" ");
        ArrayList<String> keyWords = new ArrayList<>();

        for (String s : separatedWords) {
            String cleanedWord = removeExtraSpaces(s);
            if (!cleanedWord.isEmpty()) { // Vérifie que le mot n'est pas vide
                keyWords.add(cleanedWord.toLowerCase()); // Transforme immédiatement en minuscule
            }
        }

        // Vérifie chaque produit pour trouver les mots-clés
        for (Product p : products) {
            boolean containsKeyWord = false;
            for (String keyword : keyWords) {
                if (p.getDescription().toLowerCase().contains(keyword) ||
                        p.getName().toLowerCase().contains(keyword)) {
                    containsKeyWord = true;
                    break;
                }
            }
            if (containsKeyWord) {
                keyWordsCorrespondingProducts.add(p);
            }
        }

        return keyWordsCorrespondingProducts;
    }

    public ArrayList<Product> searchWithLogicalOperators(String searchDemand) {
        ArrayList<Product> resultingProducts = new ArrayList<>();
        String[] andSeparatedWords = searchDemand.toLowerCase().split("and");

        for (Product p : products) {
            boolean matchesAllAndConditions = true;

            for (String andCondition : andSeparatedWords) {
                String cleanedAndCondition = removeExtraSpaces(andCondition);
                boolean matchesOrCondition = false;

                if (cleanedAndCondition.contains("or")) {
                    String[] orSeparatedWords = cleanedAndCondition.split("or");
                    for (String orCondition : orSeparatedWords) {
                        String cleanedOrCondition = removeExtraSpaces(orCondition);
                        if (p.getDescription().toLowerCase().contains(cleanedOrCondition) ||
                                p.getName().toLowerCase().contains(cleanedOrCondition)) {
                            matchesOrCondition = true;
                            break;
                        }
                    }
                } else {
                    // Vérifie une condition AND sans OR
                    if (p.getDescription().toLowerCase().contains(cleanedAndCondition) ||
                            p.getName().toLowerCase().contains(cleanedAndCondition)) {
                        matchesOrCondition = true;
                    }
                }

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


    public String removeExtraSpaces(String input) {
        if (input == null) {
            return ""; // Gérer le cas où la chaîne est nulle
        }
        return input.trim().replaceAll("\\s+", " ");
    }

    // Filters products to return only Pants based on a given criterion.
    public ArrayList<Product> showOnlyPants(int criteria) {
        ArrayList<Product> pants = new ArrayList<>();
        for (Product p : products) {
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
        for (Product p : products) {
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

    public ArrayList<Product> showMoreThanGivenPrice(int price){
        ArrayList<Product> productsOfGivenPrice = new ArrayList<>();
        for (Product p : products) {
            if (p.getPrice() > price) {
                productsOfGivenPrice.add(p);
            }
        }
        return productsOfGivenPrice;
    }

    public ArrayList<Product> showLessThanGivenPrice(int price){
        ArrayList<Product> productsOfGivenPrice = new ArrayList<>();
        for (Product p : products) {
            if (p.getPrice() < price) {
                productsOfGivenPrice.add(p);
            }
        }
        return productsOfGivenPrice;
    }

    public ArrayList<Product> showBetweenGivenPrice(int price1, int price2){
        ArrayList<Product> productsOfGivenPrice = new ArrayList<>();
        for (Product p : products) {
            if (p.getPrice() >=price1 && p.getPrice() <=price2) {
                productsOfGivenPrice.add(p);
            }
        }
        return productsOfGivenPrice;
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

    @Override
    public void addAnElement(Product product) {
        try {
            // Étape 1 : Insertion dans la table Product
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

            // Récupération de l'ID généré
            ResultSet generatedKeys = stmtProduct.getGeneratedKeys();
            if (generatedKeys.next()) {
                long productId = generatedKeys.getLong(1);
                product.setProductId(productId);

                // Étape 2 : Insertion dans la table Pants ou Top
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
            }
        } catch (SQLException e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
    }
    // Mettre à jour le stock d'un produit
    public void updateStock(long productId, int quantityToAdd) throws SQLException {
        String query = "UPDATE Product SET stock = stock + ? WHERE product_id = ?";
        try (PreparedStatement stmt = co.prepareStatement(query)) {
            stmt.setInt(1, quantityToAdd);
            stmt.setLong(2, productId);
            stmt.executeUpdate();
        }
    }


    @Override
    public void modifyAnElement(Product product) {
        try {
            // Étape 1 : Mise à jour de la table Product
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

            // Étape 2 : Mise à jour de Pants ou Top selon le type
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


    // Deletes a product from the database by product ID.
    @Override
    public void deleteAnElement(Product product) {
        try {
            // Step 1: Delete from the Pants table if the product is of type Pants
            if (product instanceof Pants) {
                String sqlPants = "DELETE FROM Pants WHERE product_id = ?";
                PreparedStatement stmtPants = co.prepareStatement(sqlPants);
                stmtPants.setLong(1, product.getProductId());
                stmtPants.executeUpdate();
            }

            // Step 2: Delete from the Top table if the product is of type Top
            if (product instanceof Top) {
                String sqlTop = "DELETE FROM Top WHERE product_id = ?";
                PreparedStatement stmtTop = co.prepareStatement(sqlTop);
                stmtTop.setLong(1, product.getProductId());
                stmtTop.executeUpdate();
            }

            // Step 3: Delete from the Product table
            String sqlProduct = "DELETE FROM Product WHERE product_id = ?";
            PreparedStatement stmtProduct = co.prepareStatement(sqlProduct);
            stmtProduct.setLong(1, product.getProductId());
            stmtProduct.executeUpdate();
            refresh();

        } catch (SQLException e) {
            System.out.println("Error deleting product: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
