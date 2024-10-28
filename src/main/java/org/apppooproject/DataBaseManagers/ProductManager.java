package org.apppooproject.DataBaseManagers;

import org.apppooproject.Model.Customer;
import org.apppooproject.Model.Pants;
import org.apppooproject.Model.Product;
import org.apppooproject.Model.Top;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public abstract class ProductManager implements DataManager<Product>{
    private Connection co;
    private ArrayList<Product> products;

    public ProductManager() {
        products = new ArrayList<Product>();
        loadProductFromBd();
    }

    private void loadProductFromBd() {
        Statement stmt = null;
        try {
            stmt = co.createStatement();
            String sql = "SELECT * FROM Product";
            ResultSet res = stmt.executeQuery(sql);

            while (res.next()) {
                products.add(pantsOrTop(res.getLong("product_id"),co));
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

    public Product pantsOrTop (long productId, Connection co) {
        Statement stmt = null;
        try {
            stmt = co.createStatement();
            String sqlPants = "SELECT * FROM Pants JOIN Product ON Pants.product_id = Product.product_id WHERE Pants.product_id = " + productId;
            ResultSet res = stmt.executeQuery(sqlPants);
            if (res.next()) {
                // Déterminer si le pantalon est un short (isShorts) en fonction de la valeur de longueur
                boolean isShorts = "Shorts".equalsIgnoreCase(res.getString("length"));

                // Création de l'objet Pants
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

            // Sinon, vérification si le produit est un Top
            String sqlTop = "SELECT * FROM Top JOIN Product ON Top.product_id = Product.product_id WHERE Top.product_id = " + productId;
            res = stmt.executeQuery(sqlTop);
            if (res.next()) {
                // Création de l'objet Top
                boolean isTshirt = "T-shirt".equalsIgnoreCase(res.getString("sleevesType"));
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
        return null; // afficher qu'il y a eu une erreur lors de la récupération des données de la base
    }

    public void refresh(){
        products= new ArrayList<Product>();
        loadProductFromBd();
    }


    /*
    @Override
    public void addAnElement(Product product) {

    }

    @Override
    public void modifyAnElement(Product product) {

    }

    @Override
    public void deleteAnElement(Product product) {

    }*/

}
