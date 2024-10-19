module org.example.apppooproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens org.example.apppooproject to javafx.fxml;
    exports org.example.apppooproject;
    opens SalesManager to javafx.fxml;
    exports SalesManager;
}