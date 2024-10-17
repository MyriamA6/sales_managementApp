module org.example.apppooproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.apppooproject to javafx.fxml;
    exports org.example.apppooproject;
}