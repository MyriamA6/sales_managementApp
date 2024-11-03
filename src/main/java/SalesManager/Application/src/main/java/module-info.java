module org.example.apppooproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.apppooproject to javafx.fxml;
    exports org.example.apppooproject;
}