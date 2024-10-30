package org.apppooproject.DataBaseManagers;

import org.apppooproject.Model.Pants;
import org.apppooproject.Model.Product;
import org.apppooproject.Model.Top;

import java.sql.*;
import java.util.ArrayList;

public class ProductManager implements DataManager<Product>{
    private Connection co;
    private ProductManager instance;
    private ArrayList<Product> products;

    public ProductManager() {
        try {
            this.co = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/baseSchema?useSSL=false", "root", "vautotwu");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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

    public ArrayList<Product> getProducts() {
        return products;
    }

    public Product getProductById(long productId) {
        for (Product p : products) {
            if(p.getProductId() == productId){
                return p;
            }
        }
        return null;
    }



    public ArrayList<String> searchByKeyWords(String searchDemand) {
        //récupère
        String[] separatedWords = searchDemand.split(" ");
        ArrayList<String> keyWords = new ArrayList<>();
        for(String s :separatedWords){
            if (s.equals(" ")){
                keyWords.add(s);
            }
        }
        return keyWords;
    }

    public ArrayList<Product> showOnlyPants (int criteria){
        ArrayList<Product> pants = new ArrayList<>();
        Pants pant;
        for(Product p : products){
            if(p instanceof Pants){
                pant = (Pants) p;
                switch(criteria){
                    case 1:
                        if (pant.getIsShorts()){
                            pants.add(pant);
                        }
                        break;
                    case 2:
                        if (!pant.getIsShorts()){
                            pants.add(pant);
                        }
                        break;
                    default:
                        pants.add(pant);
                }
            }
        }
        return pants;
    }

    public ArrayList<Product> showBySize (int size){
        ArrayList<Product> productsOfGivenSize = new ArrayList<>();
        for(Product p : products){
            if(p.getSize() == size){
                productsOfGivenSize.add(p);
            }
        }
        return productsOfGivenSize;
    }

    public ArrayList<Product> showOnlyTops (int criteria){
        ArrayList<Product> tops = new ArrayList<>();
        Top top;
        for(Product p : products){
            if(p instanceof Top){
                top = (Top) p;
                switch(criteria){
                    case 1:
                        if (top.getIsTshirt()){
                            tops.add(top);
                        }
                        break;
                    case 2:
                        if (!top.getIsTshirt()){
                            tops.add(top);
                        }
                        break;
                    default:
                        tops.add(top);
                }
            }
        }
        return tops;
    }


        /*for (String s : separatedConjunction) {
            String[] separatedUnionInString = s.split("or");
            separatedUnion.addAll(Arrays.asList(separatedUnionInString));
        }
        ArrayList<String> keyWords = new ArrayList<>();
        for (String s : separatedConjunction) {
            String[] separatedUnionInString = s.split("or");
            separatedUnion.addAll(Arrays.asList(separatedUnionInString));
        }*/





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
