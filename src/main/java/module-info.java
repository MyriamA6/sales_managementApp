module org.example.apppooproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires mysql.connector.java;

    opens org.apppooproject to javafx.fxml;
    exports org.apppooproject;
    opens org.apppooproject.DataBaseManagers to javafx.fxml;
    exports org.apppooproject.DataBaseManagers;
    exports org.apppooproject.Customer;
    opens org.apppooproject.Customer to javafx.fxml;
    exports org.apppooproject.Model;
    opens org.apppooproject.Model to javafx.fxml;
    exports org.apppooproject.Controllers;
    opens org.apppooproject.Controllers to javafx.fxml;
}